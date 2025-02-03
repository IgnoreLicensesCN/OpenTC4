package thaumcraft.common.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

public class ItemWandCap extends Item {
   public IIcon[] icon = new IIcon[9];

   public ItemWandCap() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:wand_cap_iron");
      this.icon[1] = ir.registerIcon("thaumcraft:wand_cap_gold");
      this.icon[2] = ir.registerIcon("thaumcraft:wand_cap_thaumium");
      this.icon[3] = ir.registerIcon("thaumcraft:wand_cap_copper");
      this.icon[4] = ir.registerIcon("thaumcraft:wand_cap_silver");
      this.icon[5] = ir.registerIcon("thaumcraft:wand_cap_silver_inert");
      this.icon[6] = ir.registerIcon("thaumcraft:wand_cap_thaumium_inert");
      this.icon[7] = ir.registerIcon("thaumcraft:wand_cap_void");
      this.icon[8] = ir.registerIcon("thaumcraft:wand_cap_void_inert");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icon[meta];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
      if (Config.foundCopperIngot) {
         par3List.add(new ItemStack(this, 1, 3));
      }

      if (Config.foundSilverIngot) {
         par3List.add(new ItemStack(this, 1, 4));
         par3List.add(new ItemStack(this, 1, 5));
      }

      par3List.add(new ItemStack(this, 1, 2));
      par3List.add(new ItemStack(this, 1, 6));
      par3List.add(new ItemStack(this, 1, 7));
      par3List.add(new ItemStack(this, 1, 8));
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }
}
