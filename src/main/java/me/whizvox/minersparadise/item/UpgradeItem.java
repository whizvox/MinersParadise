package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.MPConfig;
import me.whizvox.minersparadise.api.BoreUpgrade;
import me.whizvox.minersparadise.item.group.MPItemGroup;
import me.whizvox.minersparadise.util.StringUtil;
import me.whizvox.minersparadise.util.UpgradeUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UpgradeItem extends Item {

  private BoreUpgrade upgrade;
  private byte level;

  private ITextComponent displayName;
  private List<ITextComponent> information;

  public UpgradeItem(BoreUpgrade upgrade, byte level) {
    super(new Item.Properties().group(MPItemGroup.getInstance()));
    this.upgrade = upgrade;
    this.level = level;

    displayName = new TranslationTextComponent("item.minersparadise.bore_upgrade", new TranslationTextComponent("minersparadise.bore.upgrade." + upgrade.getId()), StringUtil.formatAsRomanNumeral(level + 1));
    information = new ArrayList<>();
    Object[] line1Args = new Object[0];;
    IFormattableTextComponent line2 = null;

    switch (upgrade) {
      case AREA:
        line1Args = new Object[] {UpgradeUtil.getAreaRadius(level)};
        break;
      case EFFICIENCY:
        line1Args = new Object[] {StringUtil.formatAsCleanFloat(UpgradeUtil.getEfficiencyCostEffect(level))};
        line2 = new TranslationTextComponent(
            "minersparadise.bore.upgrade.generic.cost",
            StringUtil.formatAsCleanFloat(UpgradeUtil.getEfficiencyCostEffect(level))
        );
        break;
      case ENERGY_CAPACITY:
        line1Args = new Object[] {StringUtil.formatAsCompactNotation(UpgradeUtil.getMaxEnergyCapacity(level))};
        break;
      case VEIN_MINING:
        line1Args = new Object[] {UpgradeUtil.getMaxVeinMineCount(level)};
        line2 = new TranslationTextComponent(
            "minersparadise.bore.upgrade.generic.cost_per_block",
            StringUtil.formatAsCleanFloat(MPConfig.BORE_COST_VEIN_MINING.get())
        );
        break;
      case TILLING:
        line1Args = new Object[] {UpgradeUtil.getTillingRadius(level)};
        break;
      case SILK_TOUCH:
        line2 = new TranslationTextComponent(
            "minersparadise.bore.upgrade.generic.cost",
            StringUtil.formatAsCleanFloat(MPConfig.BORE_COST_SILK_TOUCH.get())
        );
        break;
      case MAGNET:
        line2 = new TranslationTextComponent(
            "minersparadise.bore.upgrade.generic.cost",
            StringUtil.formatAsCleanFloat(MPConfig.BORE_COST_MAGNET.get())
        );
        break;
    }
    information.add(
        new TranslationTextComponent("minersparadise.bore.upgrade." + upgrade.getId() + ".description", line1Args)
        .mergeStyle(TextFormatting.GREEN)
    );
    if (line2 != null) {
      information.add(line2.mergeStyle(TextFormatting.RED));
    }
  }

  public BoreUpgrade getUpgrade() {
    return upgrade;
  }

  public int getLevel() {
    return level;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public ITextComponent getDisplayName(ItemStack stack) {
    return displayName;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.addAll(information);
  }

}
