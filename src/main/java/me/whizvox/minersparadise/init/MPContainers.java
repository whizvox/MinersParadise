package me.whizvox.minersparadise.init;

import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.inventory.container.ModificationStationContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class MPContainers {

  private static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MinersParadise.MODID);

  public static RegistryObject<ContainerType<ModificationStationContainer>> MODIFICATION_STATION =
    CONTAINERS.register("modification_station", createType(
      (windowId, inv, data) -> new ModificationStationContainer(windowId, inv, IWorldPosCallable.DUMMY))
    );

  public static void registerContainers(IEventBus eventBus) {
    CONTAINERS.register(eventBus);
  }

  // private helpers

  private static <T extends Container> Supplier<ContainerType<T>> createType(IContainerFactory<T> factory) {
    return () -> IForgeContainerType.create(factory);
  }

}
