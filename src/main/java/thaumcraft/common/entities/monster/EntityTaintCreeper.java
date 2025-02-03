package thaumcraft.common.entities.monster;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AICreeperSwell;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class EntityTaintCreeper extends EntityMob implements ITaintedMob {
   private int lastActiveTime;
   private int timeSinceIgnited;
   private int fuseTime = 30;
   private int explosionRadius = 3;

   public EntityTaintCreeper(World par1World) {
      super(par1World);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new AICreeperSwell(this));
      this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, (double)1.0F, 1.2));
      this.tasks.addTask(4, new AIAttackOnCollide(this, (double)1.0F, false));
      this.tasks.addTask(5, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.25F);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)2.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.25F);
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected boolean canDespawn() {
      return false;
   }

   public int getMaxSafePointTries() {
      return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
   }

   protected void fall(float par1) {
      super.fall(par1);
      this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + par1 * 1.5F);
      if (this.timeSinceIgnited > this.fuseTime - 5) {
         this.timeSinceIgnited = this.fuseTime - 5;
      }

   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
         par1NBTTagCompound.setBoolean("powered", true);
      }

      par1NBTTagCompound.setShort("Fuse", (short)this.fuseTime);
      par1NBTTagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(17, (byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0));
      if (par1NBTTagCompound.hasKey("Fuse")) {
         this.fuseTime = par1NBTTagCompound.getShort("Fuse");
      }

      if (par1NBTTagCompound.hasKey("ExplosionRadius")) {
         this.explosionRadius = par1NBTTagCompound.getByte("ExplosionRadius");
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, -1);
      this.dataWatcher.addObject(17, (byte)0);
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextBoolean()) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
      } else {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
      }

   }

   public void onUpdate() {
      if (this.isEntityAlive()) {
         this.lastActiveTime = this.timeSinceIgnited;
         int var1 = this.getCreeperState();
         if (var1 > 0 && this.timeSinceIgnited == 0) {
            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
         }

         this.timeSinceIgnited += var1;
         if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
         }

         if (this.timeSinceIgnited >= 30) {
            this.timeSinceIgnited = 30;
            if (!this.worldObj.isRemote) {
               this.worldObj.createExplosion(this, this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, 1.5F, false);
               List ents = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand((double)6.0F, (double)6.0F, (double)6.0F));
               if (ents.size() > 0) {
                  for(Object ent : ents) {
                     EntityLivingBase el = (EntityLivingBase)ent;
                     if (!(el instanceof ITaintedMob) && !el.isEntityUndead()) {
                        el.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 100, 0, false));
                     }
                  }
               }

               int x = (int)this.posX;
               int y = (int)this.posY;
               int z = (int)this.posZ;

               for(int a = 0; a < 10; ++a) {
                  int xx = x + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                  int zz = z + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                  if (this.worldObj.rand.nextBoolean() && this.worldObj.getBiomeGenForCoords(xx, zz) != ThaumcraftWorldGenerator.biomeTaint) {
                     Utils.setBiomeAt(this.worldObj, xx, zz, ThaumcraftWorldGenerator.biomeTaint);
                     if (this.worldObj.isBlockNormalCubeDefault(xx, y - 1, zz, false) && this.worldObj.getBlock(xx, y, zz).isReplaceable(this.worldObj, xx, y, zz)) {
                        this.worldObj.setBlock(xx, y, zz, ConfigBlocks.blockTaintFibres, 0, 3);
                     }
                  }
               }

               this.setDead();
            } else {
               for(int a = 0; a < Thaumcraft.proxy.particleCount(100); ++a) {
                  Thaumcraft.proxy.taintsplosionFX(this);
               }
            }
         }
      }

      super.onUpdate();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.worldObj.isRemote && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

   }

   public float getCreeperFlashIntensity(float par1) {
      return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0F;
   }

   protected String getHurtSound() {
      return "mob.creeper.say";
   }

   protected String getDeathSound() {
      return "mob.creeper.death";
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      return true;
   }

   public int getCreeperState() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   public void setCreeperState(int par1) {
      this.dataWatcher.updateObject(16, (byte)par1);
   }
}
