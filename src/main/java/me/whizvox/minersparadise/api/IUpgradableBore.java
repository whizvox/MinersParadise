package me.whizvox.minersparadise.api;

import java.util.Set;

public interface IUpgradableBore {

  // accessors

  Set<BoreUpgrade> getInstalled();

  Set<BoreUpgrade> getActive();

  boolean isInstalled(BoreUpgrade upgrade);

  boolean isActive(BoreUpgrade upgrade);

  byte getInstalledLevel(BoreUpgrade upgrade);

  byte getActiveLevel(BoreUpgrade upgrade);

  // mutators

  boolean install(BoreUpgrade upgrade, byte level);

  boolean activate(BoreUpgrade upgrade, boolean active);

  boolean remove(BoreUpgrade upgrade);

}
