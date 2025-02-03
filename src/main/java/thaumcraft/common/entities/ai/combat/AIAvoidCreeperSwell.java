package thaumcraft.common.entities.ai.combat;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AIAvoidCreeperSwell extends EntityAIBase {
   private EntityGolemBase theGolem;
   private float farSpeed;
   private float nearSpeed;
   private Entity closestLivingEntity;
   private float distanceFromEntity;
   private PathEntity entityPathEntity;
   private PathNavigate entityPathNavigate;
   Vec3 targetBlock;

   public AIAvoidCreeperSwell(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.distanceFromEntity = 5.0F;
      this.entityPathNavigate = par1EntityCreature.getNavigator();
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      if (this.farSpeed == 0.0F) {
         this.farSpeed = this.theGolem.getAIMoveSpeed() * 1.125F;
         this.nearSpeed = this.theGolem.getAIMoveSpeed() * 1.25F;
      }

      List var1 = this.theGolem.worldObj.getEntitiesWithinAABB(EntityCreeper.class, this.theGolem.boundingBox.expand((double)this.distanceFromEntity, (double)3.0F, (double)this.distanceFromEntity));
      if (var1.isEmpty()) {
         return false;
      } else if (((EntityCreeper)var1.get(0)).getCreeperState() != 1) {
         return false;
      } else {
         this.closestLivingEntity = (Entity)var1.get(0);
         if (!this.theGolem.getEntitySenses().canSee(this.closestLivingEntity)) {
            return false;
         } else {
            Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theGolem, 16, 7, Vec3.createVectorHelper(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
            if (var2 == null) {
               return false;
            } else if (this.closestLivingEntity.getDistanceSq(var2.xCoord, var2.yCoord, var2.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theGolem)) {
               return false;
            } else {
               this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(var2.xCoord, var2.yCoord, var2.zCoord);
               this.targetBlock = var2;
               return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(var2);
            }
         }
      }
   }

   public boolean continueExecuting() {
      return !this.entityPathNavigate.noPath();
   }

   public void startExecuting() {
      double var1 = this.targetBlock.xCoord + (double)0.5F - this.theGolem.posX;
      double var3 = this.targetBlock.zCoord + (double)0.5F - this.theGolem.posZ;
      float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      EntityGolemBase var10000 = this.theGolem;
      var10000.motionX += var1 / (double)var5 * (double)1.0F * (double)0.8F + this.theGolem.motionX * (double)0.2F;
      var10000 = this.theGolem;
      var10000.motionZ += var3 / (double)var5 * (double)1.0F * (double)0.8F + this.theGolem.motionZ * (double)0.2F;
      this.theGolem.motionY = 0.3;
      this.entityPathNavigate.setPath(this.entityPathEntity, (double)this.nearSpeed);
   }

   public void resetTask() {
      this.closestLivingEntity = null;
   }

   public void updateTask() {
      if (this.theGolem.getDistanceSqToEntity(this.closestLivingEntity) < (double)49.0F) {
         this.theGolem.getNavigator().setSpeed((double)this.nearSpeed);
      } else {
         this.theGolem.getNavigator().setSpeed((double)this.farSpeed);
      }

   }
}
