package me.whizvox.minersparadise.init;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.item.BoreTier;
import me.whizvox.minersparadise.item.UpgradeItem;
import me.whizvox.minersparadise.item.HandheldBoreItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MPItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinersParadise.MODID);

  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_IRON = ITEMS.register("handheld_bore_iron", () -> new HandheldBoreItem(BoreTier.IRON));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_GOLD = ITEMS.register("handheld_bore_gold", () -> new HandheldBoreItem(BoreTier.GOLD));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_DIAMOND = ITEMS.register("handheld_bore_diamond", () -> new HandheldBoreItem(BoreTier.DIAMOND));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_NETHERITE = ITEMS.register("handheld_bore_netherite", () -> new HandheldBoreItem(BoreTier.NETHERITE));

  public static final List<RegistryObject<Item>>
      UPGRADES_AREA = registerUpgrades(BoreUpgrade.AREA),
      UPGRADES_EFFICIENCY = registerUpgrades(BoreUpgrade.EFFICIENCY),
      UPGRADES_SILK_TOUCH = registerUpgrades(BoreUpgrade.SILK_TOUCH),
      UPGRADES_VEIN_MINING = registerUpgrades(BoreUpgrade.VEIN_MINING),
      UPGRADES_CAPACITY = registerUpgrades(BoreUpgrade.ENERGY_CAPACITY),
      UPGRADES_TILLING = registerUpgrades(BoreUpgrade.TILLING),
      UPGRADES_MAGNET = registerUpgrades(BoreUpgrade.MAGNET);

  private static List<RegistryObject<Item>> registerUpgrades(BoreUpgrade upgrade) {
    ArrayList<RegistryObject<Item>> items = new ArrayList<>();
    for (int i = 0; i <= upgrade.getMaxLevel(); i++) {
      final byte b = (byte) i;
      items.add(ITEMS.register("upgrade_" + upgrade.getId() + "_" + i, () -> new UpgradeItem(upgrade, b)));
    }
    return Collections.unmodifiableList(items);
  }

  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

}
