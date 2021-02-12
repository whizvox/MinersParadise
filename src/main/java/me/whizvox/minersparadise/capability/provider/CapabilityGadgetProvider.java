package me.whizvox.minersparadise.capability.provider;

import me.whizvox.minersparadise.api.IUpgradableGadget;
import me.whizvox.minersparadise.capability.MPCapabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityGadgetProvider implements ICapabilitySerializable<INBT> {

  private IUpgradableGadget gadget;
  private IEnergyStorage energy;

  private LazyOptional<IUpgradableGadget> gadgetCapability;
  private LazyOptional<IEnergyStorage> energyCapability;

  public CapabilityGadgetProvider(IUpgradableGadget gadget, int baseCapacity) {
    this.gadget = gadget;
    gadgetCapability = LazyOptional.of(() -> gadget);
    energy = new EnergyStorage(baseCapacity, baseCapacity, 0, 0);
    energyCapability = LazyOptional.of(() -> energy);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == MPCapabilities.GADGET) {
      return gadgetCapability.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCapability.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("gadget_upgrades", MPCapabilities.GADGET.writeNBT(gadget, null));
    nbt.put("energy", CapabilityEnergy.ENERGY.writeNBT(energy, null));
    return nbt;
  }

  @Override
  public void deserializeNBT(INBT tag) {
    CompoundNBT nbt = (CompoundNBT) tag;
    MPCapabilities.GADGET.readNBT(gadget, null, nbt.get("gadget_upgrades"));
    CapabilityEnergy.ENERGY.readNBT(energy, null, nbt.get("energy"));
  }

}
