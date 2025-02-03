package thaumcraft.common.lib.utils;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.mods.ChampionModifier;

public class EntityUtils {
   public static final IAttribute CHAMPION_MOD = (new RangedAttribute("tc.mobmod", (double)-2.0F, (double)-2.0F, (double)100.0F)).setDescription("Champion modifier").setShouldWatch(true);
   public static final AttributeModifier CHAMPION_HEALTH = new AttributeModifier(UUID.fromString("a62bef38-48cc-42a6-ac5e-ef913841c4fd"), "Champion health buff", (double)30.0F, 0);
   public static final AttributeModifier CHAMPION_DAMAGE = new AttributeModifier(UUID.fromString("a340d2db-d881-4c25-ac62-f0ad14cd63b0"), "Champion damage buff", (double)2.0F, 2);
   public static final AttributeModifier BOLDBUFF = new AttributeModifier(UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037"), "Bold speed boost", 0.3, 1);
   public static final AttributeModifier MIGHTYBUFF = new AttributeModifier(UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3"), "Mighty damage boost", (double)3.0F, 2);
   public static final AttributeModifier[] HPBUFF = new AttributeModifier[]{new AttributeModifier(UUID.fromString("54d621c1-dd4d-4b43-8bd2-5531c8875797"), "HEALTH BUFF 1", (double)50.0F, 0), new AttributeModifier(UUID.fromString("f51257dc-b7fa-4f7a-92d7-75d68e8592c4"), "HEALTH BUFF 2", (double)50.0F, 0), new AttributeModifier(UUID.fromString("3d6b2e42-4141-4364-b76d-0e8664bbd0bb"), "HEALTH BUFF 3", (double)50.0F, 0), new AttributeModifier(UUID.fromString("02c97a08-801c-4131-afa2-1427a6151934"), "HEALTH BUFF 4", (double)50.0F, 0), new AttributeModifier(UUID.fromString("0f354f6a-33c5-40be-93be-81b1338567f1"), "HEALTH BUFF 5", (double)50.0F, 0)};
   public static final AttributeModifier[] DMGBUFF = new AttributeModifier[]{new AttributeModifier(UUID.fromString("534f8c57-929a-48cf-bbd6-0fd851030748"), "DAMAGE BUFF 1", (double)0.5F, 0), new AttributeModifier(UUID.fromString("d317a76e-0e7c-4c61-acfd-9fa286053b32"), "DAMAGE BUFF 2", (double)0.5F, 0), new AttributeModifier(UUID.fromString("ff462d63-26a2-4363-830e-143ed97e2a4f"), "DAMAGE BUFF 3", (double)0.5F, 0), new AttributeModifier(UUID.fromString("cf1eb39e-0c67-495f-887c-0d3080828d2f"), "DAMAGE BUFF 4", (double)0.5F, 0), new AttributeModifier(UUID.fromString("3cfab9da-2701-43d8-ac07-885f16fa4117"), "DAMAGE BUFF 5", (double)0.5F, 0)};

   public static Entity getPointedEntity(World world, Entity entityplayer, double minrange, double range, float padding) {
      return getPointedEntity(world, entityplayer, minrange, range, padding, false);
   }

   public static Entity getPointedEntity(World world, Entity entityplayer, double minrange, double range, float padding, boolean nonCollide) {
      Entity pointedEntity = null;
      Vec3 vec3d = Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ);
      Vec3 vec3d1 = entityplayer.getLookVec();
      Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range);
      List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range).expand((double)padding, (double)padding, (double)padding));
      double d2 = (double)0.0F;

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (!((double)entity.getDistanceToEntity(entityplayer) < minrange) && (entity.canBeCollidedWith() || nonCollide) && world.func_147447_a(Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), false, true, false) == null) {
            float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.isVecInside(vec3d)) {
               if ((double)0.0F < d2 || d2 == (double)0.0F) {
                  pointedEntity = entity;
                  d2 = (double)0.0F;
               }
            } else if (movingobjectposition != null) {
               double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
               if (d3 < d2 || d2 == (double)0.0F) {
                  pointedEntity = entity;
                  d2 = d3;
               }
            }
         }
      }

      return pointedEntity;
   }

   public static Entity getPointedEntity(World world, EntityPlayer entityplayer, double range, Class clazz) {
      Entity pointedEntity = null;
      Vec3 vec3d = Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ);
      Vec3 vec3d1 = entityplayer.getLookVec();
      Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range);
      float f1 = 1.1F;
      List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(vec3d1.xCoord * range, vec3d1.yCoord * range, vec3d1.zCoord * range).expand((double)f1, (double)f1, (double)f1));
      double d2 = (double)0.0F;

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (entity.canBeCollidedWith() && world.func_147447_a(Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), false, true, false) == null && !clazz.isInstance(entity)) {
            float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.isVecInside(vec3d)) {
               if ((double)0.0F < d2 || d2 == (double)0.0F) {
                  pointedEntity = entity;
                  d2 = (double)0.0F;
               }
            } else if (movingobjectposition != null) {
               double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
               if (d3 < d2 || d2 == (double)0.0F) {
                  pointedEntity = entity;
                  d2 = d3;
               }
            }
         }
      }

      return pointedEntity;
   }

   public static boolean canEntityBeSeen(Entity entity, TileEntity te) {
      return te.getWorldObj().rayTraceBlocks(Vec3.createVectorHelper((double)te.xCoord + (double)0.5F, (double)te.yCoord + (double)1.25F, (double)te.zCoord + (double)0.5F), Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, double x, double y, double z) {
      return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(x, y, z), Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), false) == null;
   }

   public static boolean canEntityBeSeen(Entity entity, Entity entity2) {
      return entity.worldObj.rayTraceBlocks(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ), Vec3.createVectorHelper(entity2.posX, entity2.posY, entity2.posZ), false) == null;
   }

   public static void setRecentlyHit(EntityLivingBase ent, int hit) {
      try {
         ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, ent, hit, new String[]{"recentlyHit", "field_70718_bc"});
      } catch (Exception var3) {
      }

   }

   public static int getRecentlyHit(EntityLivingBase ent) {
      try {
         return ReflectionHelper.getPrivateValue(EntityLivingBase.class, ent, new String[]{"recentlyHit", "field_70718_bc"});
      } catch (Exception var2) {
         return 0;
      }
   }

   public static MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
      float f = 1.0F;
      float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
      float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
      double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
      double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + (double)(par1World.isRemote ? par2EntityPlayer.getEyeHeight() - par2EntityPlayer.getDefaultEyeHeight() : par2EntityPlayer.getEyeHeight());
      double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
      Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
      float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180F));
      float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180F));
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = (double)5.0F;
      if (par2EntityPlayer instanceof EntityPlayerMP) {
         d3 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
      }

      Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      return par1World.func_147447_a(vec3, vec31, par3, !par3, false);
   }

   public static ArrayList<Entity> getEntitiesInRange(World world, double x, double y, double z, Entity entity, Class<?> clazz, double range) {
      ArrayList<Entity> out = new ArrayList<>();
      List<Entity> list = (List<Entity>)world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(range, range, range));
      if (!list.isEmpty()) {
         for(Entity e : list) {
             if (entity == null || entity.getEntityId() != e.getEntityId()) {
               out.add(e);
            }
         }
      }

      return out;
   }

   public static boolean isVisibleTo(float fov, Entity ent, Entity ent2, float range) {
      double[] x = new double[]{ent2.posX, ent2.boundingBox.minY + (double)(ent2.height / 2.0F), ent2.posZ};
      double[] t = new double[]{ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ};
      Vec3 q = ent.getLookVec();
      q.xCoord *= (double)range;
      q.yCoord *= (double)range;
      q.zCoord *= (double)range;
      Vec3 l = q.addVector(ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ);
      double[] b = new double[]{l.xCoord, l.yCoord, l.zCoord};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static boolean isVisibleTo(float fov, Entity ent, double xx, double yy, double zz, float range) {
      double[] x = new double[]{xx, yy, zz};
      double[] t = new double[]{ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ};
      Vec3 q = ent.getLookVec();
      q.xCoord *= (double)range;
      q.yCoord *= (double)range;
      q.zCoord *= (double)range;
      Vec3 l = q.addVector(ent.posX, ent.boundingBox.minY + (double)ent.getEyeHeight(), ent.posZ);
      double[] b = new double[]{l.xCoord, l.yCoord, l.zCoord};
      return Utils.isLyingInCone(x, t, b, fov);
   }

   public static EntityItem entityDropSpecialItem(Entity entity, ItemStack stack, float dropheight) {
      if (stack.stackSize != 0 && stack.getItem() != null) {
         EntitySpecialItem entityitem = new EntitySpecialItem(entity.worldObj, entity.posX, entity.posY + (double)dropheight, entity.posZ, stack);
         entityitem.delayBeforeCanPickup = 10;
         entityitem.motionY = (double)0.1F;
         entityitem.motionX = (double)0.0F;
         entityitem.motionZ = (double)0.0F;
         if (entity.captureDrops) {
            entity.capturedDrops.add(entityitem);
         } else {
            entity.worldObj.spawnEntityInWorld(entityitem);
         }

         return entityitem;
      } else {
         return null;
      }
   }

   public static void makeChampion(EntityMob entity, boolean persist) {
      int type = entity.worldObj.rand.nextInt(ChampionModifier.mods.length);
      if (entity instanceof EntityCreeper) {
         type = 0;
      }

      IAttributeInstance modai = entity.getEntityAttribute(CHAMPION_MOD);
      modai.removeModifier(ChampionModifier.mods[type].attributeMod);
      modai.applyModifier(ChampionModifier.mods[type].attributeMod);
      if (!(entity instanceof EntityThaumcraftBoss)) {
         IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.maxHealth);
         iattributeinstance.removeModifier(CHAMPION_HEALTH);
         iattributeinstance.applyModifier(CHAMPION_HEALTH);
         IAttributeInstance iattributeinstance2 = entity.getEntityAttribute(SharedMonsterAttributes.attackDamage);
         iattributeinstance2.removeModifier(CHAMPION_DAMAGE);
         iattributeinstance2.applyModifier(CHAMPION_DAMAGE);
         entity.heal(25.0F);
         entity.setCustomNameTag(ChampionModifier.mods[type].getModNameLocalized() + " " + entity.getCommandSenderName());
      } else {
         ((EntityThaumcraftBoss)entity).generateName();
      }

      if (persist) {
         entity.func_110163_bv();
      }

      switch (type) {
         case 0:
            IAttributeInstance sai = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            sai.removeModifier(BOLDBUFF);
            sai.applyModifier(BOLDBUFF);
            break;
         case 3:
            IAttributeInstance mai = entity.getEntityAttribute(SharedMonsterAttributes.attackDamage);
            mai.removeModifier(MIGHTYBUFF);
            mai.applyModifier(MIGHTYBUFF);
            break;
         case 5:
            int bh = (int)entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() / 2;
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)bh);
      }

   }
}
