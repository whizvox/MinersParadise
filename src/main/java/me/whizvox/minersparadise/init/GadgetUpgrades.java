package me.whizvox.minersparadise.init;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.GadgetUpgrade;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import me.whizvox.minersparadise.common.GadgetUpgradeRegistry;
import net.minecraft.util.ResourceLocation;

public class GadgetUpgrades {

  public static IGadgetUpgrade
      AREA,
      BATTERY,
      EFFICIENCY,
      VEIN_MINING,
      FORTUNE,
      SILK_TOUCH,
      MAGNET;

  private static IGadgetUpgrade withDefaultNamespace(String name, byte maxLevel) {
    return new GadgetUpgrade(new ResourceLocation(MinersParadise.MODID, name), maxLevel);
  }

  public static void register(GadgetUpgradeRegistry registry) {
    AREA = registry.register(withDefaultNamespace("area", (byte) 9));
    BATTERY = registry.register(withDefaultNamespace("battery", (byte) 4));
    EFFICIENCY = registry.register(withDefaultNamespace("efficiency", (byte) 9));
    VEIN_MINING = registry.register(withDefaultNamespace("vein_mining", (byte) 0));
    FORTUNE = registry.register(withDefaultNamespace("fortune", (byte) 9));
    SILK_TOUCH = registry.register(withDefaultNamespace("silk_touch", (byte) 0));
    MAGNET = registry.register(withDefaultNamespace("magnet", (byte) 0));
  }

}
