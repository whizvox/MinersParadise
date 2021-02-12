package me.whizvox.minersparadise.api;

public class GadgetUpgradeData {

  private IGadgetUpgrade upgrade;
  private byte level;

  public GadgetUpgradeData(IGadgetUpgrade upgrade, byte level) {
    this.upgrade = upgrade;
    this.level = level;
  }

  public GadgetUpgradeData() {
  }

  public IGadgetUpgrade getUpgrade() {
    return upgrade;
  }

  public byte getLevel() {
    return level;
  }

  public void setUpgrade(IGadgetUpgrade upgrade) {
    this.upgrade = upgrade;
  }

  public void setLevel(byte level) {
    this.level = level;
  }

}
