package thaumcraft.client.renderers.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.tiles.TilePedestal;

@SideOnly(Side.CLIENT)
public class TilePedestalRenderer extends TileEntitySpecialRenderer {
   public void renderTileEntityAt(TilePedestal ped, double par2, double par4, double par6, float par8) {
      if (ped != null && ped.getWorldObj() != null && ped.getStackInSlot(0) != null) {
         EntityItem entityitem = null;
         float ticks = (float)Minecraft.getMinecraft().renderViewEntity.ticksExisted + par8;
         GL11.glPushMatrix();
         float h = MathHelper.sin(ticks % 32767.0F / 16.0F) * 0.05F;
         GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.15F + h, (float)par6 + 0.5F);
         GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
         if (ped.getStackInSlot(0).getItem() instanceof ItemBlock) {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
         } else {
            GL11.glScalef(1.0F, 1.0F, 1.0F);
         }

         ItemStack is = ped.getStackInSlot(0).copy();
         is.stackSize = 1;
         entityitem = new EntityItem(ped.getWorldObj(), 0.0F, 0.0F, 0.0F, is);
         entityitem.hoverStart = 0.0F;
         RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         if (!Minecraft.isFancyGraphicsEnabled()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         GL11.glPopMatrix();
      }

   }

   public void renderTileEntityAt(TileEntity tileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityAt((TilePedestal)tileEntity, par2, par4, par6, par8);
   }
}
