package thaumcraft.client.fx.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXSlimyBubble extends EntityFX {
   int particle = 144;

   public FXSlimyBubble(World world, double d, double d1, double d2, float f) {
      super(world, d, d1, d2, 0.0F, 0.0F, 0.0F);
      this.particleRed = 1.0F;
      this.particleGreen = 1.0F;
      this.particleBlue = 1.0F;
      this.particleGravity = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0F;
      this.particleScale = f;
      this.particleMaxAge = 15 + world.rand.nextInt(5);
      this.noClip = false;
      this.setSize(0.01F, 0.01F);
   }

   public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, this.particleAlpha);
      float var8 = (float)(this.particle % 16) / 16.0F;
      float var9 = var8 + 0.0625F;
      float var10 = (float)(this.particle / 16) / 16.0F;
      float var11 = var10 + 0.0625F;
      float var12 = this.particleScale;
      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
      tessellator.setBrightness(this.getBrightnessForRender(f));
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
      tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, var9, var11);
      tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, var9, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, var8, var10);
      tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, var8, var11);
   }

   public int getFXLayer() {
      return 1;
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      if (this.particleAge - 1 < 6) {
         this.particle = 144 + this.particleAge / 2;
         if (this.particleAge == 5) {
            this.posY += 0.1;
         }
      } else if (this.particleAge < this.particleMaxAge - 4) {
         this.motionY += 0.005;
         this.particle = 147 + this.particleAge % 4 / 2;
      } else {
         this.motionY /= 2.0F;
         this.particle = 150 - (this.particleMaxAge - this.particleAge) / 2;
      }

      this.posY += this.motionY;
   }
}
