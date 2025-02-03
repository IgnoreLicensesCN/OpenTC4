package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileNode;

public class ItemEldritchObject extends Item {
   public IIcon[] icon = new IIcon[5];

   public ItemEldritchObject() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:eldritch_object");
      this.icon[1] = ir.registerIcon("thaumcraft:crimson_rites");
      this.icon[2] = ir.registerIcon("thaumcraft:eldritch_object_2");
      this.icon[3] = ir.registerIcon("thaumcraft:eldritch_object_3");
      this.icon[4] = ir.registerIcon("thaumcraft:ob_placer");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return par1 < this.icon.length ? this.icon[par1] : this.icon[0];
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
      par3List.add(new ItemStack(this, 1, 2));
      par3List.add(new ItemStack(this, 1, 3));
      par3List.add(new ItemStack(this, 1, 4));
   }

   public EnumRarity getRarity(ItemStack stack) {
      switch (stack.getItemDamage()) {
         case 2:
            return EnumRarity.rare;
         case 3:
            return EnumRarity.epic;
         default:
            return EnumRarity.uncommon;
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      super.addInformation(stack, player, list, par4);
      if (stack != null) {
         switch (stack.getItemDamage()) {
            case 0:
               list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemEldritchObject.text.1"));
               break;
            case 1:
               list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemEldritchObject.text.2"));
               list.add(EnumChatFormatting.DARK_BLUE + StatCollector.translateToLocal("item.ItemEldritchObject.text.3"));
               break;
            case 2:
               list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemEldritchObject.text.4"));
               break;
            case 3:
               list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemEldritchObject.text.5"));
               list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemEldritchObject.text.6"));
               break;
            case 4:
               list.add("§oCreative Mode Only");
         }
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
      if (!par2World.isRemote && stack.getItemDamage() == 1 && !ResearchManager.isResearchComplete(player.getCommandSenderName(), "CRIMSON")) {
         PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CRIMSON"), (EntityPlayerMP)player);
         Thaumcraft.proxy.getResearchManager().completeResearch(player, "CRIMSON");
         par2World.playSoundAtEntity(player, "thaumcraft:learn", 0.75F, 1.0F);
      }

      return stack;
   }

   public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      if (itemstack.getItemDamage() != 3) {
         if (side == 1 && itemstack.getItemDamage() == 4) {
            player.swingItem();

            for(int a = 1; a <= 6; ++a) {
               if (!world.isAirBlock(x, y + a, z)) {
                  return false;
               }
            }

            world.setBlock(x, y + 1, z, ConfigBlocks.blockEldritch, 0, 3);
            world.setBlock(x, y + 3, z, ConfigBlocks.blockEldritch, 1, 3);
            world.setBlock(x, y + 4, z, ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(x, y + 5, z, ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(x, y + 6, z, ConfigBlocks.blockEldritch, 2, 3);
            world.setBlock(x, y + 7, z, ConfigBlocks.blockEldritch, 2, 3);
            return !world.isRemote;
         } else {
            return super.onItemUseFirst(itemstack, player, world, x, y, z, side, par8, par9, par10);
         }
      } else {
         TileEntity te = world.getTileEntity(x, y, z);
         if (te instanceof TileNode) {
            player.swingItem();
            if (!world.isRemote) {
               --itemstack.stackSize;
               TileNode node = (TileNode)te;
               boolean research = ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "PRIMNODE");

               for(Aspect a : node.getAspects().getAspects()) {
                  int m = node.getNodeVisBase(a);
                  if (!a.isPrimal()) {
                     if (world.rand.nextBoolean()) {
                        node.setNodeVisBase(a, (short)(m - 1));
                     }
                  } else {
                     m = m - 2 + world.rand.nextInt(research ? 9 : 6);
                     node.setNodeVisBase(a, (short)m);
                  }
               }

               for(Aspect a : Aspect.getPrimalAspects()) {
                  int m = node.getNodeVisBase(a);
                  int r = world.rand.nextInt(research ? 4 : 3);
                  if (r > 0 && r > m) {
                     node.setNodeVisBase(a, (short)r);
                     node.addToContainer(a, 1);
                  }
               }

               if (node.getNodeModifier() == NodeModifier.FADING && world.rand.nextBoolean()) {
                  node.setNodeModifier(NodeModifier.PALE);
               } else if (node.getNodeModifier() == NodeModifier.PALE && world.rand.nextBoolean()) {
                  node.setNodeModifier(null);
               } else if (node.getNodeModifier() == null && world.rand.nextInt(5) == 0) {
                  node.setNodeModifier(NodeModifier.BRIGHT);
               }

               world.markBlockForUpdate(x, y, z);
               node.markDirty();
               world.createExplosion(null, (double)x + (double)0.5F, (double)y + (double)1.5F, (double)z + (double)0.5F, 3.0F + world.rand.nextFloat() * (float)(research ? 3 : 5), true);

               for(int a = 0; a < 33; ++a) {
                  int xx = x + world.rand.nextInt(6) - world.rand.nextInt(6);
                  int yy = y + world.rand.nextInt(6) - world.rand.nextInt(6);
                  int zz = z + world.rand.nextInt(6) - world.rand.nextInt(6);
                  if (world.isAirBlock(xx, yy, zz)) {
                     if (yy < y) {
                        world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGoo, 8, 3);
                     } else {
                        world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGas, 8, 3);
                     }
                  }
               }

               return true;
            }
         }

         return false;
      }
   }
}
