package thaumcraft.common.lib.enchantment;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import thaumcraft.api.wands.IWandFocus;
import thaumcraft.api.wands.ItemFocusBasic;

public class EnchantmentFrugal extends Enchantment {
   public EnchantmentFrugal(int par1, int par2) {
      super(par1, par2, EnumEnchantmentType.all);
      this.setName("frugal");
   }

   public int getMinEnchantability(int par1) {
      return 5 + 11 * (par1 - 1);
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

   public static boolean doDamage(ItemStack par0ItemStack, int par1, Random par2Random) {
      float chance = 1.0F - (float)par1 / 5.0F;
      return par0ItemStack.getItem() instanceof ItemFocusBasic && par2Random.nextFloat() < chance;
   }

   public boolean canApplyTogether(Enchantment par1Enchantment) {
      return super.canApplyTogether(par1Enchantment);
   }
}
