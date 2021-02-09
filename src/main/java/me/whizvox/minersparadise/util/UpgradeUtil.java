package me.whizvox.minersparadise.util;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.api.IBoreTier;
import me.whizvox.minersparadise.api.IUpgradableBore;

public class UpgradeUtil {

  private static byte clampLevel(byte level) {
    return level < -1 ? -1 : level;
  }

  public static int getAreaRadius(byte level) {
    if (level < 0) {
      return 1;
    }
    return level * 2 + 3;
  }

  public static int getTillingRadius(byte level) {
    return getAreaRadius(level);
  }

  public static float getFinalEfficiency(IBoreTier tier, byte level) {
    level = clampLevel(level);
    return tier.getBaseEfficiency() * (float) Math.pow(MPConfig.BORE_EFFECT_EFFICIENCY.get(), level + 1);
  }

  public static int getMaxVeinMineCount(byte level) {
    if (level < 0) {
      return 1;
    }
    return (int) (MPConfig.BORE_BASE_VEIN_MINING.get() * Math.pow(MPConfig.BORE_EFFECT_VEIN_MINING.get(), level + 1));
  }

  public static int getMaxEnergyCapacity(byte level) {
    level = clampLevel(level);
    return (int) (MPConfig.BORE_BASE_ENERGY_CAPACITY.get() * Math.pow(MPConfig.BORE_EFFECT_BATTERY.get(), level + 1));
  }

  public static float getEfficiencyCostEffect(byte level) {
    if (level < 0) {
      return 1.0F;
    }
    return (float) (MPConfig.BORE_COST_EFFICIENCY.get() * (level + 1));
  }

  public static int getFinalEnergyCost(IUpgradableBore bore) {
    double cost = MPConfig.BORE_BASE_ENERGY_COST.get();
    cost *= getEfficiencyCostEffect(bore.getActiveLevel(BoreUpgrade.EFFICIENCY));
    if (bore.isActive(BoreUpgrade.SILK_TOUCH)) {
      cost *= MPConfig.BORE_COST_SILK_TOUCH.get();
    }
    if (bore.isActive(BoreUpgrade.VEIN_MINING)) {
      cost *= MPConfig.BORE_COST_VEIN_MINING.get();
    }
    // TODO Maybe don't multiply the cost if the player ran out of inventory space
    if (bore.isActive(BoreUpgrade.MAGNET)) {
      cost *= MPConfig.BORE_COST_MAGNET.get();
    }
    return (int) cost;
  }

}
