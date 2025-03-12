package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.TileVisNode;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.IWandable;
import thaumcraft.common.Thaumcraft;

public class TileVisRelay extends TileVisNode implements IWandable {
   public short orientation = 1;
   public byte color = -1;
   public static HashMap nearbyPlayers = new HashMap<>();
   protected Object beam1 = null;
   protected int pulse;
   public float pRed = 0.5F;
   public float pGreen = 0.5F;
   public float pBlue = 0.5F;
   public static final int[] colors = new int[]{16777086, 16727041, 37119, 40960, 15650047, 5592439};
   protected int px;
   protected int py;
   protected int pz;
   protected boolean parentLoaded = false;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public byte getAttunement() {
      return this.color;
   }

   public int getRange() {
      return 8;
   }

   public boolean isSource() {
      return false;
   }

   public void parentChanged() {
      if (this.worldObj != null && this.worldObj.isRemote) {
         this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public void invalidate() {
      this.beam1 = null;
      super.invalidate();
   }

   public void updateEntity() {
      this.drawEffect();
      super.updateEntity();
      if (!this.worldObj.isRemote && this.nodeCounter % 20 == 0) {
         List<EntityPlayer> var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(5.0F, 5.0F, 5.0F));
         if (var5 != null && !var5.isEmpty()) {
            for(EntityPlayer player : var5) {
               if (!nearbyPlayers.containsKey(player.getEntityId()) || ((WeakReference)nearbyPlayers.get(player.getEntityId())).get() == null || !(((TileVisRelay)((WeakReference)nearbyPlayers.get(player.getEntityId())).get()).getDistanceFrom(player.posX, player.posY, player.posZ) < this.getDistanceFrom(player.posX, player.posY, player.posZ))) {
                  nearbyPlayers.put(player.getEntityId(), new WeakReference(this));
               }
            }
         }
      }

   }

   protected void drawEffect() {
      if (this.worldObj.isRemote) {
         if (this.parentLoaded) {
            if (this.px == 0 && this.py == 0 && this.pz == 0) {
               this.setParent(null);
            } else {
               TileEntity tile = this.getWorldObj().getTileEntity(this.xCoord - this.px, this.yCoord - this.py, this.zCoord - this.pz);
               if (tile instanceof TileVisNode) {
                  this.setParent(new WeakReference(tile));
               }
            }

            this.parentLoaded = false;
            this.parentChanged();
         }

         if (VisNetHandler.isNodeValid(this.getParent())) {
            double xx = (double) this.getParent().get().xCoord + (double)0.5F;
            double yy = (double) this.getParent().get().yCoord + (double)0.5F;
            double zz = (double) this.getParent().get().zCoord + (double)0.5F;
            ForgeDirection d1 = ForgeDirection.UNKNOWN;
            if (this.getParent().get() instanceof TileVisRelay) {
               d1 = ForgeDirection.getOrientation(((TileVisRelay)this.getParent().get()).orientation);
            }

            ForgeDirection d2 = ForgeDirection.getOrientation(this.orientation);
            this.beam1 = Thaumcraft.proxy.beamPower(this.worldObj, xx - (double)d1.offsetX * 0.05, yy - (double)d1.offsetY * 0.05, zz - (double)d1.offsetZ * 0.05, (double)this.xCoord + (double)0.5F - (double)d2.offsetX * 0.05, (double)this.yCoord + (double)0.5F - (double)d2.offsetY * 0.05, (double)this.zCoord + (double)0.5F - (double)d2.offsetZ * 0.05, this.pRed, this.pGreen, this.pBlue, this.pulse > 0, this.beam1);
         }

         if (this.pRed < 1.0F) {
            this.pRed += 0.025F;
         }

         if (this.pRed > 1.0F) {
            this.pRed = 1.0F;
         }

         if (this.pGreen < 1.0F) {
            this.pGreen += 0.025F;
         }

         if (this.pGreen > 1.0F) {
            this.pGreen = 1.0F;
         }

         if (this.pBlue < 1.0F) {
            this.pBlue += 0.025F;
         }

         if (this.pBlue > 1.0F) {
            this.pBlue = 1.0F;
         }
      }

      if (this.pulse > 0) {
         --this.pulse;
      }

   }

   public void triggerConsumeEffect(Aspect aspect) {
      this.addPulse(aspect);
   }

   protected void addPulse(Aspect aspect) {
      int c = -1;
      if (aspect == Aspect.AIR) {
         c = 0;
      } else if (aspect == Aspect.FIRE) {
         c = 1;
      } else if (aspect == Aspect.WATER) {
         c = 2;
      } else if (aspect == Aspect.EARTH) {
         c = 3;
      } else if (aspect == Aspect.ORDER) {
         c = 4;
      } else if (aspect == Aspect.ENTROPY) {
         c = 5;
      }

      if (c >= 0 && this.pulse == 0) {
         this.pulse = 5;
         this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 0, c);
      }

   }

   public boolean receiveClientEvent(int i, int j) {
      if (i != 0) {
         return super.receiveClientEvent(i, j);
      } else {
         if (this.worldObj.isRemote) {
            Color c = new Color(colors[j]);
            this.pulse = 5;
            this.pRed = (float)c.getRed() / 255.0F;
            this.pGreen = (float)c.getGreen() / 255.0F;
            this.pBlue = (float)c.getBlue() / 255.0F;

            for(WeakReference<TileVisNode> vr = this.getParent(); VisNetHandler.isNodeValid(vr) && vr.get() instanceof TileVisRelay && ((TileVisRelay)vr.get()).pulse == 0; vr = vr.get().getParent()) {
               ((TileVisRelay)vr.get()).pRed = this.pRed;
               ((TileVisRelay)vr.get()).pGreen = this.pGreen;
               ((TileVisRelay)vr.get()).pBlue = this.pBlue;
               ((TileVisRelay)vr.get()).pulse = 5;
            }
         }

         return true;
      }
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      super.readCustomNBT(nbttagcompound);
      this.orientation = nbttagcompound.getShort("orientation");
      this.color = nbttagcompound.getByte("color");
      this.px = nbttagcompound.getByte("px");
      this.py = nbttagcompound.getByte("py");
      this.pz = nbttagcompound.getByte("pz");
      this.parentLoaded = true;
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      super.writeCustomNBT(nbttagcompound);
      nbttagcompound.setShort("orientation", this.orientation);
      nbttagcompound.setByte("color", this.color);
      if (VisNetHandler.isNodeValid(this.getParent())) {
         nbttagcompound.setByte("px", (byte)(this.xCoord - this.getParent().get().xCoord));
         nbttagcompound.setByte("py", (byte)(this.yCoord - this.getParent().get().yCoord));
         nbttagcompound.setByte("pz", (byte)(this.zCoord - this.getParent().get().zCoord));
      } else {
         nbttagcompound.setByte("px", (byte)0);
         nbttagcompound.setByte("py", (byte)0);
         nbttagcompound.setByte("pz", (byte)0);
      }

   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
      if (!this.worldObj.isRemote) {
         ++this.color;
         if (this.color > 5) {
            this.color = -1;
         }

         this.removeThisNode();
         this.nodeRefresh = true;
         this.markDirty();
         world.markBlockForUpdate(x, y, z);
         world.playSoundEffect(x, y, z, "thaumcraft:crystal", 0.2F, 1.0F);
      }

      return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {
   }
}
