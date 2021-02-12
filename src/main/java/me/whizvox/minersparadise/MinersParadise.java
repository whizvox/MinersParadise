package me.whizvox.minersparadise;

import com.mojang.authlib.GameProfile;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.common.CommandMinersParadise;
import me.whizvox.minersparadise.common.GadgetUpgradeRegistry;
import me.whizvox.minersparadise.init.GadgetUpgrades;
import me.whizvox.minersparadise.init.MPItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(MinersParadise.MODID)
public class MinersParadise {

  public static final String MODID = "minersparadise";
  public static final GameProfile FAKE_GAME_PROFILE = new GameProfile(UUID.nameUUIDFromBytes(MODID.getBytes()), MODID);

  private static final Logger LOGGER = LogManager.getLogger();

  private GadgetUpgradeRegistry gadgetUpgradeRegistry;

  public MinersParadise() {
    if (instance != null) {
      throw new IllegalStateException("Mod has already initialized");
    }
    instance = this;
    MPConfig.register(ModLoadingContext.get());
    gadgetUpgradeRegistry = new GadgetUpgradeRegistry();
    GadgetUpgrades.register(gadgetUpgradeRegistry);


    MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

    MPItems.register();

    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
  }

  public GadgetUpgradeRegistry getGadgetUpgradeRegistry() {
    return gadgetUpgradeRegistry;
  }

  public void preInit(FMLCommonSetupEvent event) {
    MPCapabilities.register();
  }

  public void registerCommands(RegisterCommandsEvent event) {
    event.getDispatcher().register(CommandMinersParadise.register());
  }

  private static MinersParadise instance = null;

  public static MinersParadise getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Instance has not yet been initialized");
    }
    return instance;
  }

}
