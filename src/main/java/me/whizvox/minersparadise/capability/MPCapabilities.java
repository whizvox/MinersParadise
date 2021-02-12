package me.whizvox.minersparadise.capability;

import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.api.IGadgetUpgrade;
import me.whizvox.minersparadise.api.IUpgradableGadget;
import me.whizvox.minersparadise.api.UpgradableGadget;
import me.whizvox.minersparadise.capability.storage.GadgetUpgradeStorage;
import me.whizvox.minersparadise.capability.storage.UpgradableGadgetStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class MPCapabilities {

  @CapabilityInject(IUpgradableGadget.class)
  public static Capability<IUpgradableGadget> GADGET;

  @CapabilityInject(IGadgetUpgrade.class)
  public static Capability<GadgetUpgradeData> UPGRADE_DATA;

  public static void register() {
    CapabilityManager.INSTANCE.register(IUpgradableGadget.class, new UpgradableGadgetStorage(), UpgradableGadget::new);
    CapabilityManager.INSTANCE.register(GadgetUpgradeData.class, new GadgetUpgradeStorage(), GadgetUpgradeData::new);
  }

}
