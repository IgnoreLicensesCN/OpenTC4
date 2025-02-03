package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.entities.monster.EntityCultist;
import thaumcraft.common.entities.monster.EntityCultistCleric;
import thaumcraft.common.entities.monster.EntityCultistKnight;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;

public class TileEldritchAltar extends TileThaumcraft {
   private boolean spawner = false;
   private boolean open = false;
   private boolean spawnedClerics = false;
   private byte spawnType = 0;
   private byte eyes = 0;
   private int counter = 0;

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.setEyes(nbttagcompound.getByte("eyes"));
      this.setOpen(nbttagcompound.getBoolean("open"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("eyes", this.getEyes());
      nbttagcompound.setBoolean("open", this.isOpen());
   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.spawnedClerics = nbttagcompound.getBoolean("spawnedClerics");
      this.spawner = nbttagcompound.getBoolean("spawner");
      this.spawnType = nbttagcompound.getByte("spawntype");
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setBoolean("spawnedClerics", this.spawnedClerics);
      nbttagcompound.setBoolean("spawner", this.spawner);
      nbttagcompound.setByte("spawntype", this.spawnType);
   }

   public double getMaxRenderDistanceSquared() {
      return (double)9216.0F;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1));
   }

   public boolean isSpawner() {
      return this.spawner;
   }

   public void setSpawner(boolean spawner) {
      this.spawner = spawner;
   }

   public boolean canUpdate() {
      return true;
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote && this.isSpawner() && this.counter++ >= 80 && this.counter % 40 == 0) {
         switch (this.spawnType) {
            case 0:
               if (!this.spawnedClerics) {
                  this.spawnClerics();
               } else {
                  this.spawnGuards();
               }
               break;
            case 1:
               this.spawnGuardian();
         }
      }

   }

   private void spawnGuards() {
      List ents = this.worldObj.getEntitiesWithinAABB(EntityCultistCleric.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand((double)24.0F, (double)16.0F, (double)24.0F));
      if (ents.size() < 1) {
         this.setSpawner(false);
      } else {
         ents = this.worldObj.getEntitiesWithinAABB(EntityCultist.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand((double)24.0F, (double)16.0F, (double)24.0F));
         if (ents.size() < 8) {
            EntityCultistKnight eg = new EntityCultistKnight(this.worldObj);
            int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
            int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
            int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
            if (World.doesBlockHaveSolidTopSurface(this.worldObj, i1, j1 - 1, k1)) {
               eg.setPosition((double)i1, (double)j1, (double)k1);
               if (this.worldObj.checkNoEntityCollision(eg.boundingBox) && this.worldObj.getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(eg.boundingBox)) {
                  eg.onSpawnWithEgg((IEntityLivingData)null);
                  eg.spawnExplosionParticle();
                  eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
                  this.worldObj.spawnEntityInWorld(eg);
               }
            }
         }

      }
   }

   private void spawnGuardian() {
      EntityEldritchGuardian eg = new EntityEldritchGuardian(this.worldObj);
      int i1 = this.xCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
      int j1 = this.yCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 0, 3) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
      int k1 = this.zCoord + MathHelper.getRandomIntegerInRange(this.worldObj.rand, 4, 10) * MathHelper.getRandomIntegerInRange(this.worldObj.rand, -1, 1);
      if (World.doesBlockHaveSolidTopSurface(this.worldObj, i1, j1 - 1, k1)) {
         eg.setPosition((double)i1, (double)j1, (double)k1);
         if (eg.getCanSpawnHere()) {
            eg.onSpawnWithEgg((IEntityLivingData)null);
            eg.spawnExplosionParticle();
            eg.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 16);
            this.worldObj.spawnEntityInWorld(eg);
         }
      }

   }

   private void spawnClerics() {
      int success = 0;

      for(int a = 0; a < 4; ++a) {
         int xx = 0;
         int zz = 0;
         switch (a) {
            case 0:
               xx = -2;
               zz = -2;
               break;
            case 1:
               xx = -2;
               zz = 2;
               break;
            case 2:
               xx = 2;
               zz = -2;
               break;
            case 3:
               xx = 2;
               zz = 2;
         }

         EntityCultistCleric cleric = new EntityCultistCleric(this.worldObj);
         if (World.doesBlockHaveSolidTopSurface(this.worldObj, this.xCoord + xx, this.yCoord - 1, this.zCoord + zz)) {
            cleric.setPosition((double)this.xCoord + (double)0.5F + (double)xx, (double)this.yCoord, (double)this.zCoord + (double)0.5F + (double)zz);
            if (this.worldObj.checkNoEntityCollision(cleric.boundingBox) && this.worldObj.getCollidingBoundingBoxes(cleric, cleric.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(cleric.boundingBox)) {
               cleric.setHomeArea(this.xCoord, this.yCoord, this.zCoord, 8);
               cleric.onSpawnWithEgg((IEntityLivingData)null);
               cleric.spawnExplosionParticle();
               if (this.worldObj.spawnEntityInWorld(cleric)) {
                  ++success;
                  cleric.setIsRitualist(true);
               }
            }
         }
      }

      if (success > 2) {
         this.spawnedClerics = true;
         this.markDirty();
      }

   }

   public byte getSpawnType() {
      return this.spawnType;
   }

   public void setSpawnType(byte spawnType) {
      this.spawnType = spawnType;
   }

   public byte getEyes() {
      return this.eyes;
   }

   public void setEyes(byte eyes) {
      this.eyes = eyes;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean open) {
      this.open = open;
   }

   public boolean checkForMaze() {
      int w = 15 + this.worldObj.rand.nextInt(8) * 2;
      int h = 15 + this.worldObj.rand.nextInt(8) * 2;
      if (!MazeHandler.mazesInRange(this.xCoord >> 4, this.zCoord >> 4, w, h)) {
         Thread t = new Thread(new MazeThread(this.xCoord >> 4, this.zCoord >> 4, w, h, this.worldObj.rand.nextLong()));
         t.start();
         return false;
      } else {
         return true;
      }
   }
}
