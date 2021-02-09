package me.whizvox.minersparadise.capability;

import me.whizvox.minersparadise.api.IUpgradableBore;
import me.whizvox.minersparadise.api.UpgradableBore;
import me.whizvox.minersparadise.capability.storage.UpgradableBoreStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class MPCapabilities {

  @CapabilityInject(IUpgradableBore.class)
  public static Capability<IUpgradableBore> UPGRADABLE_BORE;

  public static void register() {
    CapabilityManager.INSTANCE.register(IUpgradableBore.class, new UpgradableBoreStorage(), UpgradableBore::new);
  }

}
