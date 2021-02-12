package me.whizvox.minersparadise.api;

import net.minecraft.util.ResourceLocation;

public class GadgetUpgrade implements IGadgetUpgrade {

  private ResourceLocation id;
  private byte maxLevel;

  public GadgetUpgrade(ResourceLocation id, byte maxLevel) {
    this.id = id;
    this.maxLevel = maxLevel;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public byte getMaxLevel() {
    return maxLevel;
  }

}
