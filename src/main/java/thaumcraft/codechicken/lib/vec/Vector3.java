package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.codechicken.lib.util.Copyable;

public class Vector3 implements Copyable {
   public static Vector3 zero = new Vector3();
   public static Vector3 one = new Vector3(1.0F, 1.0F, 1.0F);
   public static Vector3 center = new Vector3(0.5F, 0.5F, 0.5F);
   public double x;
   public double y;
   public double z;

   public Vector3() {
   }

   public Vector3(double d, double d1, double d2) {
      this.x = d;
      this.y = d1;
      this.z = d2;
   }

   public Vector3(Vector3 vec) {
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
   }

   public Vector3(double[] da) {
      this(da[0], da[1], da[2]);
   }

   public Vector3(Vec3 vec) {
      this.x = vec.xCoord;
      this.y = vec.yCoord;
      this.z = vec.zCoord;
   }

   public Vector3(BlockCoord coord) {
      this.x = coord.x;
      this.y = coord.y;
      this.z = coord.z;
   }

   public Vector3 copy() {
      return new Vector3(this);
   }

   public static Vector3 fromEntity(Entity e) {
      return new Vector3(e.posX, e.posY, e.posZ);
   }

   public static Vector3 fromEntityCenter(Entity e) {
      return new Vector3(e.posX, e.posY - (double)e.yOffset + (double)(e.height / 2.0F), e.posZ);
   }

   public static Vector3 fromTileEntity(TileEntity e) {
      return new Vector3(e.xCoord, e.yCoord, e.zCoord);
   }

   public static Vector3 fromTileEntityCenter(TileEntity e) {
      return new Vector3((double)e.xCoord + (double)0.5F, (double)e.yCoord + (double)0.5F, (double)e.zCoord + (double)0.5F);
   }

   public static Vector3 fromAxes(double[] da) {
      return new Vector3(da[2], da[0], da[1]);
   }

   public Vector3 set(double d, double d1, double d2) {
      this.x = d;
      this.y = d1;
      this.z = d2;
      return this;
   }

   public Vector3 set(Vector3 vec) {
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
      return this;
   }

   public double getSide(int side) {
      switch (side) {
         case 0:
         case 1:
            return this.y;
         case 2:
         case 3:
            return this.z;
         case 4:
         case 5:
            return this.x;
         default:
            throw new IndexOutOfBoundsException("Switch Falloff");
      }
   }

   public Vector3 setSide(int s, double v) {
      switch (s) {
         case 0:
         case 1:
            this.y = v;
            break;
         case 2:
         case 3:
            this.z = v;
            break;
         case 4:
         case 5:
            this.x = v;
            break;
         default:
            throw new IndexOutOfBoundsException("Switch Falloff");
      }

      return this;
   }

   public double dotProduct(Vector3 vec) {
      double d = vec.x * this.x + vec.y * this.y + vec.z * this.z;
      if (d > (double)1.0F && d < 1.00001) {
         d = 1.0F;
      } else if (d < (double)-1.0F && d > -1.00001) {
         d = -1.0F;
      }

      return d;
   }

   public double dotProduct(double d, double d1, double d2) {
      return d * this.x + d1 * this.y + d2 * this.z;
   }

   public Vector3 crossProduct(Vector3 vec) {
      double d = this.y * vec.z - this.z * vec.y;
      double d1 = this.z * vec.x - this.x * vec.z;
      double d2 = this.x * vec.y - this.y * vec.x;
      this.x = d;
      this.y = d1;
      this.z = d2;
      return this;
   }

   public Vector3 add(double d, double d1, double d2) {
      this.x += d;
      this.y += d1;
      this.z += d2;
      return this;
   }

   public Vector3 add(Vector3 vec) {
      this.x += vec.x;
      this.y += vec.y;
      this.z += vec.z;
      return this;
   }

   public Vector3 add(double d) {
      return this.add(d, d, d);
   }

   public Vector3 sub(Vector3 vec) {
      return this.subtract(vec);
   }

   public Vector3 subtract(Vector3 vec) {
      this.x -= vec.x;
      this.y -= vec.y;
      this.z -= vec.z;
      return this;
   }

   public Vector3 negate(Vector3 vec) {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Vector3 multiply(double d) {
      this.x *= d;
      this.y *= d;
      this.z *= d;
      return this;
   }

   public Vector3 multiply(Vector3 f) {
      this.x *= f.x;
      this.y *= f.y;
      this.z *= f.z;
      return this;
   }

   public Vector3 multiply(double fx, double fy, double fz) {
      this.x *= fx;
      this.y *= fy;
      this.z *= fz;
      return this;
   }

   public double mag() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double magSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3 normalize() {
      double d = this.mag();
      if (d != (double)0.0F) {
         this.multiply((double)1.0F / d);
      }

      return this;
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Vector3(" + new BigDecimal(this.x, cont) + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + ")";
   }

   public Vector3 perpendicular() {
      return this.z == (double)0.0F ? this.zCrossProduct() : this.xCrossProduct();
   }

   public Vector3 xCrossProduct() {
      double d = this.z;
      double d1 = -this.y;
      this.x = 0.0F;
      this.y = d;
      this.z = d1;
      return this;
   }

   public Vector3 zCrossProduct() {
      double d = this.y;
      double d1 = -this.x;
      this.x = d;
      this.y = d1;
      this.z = 0.0F;
      return this;
   }

   public Vector3 yCrossProduct() {
      double d = -this.z;
      double d1 = this.x;
      this.x = d;
      this.y = 0.0F;
      this.z = d1;
      return this;
   }

   public Vector3 rotate(double angle, Vector3 axis) {
      Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
      return this;
   }

   public Vector3 rotate(Quat rotator) {
      rotator.rotate(this);
      return this;
   }

   public Vec3 toVec3D() {
      return Vec3.createVectorHelper(this.x, this.y, this.z);
   }

   public double angle(Vector3 vec) {
      return Math.acos(this.copy().normalize().dotProduct(vec.copy().normalize()));
   }

   public boolean isZero() {
      return this.x == (double)0.0F && this.y == (double)0.0F && this.z == (double)0.0F;
   }

   public boolean isAxial() {
      return this.x == (double)0.0F ? this.y == (double)0.0F || this.z == (double)0.0F : this.y == (double)0.0F && this.z == (double)0.0F;
   }

   @SideOnly(Side.CLIENT)
   public Vector3f vector3f() {
      return new Vector3f((float)this.x, (float)this.y, (float)this.z);
   }

   @SideOnly(Side.CLIENT)
   public Vector4f vector4f() {
      return new Vector4f((float)this.x, (float)this.y, (float)this.z, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void glVertex() {
      GL11.glVertex3d(this.x, this.y, this.z);
   }

   public Vector3 YZintercept(Vector3 end, double px) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dx == (double)0.0F) {
         return null;
      } else {
         double d = (px - this.x) / dx;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x = px;
            this.y += d * dy;
            this.z += d * dz;
            return this;
         }
      }
   }

   public Vector3 XZintercept(Vector3 end, double py) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dy == (double)0.0F) {
         return null;
      } else {
         double d = (py - this.y) / dy;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x += d * dx;
            this.y = py;
            this.z += d * dz;
            return this;
         }
      }
   }

   public Vector3 XYintercept(Vector3 end, double pz) {
      double dx = end.x - this.x;
      double dy = end.y - this.y;
      double dz = end.z - this.z;
      if (dz == (double)0.0F) {
         return null;
      } else {
         double d = (pz - this.z) / dz;
         if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
            return this;
         } else if (!MathHelper.between(0.0F, d, 1.0F)) {
            return null;
         } else {
            this.x += d * dx;
            this.y += d * dy;
            this.z = pz;
            return this;
         }
      }
   }

   public Vector3 negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
      return this;
   }

   public Translation translation() {
      return new Translation(this);
   }

   public double scalarProject(Vector3 b) {
      double l = b.mag();
      return l == (double)0.0F ? (double)0.0F : this.dotProduct(b) / l;
   }

   public Vector3 project(Vector3 b) {
      double l = b.magSquared();
      if (l == (double)0.0F) {
         this.set(0.0F, 0.0F, 0.0F);
      } else {
         double m = this.dotProduct(b) / l;
         this.set(b).multiply(m);
      }
       return this;
   }

   public boolean equals(Object o) {
      if (!(o instanceof Vector3)) {
         return false;
      } else {
         Vector3 v = (Vector3)o;
         return this.x == v.x && this.y == v.y && this.z == v.z;
      }
   }

   public boolean equalsT(Vector3 v) {
      return MathHelper.between(this.x - 1.0E-5, v.x, this.x + 1.0E-5) && MathHelper.between(this.y - 1.0E-5, v.y, this.y + 1.0E-5) && MathHelper.between(this.z - 1.0E-5, v.z, this.z + 1.0E-5);
   }

   public Vector3 apply(Transformation t) {
      t.apply(this);
      return this;
   }

   public Vector3 $tilde() {
      return this.normalize();
   }

   public Vector3 unary_$tilde() {
      return this.normalize();
   }

   public Vector3 $plus(Vector3 v) {
      return this.add(v);
   }

   public Vector3 $minus(Vector3 v) {
      return this.subtract(v);
   }

   public Vector3 $times(double d) {
      return this.multiply(d);
   }

   public Vector3 $div(double d) {
      return this.multiply((double)1.0F / d);
   }

   public Vector3 $times(Vector3 v) {
      return this.crossProduct(v);
   }

   public double $dot$times(Vector3 v) {
      return this.dotProduct(v);
   }
}
