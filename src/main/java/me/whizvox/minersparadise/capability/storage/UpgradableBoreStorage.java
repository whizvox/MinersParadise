package me.whizvox.minersparadise.capability.storage;

import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.api.IUpgradableBore;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class UpgradableBoreStorage implements Capability.IStorage<IUpgradableBore> {

  @Nullable
  @Override
  public INBT writeNBT(Capability<IUpgradableBore> capability, IUpgradableBore bore, Direction side) {
    CompoundNBT nbt = new CompoundNBT();
    ListNBT installedNbt = new ListNBT();
    bore.getInstalled().forEach((upgrade) -> {
      CompoundNBT upgradeNbt = new CompoundNBT();
      upgradeNbt.putString("id", upgrade.getId());
      upgradeNbt.putByte("level", bore.getInstalledLevel(upgrade));
      installedNbt.add(upgradeNbt);
    });
    nbt.put("installed", installedNbt);
    ListNBT activeNbt = new ListNBT();
    bore.getActive().forEach(upgrade -> activeNbt.add(StringNBT.valueOf(upgrade.getId())));
    nbt.put("active", activeNbt);
    return nbt;
  }

  @Override
  public void readNBT(Capability<IUpgradableBore> capability, IUpgradableBore bore, Direction side, INBT nbt) {
    CompoundNBT root = (CompoundNBT) nbt;
    ListNBT installedNbt = root.getList("installed", 10);
    for (int i = 0; i < installedNbt.size(); i++) {
      CompoundNBT upgradeNbt = installedNbt.getCompound(i);
      String id = upgradeNbt.getString("id");
      BoreUpgrade upgrade = BoreUpgrade.getFromId(id);
      if (upgrade == null) {
        //throw new IllegalStateException("Unknown upgrade id <" + id + ">");
      }
      byte level = upgradeNbt.getByte("level");
      if (level < 0 || level > upgrade.getMaxLevel()) {
        /*throw new IllegalStateException(
            String.format("Out-of-bounds upgrade level <%d> [0,%d] for %s", level, upgrade.getMaxLevel(), id)
        );*/
      }
      bore.install(upgrade, level);
    }
    ListNBT activeNbt = root.getList("active", 8);
    for (INBT upgradeNbt : activeNbt) {
      String id = upgradeNbt.getString();
      BoreUpgrade upgrade = BoreUpgrade.getFromId(id);
      if (upgrade == null) {
        //throw new IllegalStateException("Unknown upgrade id <" + id + ">");
      }
      bore.activate(upgrade, true);
    }
  }

}
