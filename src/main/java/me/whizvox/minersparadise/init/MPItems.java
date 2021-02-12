package me.whizvox.minersparadise.init;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.item.BoreTier;
import me.whizvox.minersparadise.item.GadgetUpgradeItem;
import me.whizvox.minersparadise.item.HandheldBoreItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MPItems {

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinersParadise.MODID);

  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_IRON = ITEMS.register("handheld_bore_iron", () -> new HandheldBoreItem(BoreTier.IRON));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_GOLD = ITEMS.register("handheld_bore_gold", () -> new HandheldBoreItem(BoreTier.GOLD));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_DIAMOND = ITEMS.register("handheld_bore_diamond", () -> new HandheldBoreItem(BoreTier.DIAMOND));
  public static final RegistryObject<HandheldBoreItem> HANDHELD_BORE_NETHERITE = ITEMS.register("handheld_bore_netherite", () -> new HandheldBoreItem(BoreTier.NETHERITE));
  public static final RegistryObject<GadgetUpgradeItem> GADGET_UPGRADE = ITEMS.register("gadget_upgrade", GadgetUpgradeItem::new);

  public static void register() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

}
