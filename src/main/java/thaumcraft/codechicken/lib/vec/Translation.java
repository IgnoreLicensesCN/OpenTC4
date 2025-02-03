package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.lwjgl.opengl.GL11;

public class Translation extends Transformation {
   public Vector3 vec;

   public Translation(Vector3 vec) {
      this.vec = vec;
   }

   public Translation(double x, double y, double z) {
      this(new Vector3(x, y, z));
   }

   public void apply(Vector3 vec) {
      vec.add(this.vec);
   }

   public void applyN(Vector3 normal) {
   }

   public void apply(Matrix4 mat) {
      mat.translate(this.vec);
   }

   public Transformation at(Vector3 point) {
      return this;
   }

   @SideOnly(Side.CLIENT)
   public void glApply() {
      GL11.glTranslated(this.vec.x, this.vec.y, this.vec.z);
   }

   public Transformation inverse() {
      return new Translation(-this.vec.x, -this.vec.y, -this.vec.z);
   }

   public Transformation merge(Transformation next) {
      return next instanceof Translation ? new Translation(this.vec.copy().add(((Translation)next).vec)) : null;
   }

   public boolean isRedundant() {
      return this.vec.equalsT(Vector3.zero);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "Translation(" + new BigDecimal(this.vec.x, cont) + ", " + new BigDecimal(this.vec.y, cont) + ", " + new BigDecimal(this.vec.z, cont) + ")";
   }
}
