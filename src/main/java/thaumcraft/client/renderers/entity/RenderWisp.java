package thaumcraft.client.renderers.entity;

import java.awt.Color;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.entities.monster.EntityWisp;

public class RenderWisp extends Render {
   int size1 = 0;
   int size2 = 0;

   public RenderWisp() {
      this.shadowSize = 0.0F;
   }

   public void renderEntityAt(Entity entity, double x, double y, double z, float fq, float pticks) {
      if (!(((EntityLiving)entity).getHealth() <= 0.0F)) {
         float f1 = ActiveRenderInfo.rotationX;
         float f2 = ActiveRenderInfo.rotationXZ;
         float f3 = ActiveRenderInfo.rotationZ;
         float f4 = ActiveRenderInfo.rotationYZ;
         float f5 = ActiveRenderInfo.rotationXY;
         float f10 = 1.0F;
         float f11 = (float)x;
         float f12 = (float)y + 0.45F;
         float f13 = (float)z;
         Tessellator tessellator = Tessellator.instance;
         Color color = new Color(0);
         if (Aspect.getAspect(((EntityWisp)entity).getType()) != null) {
            color = new Color(Aspect.getAspect(((EntityWisp)entity).getType()).getColor());
         }

         GL11.glPushMatrix();
         GL11.glDepthMask(false);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         UtilsFX.bindTexture("textures/misc/wisp.png");
         int i = entity.ticksExisted % 16;
         float size4 = (float)(this.size1 * 4);
         float float_sizeMinus0_01 = (float)this.size1 - 0.01F;
         float float_texNudge = 1.0F / ((float)this.size1 * (float)this.size1 * 2.0F);
         float float_reciprocal = 1.0F / (float)this.size1;
         float x0 = ((float)(i % 4 * this.size1) + 0.0F) / size4;
         float x1 = ((float)(i % 4 * this.size1) + float_sizeMinus0_01) / size4;
         float x2 = ((float)(i / 4 * this.size1) + 0.0F) / size4;
         float x3 = ((float)(i / 4 * this.size1) + float_sizeMinus0_01) / size4;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(240);
         if (((EntityLiving)entity).hurtTime > 0) {
            tessellator.setColorRGBA_F(1.0F, (float)color.getGreen() / 300.0F, (float)color.getBlue() / 300.0F, 1.0F);
         } else {
            tessellator.setColorRGBA_F((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
         }

         tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)x1, (double)x3);
         tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)x1, (double)x2);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)x0, (double)x2);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)x0, (double)x3);
         tessellator.draw();
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glDepthMask(false);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         UtilsFX.bindTexture(ParticleEngine.particleTexture);
         int qq = entity.ticksExisted % 16;
         float size8 = 16.0F;
         x0 = (float)qq / size8;
         x1 = (float)(qq + 1) / size8;
         x2 = 5.0F / size8;
         x3 = 6.0F / size8;
         float var11 = MathHelper.sin(((float)entity.ticksExisted + pticks) / 10.0F) * 0.1F;
         f10 = 0.4F + var11;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(240);
         tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
         tessellator.addVertexWithUV((double)(f11 - f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 - f3 * f10 - f5 * f10), (double)x1, (double)x3);
         tessellator.addVertexWithUV((double)(f11 - f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 - f3 * f10 + f5 * f10), (double)x1, (double)x2);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 + f4 * f10), (double)(f12 + f2 * f10), (double)(f13 + f3 * f10 + f5 * f10), (double)x0, (double)x2);
         tessellator.addVertexWithUV((double)(f11 + f1 * f10 - f4 * f10), (double)(f12 - f2 * f10), (double)(f13 + f3 * f10 - f5 * f10), (double)x0, (double)x3);
         tessellator.draw();
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      if (this.size1 == 0) {
         this.size1 = UtilsFX.getTextureSize("textures/misc/wisp.png", 64);
      }

      this.renderEntityAt(entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return AbstractClientPlayer.locationStevePng;
   }
}
