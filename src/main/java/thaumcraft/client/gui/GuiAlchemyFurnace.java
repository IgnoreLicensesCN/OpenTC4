package thaumcraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.container.ContainerAlchemyFurnace;
import thaumcraft.common.tiles.TileAlchemyFurnace;

@SideOnly(Side.CLIENT)
public class GuiAlchemyFurnace extends GuiContainer {
   private TileAlchemyFurnace furnaceInventory;

   public GuiAlchemyFurnace(InventoryPlayer par1InventoryPlayer, TileAlchemyFurnace par2TileEntityFurnace) {
      super(new ContainerAlchemyFurnace(par1InventoryPlayer, par2TileEntityFurnace));
      this.furnaceInventory = par2TileEntityFurnace;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      UtilsFX.bindTexture("textures/gui/gui_alchemyfurnace.png");
      int k = (this.width - this.xSize) / 2;
      int l = (this.height - this.ySize) / 2;
      GL11.glEnable(GL11.GL_BLEND);
      this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
      if (this.furnaceInventory.isBurning()) {
         int i1 = this.furnaceInventory.getBurnTimeRemainingScaled(20);
         this.drawTexturedModalRect(k + 80, l + 26 + 20 - i1, 176, 20 - i1, 16, i1);
      }

      int i1 = this.furnaceInventory.getCookProgressScaled(46);
      this.drawTexturedModalRect(k + 106, l + 13 + 46 - i1, 216, 46 - i1, 9, i1);
      i1 = this.furnaceInventory.getContentsScaled(48);
      this.drawTexturedModalRect(k + 61, l + 12 + 48 - i1, 200, 48 - i1, 8, i1);
      this.drawTexturedModalRect(k + 60, l + 8, 232, 0, 10, 55);
      GL11.glDisable(GL11.GL_BLEND);
   }
}
