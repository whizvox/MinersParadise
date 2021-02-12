package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.item.group.MPItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ProspectingProbeItem extends Item {

  public ProspectingProbeItem() {
    super(new Item.Properties()
      .group(MPItemGroup.getInstance())
      .maxStackSize(1)
    );
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    return null;
  }

}
