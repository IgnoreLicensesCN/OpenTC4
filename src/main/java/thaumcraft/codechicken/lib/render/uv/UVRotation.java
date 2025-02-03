package thaumcraft.codechicken.lib.render.uv;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import thaumcraft.codechicken.lib.math.MathHelper;

public class UVRotation extends UVTransformation {
   public double angle;

   public UVRotation(double angle) {
      this.angle = angle;
   }

   public void apply(UV uv) {
      double c = MathHelper.cos(this.angle);
      double s = MathHelper.sin(this.angle);
      double u2 = c * uv.u + s * uv.v;
      uv.v = -s * uv.u + c * uv.v;
      uv.u = u2;
   }

   public UVTransformation inverse() {
      return new UVRotation(-this.angle);
   }

   public UVTransformation merge(UVTransformation next) {
      return next instanceof UVRotation ? new UVRotation(this.angle + ((UVRotation)next).angle) : null;
   }

   public boolean isRedundant() {
      return MathHelper.between(-1.0E-5, this.angle, 1.0E-5);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "UVRotation(" + new BigDecimal(this.angle, cont) + ")";
   }
}
