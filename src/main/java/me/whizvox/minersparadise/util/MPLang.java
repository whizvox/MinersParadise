package me.whizvox.minersparadise.util;

import me.whizvox.minersparadise.api.IGadgetUpgrade;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MPLang {

  public static final ITextComponent
    COMMAND_INVALID_BORE_STACK = new TranslationTextComponent("minersparadise.command.generic.bore.invalid_stack"),
    COMMAND_INVALID_ENERGY_STACK = new TranslationTextComponent("minersparadise.command.generic.energy.invalid_stack"),
    TOOLTIP_GADGET_SHOW_UPGRADES = new TranslationTextComponent("minersparadise.gadget.tooltip.show_upgrades", "shift"),
    TOOLTIP_GADGET_NO_UPGRADES = new TranslationTextComponent("minersparadise.gadget.tooltip.no_upgrades"),
    TOOLTIP_GADGET_UPGRADES_LIST = new TranslationTextComponent("minersparadise.gadget.tooltip.upgrades_list");

  public static ITextComponent COMMAND_UPGRADE_INSTALL_SUCCESS(IGadgetUpgrade upgrade, byte level) {
    return new TranslationTextComponent("minersparadise.command.upgrade.install.success", upgrade.getId(), StringUtil.formatAsRomanNumeral(level + 1));
  }

  public static ITextComponent COMMAND_UPGRADE_INSTALL_INVALID_LEVEL(byte level, byte max) {
    return new TranslationTextComponent("minersparadise.command.upgrade.install.invalid_level", level, max);
  }

  public static ITextComponent COMMAND_UPGRADE_ACTIVATE_NOT_INSTALLED(IGadgetUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.activate.not_installed", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_ACTIVATE_SUCCESS(IGadgetUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.activate.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_DEACTIVATE_SUCCESS(IGadgetUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.deactivate.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_REMOVE_SUCCESS(IGadgetUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.remove.success", upgrade.getId());
  }

  public static ITextComponent COMMAND_UPGRADE_REMOVE_NOT_INSTALLED(IGadgetUpgrade upgrade) {
    return new TranslationTextComponent("minersparadise.command.upgrade.remove.not_installed", upgrade.getId());
  }

}
