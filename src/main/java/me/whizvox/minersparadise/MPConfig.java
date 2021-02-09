package me.whizvox.minersparadise;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class MPConfig {

  private static final String
      CATEGORY_GENERAL = "general",
      SUBCATEGORY_HANDHELD_BORE = "handheld_bore",
      SUBCATEGORY_ORE_PROBE = "ore_probe",
      SUBCATEGORY_SLIME_CHUNK_LOCATOR = "slime_chunk_locator";

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  private static final ForgeConfigSpec COMMON_CONFIG;

  public static ForgeConfigSpec.DoubleValue BORE_BASE_ENERGY_COST;
  public static ForgeConfigSpec.IntValue BORE_BASE_ENERGY_CAPACITY;
  public static ForgeConfigSpec.IntValue    BORE_BASE_VEIN_MINING;
  public static ForgeConfigSpec.DoubleValue BORE_EFFECT_BATTERY;
  public static ForgeConfigSpec.DoubleValue BORE_EFFECT_EFFICIENCY;
  public static ForgeConfigSpec.DoubleValue BORE_EFFECT_VEIN_MINING;
  public static ForgeConfigSpec.DoubleValue BORE_COST_EFFICIENCY;
  public static ForgeConfigSpec.DoubleValue BORE_COST_SILK_TOUCH;
  public static ForgeConfigSpec.DoubleValue BORE_COST_VEIN_MINING;
  public static ForgeConfigSpec.DoubleValue BORE_COST_MAGNET;

  public static ForgeConfigSpec.DoubleValue PROBE_BASE_ENERGY_COST;
  public static ForgeConfigSpec.IntValue PROBE_BASE_ENERGY_CAPACITY;
  public static ForgeConfigSpec.DoubleValue PROBE_BASE_AREA;
  public static ForgeConfigSpec.DoubleValue PROBE_EFFECT_BATTERY;
  public static ForgeConfigSpec.DoubleValue PROBE_EFFECT_AREA;
  public static ForgeConfigSpec.DoubleValue PROBE_COST_AREA;

  public static ForgeConfigSpec.DoubleValue LOCATOR_BASE_COST;
  public static ForgeConfigSpec.IntValue    LOCATOR_BASE_CAPACITY;

  static {
    COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

    COMMON_BUILDER.comment("Handheld Bore settings").push(SUBCATEGORY_HANDHELD_BORE);
    BORE_BASE_ENERGY_COST = COMMON_BUILDER.comment("Base energy cost to mine a single block without upgrades")
        .defineInRange("baseEnergyCost", 10.0D, 0.0D, 100000.0D);
    BORE_BASE_ENERGY_CAPACITY = COMMON_BUILDER.comment("Base energy capacity without upgrades")
        .defineInRange("baseEnergyCapacity", 10000, 0, Integer.MAX_VALUE);
    BORE_BASE_VEIN_MINING = COMMON_BUILDER.comment("Base number of blocks reached by level 1 vein-mining upgrade")
        .defineInRange("baseVeinMining", 8, 2, 128);
    BORE_EFFECT_BATTERY = COMMON_BUILDER.comment("Effect of each battery upgrade (exponential)")
        .defineInRange("effectCapacity", 4.0D, 1.0001D, 100.0D);
    BORE_EFFECT_EFFICIENCY = COMMON_BUILDER.comment("Effect of each efficiency upgrade level (exponential)")
        .defineInRange("effectEfficiency", 1.2D, 1.0001D, 100.0D);
    BORE_EFFECT_VEIN_MINING = COMMON_BUILDER.comment("Effect of each vein-mining upgrade (exponential)")
        .defineInRange("effectVeinMining", 1.5D, 1.0001D, 10.0D);
    BORE_COST_EFFICIENCY = COMMON_BUILDER.comment("Energy cost of each efficiency upgrade (linear)")
        .defineInRange("costEfficiency", 0.15D, 0.0D, 100.0D);
    BORE_COST_SILK_TOUCH = COMMON_BUILDER.comment("Energy cost of using the silk touch upgrade")
        .defineInRange("costSilkTouch", 2.0D, 0.0D, 100.0D);
    BORE_COST_VEIN_MINING = COMMON_BUILDER.comment("Energy cost of using a vein-mining upgrade (flat)")
        .defineInRange("costVeinMining", 1.0D, 0.0D, 100.0D);
    BORE_COST_MAGNET = COMMON_BUILDER.comment("Energy cost of using the magnet upgrade")
        .defineInRange("costMagnet", 0.1D, 0.0D, 100.0D);
    COMMON_BUILDER.pop();

    COMMON_BUILDER.comment("Ore Probe settings").push(SUBCATEGORY_ORE_PROBE);
    PROBE_BASE_ENERGY_COST = COMMON_BUILDER.comment("Base energy cost for 1 use without upgrades")
        .defineInRange("baseEnergyCost", 100.0D, 0.0D, 1000000.0D);
    PROBE_BASE_ENERGY_CAPACITY = COMMON_BUILDER.comment("Base energy capacity without upgrades")
        .defineInRange("baseEnergyCapacity", 10000, 1, Integer.MAX_VALUE);
    PROBE_BASE_AREA = COMMON_BUILDER.comment("Base search radius without upgrades")
        .defineInRange("baseAreaRadius", 5.0D, 1.0D, 100.0D);
    PROBE_EFFECT_BATTERY = COMMON_BUILDER.comment("Effect of each battery upgrade (exponential)")
        .defineInRange("effectCapacity", 4.0D, 1.0D, 10.0D);
    PROBE_EFFECT_AREA = COMMON_BUILDER.comment("Effect on the search radius of each area upgrade (exponential)")
        .defineInRange("effectArea", 2.5D, 1.0D, 10.0D);
    PROBE_COST_AREA = COMMON_BUILDER.comment("Energy cost of each area upgrade level (linear)")
        .defineInRange("costArea", 3.0D, 1.0D, 1000.0D);
    COMMON_BUILDER.pop();

    COMMON_BUILDER.comment("Slime Chunk Locator settings").push(SUBCATEGORY_SLIME_CHUNK_LOCATOR);
    LOCATOR_BASE_COST = COMMON_BUILDER.comment("Base energy cost for 1 use without upgrades")
        .defineInRange("baseEnergyCost", 100.0D, 0.0D, 1000000.0D);
    LOCATOR_BASE_CAPACITY = COMMON_BUILDER.comment("Base energy capacity without upgrades")
        .defineInRange("baseEnergyCapacity", 10000, 10, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();

    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void register(ModLoadingContext context) {
    context.registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
  }

}
