package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;

public class TileEldritchCap extends TileEntity {
   public boolean canUpdate() {
      return false;
   }

   public double getMaxRenderDistanceSquared() {
      return (double)9216.0F;
   }
}
