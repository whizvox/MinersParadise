package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.api.IBoreTier;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.init.GadgetUpgrades;
import me.whizvox.minersparadise.item.impl.UpgradableBore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class HandheldBoreItem extends UpgradableGadgetItem {

  private IBoreTier tier;

  public HandheldBoreItem(IBoreTier tier) {
    super(new Item.Properties(), UpgradableBore::new, MPConfig.BORE_BASE_ENERGY_CAPACITY.get());
    this.tier = tier;
  }

  public IBoreTier getTier() {
    return tier;
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
        stack.getCapability(MPCapabilities.GADGET).ifPresent(bore -> {
          if (bore.isActive(GadgetUpgrades.EFFICIENCY)) {
            destroySpeed.set(
                tier.getBaseEfficiency() *
                    (float) Math.pow(MPConfig.BORE_EFFECT_EFFICIENCY.get(), bore.getActiveLevel(GadgetUpgrades.EFFICIENCY) + 1)
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

}
