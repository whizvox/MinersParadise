package me.whizvox.minersparadise;

import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.item.HandheldBoreItem;
import me.whizvox.minersparadise.util.NBTUtil;
import me.whizvox.minersparadise.util.UpgradeUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class MPEventHandler {

  private static final Vector3i[] DIRECTIONS;
  static {
    ArrayList<Vector3i> directions = new ArrayList<>();
    for (int xoff = -1; xoff <= 1; xoff++) {
      for (int yoff = -1; yoff <= 1; yoff++) {
        for (int zoff = -1; zoff <= 1; zoff++) {
          if (xoff == 0 && yoff == 0 && zoff == 0) {
            continue;
          }
          directions.add(new Vector3i(xoff, yoff, zoff));
        }
      }
    }
    DIRECTIONS = directions.toArray(new Vector3i[0]);
  }

  private static void breadthFirstBlockSearch(IBlockReader world, Block check, int maxBlocks, Set<BlockPos> output, BlockPos currentPos) {
    Queue<BlockPos> q = new ArrayDeque<>();
    q.add(currentPos);
    while (!q.isEmpty()) {
      BlockPos pos = q.remove();
      for (Vector3i direction : DIRECTIONS) {
        BlockPos pos0 = pos.add(direction);
        if (!output.contains(pos0) && world.getBlockState(pos0).getBlock() == check) {
          output.add(pos0);
          if (output.size() >= maxBlocks) {
            return;
          }
          q.add(pos0);
        }
      }
    }
  }

  private static boolean breakingBlock = false;
  private static ArrayList<ItemStack> drops = new ArrayList<>();

  @SubscribeEvent
  public static void onBlockBreak(BlockEvent.BreakEvent event) {
    if (!breakingBlock && !event.getWorld().isRemote() && event.getPlayer() instanceof ServerPlayerEntity) {
      ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
      ItemStack heldStack = event.getPlayer().getHeldItemMainhand();
      if (heldStack.getItem() instanceof HandheldBoreItem) {
        heldStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
          if (energy.getEnergyStored() > 0) {
            heldStack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
              boolean isMagnetOn = bore.isActive(BoreUpgrade.MAGNET);
              HashSet<BlockPos> blocksToMine = new HashSet<>();
              blocksToMine.add(event.getPos());
              if (bore.isActive(BoreUpgrade.AREA)) {
                RayTraceResult rayTrace = event.getPlayer().pick(event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue(), 1F, false);
                if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
                  Direction.Axis axis = ((BlockRayTraceResult) rayTrace).getFace().getAxis();
                  int radius = UpgradeUtil.getAreaRadius(bore.getActiveLevel(BoreUpgrade.AREA));
                  for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                      if (i != 0 || j != 0) {
                        Vector3i offset;
                        switch (axis) {
                          case X:
                            offset = new Vector3i(0, i, j);
                            break;
                          case Y:
                            offset = new Vector3i(i, 0, j);
                            break;
                          default:
                            offset = new Vector3i(i, j, 0);
                        }
                      }
                    }
                  }
                }
              }

              if (bore.isActive(BoreUpgrade.VEIN_MINING)) {
                breadthFirstBlockSearch(event.getWorld(), event.getState().getBlock(), UpgradeUtil.getMaxVeinMineCount(bore.getActiveLevel(BoreUpgrade.VEIN_MINING)), blocksToMine, event.getPos());
              }

              breakingBlock = true;
              drops.clear();
              blocksToMine.forEach(player.interactionManager::tryHarvestBlock);
              breakingBlock = false;
              if (isMagnetOn) {
                boolean dropInWorld = false;
                for (ItemStack stack : drops) {
                  if (!dropInWorld && !player.inventory.addItemStackToInventory(stack)) {
                    dropInWorld = true;
                  } else {
                    Block.spawnAsEntity((World) event.getWorld(), event.getPos(), stack);
                  }
                }
              } else {
                drops.forEach(stack -> Block.spawnAsEntity((World) event.getWorld(), event.getPos(), stack));
              }

              energy.receiveEnergy(-UpgradeUtil.getFinalEnergyCost(bore), false);
              NBTUtil.setEnergy(heldStack, energy.getEnergyStored());

            });
          }
        });
      }
    }
  }

  @SubscribeEvent
  public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
    if (breakingBlock && event.getEntity() instanceof ItemEntity) {
      drops.add(((ItemEntity) event.getEntity()).getItem());
      event.setCanceled(true);
    }
  }

}
