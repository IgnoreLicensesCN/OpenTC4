package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import thaumcraft.codechicken.lib.math.MathHelper;

public class Rotation extends Transformation {
   public static Transformation[] quarterRotations = new Transformation[]{new RedundantTransformation(), new VariableTransformation(new Matrix4(0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d1 = vec.x;
         double d2 = vec.z;
         vec.x = -d2;
         vec.z = d1;
      }

      public Transformation inverse() {
         return Rotation.quarterRotations[3];
      }
   }, new VariableTransformation(new Matrix4(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         vec.x = -vec.x;
         vec.z = -vec.z;
      }

      public Transformation inverse() {
         return this;
      }
   }, new VariableTransformation(new Matrix4(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d1 = vec.x;
         double d2 = vec.z;
         vec.x = d2;
         vec.z = -d1;
      }

      public Transformation inverse() {
         return Rotation.quarterRotations[1];
      }
   }};
   public static Transformation[] sideRotations = new Transformation[]{new RedundantTransformation(), new VariableTransformation(new Matrix4(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         vec.y = -vec.y;
         vec.z = -vec.z;
      }

      public Transformation inverse() {
         return this;
      }
   }, new VariableTransformation(new Matrix4(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d1 = vec.y;
         double d2 = vec.z;
         vec.y = -d2;
         vec.z = d1;
      }

      public Transformation inverse() {
         return Rotation.sideRotations[3];
      }
   }, new VariableTransformation(new Matrix4(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d1 = vec.y;
         double d2 = vec.z;
         vec.y = d2;
         vec.z = -d1;
      }

      public Transformation inverse() {
         return Rotation.sideRotations[2];
      }
   }, new VariableTransformation(new Matrix4(0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d0 = vec.x;
         double d1 = vec.y;
         vec.x = d1;
         vec.y = -d0;
      }

      public Transformation inverse() {
         return Rotation.sideRotations[5];
      }
   }, new VariableTransformation(new Matrix4(0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)) {
      public void apply(Vector3 vec) {
         double d0 = vec.x;
         double d1 = vec.y;
         vec.x = -d1;
         vec.y = d0;
      }

      public Transformation inverse() {
         return Rotation.sideRotations[4];
      }
   }};
   public static Vector3[] axes = new Vector3[]{new Vector3(0.0F, -1.0F, 0.0F), new Vector3(0.0F, 1.0F, 0.0F), new Vector3(0.0F, 0.0F, -1.0F), new Vector3(0.0F, 0.0F, 1.0F), new Vector3(-1.0F, 0.0F, 0.0F), new Vector3(1.0F, 0.0F, 0.0F)};
   public static int[] sideRotMap = new int[]{3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2};
   public static int[] rotSideMap = new int[]{-1, -1, 2, 0, 1, 3, -1, -1, 2, 0, 3, 1, 2, 0, -1, -1, 3, 1, 2, 0, -1, -1, 1, 3, 2, 0, 1, 3, -1, -1, 2, 0, 3, 1, -1, -1};
   public static int[] sideRotOffsets = new int[]{0, 2, 2, 0, 1, 3};
   public double angle;
   public Vector3 axis;
   private Quat quat;

   public static int rotateSide(int s, int r) {
      return sideRotMap[s << 2 | r];
   }

   public static int rotationTo(int s1, int s2) {
      if ((s1 & 6) == (s2 & 6)) {
         throw new IllegalArgumentException("Faces " + s1 + " and " + s2 + " are opposites");
      } else {
         return rotSideMap[s1 * 6 + s2];
      }
   }

   public static int getSidedRotation(EntityPlayer player, int side) {
      Vector3 look = new Vector3(player.getLook(1.0F));
      double max = 0.0F;
      int maxr = 0;

      for(int r = 0; r < 4; ++r) {
         Vector3 axis = axes[rotateSide(side ^ 1, r)];
         double d = look.scalarProject(axis);
         if (d > max) {
            max = d;
            maxr = r;
         }
      }

      return maxr;
   }

   public static Transformation sideOrientation(int s, int r) {
      return quarterRotations[(r + sideRotOffsets[s]) % 4].with(sideRotations[s]);
   }

   public static int getSideFromLookAngle(EntityLivingBase entity) {
      Vector3 look = new Vector3(entity.getLook(1.0F));
      double max = 0.0F;
      int maxs = 0;

      for(int s = 0; s < 6; ++s) {
         double d = look.scalarProject(axes[s]);
         if (d > max) {
            max = d;
            maxs = s;
         }
      }

      return maxs;
   }

   public Rotation(double angle, Vector3 axis) {
      this.angle = angle;
      this.axis = axis;
   }

   public Rotation(double angle, double x, double y, double z) {
      this(angle, new Vector3(x, y, z));
   }

   public Rotation(Quat quat) {
      this.quat = quat;
      this.angle = Math.acos(quat.s) * (double)2.0F;
      if (this.angle == (double)0.0F) {
         this.axis = new Vector3(0.0F, 1.0F, 0.0F);
      } else {
         double sa = Math.sin(this.angle * (double)0.5F);
         this.axis = new Vector3(quat.x / sa, quat.y / sa, quat.z / sa);
      }

   }

   public void apply(Vector3 vec) {
      if (this.quat == null) {
         this.quat = Quat.aroundAxis(this.axis, this.angle);
      }

      vec.rotate(this.quat);
   }

   public void applyN(Vector3 normal) {
      this.apply(normal);
   }

   public void apply(Matrix4 mat) {
      mat.rotate(this.angle, this.axis);
   }

   public Quat toQuat() {
      if (this.quat == null) {
         this.quat = Quat.aroundAxis(this.axis, this.angle);
      }

      return this.quat;
   }

   @SideOnly(Side.CLIENT)
   public void glApply() {
      GL11.glRotatef((float)(this.angle * (180D / Math.PI)), (float)this.axis.x, (float)this.axis.y, (float)this.axis.z);
   }

   public Transformation inverse() {
      return new Rotation(-this.angle, this.axis);
   }

   public Transformation merge(Transformation next) {
      if (next instanceof Rotation) {
         Rotation r = (Rotation)next;
         return r.axis.equalsT(this.axis) ? new Rotation(this.angle + r.angle, this.axis) : new Rotation(this.toQuat().copy().multiply(r.toQuat()));
      } else {
         return null;
      }
   }

   public boolean isRedundant() {
      return MathHelper.between(-1.0E-5, this.angle, 1.0E-5);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Rotation(" + new BigDecimal(this.angle, cont) + ", " + new BigDecimal(this.axis.x, cont) + ", " + new BigDecimal(this.axis.y, cont) + ", " + new BigDecimal(this.axis.z, cont) + ")";
   }
}
