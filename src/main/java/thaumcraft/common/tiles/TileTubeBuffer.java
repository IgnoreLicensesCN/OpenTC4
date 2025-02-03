package thaumcraft.common.tiles;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.wands.IWandable;
import thaumcraft.codechicken.lib.raytracer.IndexedCuboid6;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.codechicken.lib.vec.Cuboid6;

public class TileTubeBuffer extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, IWandable {
   public AspectList aspects = new AspectList();
   public final int MAXAMOUNT = 8;
   public boolean[] openSides = new boolean[]{true, true, true, true, true, true};
   public byte[] chokedSides = new byte[]{0, 0, 0, 0, 0, 0};
   int count = 0;
   int bellows = -1;

   public boolean canUpdate() {
      return true;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspects.readFromNBT(nbttagcompound);
      byte[] sides = nbttagcompound.getByteArray("open");
      if (sides != null && sides.length == 6) {
         for(int a = 0; a < 6; ++a) {
            this.openSides[a] = sides[a] == 1;
         }
      }

      this.chokedSides = nbttagcompound.getByteArray("choke");
      if (this.chokedSides == null || this.chokedSides.length < 6) {
         this.chokedSides = new byte[]{0, 0, 0, 0, 0, 0};
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspects.writeToNBT(nbttagcompound);
      byte[] sides = new byte[6];

      for(int a = 0; a < 6; ++a) {
         sides[a] = (byte)(this.openSides[a] ? 1 : 0);
      }

      nbttagcompound.setByteArray("open", sides);
      nbttagcompound.setByteArray("choke", this.chokedSides);
   }

   public AspectList getAspects() {
      return this.aspects;
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tt, int am) {
      if (am != 1) {
         return am;
      } else if (this.aspects.visSize() < 8) {
         this.aspects.add(tt, am);
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return 0;
      } else {
         return am;
      }
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      if (this.aspects.getAmount(tt) >= am) {
         this.aspects.remove(tt, am);
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amt) {
      return this.aspects.getAmount(tag) >= amt;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return this.aspects.getAmount(tag);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(ForgeDirection face) {
      return this.openSides[face.ordinal()];
   }

   public boolean canInputFrom(ForgeDirection face) {
      return this.openSides[face.ordinal()];
   }

   public boolean canOutputTo(ForgeDirection face) {
      return this.openSides[face.ordinal()];
   }

   public void setSuction(Aspect aspect, int amount) {
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public int getMinimumSuction() {
      return 0;
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return null;
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return this.chokedSides[loc.ordinal()] == 2 ? 0 : (this.bellows > 0 && this.chokedSides[loc.ordinal()] != 1 ? this.bellows * 32 : 1);
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return this.aspects.size() > 0 ? this.aspects.getAspects()[this.worldObj.rand.nextInt(this.aspects.getAspects().length)] : null;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return this.aspects.visSize();
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      if (!this.canOutputTo(face)) {
         return 0;
      } else {
         TileEntity te = null;
         IEssentiaTransport ic = null;
         int suction = 0;
         te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, face);
         if (te != null) {
            ic = (IEssentiaTransport)te;
            suction = ic.getSuctionAmount(face.getOpposite());
         }

         for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (this.canOutputTo(dir) && dir != face) {
               te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, dir);
               if (te != null) {
                  ic = (IEssentiaTransport)te;
                  int sa = ic.getSuctionAmount(dir.getOpposite());
                  Aspect su = ic.getSuctionType(dir.getOpposite());
                  if ((su == aspect || su == null) && suction < sa && this.getSuctionAmount(dir) < sa) {
                     return 0;
                  }
               }
            }
         }

         if (amount > this.aspects.getAmount(aspect)) {
            amount = this.aspects.getAmount(aspect);
         }

         return this.takeFromContainer(aspect, amount) ? amount : 0;
      }
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
   }

   public void updateEntity() {
      ++this.count;
      if (this.bellows < 0 || this.count % 20 == 0) {
         this.getBellows();
      }

      if (!this.worldObj.isRemote && this.count % 5 == 0) {
         int var10000 = this.aspects.visSize();
         this.getClass();
         if (var10000 < 8) {
            this.fillBuffer();
         }
      }

   }

   void fillBuffer() {
      TileEntity te = null;
      IEssentiaTransport ic = null;

      for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
         te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, dir);
         if (te != null) {
            ic = (IEssentiaTransport)te;
            if (ic.getEssentiaAmount(dir.getOpposite()) > 0 && ic.getSuctionAmount(dir.getOpposite()) < this.getSuctionAmount(dir) && this.getSuctionAmount(dir) >= ic.getMinimumSuction()) {
               Aspect ta = ic.getEssentiaType(dir.getOpposite());
               this.addToContainer(ta, ic.takeEssentia(ta, 1, dir.getOpposite()));
               return;
            }
         }
      }

   }

   public void getBellows() {
      this.bellows = TileBellows.getBellows(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.VALID_DIRECTIONS);
   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
      MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
       if (hit != null) {
           if (hit.subHit >= 0 && hit.subHit < 6) {
               player.swingItem();
               if (player.isSneaking()) {
                   player.worldObj.playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:squeek", 0.6F, 1.1F + world.rand.nextFloat() * 0.2F, false);
                   if (!this.worldObj.isRemote) {
                       ++this.chokedSides[hit.subHit];
                       if (this.chokedSides[hit.subHit] > 2) {
                           this.chokedSides[hit.subHit] = 0;
                       }

                       this.markDirty();
                       world.markBlockForUpdate(x, y, z);
                   }
               } else {
                   player.worldObj.playSound((double) x + (double) 0.5F, (double) y + (double) 0.5F, (double) z + (double) 0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
                   this.openSides[hit.subHit] = !this.openSides[hit.subHit];
                   ForgeDirection dir = ForgeDirection.getOrientation(hit.subHit);
                   TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                   if (tile != null && tile instanceof TileTube) {
                       ((TileTube) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                       world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                       tile.markDirty();
                   }

                   if (tile != null && tile instanceof TileTubeBuffer) {
                       ((TileTubeBuffer) tile).openSides[dir.getOpposite().ordinal()] = this.openSides[hit.subHit];
                       world.markBlockForUpdate(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
                       tile.markDirty();
                   }

                   this.markDirty();
                   world.markBlockForUpdate(x, y, z);
               }
           }

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

   public MovingObjectPosition rayTrace(World world, Vec3 vec3d, Vec3 vec3d1, MovingObjectPosition fullblock) {
      return fullblock;
   }

   private boolean canConnectSide(int side) {
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      TileEntity tile = this.worldObj.getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
      return tile != null && tile instanceof IEssentiaTransport;
   }

   public void addTraceableCuboids(List cuboids) {
      float min = 0.42F;
      float max = 0.58F;
      if (this.canConnectSide(0)) {
         cuboids.add(new IndexedCuboid6(0, new Cuboid6((double)((float)this.xCoord + min), (double)this.yCoord, (double)((float)this.zCoord + min), (double)((float)this.xCoord + max), (double)this.yCoord + (double)0.5F, (double)((float)this.zCoord + max))));
      }

      if (this.canConnectSide(1)) {
         cuboids.add(new IndexedCuboid6(1, new Cuboid6((double)((float)this.xCoord + min), (double)this.yCoord + (double)0.5F, (double)((float)this.zCoord + min), (double)((float)this.xCoord + max), (double)(this.yCoord + 1), (double)((float)this.zCoord + max))));
      }

      if (this.canConnectSide(2)) {
         cuboids.add(new IndexedCuboid6(2, new Cuboid6((double)((float)this.xCoord + min), (double)((float)this.yCoord + min), (double)this.zCoord, (double)((float)this.xCoord + max), (double)((float)this.yCoord + max), (double)this.zCoord + (double)0.5F)));
      }

      if (this.canConnectSide(3)) {
         cuboids.add(new IndexedCuboid6(3, new Cuboid6((double)((float)this.xCoord + min), (double)((float)this.yCoord + min), (double)this.zCoord + (double)0.5F, (double)((float)this.xCoord + max), (double)((float)this.yCoord + max), (double)(this.zCoord + 1))));
      }

      if (this.canConnectSide(4)) {
         cuboids.add(new IndexedCuboid6(4, new Cuboid6((double)this.xCoord, (double)((float)this.yCoord + min), (double)((float)this.zCoord + min), (double)this.xCoord + (double)0.5F, (double)((float)this.yCoord + max), (double)((float)this.zCoord + max))));
      }

      if (this.canConnectSide(5)) {
         cuboids.add(new IndexedCuboid6(5, new Cuboid6((double)this.xCoord + (double)0.5F, (double)((float)this.yCoord + min), (double)((float)this.zCoord + min), (double)(this.xCoord + 1), (double)((float)this.yCoord + max), (double)((float)this.zCoord + max))));
      }

      cuboids.add(new IndexedCuboid6(6, new Cuboid6((double)((float)this.xCoord + 0.25F), (double)((float)this.yCoord + 0.25F), (double)((float)this.zCoord + 0.25F), (double)((float)this.xCoord + 0.75F), (double)((float)this.yCoord + 0.75F), (double)((float)this.zCoord + 0.75F))));
   }
}
