package thaumcraft.client.fx.particles;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXBoreSparkle extends EntityFX {
   private double targetX;
   private double targetY;
   private double targetZ;
   public int particle = 24;

   public FXBoreSparkle(World par1World, double par2, double par4, double par6, double tx, double ty, double tz) {
      super(par1World, par2, par4, par6, (double)0.0F, (double)0.0F, (double)0.0F);
      this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
      this.particleScale = this.rand.nextFloat() * 0.5F + 0.5F;
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
      double dx = tx - this.posX;
      double dy = ty - this.posY;
      double dz = tz - this.posZ;
      int base = (int)(MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz) * 3.0F);
      if (base < 1) {
         base = 1;
      }

      this.particleMaxAge = base / 2 + this.rand.nextInt(base);
      float f3 = 0.01F;
      this.motionX = (double)((float)this.rand.nextGaussian() * f3);
      this.motionY = (double)((float)this.rand.nextGaussian() * f3);
      this.motionZ = (double)((float)this.rand.nextGaussian() * f3);
      this.particleRed = 0.2F;
      this.particleGreen = 0.6F + this.rand.nextFloat() * 0.3F;
      this.particleBlue = 0.2F;
      this.particleGravity = 0.2F;
      this.noClip = false;
      EntityLivingBase renderentity = FMLClientHandler.instance().getClient().renderViewEntity;
      int visibleDistance = 64;
      if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
         visibleDistance = 32;
      }

      if (renderentity.getDistance(this.posX, this.posY, this.posZ) > (double)visibleDistance) {
         this.particleMaxAge = 0;
      }

   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      float bob = MathHelper.sin((float)this.particleAge / 3.0F) * 0.5F + 1.0F;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
      int part = this.particleAge % 4;
      float var8 = (float)part / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = 0.25F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.1F * this.particleScale * bob;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      float var16 = 1.0F;
      tessellator.setBrightness(240);
      tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 1.0F);
      tessellator.addVertexWithUV((double)(var13 - f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 - f3 * var12 - f5 * var12), (double)var9, (double)var11);
      tessellator.addVertexWithUV((double)(var13 - f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 - f3 * var12 + f5 * var12), (double)var9, (double)var10);
      tessellator.addVertexWithUV((double)(var13 + f1 * var12 + f4 * var12), (double)(var14 + f2 * var12), (double)(var15 + f3 * var12 + f5 * var12), (double)var8, (double)var10);
      tessellator.addVertexWithUV((double)(var13 + f1 * var12 - f4 * var12), (double)(var14 - f2 * var12), (double)(var15 + f3 * var12 - f5 * var12), (double)var8, (double)var11);
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ < this.particleMaxAge && (MathHelper.floor_double(this.posX) != MathHelper.floor_double(this.targetX) || MathHelper.floor_double(this.posY) != MathHelper.floor_double(this.targetY) || MathHelper.floor_double(this.posZ) != MathHelper.floor_double(this.targetZ))) {
         if (!this.noClip) {
            this.pushOutOfBlocks(this.posX, this.posY, this.posZ);
         }

         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.985;
         this.motionY *= 0.985;
         this.motionZ *= 0.985;
         double dx = this.targetX - this.posX;
         double dy = this.targetY - this.posY;
         double dz = this.targetZ - this.posZ;
         double d13 = 0.3;
         double d11 = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
         if (d11 < (double)4.0F) {
            this.particleScale *= 0.9F;
            d13 = 0.6;
         }

         dx /= d11;
         dy /= d11;
         dz /= d11;
         this.motionX += dx * d13;
         this.motionY += dy * d13;
         this.motionZ += dz * d13;
         this.motionX = (double)MathHelper.clamp_float((float)this.motionX, -0.35F, 0.35F);
         this.motionY = (double)MathHelper.clamp_float((float)this.motionY, -0.35F, 0.35F);
         this.motionZ = (double)MathHelper.clamp_float((float)this.motionZ, -0.35F, 0.35F);
      } else {
         this.setDead();
      }
   }

   public void setGravity(float value) {
      this.particleGravity = value;
   }

   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - (double)var7;
      double var12 = par3 - (double)var8;
      double var14 = par5 - (double)var9;
      if (!this.worldObj.isAirBlock(var7, var8, var9) && !this.worldObj.isAnyLiquid(this.boundingBox)) {
         boolean var16 = !this.worldObj.isBlockNormalCubeDefault(var7 - 1, var8, var9, true);
         boolean var17 = !this.worldObj.isBlockNormalCubeDefault(var7 + 1, var8, var9, true);
         boolean var18 = !this.worldObj.isBlockNormalCubeDefault(var7, var8 - 1, var9, true);
         boolean var19 = !this.worldObj.isBlockNormalCubeDefault(var7, var8 + 1, var9, true);
         boolean var20 = !this.worldObj.isBlockNormalCubeDefault(var7, var8, var9 - 1, true);
         boolean var21 = !this.worldObj.isBlockNormalCubeDefault(var7, var8, var9 + 1, true);
         byte var22 = -1;
         double var23 = (double)9999.0F;
         if (var16 && var10 < var23) {
            var23 = var10;
            var22 = 0;
         }

         if (var17 && (double)1.0F - var10 < var23) {
            var23 = (double)1.0F - var10;
            var22 = 1;
         }

         if (var18 && var12 < var23) {
            var23 = var12;
            var22 = 2;
         }

         if (var19 && (double)1.0F - var12 < var23) {
            var23 = (double)1.0F - var12;
            var22 = 3;
         }

         if (var20 && var14 < var23) {
            var23 = var14;
            var22 = 4;
         }

         if (var21 && (double)1.0F - var14 < var23) {
            var23 = (double)1.0F - var14;
            var22 = 5;
         }

         float var25 = this.rand.nextFloat() * 0.05F + 0.025F;
         float var26 = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
         if (var22 == 0) {
            this.motionX = (double)(-var25);
            this.motionY = this.motionZ = (double)var26;
         }

         if (var22 == 1) {
            this.motionX = (double)var25;
            this.motionY = this.motionZ = (double)var26;
         }

         if (var22 == 2) {
            this.motionY = (double)(-var25);
            this.motionX = this.motionZ = (double)var26;
         }

         if (var22 == 3) {
            this.motionY = (double)var25;
            this.motionX = this.motionZ = (double)var26;
         }

         if (var22 == 4) {
            this.motionZ = (double)(-var25);
            this.motionY = this.motionX = (double)var26;
         }

         if (var22 == 5) {
            this.motionZ = (double)var25;
            this.motionY = this.motionX = (double)var26;
         }

         return true;
      } else {
         return false;
      }
   }
}
