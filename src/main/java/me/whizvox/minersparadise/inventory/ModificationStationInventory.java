package me.whizvox.minersparadise.inventory;

import me.whizvox.minersparadise.api.IUpgradableGadget;
import me.whizvox.minersparadise.capability.MPCapabilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public class ModificationStationInventory implements IInventory {

  private ItemStack[] stacks;

  public ModificationStationInventory() {
    stacks = new ItemStack[22];
  }

  public boolean hasGadget() {
    return !stacks[0].isEmpty();
  }

  public Optional<IUpgradableGadget> getGadget() {
    return stacks[0].getCapability(MPCapabilities.GADGET).resolve();
  }

  @Override
  public int getSizeInventory() {
    return stacks.length;
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack stack : stacks) {
      if (!stack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    if (index >= 0 && index < getSizeInventory()) {
      return stacks[index];
    }
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack decrStackSize(int index, int count) {
    return getStackInSlot(index).split(count);
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    if (index >= 0 && index < getSizeInventory()) {
      ItemStack stack = stacks[index];
      stacks[index] = ItemStack.EMPTY;
      return stack;
    }
    return ItemStack.EMPTY;
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (index >= 0 && index < getSizeInventory()) {
      stacks[index] = stack;
    }
  }

  @Override
  public void markDirty() {
  }

  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return true;
  }

  @Override
  public void clear() {
    Arrays.fill(stacks, ItemStack.EMPTY);
  }

}
