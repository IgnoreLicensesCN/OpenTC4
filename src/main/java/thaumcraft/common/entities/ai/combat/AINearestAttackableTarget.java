package thaumcraft.common.entities.ai.combat;

import java.util.List;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class AINearestAttackableTarget extends EntityAITarget {
   EntityGolemBase theGolem;
   EntityLivingBase target;
   int targetChance;
   private final IEntitySelector entitySelector;
   private float targetDistance;
   private AINearestAttackableTargetSorter theNearestAttackableTargetSorter;

   public AINearestAttackableTarget(EntityGolemBase par1EntityLiving, int par4, boolean par5) {
      this(par1EntityLiving, 0.0F, par4, par5, false, null);
   }

   public AINearestAttackableTarget(EntityGolemBase par1, float par3, int par4, boolean par5, boolean par6, IEntitySelector par7IEntitySelector) {
      super(par1, par5, par6);
      this.targetDistance = 0.0F;
      this.theGolem = par1;
      this.targetDistance = 0.0F;
      this.targetChance = par4;
      this.theNearestAttackableTargetSorter = new AINearestAttackableTargetSorter(this, par1);
      this.entitySelector = par7IEntitySelector;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      this.targetDistance = this.theGolem.getRange();
      if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
         return false;
      } else {
         List<Entity> var5 = (List<Entity>)this.taskOwner.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, this.taskOwner.boundingBox.expand(this.targetDistance, 4.0F, this.targetDistance), this.entitySelector);
         var5.sort(this.theNearestAttackableTargetSorter);

         for(Entity var3 : var5) {
            EntityLivingBase var4 = (EntityLivingBase)var3;
            if (this.theGolem.isValidTarget(var3)) {
               this.target = var4;
               return true;
            }
         }

         return false;
      }
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.target);
      super.startExecuting();
   }
}
