package me.whizvox.minersparadise.inventory.container;

import me.whizvox.minersparadise.init.MPContainers;
import me.whizvox.minersparadise.inventory.ModificationStationInventory;
import me.whizvox.minersparadise.inventory.slot.ModificationStationGadgetSlot;
import me.whizvox.minersparadise.inventory.slot.ModificationStationUpgradeSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;

public class ModificationStationContainer extends Container {

  private ModificationStationInventory inventory;
  private PlayerEntity player;
  private IWorldPosCallable worldPosCallable;

  public ModificationStationContainer(int windowId, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
    super(MPContainers.MODIFICATION_STATION.get(), windowId);
    player = playerInventory.player;
    this.worldPosCallable = worldPosCallable;
    inventory = new ModificationStationInventory();

    // player hotbar
    for (int x = 0; x < 9; x++) {
      addSlot(new Slot(playerInventory, x, 8 + x * 18, 172));
    }
    // player inventory
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        addSlot(new Slot(playerInventory, 9 + x + y * 3, 8 + x * 18, 114 + y * 18));
      }
    }
    // modification station inventory
    addSlot(new ModificationStationGadgetSlot(inventory, 0, 30, 80));
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 7; x++) {
        addSlot(new ModificationStationUpgradeSlot(inventory, 1 + x + y * 3, 30 + x * 18, 18 + y * 18));
      }
    }
  }

  @Override
  public boolean canInteractWith(PlayerEntity player) {
    return true;
  }

  public void onUpgradeAdded() {

  }

  /*@Override
  public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex) {
    ItemStack stack = player.inventory.getStackInSlot(sourceSlotIndex);
    slot
  }*/

}
