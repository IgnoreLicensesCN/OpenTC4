package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;

public class ItemNuggetEdible extends ItemFood {
   public final int itemUseDuration = 10;
   public final String iconName;
   public IIcon icon;

   public ItemNuggetEdible(String iconName) {
      super(1, 0.3F, false);
      this.iconName = iconName;
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return this.itemUseDuration;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:" + this.iconName);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icon;
   }
}
