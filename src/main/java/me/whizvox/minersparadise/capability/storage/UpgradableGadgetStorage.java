package me.whizvox.minersparadise.capability.storage;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import me.whizvox.minersparadise.api.IUpgradableGadget;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;

public class UpgradableGadgetStorage implements Capability.IStorage<IUpgradableGadget> {

  @Nullable
  @Override
  public INBT writeNBT(Capability<IUpgradableGadget> capability, IUpgradableGadget gadget, Direction side) {
    ListNBT nbt = new ListNBT();
    gadget.getInstalled().forEach(upgradeData -> {
      CompoundNBT upgradeDataNbt = new CompoundNBT();
      upgradeDataNbt.putString("id", upgradeData.getUpgrade().getId().toString());
      upgradeDataNbt.putByte("lvl", upgradeData.getLevel());
      upgradeDataNbt.putByte("activeLvl", gadget.getActiveLevel(upgradeData.getUpgrade()));
    });
    return nbt;
  }

  @Override
  public void readNBT(Capability<IUpgradableGadget> capability, IUpgradableGadget gadget, Direction side, INBT nbt) {
    ListNBT upgradesNbt = (ListNBT) nbt;
    for (int i = 0; i < upgradesNbt.size(); i++) {
      CompoundNBT upgradeNbt = upgradesNbt.getCompound(i);
      String idStr = upgradeNbt.getString("id");
      Optional<IGadgetUpgrade> upgrade = MinersParadise.getInstance().getGadgetUpgradeRegistry().get(new ResourceLocation(idStr));
      if (upgrade.isPresent()) {
        gadget.install(new GadgetUpgradeData(upgrade.get(), upgradeNbt.getByte("lvl")));
      } else {
        throw new IllegalArgumentException("Unregistered upgrade ID: " + idStr);
      }
      gadget.activate(upgrade.get(), upgradeNbt.getByte("activeLvl"));
    }
  }

}
