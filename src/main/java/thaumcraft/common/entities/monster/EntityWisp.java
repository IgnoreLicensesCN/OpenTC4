package thaumcraft.common.entities.monster;

import cpw.mods.fml.common.network.NetworkRegistry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class EntityWisp extends EntityFlying implements IMob {
   public int courseChangeCooldown = 0;
   public double waypointX;
   public double waypointY;
   public double waypointZ;
   private Entity targetedEntity = null;
   private int aggroCooldown = 0;
   public int prevAttackCounter = 0;
   public int attackCounter = 0;

   public EntityWisp(World world) {
      super(world);
      this.setSize(0.9F, 0.9F);
      this.experienceValue = 5;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)22.0F);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)3.0F);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public int decreaseAirSupply(int par1) {
      return par1;
   }

   public boolean attackEntityFrom(DamageSource damagesource, float i) {
      if (damagesource.getSourceOfDamage() instanceof EntityLivingBase) {
         this.targetedEntity = (EntityLivingBase)damagesource.getSourceOfDamage();
         this.aggroCooldown = 200;
      }

      if (damagesource.getEntity() instanceof EntityLivingBase) {
         this.targetedEntity = (EntityLivingBase)damagesource.getEntity();
         this.aggroCooldown = 200;
      }

      return super.attackEntityFrom(damagesource, i);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(22, String.valueOf(""));
   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if (this.worldObj.isRemote) {
         Thaumcraft.proxy.burst(this.worldObj, this.posX, this.posY + (double)0.45F, this.posZ, 1.0F);
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote && this.ticksExisted <= 1) {
         Thaumcraft.proxy.burst(this.worldObj, this.posX, this.posY + (double)0.45F, this.posZ, 1.0F);
      }

      if (this.worldObj.isRemote && this.worldObj.rand.nextBoolean() && Aspect.getAspect(this.getType()) != null) {
         Color color = new Color(Aspect.getAspect(this.getType()).getColor());
         Thaumcraft.proxy.wispFX(this.worldObj, this.posX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.7F), this.posY + (double)0.45F + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.7F), this.posZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.7F), 0.1F, (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F);
      }

   }

   public String getType() {
      return this.dataWatcher.getWatchableObjectString(22);
   }

   public void setType(String t) {
      this.dataWatcher.updateObject(22, String.valueOf(t));
   }

   protected void updateEntityActionState() {
      if (!this.worldObj.isRemote && Aspect.getAspect(this.getType()) == null) {
         BiomeGenBase bg = this.worldObj.getBiomeGenForCoords(MathHelper.ceiling_double_int(this.posX), MathHelper.ceiling_double_int(this.posZ));
         if (bg.biomeID == ThaumcraftWorldGenerator.biomeEerie.biomeID) {
            switch (this.rand.nextInt(6)) {
               case 0:
                  this.setType(Aspect.DARKNESS.getTag());
                  break;
               case 1:
                  this.setType(Aspect.UNDEAD.getTag());
                  break;
               case 2:
                  this.setType(Aspect.ENTROPY.getTag());
                  break;
               case 3:
                  this.setType(Aspect.ELDRITCH.getTag());
                  break;
               case 4:
                  this.setType(Aspect.POISON.getTag());
                  break;
               case 5:
                  this.setType(Aspect.DEATH.getTag());
            }
         } else if (this.worldObj.rand.nextInt(10) != 0) {
            ArrayList<Aspect> as = Aspect.getPrimalAspects();
            this.setType(((Aspect)as.get(this.worldObj.rand.nextInt(as.size()))).getTag());
         } else {
            ArrayList<Aspect> as = Aspect.getCompoundAspects();
            this.setType(((Aspect)as.get(this.worldObj.rand.nextInt(as.size()))).getTag());
         }
      }

      if (!this.worldObj.isRemote && this.worldObj.difficultySetting.getDifficultyId() == 0) {
         this.setDead();
      }

      this.despawnEntity();
      this.prevAttackCounter = this.attackCounter;
      double attackrange = (double)16.0F;
      double d = this.waypointX - this.posX;
      double d1 = this.waypointY - this.posY;
      double d2 = this.waypointZ - this.posZ;
      double d3 = d * d + d1 * d1 + d2 * d2;
      if (d3 < (double)1.0F || d3 > (double)3600.0F) {
         this.waypointX = this.posX + (double)(this.rand.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
         this.waypointY = this.posY + (double)(this.rand.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
         this.waypointZ = this.posZ + (double)(this.rand.nextFloat() * 2.0F - 1.0F) * (double)16.0F;
      }

      if (this.courseChangeCooldown-- <= 0) {
         this.courseChangeCooldown += this.rand.nextInt(5) + 2;
         d3 = (double)MathHelper.sqrt_double(d3);
         if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
            this.motionX += d / d3 * 0.1;
            this.motionY += d1 / d3 * 0.1;
            this.motionZ += d2 / d3 * 0.1;
         } else {
            this.waypointX = this.posX;
            this.waypointY = this.posY;
            this.waypointZ = this.posZ;
         }
      }

      if (this.targetedEntity != null && this.targetedEntity.isDead) {
         this.targetedEntity = null;
      }

      --this.aggroCooldown;
      if (this.worldObj.rand.nextInt(1000) == 0 && (this.targetedEntity == null || this.aggroCooldown-- <= 0)) {
         this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, (double)16.0F);
         if (this.targetedEntity != null) {
            this.aggroCooldown = 50;
         }
      }

      if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < attackrange * attackrange) {
         double d5 = this.targetedEntity.posX - this.posX;
         double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
         double d7 = this.targetedEntity.posZ - this.posZ;
         this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / 3.141593F;
         if (this.canEntityBeSeen(this.targetedEntity)) {
            ++this.attackCounter;
            if (this.attackCounter == 20) {
               this.worldObj.playSoundAtEntity(this, "thaumcraft:zap", 1.0F, 1.1F);
               PacketHandler.INSTANCE.sendToAllAround(new PacketFXWispZap(this.getEntityId(), this.targetedEntity.getEntityId()), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, (double)32.0F));
               float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
               if (!(Math.abs(this.targetedEntity.motionX) > (double)0.1F) && !(Math.abs(this.targetedEntity.motionY) > (double)0.1F) && !(Math.abs(this.targetedEntity.motionZ) > (double)0.1F)) {
                  if (this.worldObj.rand.nextFloat() < 0.66F) {
                     this.targetedEntity.attackEntityFrom(DamageSource.causeMobDamage(this), damage + 1.0F);
                  }
               } else if (this.worldObj.rand.nextFloat() < 0.4F) {
                  this.targetedEntity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
               }

               this.attackCounter = -20 + this.worldObj.rand.nextInt(20);
            }
         } else if (this.attackCounter > 0) {
            --this.attackCounter;
         }
      } else {
         this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.141593F;
         if (this.attackCounter > 0) {
            --this.attackCounter;
         }
      }

   }

   private boolean isCourseTraversable(double d, double d1, double d2, double d3) {
      double d4 = (this.waypointX - this.posX) / d3;
      double d5 = (this.waypointY - this.posY) / d3;
      double d6 = (this.waypointZ - this.posZ) / d3;
      AxisAlignedBB axisalignedbb = this.boundingBox.copy();

      for(int i = 1; (double)i < d3; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      int x = (int)this.waypointX;
      int y = (int)this.waypointY;
      int z = (int)this.waypointZ;
      if (this.worldObj.getBlock(x, y, z).getMaterial().isLiquid()) {
         return false;
      } else {
         for(int a = 0; a < 11; ++a) {
            if (!this.worldObj.isAirBlock(x, y - a, z)) {
               return true;
            }
         }

         return false;
      }
   }

   protected String getLivingSound() {
      return "thaumcraft:wisplive";
   }

   protected String getHurtSound() {
      return "random.fizz";
   }

   protected String getDeathSound() {
      return "thaumcraft:wispdead";
   }

   protected Item getDropItem() {
      return Item.getItemById(0);
   }

   protected void dropFewItems(boolean flag, int i) {
      if (Aspect.getAspect(this.getType()) != null) {
         ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
         new AspectList();
         ((ItemWispEssence)ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.getAspect(this.getType()), 2));
         this.entityDropItem(ess, 0.0F);
      }

   }

   protected float getSoundVolume() {
      return 0.25F;
   }

   protected boolean canDespawn() {
      return true;
   }

   public boolean getCanSpawnHere() {
      int count = 0;

      try {
         List l = this.worldObj.getEntitiesWithinAABB(EntityWisp.class, this.boundingBox.expand((double)16.0F, (double)16.0F, (double)16.0F));
         if (l != null) {
            count = l.size();
         }
      } catch (Exception var3) {
      }

      return count < 8 && this.worldObj.difficultySetting.getDifficultyId() > 0 && this.isValidLightLevel() && super.getCanSpawnHere();
   }

   protected boolean isValidLightLevel() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.boundingBox.minY);
      int k = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32)) {
         return false;
      } else {
         int l = this.worldObj.getBlockLightValue(i, j, k);
         if (this.worldObj.isThundering()) {
            int i1 = this.worldObj.skylightSubtracted;
            this.worldObj.skylightSubtracted = 10;
            l = this.worldObj.getBlockLightValue(i, j, k);
            this.worldObj.skylightSubtracted = i1;
         }

         return l <= this.rand.nextInt(8);
      }
   }

   public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      super.writeEntityToNBT(nbttagcompound);
      nbttagcompound.setString("Type", this.getType());
   }

   public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      super.readEntityFromNBT(nbttagcompound);
      this.setType(nbttagcompound.getString("Type"));
   }

   public int getMaxSpawnedInChunk() {
      return 2;
   }
}
