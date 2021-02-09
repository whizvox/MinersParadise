package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.api.IBoreTier;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.capability.provider.CapabilityBoreProvider;
import me.whizvox.minersparadise.item.group.MPItemGroup;
import me.whizvox.minersparadise.util.NBTUtil;
import me.whizvox.minersparadise.util.StringUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HandheldBoreItem extends Item {

  private IBoreTier tier;

  public HandheldBoreItem(IBoreTier tier) {
    super(new Item.Properties()
        .group(MPItemGroup.getInstance())
        .maxStackSize(1)
        .setNoRepair()
    );
    this.tier = tier;
  }

  public IBoreTier getTier() {
    return tier;
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new CapabilityBoreProvider();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean showDurabilityBar(ItemStack stack) {
    return stack.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored() < e.getMaxEnergyStored()).orElse(false);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public double getDurabilityForDisplay(ItemStack stack) {
    return stack.getCapability(CapabilityEnergy.ENERGY, null)
        .map(e -> 1.0D - ((double) e.getEnergyStored() / e.getMaxEnergyStored()))
        .orElse(0.0D);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0x15d600;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
      boolean sneakPressed = Screen.hasShiftDown();
      if (!sneakPressed) {
        tooltip.add(new TranslationTextComponent(
            "minersparadise.bore.energy",
            StringUtil.formatAsCompactNotation(e.getEnergyStored()),
            StringUtil.formatAsCompactNotation(e.getMaxEnergyStored()))
            .mergeStyle(TextFormatting.GREEN)
        );
        tooltip.add(new TranslationTextComponent("minersparadise.bore.show_information", "shift")
            .mergeStyle(TextFormatting.YELLOW, TextFormatting.ITALIC));
      } else {
        tooltip.add(new TranslationTextComponent(
            "minersparadise.bore.energy.descriptive",
            e.getEnergyStored(),
            e.getMaxEnergyStored())
            .mergeStyle(TextFormatting.GREEN)
        );
        AtomicBoolean noUpgrades = new AtomicBoolean(true);
        stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
          Set<BoreUpgrade> upgrades = bore.getInstalled();
          if (upgrades.size() > 0) {
            noUpgrades.set(false);
            upgrades.forEach(upgrade -> tooltip.add(
                new StringTextComponent("- ")
                  .append(new TranslationTextComponent("minersparadise.bore.upgrade." + upgrade.getId())
                  .append(new StringTextComponent(" " + StringUtil.formatAsRomanNumeral(bore.getInstalledLevel(upgrade) + 1)))).mergeStyle(bore.isActive(upgrade) ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY)));
          }
        });
        if (noUpgrades.get()) {
          tooltip.add(new TranslationTextComponent("minersparadise.bore.no_upgrades"));
        }
      }
    });
  }

  @Override
  public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
    return stack.getCapability(CapabilityEnergy.ENERGY, null)
        .map(e -> e.getEnergyStored() > 0 ? tier.getHarvestLevel() : -1)
        .orElse(-1);
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    AtomicReference<Float> destroySpeed = new AtomicReference<>(1.0F);
    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
      if (e.getEnergyStored() > 0) {
        stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
          if (bore.isActive(BoreUpgrade.EFFICIENCY)) {
            destroySpeed.set(
                tier.getBaseEfficiency() *
                    (float) Math.pow(MPConfig.BORE_EFFECT_EFFICIENCY.get(), bore.getActiveLevel(BoreUpgrade.EFFICIENCY) + 1)
            );
          } else {
            destroySpeed.set(tier.getBaseEfficiency());
          }
        });
      }
    });
    return destroySpeed.get();
  }

  @Override
  public boolean canHarvestBlock(ItemStack stack, BlockState state) {
    return stack.getCapability(CapabilityEnergy.ENERGY)
        .map(e -> e.getEnergyStored() > 0)
        .orElse(false);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity bearer) {
    /*if (!world.isRemote() && state.getBlockHardness(world, pos) > 0) {
      stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
        if (energy.getEnergyStored() > 0) {
          stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
            energy.receiveEnergy(-UpgradeUtil.getFinalEnergyCost(bore), false);
          });
        }
      });
    }*/
    return true;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    if (!world.isRemote() && player.isSneaking()) {
      ItemStack stack = player.getHeldItem(hand);
      stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
        if (!bore.isInstalled(BoreUpgrade.VEIN_MINING)) {
          bore.install(BoreUpgrade.VEIN_MINING, (byte) 0);
          bore.activate(BoreUpgrade.VEIN_MINING, true);
          player.sendStatusMessage(new StringTextComponent("First vein-mining level: 1"), false);
          NBTUtil.setUpgrades(stack, bore);
        } else {
          byte level = bore.getActiveLevel(BoreUpgrade.VEIN_MINING);
          if (level >= BoreUpgrade.VEIN_MINING.getMaxLevel()) {
            level = 0;
          } else {
            level++;
          }
          if (bore.install(BoreUpgrade.VEIN_MINING, level)) {
            player.sendStatusMessage(new StringTextComponent("Vein-mining level: " + (level + 1)), false);
            NBTUtil.setUpgrades(stack, bore);
          } else {
            player.sendStatusMessage(new StringTextComponent("Could not apply vein-mining level"), false);
          }
        }
      });
    }
    return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
  }

  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    super.fillItemGroup(group, items);
    if (isInGroup(group)) {
      ItemStack charged = new ItemStack(this);
      NBTUtil.setEnergy(charged, MPConfig.BORE_BASE_ENERGY_CAPACITY.get());
      items.add(charged);
    }
  }

}
