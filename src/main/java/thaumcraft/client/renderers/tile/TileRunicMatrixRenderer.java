package thaumcraft.client.renderers.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.models.ModelCube;
import thaumcraft.common.tiles.TileInfusionMatrix;

@SideOnly(Side.CLIENT)
public class TileRunicMatrixRenderer extends TileEntitySpecialRenderer {
   private ModelCube model = new ModelCube(0);
   private ModelCube model_over = new ModelCube(32);
   int type = 0;

   public TileRunicMatrixRenderer(int type) {
      this.type = type;
   }

   private void drawHalo(TileEntity is, double x, double y, double z, float par8, int count) {
      GL11.glPushMatrix();
      GL11.glTranslated(x + (double)0.5F, y + (double)0.5F, z + (double)0.5F);
      int q = !FMLClientHandler.instance().getClient().gameSettings.fancyGraphics ? 10 : 20;
      Tessellator tessellator = Tessellator.instance;
      RenderHelper.disableStandardItemLighting();
      float f1 = (float)count / 500.0F;
      float f3 = 0.9F;
      float f2 = 0.0F;
      Random random = new Random(245L);
      GL11.glDisable(3553);
      GL11.glShadeModel(7425);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(770, 1);
      GL11.glDisable(3008);
      GL11.glEnable(2884);
      GL11.glDepthMask(false);
      GL11.glPushMatrix();

      for(int i = 0; i < q; ++i) {
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(random.nextFloat() * 360.0F + f1 * 360.0F, 0.0F, 0.0F, 1.0F);
         tessellator.startDrawing(6);
         float fa = random.nextFloat() * 20.0F + 5.0F + f2 * 10.0F;
         float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
         fa /= 20.0F / ((float)Math.min(count, 50) / 50.0F);
         f4 /= 20.0F / ((float)Math.min(count, 50) / 50.0F);
         tessellator.setColorRGBA_I(16777215, (int)(255.0F * (1.0F - f2)));
         tessellator.addVertex(0.0F, 0.0F, 0.0F);
         tessellator.setColorRGBA_I(13369599, 0);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.addVertex(0.0F, fa, f4);
         tessellator.addVertex(-0.866 * (double)f4, fa, -0.5F * f4);
         tessellator.draw();
      }

      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glDisable(2884);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glShadeModel(7424);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      RenderHelper.enableStandardItemLighting();
      GL11.glBlendFunc(770, 771);
      GL11.glPopMatrix();
   }

   public void renderInfusionMatrix(TileInfusionMatrix is, double par2, double par4, double par6, float par8) {
      GL11.glPushMatrix();
      UtilsFX.bindTexture("textures/models/infuser.png");
      GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 0.5F, (float)par6 + 0.5F);
      float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + par8;
      if (is.getWorldObj() != null) {
         GL11.glRotatef(ticks % 360.0F * is.startUp, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(35.0F * is.startUp, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F * is.startUp, 0.0F, 0.0F, 1.0F);
      }

      float instability = Math.min(6.0F, 1.0F + (float)is.instability * 0.66F * ((float)Math.min(is.craftCount, 50) / 50.0F));
      float b1 = 0.0F;
      float b2 = 0.0F;
      float b3 = 0.0F;
      int aa = 0;
      int bb = 0;
      int cc = 0;

      for(int a = 0; a < 2; ++a) {
         for(int b = 0; b < 2; ++b) {
            for(int c = 0; c < 2; ++c) {
               if (is.active) {
                  b1 = MathHelper.sin((ticks + (float)(a * 10)) / (15.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
                  b2 = MathHelper.sin((ticks + (float)(b * 10)) / (14.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
                  b3 = MathHelper.sin((ticks + (float)(c * 10)) / (13.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
               }

               aa = a == 0 ? -1 : 1;
               bb = b == 0 ? -1 : 1;
               cc = c == 0 ? -1 : 1;
               GL11.glPushMatrix();
               GL11.glTranslatef(b1 + (float)aa * 0.25F, b2 + (float)bb * 0.25F, b3 + (float)cc * 0.25F);
               if (a > 0) {
                  GL11.glRotatef(90.0F, (float)a, 0.0F, 0.0F);
               }

               if (b > 0) {
                  GL11.glRotatef(90.0F, 0.0F, (float)b, 0.0F);
               }

               if (c > 0) {
                  GL11.glRotatef(90.0F, 0.0F, 0.0F, (float)c);
               }

               GL11.glScaled(0.45, 0.45, 0.45);
               this.model.render();
               GL11.glPopMatrix();
            }
         }
      }

      if (is.active) {
         GL11.glPushMatrix();
         GL11.glAlphaFunc(516, 0.003921569F);
         GL11.glEnable(GL11.GL_BLEND);
         GL11.glBlendFunc(770, 1);

         for(int a = 0; a < 2; ++a) {
            for(int b = 0; b < 2; ++b) {
               for(int c = 0; c < 2; ++c) {
                  b1 = MathHelper.sin((ticks + (float)(a * 10)) / (15.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
                  b2 = MathHelper.sin((ticks + (float)(b * 10)) / (14.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
                  b3 = MathHelper.sin((ticks + (float)(c * 10)) / (13.0F - instability / 2.0F)) * 0.01F * is.startUp * instability;
                  aa = a == 0 ? -1 : 1;
                  bb = b == 0 ? -1 : 1;
                  cc = c == 0 ? -1 : 1;
                  GL11.glPushMatrix();
                  GL11.glTranslatef(b1 + (float)aa * 0.25F, b2 + (float)bb * 0.25F, b3 + (float)cc * 0.25F);
                  if (a > 0) {
                     GL11.glRotatef(90.0F, (float)a, 0.0F, 0.0F);
                  }

                  if (b > 0) {
                     GL11.glRotatef(90.0F, 0.0F, (float)b, 0.0F);
                  }

                  if (c > 0) {
                     GL11.glRotatef(90.0F, 0.0F, 0.0F, (float)c);
                  }

                  GL11.glScaled(0.45, 0.45, 0.45);
                  int j = 15728880;
                  int k = j % 65536;
                  int l = j / 65536;
                  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
                  GL11.glColor4f(0.8F, 0.1F, 1.0F, (MathHelper.sin((ticks + (float)(a * 2) + (float)(b * 3) + (float)(c * 4)) / 4.0F) * 0.1F + 0.2F) * is.startUp);
                  this.model_over.render();
                  GL11.glPopMatrix();
               }
            }
         }

         GL11.glDisable(GL11.GL_BLEND);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
      if (is.crafting) {
         this.drawHalo(is, par2, par4, par6, par8, is.craftCount);
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      switch (this.type) {
         case 0:
            this.renderInfusionMatrix((TileInfusionMatrix)tileEntity, par2, par4, par6, par8);
            break;
         case 1:
            this.renderTileEntityAt(tileEntity, par2, par4, par6, par8);
      }

   }
}
