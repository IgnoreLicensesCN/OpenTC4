package thaumcraft.client.fx.beams;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class FXBeamGolemBoss extends EntityFX {
   public int particle = 16;
   EntityLivingBase boss = null;
   double movX = 0.0F;
   double movY = 0.0F;
   double movZ = 0.0F;
   private float length = 0.0F;
   private float rotYaw = 0.0F;
   private float rotPitch = 0.0F;
   private float prevYaw = 0.0F;
   private float prevPitch = 0.0F;
   private Entity targetEntity = null;
   private double tX = 0.0F;
   private double tY = 0.0F;
   private double tZ = 0.0F;
   private double ptX = 0.0F;
   private double ptY = 0.0F;
   private double ptZ = 0.0F;
   private int type = 0;
   private float endMod = 1.0F;
   private boolean reverse = false;
   private boolean pulse = true;
   private int rotationspeed = 5;
   private float prevSize = 0.0F;
   public int blendmode = 1;
   public float width = 1.0F;

   public FXBeamGolemBoss(World par1World, EntityLivingBase boss, Entity entity, float red, float green, float blue, int age) {
      super(par1World, boss.posX, boss.posY, boss.posZ, 0.0F, 0.0F, 0.0F);
      this.boss = boss;
      float f1 = MathHelper.cos(-boss.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
      float f2 = MathHelper.sin(-boss.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
      float f3 = -MathHelper.cos(-boss.rotationPitch * ((float)Math.PI / 180F));
      float f4 = MathHelper.sin(-boss.rotationPitch * ((float)Math.PI / 180F));
      Vec3 v = Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
      this.prevPosX = this.posX = boss.posX + v.xCoord * (double)0.5F;
      this.prevPosY = this.posY = boss.posY + (double)boss.getEyeHeight();
      this.prevPosZ = this.posZ = boss.posZ + v.zCoord * (double)0.5F;
      this.particleRed = red;
      this.particleGreen = green;
      this.particleBlue = blue;
      this.setSize(0.02F, 0.02F);
      this.noClip = true;
      this.motionX = 0.0F;
      this.motionY = 0.0F;
      this.motionZ = 0.0F;
      this.targetEntity = entity;
      this.tX = this.targetEntity.prevPosX;
      this.tY = this.targetEntity.boundingBox.minY + (double)(this.targetEntity.height / 2.0F);
      this.tZ = this.targetEntity.prevPosZ;
      float xd = (float)(this.posX - this.tX);
      float yd = (float)(this.posY - this.tY);
      float zd = (float)(this.posZ - this.tZ);
      this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
      double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
      this.rotYaw = (float)(Math.atan2(xd, zd) * (double)180.0F / Math.PI);
      this.rotPitch = (float)(Math.atan2(yd, var7) * (double)180.0F / Math.PI);
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      this.particleMaxAge = age;
      EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
      int visibleDistance = 50;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 25;
      }

      if (renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance) {
         this.particleMaxAge = 0;
      }

   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.ptX = this.tX;
      this.ptY = this.tY;
      this.ptZ = this.tZ;
      float f1 = MathHelper.cos(-this.boss.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
      float f2 = MathHelper.sin(-this.boss.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
      float f3 = -MathHelper.cos(-this.boss.rotationPitch * ((float)Math.PI / 180F));
      float f4 = MathHelper.sin(-this.boss.rotationPitch * ((float)Math.PI / 180F));
      Vec3 v = Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
      this.posX = this.boss.posX + v.xCoord * (double)0.5F;
      this.posY = this.boss.posY + (double)this.boss.getEyeHeight();
      this.posZ = this.boss.posZ + v.zCoord * (double)0.5F;
      this.prevYaw = this.rotYaw;
      this.prevPitch = this.rotPitch;
      if (this.targetEntity != null) {
         this.tX = this.targetEntity.prevPosX;
         this.tY = this.targetEntity.boundingBox.minY + (double)(this.targetEntity.height / 2.0F);
         this.tZ = this.targetEntity.prevPosZ;
      }

      float xd = (float)(this.posX - this.tX);
      float yd = (float)(this.posY - this.tY);
      float zd = (float)(this.posZ - this.tZ);
      this.length = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
      double var7 = MathHelper.sqrt_double(xd * xd + zd * zd);
      this.rotYaw = (float)(Math.atan2(xd, zd) * (double)180.0F / Math.PI);
      this.rotPitch = (float)(Math.atan2(yd, var7) * (double)180.0F / Math.PI);
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

   }

   public void setRGB(float r, float g, float b) {
      this.particleRed = r;
      this.particleGreen = g;
      this.particleBlue = b;
   }

   public void setType(int type) {
      this.type = type;
   }

   public void setEndMod(float endMod) {
      this.endMod = endMod;
   }

   public void setReverse(boolean reverse) {
      this.reverse = reverse;
   }

   public void setPulse(boolean pulse) {
      this.pulse = pulse;
   }

   public void setRotationspeed(int rotationspeed) {
      this.rotationspeed = rotationspeed;
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      tessellator.draw();
      GL11.glPushMatrix();
      float var9 = 1.0F;
      float slide = (float)Minecraft.getMinecraft().thePlayer.ticksExisted;
      float rot = (float)(this.worldObj.provider.getWorldTime() % (long)(360 / this.rotationspeed) * (long)this.rotationspeed) + (float)this.rotationspeed * f;
      float size = this.width;
      if (this.pulse) {
         size = Math.min((float)this.particleAge / 4.0F, this.width);
         size = (float)((double)this.prevSize + (double)(size - this.prevSize) * (double)f);
      }

      float op = 0.4F;
      if (this.pulse && this.particleMaxAge - this.particleAge <= 4) {
         op = 0.4F - (float)(4 - (this.particleMaxAge - this.particleAge)) * 0.1F;
      }

      switch (this.type) {
         case 1:
            UtilsFX.bindTexture("textures/misc/beam1.png");
            break;
         case 2:
            UtilsFX.bindTexture("textures/misc/beam2.png");
            break;
         case 3:
            UtilsFX.bindTexture("textures/misc/beam3.png");
            break;
         default:
            UtilsFX.bindTexture("textures/misc/beam.png");
      }

      GL11.glTexParameterf(3553, 10242, 10497.0F);
      GL11.glTexParameterf(3553, 10243, 10497.0F);
      GL11.glDisable(2884);
      float var11 = slide + f;
      if (this.reverse) {
         var11 *= -1.0F;
      }

      float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, this.blendmode);
      GL11.glDepthMask(false);
      float xx = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float yy = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float zz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      GL11.glTranslated(xx, yy, zz);
      float ry = (float)((double)this.prevYaw + (double)(this.rotYaw - this.prevYaw) * (double)f);
      float rp = (float)((double)this.prevPitch + (double)(this.rotPitch - this.prevPitch) * (double)f);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(180.0F + ry, 0.0F, 0.0F, -1.0F);
      GL11.glRotatef(rp, 1.0F, 0.0F, 0.0F);
      double var44 = -0.15 * (double)size;
      double var17 = 0.15 * (double)size;
      double var44b = -0.15 * (double)size * (double)this.endMod;
      double var17b = 0.15 * (double)size * (double)this.endMod;
      GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);

      for(int t = 0; t < 3; ++t) {
         double var29 = this.length * size / this.width * var9;
         double var31 = 0.0F;
         double var33 = 1.0F;
         double var35 = -1.0F + var12 + (float)t / 3.0F;
         double var37 = (double)(this.length * size / this.width * var9) + var35;
         GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
         tessellator.startDrawingQuads();
         tessellator.setBrightness(200);
         tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, op);
         tessellator.addVertexWithUV(var44b, var29, 0.0F, var33, var37);
         tessellator.addVertexWithUV(var44, 0.0F, 0.0F, var33, var35);
         tessellator.addVertexWithUV(var17, 0.0F, 0.0F, var31, var35);
         tessellator.addVertexWithUV(var17b, var29, 0.0F, var31, var37);
         tessellator.draw();
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glPopMatrix();
      this.renderImpact(tessellator, f, f1, f2, f3, f4, f5);
      Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());
      tessellator.startDrawingQuads();
      this.prevSize = size;
   }

   public void renderImpact(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glPushMatrix();
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      UtilsFX.bindTexture(ParticleEngine.particleTexture);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F);
      int part = this.particleAge % 16;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.3125F;
      float var11 = var10 + 0.0624375F;
      float var12 = this.endMod / 4.0F * this.width;
      float var13 = (float)(this.ptX + (this.tX - this.ptX) * (double)f - interpPosX);
      float var14 = (float)(this.ptY + (this.tY - this.ptY) * (double)f - interpPosY);
      float var15 = (float)(this.ptZ + (this.tZ - this.ptZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(200);
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, 0.66F);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
      tessellator.draw();
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glPopMatrix();
   }
}
