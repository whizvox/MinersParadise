package me.whizvox.minersparadise.inventory.slot;

import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.inventory.ModificationStationInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ModificationStationGadgetSlot extends Slot {

  public ModificationStationGadgetSlot(ModificationStationInventory inventory, int index, int xPosition, int yPosition) {
    super(inventory, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return stack.getCapability(MPCapabilities.GADGET).map(gadget -> true).orElse(false);
  }

}
