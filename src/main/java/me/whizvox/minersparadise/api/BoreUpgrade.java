package me.whizvox.minersparadise.api;

import me.whizvox.minersparadise.MPConfig;
import net.minecraft.util.math.MathHelper;

public enum BoreUpgrade {

  NONE(0),
  AREA(3),
  EFFICIENCY(4),
  SILK_TOUCH(0),
  VEIN_MINING(4),
  ENERGY_CAPACITY(4),
  TILLING(1),
  MAGNET(0);

  private String id;
  private int maxLevel; // zero-indexed

  BoreUpgrade(int maxLevel) {
    this.maxLevel = maxLevel;
    id = toString().toLowerCase();
  }

  public String getId() {
    return id;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public static BoreUpgrade getFromId(String id) {
    for (BoreUpgrade upgrade : values()) {
      if (upgrade != NONE && upgrade.id.equals(id)) {
        return upgrade;
      }
    }
    return null;
  }

}
