package me.whizvox.minersparadise;

import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.init.GadgetUpgrades;
import me.whizvox.minersparadise.item.HandheldBoreItem;
import me.whizvox.minersparadise.util.NBTUtil;
import me.whizvox.minersparadise.util.UpgradeUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.energy.CapabilityEnergy;
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

  private static boolean breakingBlocks = false;

  @SubscribeEvent
  public static void onBlockBreak(BlockEvent.BreakEvent event) {
    if (!breakingBlocks && !event.getWorld().isRemote() && event.getPlayer() instanceof ServerPlayerEntity) {
      ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
      ItemStack heldStack = event.getPlayer().getHeldItemMainhand();
      if (heldStack.getItem() instanceof HandheldBoreItem) {
        heldStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
          if (energy.getEnergyStored() > 0) {
            heldStack.getCapability(MPCapabilities.GADGET).ifPresent(gadget -> {
              boolean isMagnetOn = gadget.isActive(GadgetUpgrades.MAGNET);
              boolean isSilkTouchEnabled = gadget.isActive(GadgetUpgrades.SILK_TOUCH);
              byte fortuneLevel = gadget.getActiveLevel(GadgetUpgrades.FORTUNE);
              HashSet<BlockPos> blocksToMine = new HashSet<>();
              blocksToMine.add(event.getPos());
              if (gadget.isActive(GadgetUpgrades.AREA)) {
                RayTraceResult rayTrace = event.getPlayer().pick(event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue(), 1F, false);
                if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
                  Direction.Axis axis = ((BlockRayTraceResult) rayTrace).getFace().getAxis();
                  int radius = UpgradeUtil.getAreaRadius(gadget.getActiveLevel(GadgetUpgrades.AREA));
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

              if (gadget.isActive(GadgetUpgrades.VEIN_MINING)) {
                breadthFirstBlockSearch(event.getWorld(), event.getState().getBlock(), UpgradeUtil.getMaxVeinMineCount(gadget.getActiveLevel(GadgetUpgrades.VEIN_MINING)), blocksToMine, event.getPos());
              }

              breakingBlocks = true;
              Set<ItemStack> drops = new HashSet<>();
              collectDrops((ServerWorld) event.getWorld(), blocksToMine, isSilkTouchEnabled, fortuneLevel, drops);
              breakingBlocks = false;
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

              energy.receiveEnergy(-UpgradeUtil.getFinalEnergyCost(gadget), false);
              NBTUtil.setEnergy(heldStack, energy.getEnergyStored());

            });
          }
        });
      }
    }
  }

  private static void collectDrops(ServerWorld world, Set<BlockPos> positions, boolean silkTouch, int fortuneLevel, Set<ItemStack> drops) {
    ItemStack stack = new ItemStack(Items.NETHERITE_PICKAXE);
    if (silkTouch) {
      stack.addEnchantment(Enchantments.SILK_TOUCH, 0);
    }
    if (fortuneLevel > -1) {
      stack.addEnchantment(Enchantments.FORTUNE, fortuneLevel);
    }
    positions.forEach(pos -> {
      LootContext.Builder builder = new LootContext.Builder(world)
        .withRandom(world.getRandom())
        .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(pos))
        .withParameter(LootParameters.TOOL, stack)
        .withNullableParameter(LootParameters.THIS_ENTITY, new FakePlayer(world, MinersParadise.FAKE_GAME_PROFILE))
        .withNullableParameter(LootParameters.BLOCK_ENTITY, world.getTileEntity(pos));
      drops.addAll(world.getBlockState(pos).getDrops(builder));
    });
  }

}
