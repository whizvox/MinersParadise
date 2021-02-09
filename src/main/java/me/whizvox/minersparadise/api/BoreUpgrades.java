package me.whizvox.minersparadise.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoreUpgrades {

  private Map<BoreUpgrade, Byte> installed;
  private Set<BoreUpgrade> active;

  public BoreUpgrades() {
    installed = new HashMap<>();
    active = new HashSet<>();
  }

  public BoreUpgrades(CompoundNBT nbt) {
    this();
    deserialize(nbt);
  }

  public BoreUpgrades(ItemStack stack) {
    this();
    deserialize(stack);
  }

  public Set<BoreUpgrade> getInstalled() {
    return installed.keySet();
  }

  public Set<BoreUpgrade> getActive() {
    return active;
  }

  public byte getLevel(BoreUpgrade upgrade) {
    return active.contains(upgrade) ? installed.getOrDefault(upgrade, (byte) -1) : (byte) -1;
  }

  public void add(BoreUpgrade upgrade, byte level) {
    installed.put(upgrade, level);
  }

  public boolean remove(BoreUpgrade upgrade) {
    boolean res = installed.remove(upgrade) != null;
    active.remove(upgrade);
    return res;
  }

  public boolean activate(BoreUpgrade upgrade) {
    if (installed.containsKey(upgrade) && !active.contains(upgrade)) {
      active.add(upgrade);
      return true;
    }
    return false;
  }

  public boolean deactivate(BoreUpgrade upgrade) {
    if (installed.containsKey(upgrade) && active.contains(upgrade)) {
      active.remove(upgrade);
      return true;
    }
    return false;
  }

  public INBT serialize() {
    CompoundNBT nbt = new CompoundNBT();
    ListNBT installedNbt = new ListNBT();
    installed.forEach((upgrade, level) -> {
      CompoundNBT upgradeNbt = new CompoundNBT();
      upgradeNbt.putString("id", upgrade.getId());
      upgradeNbt.putByte("level", level);
      installedNbt.add(upgradeNbt);
    });
    nbt.put("installed", installedNbt);
    ListNBT activeNbt = new ListNBT();
    active.forEach(upgrade -> activeNbt.add(StringNBT.valueOf(upgrade.getId())));
    nbt.put("active", activeNbt);
    return nbt;
  }

  public void deserialize(INBT nbt) {
    installed.clear();
    active.clear();
    CompoundNBT root = (CompoundNBT) nbt;
    ListNBT installedNbt = root.getList("installed", 10);
    for (int i = 0; i < installedNbt.size(); i++) {
      CompoundNBT upgradeNbt = installedNbt.getCompound(i);
      String id = upgradeNbt.getString("id");
      BoreUpgrade upgrade = BoreUpgrade.getFromId(id);
      if (upgrade == null) {
        throw new IllegalStateException("Unknown upgrade id <" + id + ">");
      }
      byte level = upgradeNbt.getByte("level");
      if (level < 0 || level > upgrade.getMaxLevel()) {
        throw new IllegalStateException(
            String.format("Out-of-bounds upgrade level <%d> [0,%d] for %s", level, upgrade.getMaxLevel(), id)
        );
      }
      installed.put(upgrade, level);
    }
    ListNBT activeNbt = root.getList("active", 8);
    for (INBT upgradeNbt : activeNbt) {
      String id = upgradeNbt.getString();
      BoreUpgrade upgrade = BoreUpgrade.getFromId(id);
      if (upgrade == null) {
        throw new IllegalStateException("Unknown upgrade id <" + id + ">");
      }
      active.add(upgrade);
    }
  }

  private static final String KEY_UPGRADES = "upgrades";

  public void serialize(ItemStack stack) {
    stack.getOrCreateTag().put(KEY_UPGRADES, serialize());
  }

  // stack is not read-only! will be written to if no upgrades tag was found
  public void deserialize(ItemStack stack) {
    CompoundNBT nbt = stack.getOrCreateTag();
    if (nbt.contains(KEY_UPGRADES)) {
      deserialize(nbt.get(KEY_UPGRADES));
    } else {
      serialize();
    }
  }

}
