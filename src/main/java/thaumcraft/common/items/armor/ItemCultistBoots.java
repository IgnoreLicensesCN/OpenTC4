package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;

public class ItemCultistBoots extends ItemArmor implements IRepairable, IRunicArmor, IWarpingGear, IVisDiscountGear {
   public IIcon icon;

   public ItemCultistBoots(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:cultistboots");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return "thaumcraft:textures/models/cultistboots.png";
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(Items.iron_ingot)) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   public int getWarp(ItemStack itemstack, EntityPlayer player) {
      return 1;
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
      return 1;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, (Aspect)null) + "%");
   }
}
