package thaumcraft.client.renderers.models.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import thaumcraft.common.entities.monster.EntityWatcher;

@SideOnly(Side.CLIENT)
public class ModelWatcher extends ModelBase {
   private ModelRenderer guardianBody;
   private ModelRenderer guardianEye;
   private ModelRenderer[] guardianSpines;
   private ModelRenderer[] guardianTail;
   private static final String __OBFID = "CL_00002628";

   public ModelWatcher() {
      this.textureWidth = 64;
      this.textureHeight = 64;
      this.guardianSpines = new ModelRenderer[12];
      this.guardianBody = new ModelRenderer(this);
      this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
      this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
      this.guardianBody.mirror = true;
      this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12);
      this.guardianBody.mirror = false;
      this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
      this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);

      for(int i = 0; i < this.guardianSpines.length; ++i) {
         this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
         this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
         this.guardianBody.addChild(this.guardianSpines[i]);
      }

      this.guardianEye = new ModelRenderer(this, 8, 0);
      this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
      this.guardianBody.addChild(this.guardianEye);
      this.guardianTail = new ModelRenderer[3];
      this.guardianTail[0] = new ModelRenderer(this, 40, 0);
      this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
      this.guardianTail[1] = new ModelRenderer(this, 0, 54);
      this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
      this.guardianTail[2] = new ModelRenderer(this);
      this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
      this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
      this.guardianBody.addChild(this.guardianTail[0]);
      this.guardianTail[0].addChild(this.guardianTail[1]);
      this.guardianTail[1].addChild(this.guardianTail[2]);
   }

   public int func_178706_a() {
      return 54;
   }

   public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
      this.guardianBody.render(p_78088_7_);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      EntityWatcher entityguardian = (EntityWatcher)p_78087_7_;
      float f6 = p_78087_3_ - (float)entityguardian.ticksExisted;
      this.guardianBody.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
      this.guardianBody.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
      float[] afloat = new float[]{1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F};
      float[] afloat1 = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F};
      float[] afloat2 = new float[]{0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F};
      float[] afloat3 = new float[]{0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F};
      float[] afloat4 = new float[]{-8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F};
      float[] afloat5 = new float[]{8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F};
      float f7 = (1.0F - entityguardian.func_175469_o(f6)) * 0.55F;

      for(int i = 0; i < 12; ++i) {
         this.guardianSpines[i].rotateAngleX = (float)Math.PI * afloat[i];
         this.guardianSpines[i].rotateAngleY = (float)Math.PI * afloat1[i];
         this.guardianSpines[i].rotateAngleZ = (float)Math.PI * afloat2[i];
         this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + (float)i) * 0.01F - f7);
         this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + (float)i) * 0.01F - f7);
         this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(p_78087_3_ * 1.5F + (float)i) * 0.01F - f7);
      }

      this.guardianEye.rotationPointZ = -8.25F;
      Entity object = Minecraft.getMinecraft().renderViewEntity;
      if (entityguardian.func_175474_cn()) {
         object = entityguardian.getTargetedEntity();
      }

      if (object != null) {
         Vec3 vec3 = this.getPositionEyes(object, 0.0F);
         Vec3 vec31 = this.getPositionEyes(p_78087_7_, 0.0F);
         double d0 = vec3.yCoord - vec31.yCoord;
         if (d0 > (double)0.0F) {
            this.guardianEye.rotationPointY = 0.0F;
         } else {
            this.guardianEye.rotationPointY = 1.0F;
         }

         Vec3 vec32 = entityguardian.getLook(0.0F);
         vec32 = Vec3.createVectorHelper(vec32.xCoord, 0.0F, vec32.zCoord);
         Vec3 vec33 = Vec3.createVectorHelper(vec31.xCoord - vec3.xCoord, 0.0F, vec31.zCoord - vec3.zCoord).normalize();
         vec33.rotateAroundY(((float)Math.PI / 2F));
         double d1 = vec32.dotProduct(vec33);
         this.guardianEye.rotationPointX = MathHelper.sqrt_float((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1);
      }

      this.guardianEye.showModel = true;
      float f8 = entityguardian.func_175471_a(f6);
      this.guardianTail[0].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.05F;
      this.guardianTail[1].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.1F;
      this.guardianTail[1].rotationPointX = -1.5F;
      this.guardianTail[1].rotationPointY = 0.5F;
      this.guardianTail[1].rotationPointZ = 14.0F;
      this.guardianTail[2].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.15F;
      this.guardianTail[2].rotationPointX = 0.5F;
      this.guardianTail[2].rotationPointY = 0.5F;
      this.guardianTail[2].rotationPointZ = 6.0F;
   }

   private Vec3 getPositionEyes(Entity e, float p_174824_1_) {
      if (p_174824_1_ == 1.0F) {
         return Vec3.createVectorHelper(e.posX, e.posY + (double)e.getEyeHeight(), e.posZ);
      } else {
         double d0 = e.prevPosX + (e.posX - e.prevPosX) * (double)p_174824_1_;
         double d1 = e.prevPosY + (e.posY - e.prevPosY) * (double)p_174824_1_ + (double)e.getEyeHeight();
         double d2 = e.prevPosZ + (e.posZ - e.prevPosZ) * (double)p_174824_1_;
         return Vec3.createVectorHelper(d0, d1, d2);
      }
   }
}
