package me.whizvox.minersparadise.item.group;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.MinersParadise;
import me.whizvox.minersparadise.init.MPItems;
import me.whizvox.minersparadise.item.HandheldBoreItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class MPItemGroup extends ItemGroup {

  private MPItemGroup() {
    super(MinersParadise.MODID);
  }

  @Override
  public ItemStack createIcon() {
    HandheldBoreItem item = MPItems.HANDHELD_BORE_DIAMOND.get();
    ItemStack stack = new ItemStack(item);
    // very informal way of doing this, but bypasses the transfer rate limitation
    stack.getOrCreateTag().putInt("energy", MPConfig.BORE_BASE_ENERGY_CAPACITY.get());
    return stack;
  }

  private static LazyOptional<MPItemGroup> instance = LazyOptional.of(MPItemGroup::new);

  public static MPItemGroup getInstance() {
    return instance.orElse(null);
  }

}
