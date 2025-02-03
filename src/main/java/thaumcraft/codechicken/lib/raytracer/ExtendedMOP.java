package thaumcraft.codechicken.lib.raytracer;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ExtendedMOP extends MovingObjectPosition implements Comparable<ExtendedMOP> {
   public Object data;
   public double dist;

   public ExtendedMOP(Entity entity, Object data) {
      super(entity);
      this.setData(data);
   }

   public ExtendedMOP(int x, int y, int z, int side, Vec3 hit, Object data) {
      super(x, y, z, side, hit);
      this.setData(data);
   }

   public ExtendedMOP(MovingObjectPosition mop, Object data, double dist) {
      super(0, 0, 0, 0, mop.hitVec);
      this.typeOfHit = mop.typeOfHit;
      this.blockX = mop.blockX;
      this.blockY = mop.blockY;
      this.blockZ = mop.blockZ;
      this.sideHit = mop.sideHit;
      this.subHit = mop.subHit;
      this.setData(data);
      this.dist = dist;
   }

   public void setData(Object data) {
      if (data instanceof Integer) {
         this.subHit = (Integer)data;
      }

      this.data = data;
   }

   public static Object getData(MovingObjectPosition mop) {
      return mop instanceof ExtendedMOP ? ((ExtendedMOP)mop).data : mop.subHit;
   }

   public int compareTo(ExtendedMOP o) {
      return this.dist == o.dist ? 0 : (this.dist < o.dist ? -1 : 1);
   }
}
