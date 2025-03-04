package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.Utils;

public class ItemLootBag extends Item {
   public IIcon[] icon = new IIcon[3];

   public ItemLootBag() {
      this.setMaxStackSize(16);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:lootbag");
      this.icon[1] = ir.registerIcon("thaumcraft:lootbagunc");
      this.icon[2] = ir.registerIcon("thaumcraft:lootbagrare");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon[par1];
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
      par3List.add(new ItemStack(this, 1, 2));
   }

   public EnumRarity getRarity(ItemStack stack) {
      switch (stack.getItemDamage()) {
         case 1:
            return EnumRarity.uncommon;
         case 2:
            return EnumRarity.rare;
         default:
            return EnumRarity.common;
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.addInformation(stack, player, list, par4);
      list.add(StatCollector.translateToLocal("tc.lootbag"));
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         int q = 8 + world.rand.nextInt(5);

         for(int a = 0; a < q; ++a) {
            ItemStack is = Utils.generateLoot(stack.getItemDamage(), world.rand);
            world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, is.copy()));
         }

         world.playSoundAtEntity(player, "thaumcraft:coins", 0.75F, 1.0F);
      }

      --stack.stackSize;
      return stack;
   }
}
