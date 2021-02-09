package me.whizvox.minersparadise;

import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.common.CommandMinersParadise;
import me.whizvox.minersparadise.init.MPItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinersParadise.MODID)
public class MinersParadise
{

  public static final String MODID = "minersparadise";

  // Directly reference a log4j logger.
  private static final Logger LOGGER = LogManager.getLogger();

  public MinersParadise() {
    MPConfig.register(ModLoadingContext.get());

    MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

    MPItems.register();

    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
  }

  public void preInit(FMLCommonSetupEvent event) {
    MPCapabilities.register();
  }

  public void registerCommands(RegisterCommandsEvent event) {
    event.getDispatcher().register(CommandMinersParadise.register());
  }

}
