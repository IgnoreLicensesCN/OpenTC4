package thaumcraft.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServer;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemThaumometer extends Item {
   public IIcon icon;
   ScanResult startScan = null;

   public ItemThaumometer() {
      this.setMaxStackSize(1);
      this.setNoRepair();
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @Override
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   @Override
   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:blank");
   }

   @Override
   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack itemstack) {
      return 25;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.none;
   }

   private ScanResult doScan(ItemStack stack, World world, EntityPlayer p, int count) {
      Entity pointedEntity = EntityUtils.getPointedEntity(p.worldObj, p, 0.5F, 10.0F, 0.0F, true);
      if (pointedEntity != null) {
         ScanResult sr = new ScanResult((byte)2, 0, 0, pointedEntity, "");
         if (ScanManager.isValidScanTarget(p, sr, "@")) {
            Thaumcraft.proxy.blockRunes(world, pointedEntity.posX - (double)0.5F, pointedEntity.posY + (double)(pointedEntity.getEyeHeight() / 2.0F), pointedEntity.posZ - (double)0.5F, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, (int)(pointedEntity.height * 15.0F), 0.03F);
            return sr;
         } else {
            return null;
         }
      } else {
         MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(p.worldObj, p, true);
         if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if (tile instanceof INode) {
               ScanResult sr = new ScanResult((byte)3, 0, 0, null, "NODE" + ((INode)tile).getId());
               if (ScanManager.isValidScanTarget(p, sr, "@")) {
                  Thaumcraft.proxy.blockRunes(world, mop.blockX, (double)mop.blockY + (double)0.25F, mop.blockZ, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, 15, 0.03F);
                  return sr;
               }

               return null;
            }

            Block bi = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if (bi != Blocks.air) {
               int md = bi.getDamageValue(world, mop.blockX, mop.blockY, mop.blockZ);
               ItemStack is = bi.getPickBlock(mop, p.worldObj, mop.blockX, mop.blockY, mop.blockZ);
               ScanResult sr = null;

               try {
                  if (is == null) {
                     is = BlockUtils.createStackedBlock(bi, md);
                  }
               } catch (Exception ignored) {
               }

               try {
                  if (is == null) {
                     sr = new ScanResult((byte)1, Block.getIdFromBlock(bi), md, null, "");
                  } else {
                     sr = new ScanResult((byte)1, Item.getIdFromItem(is.getItem()), is.getItemDamage(), null, "");
                  }
               } catch (Exception ignored) {
               }

               if (ScanManager.isValidScanTarget(p, sr, "@")) {
                  Thaumcraft.proxy.blockRunes(world, mop.blockX, (double)mop.blockY + (double)0.25F, mop.blockZ, 0.3F + world.rand.nextFloat() * 0.7F, 0.0F, 0.3F + world.rand.nextFloat() * 0.7F, 15, 0.03F);
                  return sr;
               }

               return null;
            }
         }

         for(IScanEventHandler seh : ThaumcraftApi.scanEventhandlers) {
            ScanResult scan = seh.scanPhenomena(stack, world, p);
            if (scan != null) {
               return scan;
            }
         }

         return null;
      }
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
      if (world.isRemote) {
         ScanResult scan = this.doScan(stack, world, p, 0);
         if (scan != null) {
            this.startScan = scan;
         }
      }

      p.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }

   @Override
   public void onUsingTick(ItemStack stack, EntityPlayer p, int count) {
      if (p.worldObj.isRemote && p.getCommandSenderName() == Minecraft.getMinecraft().thePlayer.getCommandSenderName()) {
         ScanResult scan = this.doScan(stack, p.worldObj, p, count);
         if (scan != null && scan.equals(this.startScan)) {
            if (count <= 5) {
               this.startScan = null;
               p.stopUsingItem();
               if (ScanManager.completeScan(p, scan, "@")) {
                  PacketHandler.INSTANCE.sendToServer(new PacketScannedToServer(scan, p, "@"));
               }
            }

            if (count % 2 == 0) {
               p.worldObj.playSound(p.posX, p.posY, p.posZ, "thaumcraft:cameraticks", 0.2F, 0.45F + p.worldObj.rand.nextFloat() * 0.1F, false);
            }
         } else {
            this.startScan = null;
         }
      }

   }

   @Override
   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
      super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
      this.startScan = null;
   }
}
