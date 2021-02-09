package me.whizvox.minersparadise.capability.provider;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.api.IUpgradableBore;
import me.whizvox.minersparadise.api.UpgradableBore;
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
import java.util.Objects;

public class CapabilityBoreProvider implements ICapabilitySerializable<INBT> {

  private LazyOptional<IEnergyStorage> energyCapability;
  private LazyOptional<IUpgradableBore> boreCapability;

  public CapabilityBoreProvider() {
    energyCapability = LazyOptional.of(() -> new EnergyStorage(MPConfig.BORE_BASE_ENERGY_CAPACITY.get(), 10000));
    boreCapability = LazyOptional.of(UpgradableBore::new);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energyCapability.cast();
    }
    if (cap == MPCapabilities.UPGRADABLE_BORE) {
      return boreCapability.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public INBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    boreCapability.ifPresent(bore ->
        nbt.put("upgrades", Objects.requireNonNull(MPCapabilities.UPGRADABLE_BORE.writeNBT(bore, null)))
    );
    energyCapability.ifPresent(energy ->
        nbt.put("energy", Objects.requireNonNull(CapabilityEnergy.ENERGY.writeNBT(energy, null)))
    );
    return nbt;
  }

  @Override
  public void deserializeNBT(INBT nbt) {
    CompoundNBT root = (CompoundNBT) nbt;
    boreCapability.ifPresent(bore ->
        MPCapabilities.UPGRADABLE_BORE.readNBT(bore, null, root.get("upgrades"))
    );
    energyCapability.ifPresent(energy ->
        CapabilityEnergy.ENERGY.readNBT(energy, null, root.get("energy"))
    );
  }

}
