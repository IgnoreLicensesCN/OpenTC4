package thaumcraft.common.lib.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import thaumcraft.api.wands.IWandFocus;

public class EnchantmentPotency extends Enchantment {
   public EnchantmentPotency(int par1, int par2) {
      super(par1, par2, EnumEnchantmentType.all);
      this.setName("potency");
   }

   public int getMinEnchantability(int par1) {
      return 10 + 11 * (par1 - 1);
   }

   public int getMaxEnchantability(int par1) {
      return super.getMinEnchantability(par1) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }

   public boolean canApply(ItemStack stack) {
      return stack.getItem() instanceof IWandFocus && ((IWandFocus)stack.getItem()).acceptsEnchant(this.effectId) || stack.getItem() instanceof ItemBook;
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack) {
      return this.canApply(stack);
   }
}
