package me.whizvox.minersparadise.item.impl;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import me.whizvox.minersparadise.api.UpgradableGadget;
import me.whizvox.minersparadise.common.GadgetUpgradeRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class UpgradableBore extends UpgradableGadget {

  private static final Map<IGadgetUpgrade, Byte> SUPPORTED_UPGRADES;
  static {
    SUPPORTED_UPGRADES = new HashMap<>();
    GadgetUpgradeRegistry registry = MinersParadise.getInstance().getGadgetUpgradeRegistry();
    registry.get(new ResourceLocation("minersparadise", "efficiency")).ifPresent(upgrade -> SUPPORTED_UPGRADES.put(upgrade, (byte) 9));
    registry.get(new ResourceLocation("minersparadise", "area")).ifPresent(upgrade -> SUPPORTED_UPGRADES.put(upgrade, (byte) 9));
    registry.get(new ResourceLocation("minersparadise", "vein_mining")).ifPresent(upgrade -> SUPPORTED_UPGRADES.put(upgrade, (byte) 9));
    registry.get(new ResourceLocation("minersparadise", "silk_touch")).ifPresent(upgrade -> SUPPORTED_UPGRADES.put(upgrade, (byte) 0));
    registry.get(new ResourceLocation("minersparadise", "magnet")).ifPresent(upgrade -> SUPPORTED_UPGRADES.put(upgrade, (byte) 0));
  }

  @Override
  public boolean isSupported(IGadgetUpgrade upgrade, byte level) {
    return level <= SUPPORTED_UPGRADES.getOrDefault(upgrade, Byte.MIN_VALUE);
  }

}
