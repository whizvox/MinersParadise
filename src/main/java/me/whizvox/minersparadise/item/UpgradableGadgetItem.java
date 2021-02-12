package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.api.IUpgradableGadget;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.capability.provider.CapabilityGadgetProvider;
import me.whizvox.minersparadise.item.group.MPItemGroup;
import me.whizvox.minersparadise.util.MPLang;
import me.whizvox.minersparadise.util.StringUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class UpgradableGadgetItem extends Item {

  private final Supplier<IUpgradableGadget> gadgetSupplier;
  private final int baseEnergyCapacity;

  public UpgradableGadgetItem(Properties additionalProperties, Supplier<IUpgradableGadget> gadgetSupplier, int baseEnergyCapacity) {
    super(additionalProperties.maxStackSize(1)
        .group(MPItemGroup.getInstance()));
    this.gadgetSupplier = gadgetSupplier;
    this.baseEnergyCapacity = baseEnergyCapacity;
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return new CapabilityGadgetProvider(gadgetSupplier.get(), baseEnergyCapacity);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    boolean showDetailedInfo = Screen.hasShiftDown();
    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
      if (showDetailedInfo) {
        tooltip.add(new TranslationTextComponent(
          "minersparadise.gadget.tooltip.stored_energy.detailed",
          energy.getEnergyStored(),
          energy.getMaxEnergyStored()
        ));
      } else {
        tooltip.add(new TranslationTextComponent(
          "minersparadise.gadget.tooltip.stored_energy",
          StringUtil.formatAsCompactNotation(energy.getEnergyStored()),
          StringUtil.formatAsCompactNotation(energy.getMaxEnergyStored())
        ));
      }
    });
    stack.getCapability(MPCapabilities.GADGET).ifPresent(gadget -> {
      if (Screen.hasShiftDown()) {
        tooltip.add(MPLang.TOOLTIP_GADGET_UPGRADES_LIST);
        Set<GadgetUpgradeData> installed = gadget.getInstalled();
        if (!installed.isEmpty()) {
          installed.forEach(upgrade -> {
            final ResourceLocation id = upgrade.getUpgrade().getId();
            tooltip.add(new StringTextComponent("- ")
              .append(new TranslationTextComponent(id.getNamespace() + ".gadget_upgrade." + id.getPath())
                .mergeStyle(gadget.isActive(upgrade.getUpgrade()) ? TextFormatting.YELLOW : TextFormatting.DARK_GRAY)
              )
            );
          });
        } else {
          tooltip.add(MPLang.TOOLTIP_GADGET_NO_UPGRADES);
        }
      } else {
        tooltip.add(MPLang.TOOLTIP_GADGET_SHOW_UPGRADES);
      }
    });
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    return stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> energy.getEnergyStored() > 0).orElse(false);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    return stack.getCapability(CapabilityEnergy.ENERGY, null)
      .map(e -> 1.0D - ((double) e.getEnergyStored() / e.getMaxEnergyStored()))
      .orElse(0.0D);
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0x15d600;
  }

  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    super.fillItemGroup(group, items);
   /* if (isInGroup(group)) {
      ItemStack charged = new ItemStack(this);
      Optional<IEnergyStorage> energyOptional = charged.getCapability(CapabilityEnergy.ENERGY).resolve();
      if (energyOptional.isPresent()) {
        IEnergyStorage energy = energyOptional.get();
        NBTUtil.setEnergy(charged, energy.getMaxEnergyStored());
        items.add(charged);
      }
    }*/
  }

}
