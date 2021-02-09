package me.whizvox.minersparadise.util;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.api.IUpgradableBore;
import me.whizvox.minersparadise.capability.MPCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class NBTUtil {

  private static final Logger LOGGER = LogManager.getLogger(MinersParadise.MODID + " NBT");

  // private helpers

  private static final int
      ID_BYTE_ARRAY = 7,
      ID_STRING = 8,
      ID_LIST = 9,
      ID_COMPOUND = 10,
      ID_INT_ARRAY = 11,
      ID_LONG_ARRAY = 12,
      ID_NUMBER = 99,
      ID_BOOLEAN = ID_NUMBER;

  private static final String
      KEY_ENERGY = "energy",
      KEY_UPGRADES = "upgrades";

  private static int getInt(ItemStack stack, String key, int defaultValue) {
    CompoundNBT nbt = stack.getOrCreateTag();
    if (nbt.contains(key, ID_NUMBER)) {
      return nbt.getInt(key);
    }
    nbt.putInt(key, defaultValue);
    return defaultValue;
  }

  private static CompoundNBT getCompound(ItemStack stack, String key, CompoundNBT defaultValue) {
    CompoundNBT nbt = stack.getOrCreateTag();
    if (nbt.contains(key, ID_COMPOUND)) {
      return nbt.getCompound(key);
    }
    nbt.put(key, defaultValue);
    return defaultValue;
  }

  private static void setInt(ItemStack stack, String key, int value) {
    stack.getOrCreateTag().putInt(key, value);
  }

  // public helpers

  public static void setEnergy(ItemStack stack, IEnergyStorage energy) {
    stack.getOrCreateTag().put(KEY_ENERGY, CapabilityEnergy.ENERGY.writeNBT(energy, null));
  }

  // less elegant but easy
  public static void setEnergy(ItemStack stack, int energy) {
    setInt(stack, KEY_ENERGY, energy);
  }

  public static void setUpgrades(ItemStack stack, IUpgradableBore bore) {
    stack.getOrCreateTag().put(KEY_UPGRADES, MPCapabilities.UPGRADABLE_BORE.writeNBT(bore, null));
  }

}
