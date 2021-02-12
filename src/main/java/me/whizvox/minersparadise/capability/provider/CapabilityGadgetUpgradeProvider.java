package me.whizvox.minersparadise.capability.provider;

import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.capability.MPCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityGadgetUpgradeProvider implements ICapabilitySerializable<INBT> {

  private GadgetUpgradeData upgrade;
  private LazyOptional<GadgetUpgradeData> upgradeCapability;

  public CapabilityGadgetUpgradeProvider(GadgetUpgradeData upgrade) {
    this.upgrade = upgrade;
    upgradeCapability = LazyOptional.of(() -> upgrade);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    return cap == MPCapabilities.UPGRADE_DATA ? upgradeCapability.cast() : LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    return MPCapabilities.UPGRADE_DATA.writeNBT(upgrade, null);
  }

  @Override
  public void deserializeNBT(INBT nbt) {
    MPCapabilities.UPGRADE_DATA.readNBT(upgrade, null, nbt);
  }

}
