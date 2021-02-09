package me.whizvox.minersparadise.util;

import me.whizvox.minersparadise.api.BoreUpgrade;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MPLang {

  public static final ITextComponent
    COMMAND_INVALID_BORE_STACK = new TranslationTextComponent("minersparadise.command.generic.bore.invalid_stack"),
    COMMAND_INVALID_ENERGY_STACK = new TranslationTextComponent("minersparadise.command.generic.energy.invalid_stack");

  public static ITextComponent COMMAND_UPGRADE_INSTALL_SUCCESS(BoreUpgrade upgrade, byte level) {
    return new TranslationTextComponent("minersparadise.command.upgrade.install.success", upgrade.getId(), StringUtil.formatAsRomanNumeral(level + 1));
  }

  public static ITextComponent COMMAND_UPGRADE_INSTALL_INVALID_LEVEL(byte level, byte max) {
    return new TranslationTextComponent("minersparadise.command.upgrade.install.invalid_level", level, max);
  }

  public static ITextComponent COMMAND_UPGRADE_ACTIVATE_NOT_INSTALLED(BoreUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.activate.not_installed", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_ACTIVATE_SUCCESS(BoreUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.activate.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_DEACTIVATE_SUCCESS(BoreUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.deactivate.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_REMOVE_SUCCESS(BoreUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.remove.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_REMOVE_NOT_INSTALLED(BoreUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.remove.not_installed", upgrade.getId());
  }

}
