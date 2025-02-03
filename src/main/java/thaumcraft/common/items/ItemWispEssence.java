package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;

public class ItemWispEssence extends Item implements IEssentiaContainerItem {
   public IIcon icon;
   static Aspect[] displayAspects;

   public ItemWispEssence() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:wispessence");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(Aspect tag : Aspect.aspects.values()) {
         ItemStack i = new ItemStack(this, 1, 0);
         this.setAspects(i, (new AspectList()).add(tag, 2));
         par3List.add(i);
      }

   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      if (aspects != null && aspects.size() > 0) {
         for(Aspect tag : aspects.getAspectsSorted()) {
            if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
               list.add(tag.getName() + " x" + aspects.getAmount(tag));
            } else {
               list.add(StatCollector.translateToLocal("tc.aspect.unknown"));
            }
         }
      }

      super.addInformation(stack, player, list, par4);
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      if (this.getAspects(stack) != null) {
         return this.getAspects(stack).getAspects()[0].getColor();
      } else {
         int idx = (int)(System.currentTimeMillis() / 500L % (long)displayAspects.length);
         return displayAspects[idx].getColor();
      }
   }

   public AspectList getAspects(ItemStack itemstack) {
      if (itemstack.hasTagCompound()) {
         AspectList aspects = new AspectList();
         aspects.readFromNBT(itemstack.getTagCompound());
         return aspects.size() > 0 ? aspects : null;
      } else {
         return null;
      }
   }

   public void setAspects(ItemStack itemstack, AspectList aspects) {
      if (!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      aspects.writeToNBT(itemstack.getTagCompound());
   }

   static {
      displayAspects = Aspect.aspects.values().toArray(new Aspect[0]);
   }
}
