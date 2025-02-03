package thaumcraft.codechicken.lib.render.uv;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class UVScale extends UVTransformation {
   double su;
   double sv;

   public UVScale(double scaleu, double scalev) {
      this.su = scaleu;
      this.sv = scalev;
   }

   public UVScale(double d) {
      this(d, d);
   }

   public void apply(UV uv) {
      uv.u *= this.su;
      uv.v *= this.sv;
   }

   public UVTransformation inverse() {
      return new UVScale((double)1.0F / this.su, (double)1.0F / this.sv);
   }

   public String toString() {
      MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
      return "UVScale(" + new BigDecimal(this.su, cont) + ", " + new BigDecimal(this.sv, cont) + ")";
   }
}
