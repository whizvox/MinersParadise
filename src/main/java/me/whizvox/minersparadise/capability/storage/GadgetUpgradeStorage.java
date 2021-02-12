package me.whizvox.minersparadise.capability.storage;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;

public class GadgetUpgradeStorage implements Capability.IStorage<GadgetUpgradeData> {
  
  @Nullable
  @Override
  public INBT writeNBT(Capability<GadgetUpgradeData> capability, GadgetUpgradeData upgradeData, Direction side) {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putString("id", upgradeData.getUpgrade().getId().toString());
    nbt.putByte("lvl", upgradeData.getLevel());
    return nbt;
  }

  @Override
  public void readNBT(Capability<GadgetUpgradeData> capability, GadgetUpgradeData upgradeData, Direction side, INBT tag) {
    CompoundNBT nbt = (CompoundNBT) tag;
    String idStr = nbt.getString("id");
    Optional<IGadgetUpgrade> upgradeOptional = MinersParadise.getInstance().getGadgetUpgradeRegistry().get(new ResourceLocation(idStr));
    if (upgradeOptional.isPresent()) {
      upgradeData.setUpgrade(upgradeOptional.get());
    } else {
      throw new IllegalArgumentException("Unregistered upgrade ID: " + idStr);
    }
    upgradeData.setLevel(nbt.getByte("lvl"));
  }

}
