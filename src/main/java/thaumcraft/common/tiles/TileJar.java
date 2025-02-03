package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.TileThaumcraft;

public class TileJar extends TileThaumcraft {
   protected static Random rand = new Random();
   ResourceLocation texture = new ResourceLocation("thaumcraft", "textures/models/jar.png");

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
   }

   public boolean canUpdate() {
      return true;
   }

   public void updateEntity() {
      super.updateEntity();
   }

   public ResourceLocation getTexture() {
      return this.texture;
   }
}
