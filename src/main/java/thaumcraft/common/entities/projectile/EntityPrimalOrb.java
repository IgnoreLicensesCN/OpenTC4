package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class EntityPrimalOrb extends EntityThrowable implements IEntityAdditionalSpawnData {
   int count = 0;
   boolean seeker = false;
   int oi = 0;

   public EntityPrimalOrb(World par1World) {
      super(par1World);
   }

   public EntityPrimalOrb(World par1World, EntityLivingBase par2EntityLiving, boolean seeker) {
      super(par1World, par2EntityLiving);
      this.seeker = seeker;
      this.oi = par2EntityLiving.getEntityId();
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeBoolean(this.seeker);
      data.writeInt(this.oi);
   }

   public void readSpawnData(ByteBuf data) {
      this.seeker = data.readBoolean();
      this.oi = data.readInt();
   }

   protected float getGravityVelocity() {
      return 0.001F;
   }

   protected float func_70182_d() {
      return 0.5F;
   }

   public void onUpdate() {
      ++this.count;
      if (this.isInsideOfMaterial(Material.portal)) {
         this.onImpact(new MovingObjectPosition(this));
      }

      if (this.worldObj.isRemote) {
         for(int a = 0; a < 6; ++a) {
            Thaumcraft.proxy.wispFX4(this.worldObj, (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F, (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F, (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F, this, a, true, 0.0F);
         }

         Thaumcraft.proxy.wispFX2(this.worldObj, this.posX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.posY + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.posZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), 0.1F, this.rand.nextInt(6), true, true, 0.0F);
      }

      Random rr = new Random(this.getEntityId() + this.count);
      if (this.ticksExisted > 20) {
         if (!this.seeker) {
            this.motionX += (rr.nextFloat() - rr.nextFloat()) * 0.01F;
            this.motionY += (rr.nextFloat() - rr.nextFloat()) * 0.01F;
            this.motionZ += (rr.nextFloat() - rr.nextFloat()) * 0.01F;
         } else {
            List<Entity> l = EntityUtils.getEntitiesInRange(this.worldObj, this.posX, this.posY, this.posZ, this, EntityLivingBase.class, 16.0F);
            double d = Double.MAX_VALUE;
            Entity t = null;

            for(Entity e : l) {
               if (e.getEntityId() != this.oi && !e.isDead) {
                  double dd = this.getDistanceSqToEntity(e);
                  if (dd < d) {
                     d = dd;
                     t = e;
                  }
               }
            }

            if (t != null) {
               double dx = t.posX - this.posX;
               double dy = t.boundingBox.minY + (double)t.height * 0.9 - this.posY;
               double dz = t.posZ - this.posZ;
               double d13 = 0.2;
               dx /= d;
               dy /= d;
               dz /= d;
               this.motionX += dx * d13;
               this.motionY += dy * d13;
               this.motionZ += dz * d13;
               this.motionX = MathHelper.clamp_float((float)this.motionX, -0.2F, 0.2F);
               this.motionY = MathHelper.clamp_float((float)this.motionY, -0.2F, 0.2F);
               this.motionZ = MathHelper.clamp_float((float)this.motionZ, -0.2F, 0.2F);
            }
         }
      }

      super.onUpdate();
      if (this.ticksExisted > 5000) {
         this.setDead();
      }

   }

   protected void onImpact(MovingObjectPosition mop) {
      if (this.worldObj.isRemote) {
         for(int a = 0; a < 6; ++a) {
            for(int b = 0; b < 6; ++b) {
               float fx = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.5F;
               float fy = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.5F;
               float fz = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.5F;
               Thaumcraft.proxy.wispFX3(this.worldObj, this.posX + (double)fx, this.posY + (double)fy, this.posZ + (double)fz, this.posX + (double)(fx * 10.0F), this.posY + (double)(fy * 10.0F), this.posZ + (double)(fz * 10.0F), 0.4F, b, true, 0.05F);
            }
         }
      }

      if (!this.worldObj.isRemote) {
         float specialchance = 1.0F;
         float expl = 2.0F;
         if (mop.typeOfHit == MovingObjectType.BLOCK && this.isInsideOfMaterial(Material.portal)) {
            expl = 4.0F;
            specialchance = 10.0F;
         }

         this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, expl, true);
         if (!this.seeker && (float)this.rand.nextInt(100) <= specialchance) {
            if (this.rand.nextBoolean()) {
               this.taintSplosion();
            } else {
               ThaumcraftWorldGenerator.createRandomNodeAt(this.worldObj, mop.blockX, mop.blockY, mop.blockZ, this.rand, false, false, true);
            }
         }

         this.setDead();
      }

   }

   public void taintSplosion() {
      int x = (int)this.posX;
      int y = (int)this.posY;
      int z = (int)this.posZ;

      for(int a = 0; a < 10; ++a) {
         int xx = x + (int)(this.rand.nextFloat() - this.rand.nextFloat() * 6.0F);
         int zz = z + (int)(this.rand.nextFloat() - this.rand.nextFloat() * 6.0F);
         if (this.rand.nextBoolean() && this.worldObj.getBiomeGenForCoords(xx, zz) != ThaumcraftWorldGenerator.biomeTaint) {
            Utils.setBiomeAt(this.worldObj, xx, zz, ThaumcraftWorldGenerator.biomeTaint);
            int yy = this.worldObj.getHeightValue(xx, zz);
            if (!this.worldObj.isAirBlock(xx, yy - 1, zz)) {
               this.worldObj.setBlock(xx, yy, zz, ConfigBlocks.blockTaintFibres, 0, 3);
            }
         }
      }

   }

   public float getShadowSize() {
      return 0.1F;
   }
}
