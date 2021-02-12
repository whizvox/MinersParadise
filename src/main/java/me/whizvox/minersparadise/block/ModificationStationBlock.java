package me.whizvox.minersparadise.block;

import me.whizvox.minersparadise.inventory.container.ModificationStationContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModificationStationBlock extends Block {

  public ModificationStationBlock() {
    super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(2.0F, 4.0F));
  }

  @Nullable
  @Override
  public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
    return new SimpleNamedContainerProvider((id, inventory, player) -> {
      return new ModificationStationContainer(id, inventory, IWorldPosCallable.of(worldIn, pos));
    }, new TranslationTextComponent("minersparadise.container.modification_station"));
  }

}
