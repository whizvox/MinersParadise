package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.api.IBoreTier;
import net.minecraft.item.ItemTier;

public enum BoreTier implements IBoreTier {

  IRON(ItemTier.IRON.getHarvestLevel(), ItemTier.IRON.getEfficiency(), 8),
  GOLD(ItemTier.GOLD.getHarvestLevel(), ItemTier.GOLD.getEfficiency(), 13),
  DIAMOND(ItemTier.DIAMOND.getHarvestLevel(), ItemTier.DIAMOND.getEfficiency(), 18),
  NETHERITE(ItemTier.NETHERITE.getHarvestLevel(), ItemTier.NETHERITE.getEfficiency(), 23);

  private int harvestLevel;
  private float baseEfficiency;
  private short upgradeSlots;

  BoreTier(int harvestLevel, float baseEfficiency, int upgradeSlots) {
    this.harvestLevel = harvestLevel;
    this.baseEfficiency = baseEfficiency;
    this.upgradeSlots = (short) upgradeSlots;
  }

  @Override
  public int getHarvestLevel() {
    return harvestLevel;
  }

  @Override
  public float getBaseEfficiency() {
    return baseEfficiency;
  }

  @Override
  public short getUpgradeSlots() {
    return upgradeSlots;
  }

}
