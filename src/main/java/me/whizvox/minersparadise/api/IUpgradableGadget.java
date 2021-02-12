package me.whizvox.minersparadise.api;

import java.util.Set;

public interface IUpgradableGadget {

  // accessors

  Set<GadgetUpgradeData> getInstalled();

  Set<GadgetUpgradeData> getActive();

  boolean isSupported(IGadgetUpgrade upgrade, byte level);

  boolean isInstalled(IGadgetUpgrade upgrade);

  boolean isActive(IGadgetUpgrade upgrade);

  byte getActiveLevel(IGadgetUpgrade upgrade);

  // mutators

  boolean install(GadgetUpgradeData data);

  boolean activate(IGadgetUpgrade upgrade, byte level);

  boolean remove(IGadgetUpgrade upgrade);

}
