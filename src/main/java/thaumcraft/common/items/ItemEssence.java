package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileJarFillable;

public class ItemEssence extends Item implements IEssentiaContainerItem {
   public IIcon icon;
   public IIcon iconOverlay;

   public ItemEssence() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:phial");
      this.iconOverlay = ir.registerIcon("thaumcraft:essence");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public int getRenderPasses(int metadata) {
      return metadata == 0 ? 1 : 2;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par1 != 0 && par2 != 0 ? this.iconOverlay : this.icon;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      if (stack.getItemDamage() != 0 && par2 != 0) {
         return stack.getItemDamage() == 1 && this.getAspects(stack) != null ? this.getAspects(stack).getAspects()[0].getColor() : 16777215;
      } else {
         return 16777215;
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));

      for(Aspect tag : Aspect.aspects.values()) {
         ItemStack i = new ItemStack(this, 1, 1);
         this.setAspects(i, (new AspectList()).add(tag, 8));
         par3List.add(i);
      }

   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      if (aspects != null && aspects.size() > 0) {
         for(Aspect tag : aspects.getAspectsSorted()) {
            if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
               list.add(tag.getName() + " x " + aspects.getAmount(tag));
            } else {
               list.add(StatCollector.translateToLocal("tc.aspect.unknown"));
            }
         }
      }

      super.addInformation(stack, player, list, par4);
   }

   public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float f1, float f2, float f3) {
      Block bi = world.getBlock(x, y, z);
      int md = world.getBlockMetadata(x, y, z);
      if (itemstack.getItemDamage() == 0 && bi == ConfigBlocks.blockMetalDevice && md == 1) {
         TileAlembic tile = (TileAlembic)world.getTileEntity(x, y, z);
         if (tile.amount >= 8) {
            if (world.isRemote) {
               player.swingItem();
               return false;
            }

            ItemStack phial = new ItemStack(this, 1, 1);
            this.setAspects(phial, (new AspectList()).add(tile.aspect, 8));
            if (tile.takeFromContainer(tile.aspect, 8)) {
               --itemstack.stackSize;
               if (!player.inventory.addItemStackToInventory(phial)) {
                  world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, phial));
               }

               world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
               player.inventoryContainer.detectAndSendChanges();
               return true;
            }
         }
      }

      if (itemstack.getItemDamage() == 0 && bi == ConfigBlocks.blockJar && (md == 0 || md == 3)) {
         TileJarFillable tile = (TileJarFillable)world.getTileEntity(x, y, z);
         if (tile.amount >= 8) {
            if (world.isRemote) {
               player.swingItem();
               return false;
            }

            Aspect asp = Aspect.getAspect(tile.aspect.getTag());
            if (tile.takeFromContainer(asp, 8)) {
               --itemstack.stackSize;
               ItemStack phial = new ItemStack(this, 1, 1);
               this.setAspects(phial, (new AspectList()).add(asp, 8));
               if (!player.inventory.addItemStackToInventory(phial)) {
                  world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, phial));
               }

               world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
               player.inventoryContainer.detectAndSendChanges();
               return true;
            }
         }
      }

      AspectList al = this.getAspects(itemstack);
      if (al != null && al.size() == 1) {
         Aspect aspect = al.getAspects()[0];
         if (itemstack.getItemDamage() != 0 && bi == ConfigBlocks.blockJar && (md == 0 || md == 3)) {
            TileJarFillable tile = (TileJarFillable)world.getTileEntity(x, y, z);
            if (tile.amount <= tile.maxAmount - 8 && tile.doesContainerAccept(aspect)) {
               if (world.isRemote) {
                  player.swingItem();
                  return false;
               }

               if (tile.addToContainer(aspect, 8) == 0) {
                  world.markBlockForUpdate(x, y, z);
                  tile.markDirty();
                  --itemstack.stackSize;
                  if (!player.inventory.addItemStackToInventory(new ItemStack(this, 1, 0))) {
                     world.spawnEntityInWorld(new EntityItem(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, new ItemStack(this, 1, 0)));
                  }

                  world.playSoundAtEntity(player, "game.neutral.swim", 0.25F, 1.0F);
                  player.inventoryContainer.detectAndSendChanges();
                  return true;
               }
            }
         }
      }

      return false;
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
}
