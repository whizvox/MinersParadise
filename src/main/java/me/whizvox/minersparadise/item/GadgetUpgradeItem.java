package me.whizvox.minersparadise.item;

import me.whizvox.minersparadise.capability.MPCapabilities;
import me.whizvox.minersparadise.item.group.MPItemGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class GadgetUpgradeItem extends Item {

  private static final TranslationTextComponent BAD_ITEM_COMP = new TranslationTextComponent("minersparadise.upgrade.no_capability");

  public GadgetUpgradeItem() {
    super(new Item.Properties()
      .maxStackSize(10)
      .group(MPItemGroup.getInstance())
    );
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public ITextComponent getDisplayName(ItemStack stack) {
    return stack.getCapability(MPCapabilities.UPGRADE_DATA).map(data -> {
      final ResourceLocation id = data.getUpgrade().getId();
      return new TranslationTextComponent(id.getNamespace() + ".gadget_upgrade." + id.getPath());
    }).orElse(BAD_ITEM_COMP);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    stack.getCapability(MPCapabilities.UPGRADE_DATA).ifPresent(upgradeData -> {
      ResourceLocation id = upgradeData.getUpgrade().getId();
      tooltip.add(new TranslationTextComponent(id.getNamespace() + ".gadget_upgrade." + id.getPath() + ".description").mergeStyle(TextFormatting.GREEN));
    });
  }

  @Override
  public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    /*if (isInGroup(group)) {
      MinersParadise.getInstance().getGadgetUpgradeRegistry().forEach(upgrade -> {
        for (byte i = 0; i <= upgrade.getMaxLevel(); i++) {
          ItemStack stack = new ItemStack(this);
          NBTUtil.setUpgrade(stack, upgrade.getId(), i);
          items.add(stack);
        }
      });
    }*/
  }

}
