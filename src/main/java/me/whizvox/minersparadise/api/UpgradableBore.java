package me.whizvox.minersparadise.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpgradableBore implements IUpgradableBore {

  private Map<BoreUpgrade, Byte> installed;
  private Set<BoreUpgrade> active;

  public UpgradableBore() {
    installed = new HashMap<>();
    active = new HashSet<>();
  }

  @Override
  public Set<BoreUpgrade> getInstalled() {
    return installed.keySet();
  }

  @Override
  public Set<BoreUpgrade> getActive() {
    return active;
  }

  @Override
  public boolean isInstalled(BoreUpgrade upgrade) {
    return installed.containsKey(upgrade);
  }

  @Override
  public boolean isActive(BoreUpgrade upgrade) {
    return active.contains(upgrade);
  }

  @Override
  public byte getInstalledLevel(BoreUpgrade upgrade) {
    return isInstalled(upgrade) ? installed.get(upgrade) : -1;
  }

  @Override
  public byte getActiveLevel(BoreUpgrade upgrade) {
    return isActive(upgrade) ? getInstalledLevel(upgrade) : -1;
  }

  @Override
  public boolean install(BoreUpgrade upgrade, byte level) {
    if (level >= 0 && level <= upgrade.getMaxLevel()) {
      installed.put(upgrade, level);
      return true;
    }
    return false;
  }

  @Override
  public boolean activate(BoreUpgrade upgrade, boolean activate) {
    if (installed.containsKey(upgrade)) {
      if (activate) {
        active.add(upgrade);
      } else {
        active.remove(upgrade);
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean remove(BoreUpgrade upgrade) {
    boolean res = installed.remove(upgrade) != null;
    if (res) {
      active.remove(upgrade);
    }
    return res;
  }

}
