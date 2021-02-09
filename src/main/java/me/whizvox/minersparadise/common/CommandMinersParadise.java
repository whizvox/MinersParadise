package me.whizvox.minersparadise.common;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.util.NBTUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.whizvox.minersparadise.util.MPLang.*;

public class CommandMinersParadise {

  public static LiteralArgumentBuilder<CommandSource> register() {
    return Commands.literal("minersparadise")
      .then(registerUpgrade())
      .then(registerEnergize());
  }

  // delegated subcommand registry helpers

  static ArgumentBuilder<CommandSource, ?> registerUpgrade() {
    return Commands.literal("upgrade")
      .requires(cs -> cs.hasPermissionLevel(2))
      .then(Commands.literal("install")
        .then(Commands.argument("id", BORE_UPGRADE_TYPE)
          .then(Commands.argument("level", BORE_UPGRADE_LEVEL_TYPE)
            .executes(ctx -> installUpgrade(ctx, ctx.getArgument("id", BoreUpgrade.class), ctx.getArgument("level", Byte.class))))
          .executes(ctx -> installUpgrade(ctx, ctx.getArgument("id", BoreUpgrade.class), (byte) 0))))
      .then(Commands.literal("activate")
        .then(Commands.argument("id", BORE_UPGRADE_TYPE)
          .executes(ctx -> activateUpgrade(ctx, ctx.getArgument("id", BoreUpgrade.class), true))))
      .then(Commands.literal("deactivate")
        .then(Commands.argument("id", BORE_UPGRADE_TYPE)
          .executes(ctx -> activateUpgrade(ctx, ctx.getArgument("id", BoreUpgrade.class), false))))
      .then(Commands.literal("remove")
        .then(Commands.argument("id", BORE_UPGRADE_TYPE)
          .executes(ctx -> removeUpgrade(ctx, ctx.getArgument("id", BoreUpgrade.class)))));
  }

  static ArgumentBuilder<CommandSource, ?> registerEnergize() {
    return Commands.literal("energize")
      .requires(cs -> cs.hasPermissionLevel(2))
      .executes(ctx -> energizeItem(ctx, Integer.MAX_VALUE))
      .then(Commands.argument("amount", ENERGY_LEVEL_TYPE)
        .executes(ctx -> energizeItem(ctx, ctx.getArgument("amount", Integer.class)))
      );
  }

  // types used for parsing command arguments

  private static ArgumentType<BoreUpgrade> BORE_UPGRADE_TYPE = new ArgumentType<BoreUpgrade>() {
    @Override
    public BoreUpgrade parse(StringReader reader) throws CommandSyntaxException {
      String id = reader.readString();
      BoreUpgrade upgrade = BoreUpgrade.getFromId(id);
      if (upgrade == null) {
        throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect(), new TranslationTextComponent("minersparadise.command.upgrade.invalid_upgrade", id));
      }
      return upgrade;
    }
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return ISuggestionProvider.suggest(Arrays.stream(BoreUpgrade.values()).map(BoreUpgrade::getId), builder);
    }
  };
  private static ArgumentType<Byte> BORE_UPGRADE_LEVEL_TYPE = reader -> {
    byte level = (byte) reader.readInt();
    if (level >= 0) {
      return level;
    }
    throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow(), new TranslationTextComponent("minersparadise.command.upgrade.invalid_level"));
  };
  private static ArgumentType<Integer> ENERGY_LEVEL_TYPE = StringReader::readInt;

  // private helpers

  private static void sendMessage(CommandSource source, ITextComponent message) {
    source.sendFeedback(message, true);
  }

  private static int installUpgrade(CommandContext<CommandSource> ctx, BoreUpgrade upgrade, byte level) throws CommandSyntaxException {
    CommandSource source = ctx.getSource();
    if (level > upgrade.getMaxLevel()) {
      throw new CommandSyntaxException(
        CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh(),
        COMMAND_UPGRADE_INSTALL_INVALID_LEVEL(level, (byte) upgrade.getMaxLevel())
      );
    }
    ItemStack stack = source.asPlayer().getHeldItemMainhand();
    AtomicBoolean hasCapability = new AtomicBoolean(false);
    stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
      hasCapability.set(true);
      bore.install(upgrade, level);
      NBTUtil.setUpgrades(stack, bore);
      sendMessage(source, COMMAND_UPGRADE_INSTALL_SUCCESS(upgrade, level));
    });
    if (!hasCapability.get()) {
      sendMessage(source, COMMAND_INVALID_BORE_STACK);
      return -1;
    }
    return 0;
  }

  private static int activateUpgrade(CommandContext<CommandSource> ctx, BoreUpgrade upgrade, boolean activate) throws CommandSyntaxException {
    CommandSource source = ctx.getSource();
    ItemStack stack = source.asPlayer().getHeldItemMainhand();
    AtomicBoolean hasCapability = new AtomicBoolean(false);
    stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
      hasCapability.set(true);
      if (!bore.isInstalled(upgrade)) {
        sendMessage(source, COMMAND_UPGRADE_ACTIVATE_NOT_INSTALLED(upgrade));
      } else {
        bore.activate(upgrade, activate);
        NBTUtil.setUpgrades(stack, bore);
        if (activate) {
          sendMessage(source, COMMAND_UPGRADE_ACTIVATE_SUCCESS(upgrade));
        } else {
          sendMessage(source, COMMAND_UPGRADE_DEACTIVATE_SUCCESS(upgrade));
        }
      }
    });
    if (!hasCapability.get()) {
      sendMessage(source, COMMAND_INVALID_BORE_STACK);
      return -1;
    }
    return 0;
  }

  private static int removeUpgrade(CommandContext<CommandSource> ctx, BoreUpgrade upgrade) throws CommandSyntaxException {
    CommandSource source = ctx.getSource();
    ServerPlayerEntity player = source.asPlayer();
    ItemStack stack = player.getHeldItemMainhand();
    AtomicBoolean hasCapability = new AtomicBoolean(false);
    stack.getCapability(MPCapabilities.UPGRADABLE_BORE).ifPresent(bore -> {
      hasCapability.set(true);
      if (bore.isInstalled(upgrade)) {
        bore.remove(upgrade);
        NBTUtil.setUpgrades(stack, bore);
        sendMessage(source, COMMAND_UPGRADE_REMOVE_SUCCESS(upgrade));
      } else {
        sendMessage(source, COMMAND_UPGRADE_REMOVE_NOT_INSTALLED(upgrade));
      }
    });
    if (!hasCapability.get()) {
      sendMessage(source, COMMAND_INVALID_BORE_STACK);
      return -1;
    }
    return 0;
  }

  private static int energizeItem(CommandContext<CommandSource> ctx, int amount) throws CommandSyntaxException {
    CommandSource source = ctx.getSource();
    ServerPlayerEntity player = source.asPlayer();
    ItemStack stack = player.getHeldItemMainhand();
    AtomicBoolean hasCapability = new AtomicBoolean(false);
    stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energy -> {
      hasCapability.set(true);
      energy.receiveEnergy(amount, false);
      NBTUtil.setEnergy(stack, energy);
      source.sendFeedback(new TranslationTextComponent("minersparadise.command.energize.success", energy.getEnergyStored()), true);
    });
    if (!hasCapability.get()) {
      sendMessage(source, COMMAND_INVALID_ENERGY_STACK);
      return -1;
    }
    return 0;
  }

}
