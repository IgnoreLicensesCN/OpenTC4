package thaumcraft.common.entities.monster;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigItems;

public class EntityInhabitedZombie extends EntityZombie {
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)5.0F);
      this.getEntityAttribute(field_110186_bp).setBaseValue((double)0.0F);
   }

   public EntityInhabitedZombie(World world) {
      super(world);
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, 0, true));
   }

   public void onKillEntity(EntityLivingBase par1EntityLivingBase) {
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      float diff = this.worldObj.difficultySetting == EnumDifficulty.HARD ? 0.9F : 0.6F;
      this.setCurrentItemOrArmor(4, new ItemStack(ConfigItems.itemHelmetCultistPlate));
      if (this.rand.nextFloat() <= diff) {
         this.setCurrentItemOrArmor(3, new ItemStack(ConfigItems.itemChestCultistPlate));
      }

      if (this.rand.nextFloat() <= diff) {
         this.setCurrentItemOrArmor(2, new ItemStack(ConfigItems.itemLegsCultistPlate));
      }

      return p_110161_1_;
   }

   protected Item getDropItem() {
      return Item.getItemById(0);
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
   }

   protected void onDeathUpdate() {
      if (!this.worldObj.isRemote) {
         EntityEldritchCrab crab = new EntityEldritchCrab(this.worldObj);
         crab.setPositionAndRotation(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, this.rotationYaw, this.rotationPitch);
         crab.setHelm(true);
         this.worldObj.spawnEntityInWorld(crab);
         if ((this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            int i = this.getExperiencePoints(this.attackingPlayer);

            while(i > 0) {
               int j = EntityXPOrb.getXPSplit(i);
               i -= j;
               this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }
         }
      }

      for(int i = 0; i < 20; ++i) {
         double d2 = this.rand.nextGaussian() * 0.02;
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
      }

      this.setDead();
   }

   public void onDeath(DamageSource p_70645_1_) {
   }

   protected String getLivingSound() {
      return "thaumcraft:crabtalk";
   }

   protected String getHurtSound() {
      return "game.hostile.hurt";
   }

   public boolean getCanSpawnHere() {
      List ents = this.worldObj.getEntitiesWithinAABB(EntityInhabitedZombie.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + (double)1.0F, this.posY + (double)1.0F, this.posZ + (double)1.0F).expand((double)32.0F, (double)16.0F, (double)32.0F));
      return ents.size() > 0 ? false : super.getCanSpawnHere();
   }
}
