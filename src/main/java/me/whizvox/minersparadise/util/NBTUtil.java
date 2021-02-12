package me.whizvox.minersparadise.util;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import me.whizvox.minersparadise.api.IUpgradableGadget;
import me.whizvox.minersparadise.capability.MPCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

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
      KEY_UPGRADES = "upgrades",
      KEY_UPGRADE = "upgrade";

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

  public static void setUpgrades(ItemStack stack, IUpgradableGadget bore) {
    stack.getOrCreateTag().put(KEY_UPGRADES, MPCapabilities.GADGET.writeNBT(bore, null));
  }

  public static void setUpgrade(ItemStack stack, GadgetUpgradeData upgrade) {
    stack.getOrCreateTag().put(KEY_UPGRADE, MPCapabilities.UPGRADE_DATA.writeNBT(upgrade, null));
  }

  public static void setUpgrade(ItemStack stack, ResourceLocation id, byte level) {
    MinersParadise.getInstance().getGadgetUpgradeRegistry().get(id).ifPresent(upgrade -> setUpgrade(stack, new GadgetUpgradeData(upgrade, level)));
  }

}
