package me.whizvox.minersparadise.inventory.slot;

import me.whizvox.minersparadise.api.GadgetUpgradeData;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.inventory.ModificationStationInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ModificationStationUpgradeSlot extends Slot {

  public ModificationStationUpgradeSlot(ModificationStationInventory inv, int index, int xPosition, int yPosition) {
    super(inv, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return ((ModificationStationInventory) inventory).getGadget().map(gadget -> {
      GadgetUpgradeData upgradeData = stack.getCapability(MPCapabilities.UPGRADE_DATA).resolve().get();
      return true;
    }).orElse(false);
  }

  @Override
  public boolean isEnabled() {
    return ((ModificationStationInventory) inventory).hasGadget();
  }

}
