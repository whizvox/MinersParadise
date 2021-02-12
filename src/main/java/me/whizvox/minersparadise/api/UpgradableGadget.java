package me.whizvox.minersparadise.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UpgradableGadget implements IUpgradableGadget {

  private Set<GadgetUpgradeData> installed;
  private Map<IGadgetUpgrade, Byte> activeLevels;

  public UpgradableGadget() {
    installed = new HashSet<>();
    activeLevels = new HashMap<>();
  }

  @Override
  public Set<GadgetUpgradeData> getInstalled() {
    return installed;
  }

  @Override
  public Set<GadgetUpgradeData> getActive() {
    return installed.stream().filter(data -> isActive(data.getUpgrade())).collect(Collectors.toSet());
  }

  @Override
  public boolean isSupported(IGadgetUpgrade upgrade, byte level) {
    return false;
  }

  @Override
  public boolean isInstalled(IGadgetUpgrade upgrade) {
    return activeLevels.containsKey(upgrade);
  }

  @Override
  public boolean isActive(IGadgetUpgrade upgrade) {
    return getActiveLevel(upgrade) >= 0;
  }

  @Override
  public byte getActiveLevel(IGadgetUpgrade upgrade) {
    return activeLevels.getOrDefault(upgrade, (byte) -1);
  }

  @Override
  public boolean install(GadgetUpgradeData data) {
    if (isSupported(data.getUpgrade(), data.getLevel())) {
      installed.add(data);
      activeLevels.put(data.getUpgrade(), data.getLevel());
      return true;
    }
    return false;
  }

  @Override
  public boolean activate(IGadgetUpgrade upgrade, byte level) {
    if (isInstalled(upgrade) && isSupported(upgrade, level)) {
      activeLevels.put(upgrade, level);
      return true;
    }
    return false;
  }

  @Override
  public boolean remove(IGadgetUpgrade upgrade) {
    if (isInstalled(upgrade)) {
      installed.removeIf(data -> data.getUpgrade() == upgrade);
      return true;
    }
    return false;
  }

}
