package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRunicArmor;
import thaumcraft.common.Thaumcraft;

public class ItemGirdleHover extends Item implements IBauble, IRunicArmor {
   public IIcon icon;

   public ItemGirdleHover() {
      this.maxStackSize = 1;
      this.canRepair = false;
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:hovergirdle");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
   }

   public EnumRarity getRarity(ItemStack par1ItemStack) {
      return EnumRarity.rare;
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.BELT;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
      if (player.fallDistance > 0.0F) {
         player.fallDistance -= 0.33F;
      }

   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }
}
