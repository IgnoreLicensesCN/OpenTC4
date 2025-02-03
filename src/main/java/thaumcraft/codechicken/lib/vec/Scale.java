package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.lwjgl.opengl.GL11;

public class Scale extends Transformation {
   public Vector3 factor;

   public Scale(Vector3 factor) {
      this.factor = factor;
   }

   public Scale(double factor) {
      this(new Vector3(factor, factor, factor));
   }

   public Scale(double x, double y, double z) {
      this(new Vector3(x, y, z));
   }

   public void apply(Vector3 vec) {
      vec.multiply(this.factor);
   }

   public void applyN(Vector3 normal) {
   }

   public void apply(Matrix4 mat) {
      mat.scale(this.factor);
   }

   @SideOnly(Side.CLIENT)
   public void glApply() {
      GL11.glScaled(this.factor.x, this.factor.y, this.factor.z);
   }

   public Transformation inverse() {
      return new Scale((double)1.0F / this.factor.x, (double)1.0F / this.factor.y, (double)1.0F / this.factor.z);
   }

   public Transformation merge(Transformation next) {
      return next instanceof Scale ? new Scale(this.factor.copy().multiply(((Scale)next).factor)) : null;
   }

   public boolean isRedundant() {
      return this.factor.equalsT(Vector3.one);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Scale(" + new BigDecimal(this.factor.x, cont) + ", " + new BigDecimal(this.factor.y, cont) + ", " + new BigDecimal(this.factor.z, cont) + ")";
   }
}
