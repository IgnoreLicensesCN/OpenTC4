package thaumcraft.client.renderers.tile;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileHole;

public class TileHoleRenderer extends TileEntitySpecialRenderer {
   FloatBuffer fBuffer = GLAllocation.createDirectFloatBuffer(16);
   private String t1 = "textures/misc/tunnel.png";
   private String t2 = "textures/misc/particlefield.png";
   private String t3 = "textures/misc/particlefield32.png";
   private boolean inrange;

   public void drawPlaneYPos(TileHole tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.999F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(y + (double)offset);
            float f9 = f8 - ActiveRenderInfo.objectY;
            float f10 = f8 + f5 - ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 = (float)(y + (double)offset) + f11;
            GL11.glTranslatef(px, f11, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -pz, -py);
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -py);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + (double)offset, z + (double)1.0F);
            tessellator.addVertex(x, y + (double)offset, z);
            tessellator.addVertex(x + (double)1.0F, y + (double)offset, z);
            tessellator.addVertex(x + (double)1.0F, y + (double)offset, z + (double)1.0F);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y + (double)offset, z + (double)1.0F, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x, y + (double)offset, z, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)offset, z, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)offset, z + (double)1.0F, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneYNeg(TileHole tileentityendportal, double x, double y, double z, float f) {
      float f1 = (float)TileEntityRendererDispatcher.staticPlayerX;
      float f2 = (float)TileEntityRendererDispatcher.staticPlayerY;
      float f3 = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.001F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(-(y + (double)offset));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 = (float)(y + (double)offset) + f11;
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + (double)offset, z);
            tessellator.addVertex(x, y + (double)offset, z + (double)1.0F);
            tessellator.addVertex(x + (double)1.0F, y + (double)offset, z + (double)1.0F);
            tessellator.addVertex(x + (double)1.0F, y + (double)offset, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y + (double)offset, z, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x, y + (double)offset, z + (double)1.0F, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)offset, z + (double)1.0F, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)offset, z, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZNeg(TileHole tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.001F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(-(z + (double)offset));
            float f9 = f8 + ActiveRenderInfo.objectZ;
            float f10 = f8 + f5 + ActiveRenderInfo.objectZ;
            float f11 = f9 / f10;
            f11 = (float)(z + (double)offset) + f11;
            GL11.glTranslatef(px, py, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -py, -pz);
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -pz);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y + (double)1.0F, z + (double)offset);
            tessellator.addVertex(x, y, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y + (double)1.0F, z + (double)offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y + (double)1.0F, z + (double)offset, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x, y, z + (double)offset, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y, z + (double)offset, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)1.0F, z + (double)offset, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneZPos(TileHole tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.999F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(z + (double)offset);
            float f9 = f8 - ActiveRenderInfo.objectZ;
            float f10 = f8 + f5 - ActiveRenderInfo.objectZ;
            float f11 = f9 / f10;
            f11 = (float)(z + (double)offset) + f11;
            GL11.glTranslatef(px, py, f11);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-px, -py, -pz);
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -pz);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x, y, z + (double)offset);
            tessellator.addVertex(x, y + (double)1.0F, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y + (double)1.0F, z + (double)offset);
            tessellator.addVertex(x + (double)1.0F, y, z + (double)offset);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x, y, z + (double)offset, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x, y + (double)1.0F, z + (double)offset, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y + (double)1.0F, z + (double)offset, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)1.0F, y, z + (double)offset, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXNeg(TileHole tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.001F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(-(x + (double)offset));
            float f9 = f8 + ActiveRenderInfo.objectX;
            float f10 = f8 + f5 + ActiveRenderInfo.objectX;
            float f11 = f9 / f10;
            f11 = (float)(x + (double)offset) + f11;
            GL11.glTranslatef(f11, py, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-pz, -py, -px);
            GL11.glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -px);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + (double)offset, y + (double)1.0F, z);
            tessellator.addVertex(x + (double)offset, y + (double)1.0F, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)1.0F, z, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)1.0F, z + (double)1.0F, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z + (double)1.0F, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   public void drawPlaneXPos(TileHole tileentityendportal, double x, double y, double z, float f) {
      float px = (float)TileEntityRendererDispatcher.staticPlayerX;
      float py = (float)TileEntityRendererDispatcher.staticPlayerY;
      float pz = (float)TileEntityRendererDispatcher.staticPlayerZ;
      GL11.glDisable(2896);
      Random random = new Random(31100L);
      float offset = 0.999F;
      if (this.inrange) {
         for(int i = 0; i < 16; ++i) {
            GL11.glPushMatrix();
            float f5 = (float)(16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);
            if (i == 0) {
               UtilsFX.bindTexture(this.t1);
               f7 = 0.1F;
               f5 = 65.0F;
               f6 = 0.125F;
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
            }

            if (i == 1) {
               UtilsFX.bindTexture(this.t2);
               GL11.glEnable(3042);
               GL11.glBlendFunc(1, 1);
               f6 = 0.5F;
            }

            float f8 = (float)(x + (double)offset);
            float f9 = f8 - ActiveRenderInfo.objectX;
            float f10 = f8 + f5 - ActiveRenderInfo.objectX;
            float f11 = f9 / f10;
            f11 = (float)(x + (double)offset) + f11;
            GL11.glTranslatef(f11, py, pz);
            GL11.glTexGeni(8192, 9472, 9217);
            GL11.glTexGeni(8193, 9472, 9217);
            GL11.glTexGeni(8194, 9472, 9217);
            GL11.glTexGeni(8195, 9472, 9216);
            GL11.glTexGen(8193, 9473, this.calcFloatBuffer(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glTexGen(8192, 9473, this.calcFloatBuffer(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(8194, 9473, this.calcFloatBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(8195, 9474, this.calcFloatBuffer(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glEnable(3168);
            GL11.glEnable(3169);
            GL11.glEnable(3170);
            GL11.glEnable(3171);
            GL11.glPopMatrix();
            GL11.glMatrixMode(5890);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(System.currentTimeMillis() % 700000L) / 250000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-pz, -py, -px);
            GL11.glTranslatef(ActiveRenderInfo.objectZ * f5 / f9, ActiveRenderInfo.objectY * f5 / f9, -px);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            float f12 = random.nextFloat() * 0.5F + 0.4F;
            float f13 = random.nextFloat() * 0.5F + 0.5F;
            if (i == 0) {
               f13 = 1.0F;
               f12 = 1.0F;
               f11 = 1.0F;
            }

            tessellator.setBrightness(180);
            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(x + (double)offset, y, z);
            tessellator.addVertex(x + (double)offset, y, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y + (double)1.0F, z + (double)1.0F);
            tessellator.addVertex(x + (double)offset, y + (double)1.0F, z);
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(5888);
         }
      } else {
         GL11.glPushMatrix();
         UtilsFX.bindTexture(this.t3);
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(180);
         tessellator.setColorRGBA_F(0.5F, 0.5F, 0.5F, 1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z, (double)1.0F, (double)1.0F);
         tessellator.addVertexWithUV(x + (double)offset, y, z + (double)1.0F, (double)1.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)1.0F, z + (double)1.0F, (double)0.0F, (double)0.0F);
         tessellator.addVertexWithUV(x + (double)offset, y + (double)1.0F, z, (double)0.0F, (double)1.0F);
         tessellator.draw();
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   private FloatBuffer calcFloatBuffer(float f, float f1, float f2, float f3) {
      this.fBuffer.clear();
      this.fBuffer.put(f).put(f1).put(f2).put(f3);
      this.fBuffer.flip();
      return this.fBuffer;
   }

   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
      double var10002 = (double)te.xCoord + (double)0.5F;
      double var10003 = (double)te.yCoord + (double)0.5F;
      double var10004 = (double)te.zCoord;
      this.inrange = Minecraft.getMinecraft().renderViewEntity.getDistanceSq(var10002, var10003, var10004 + (double)0.5F) < (double)512.0F;
      GL11.glDisable(2912);
      if (te.getWorldObj().getBlock(te.xCoord, te.yCoord + 1, te.zCoord).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord, te.yCoord + 1, te.zCoord) != ConfigBlocks.blockHole) {
         this.drawPlaneYPos((TileHole)te, x, y, z, f);
      }

      if (te.getWorldObj().getBlock(te.xCoord, te.yCoord - 1, te.zCoord).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord, te.yCoord - 1, te.zCoord) != ConfigBlocks.blockHole) {
         this.drawPlaneYNeg((TileHole)te, x, y, z, f);
      }

      if (te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord - 1).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord - 1) != ConfigBlocks.blockHole) {
         this.drawPlaneZNeg((TileHole)te, x, y, z, f);
      }

      if (te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord + 1).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord + 1) != ConfigBlocks.blockHole) {
         this.drawPlaneZPos((TileHole)te, x, y, z, f);
      }

      if (te.getWorldObj().getBlock(te.xCoord - 1, te.yCoord, te.zCoord).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord - 1, te.yCoord, te.zCoord) != ConfigBlocks.blockHole) {
         this.drawPlaneXNeg((TileHole)te, x, y, z, f);
      }

      if (te.getWorldObj().getBlock(te.xCoord + 1, te.yCoord, te.zCoord).isOpaqueCube() && te.getWorldObj().getBlock(te.xCoord + 1, te.yCoord, te.zCoord) != ConfigBlocks.blockHole) {
         this.drawPlaneXPos((TileHole)te, x, y, z, f);
      }

      GL11.glEnable(2912);
   }
}
