package thaumcraft.codechicken.lib.colour;

public class ColourRGBA extends Colour {
   public ColourRGBA(int colour) {
      super(colour >> 24 & 255, colour >> 16 & 255, colour >> 8 & 255, colour & 255);
   }

   public ColourRGBA(double r, double g, double b, double a) {
      super((int)((double)255.0F * r), (int)((double)255.0F * g), (int)((double)255.0F * b), (int)((double)255.0F * a));
   }

   public ColourRGBA(int r, int g, int b, int a) {
      super(r, g, b, a);
   }

   public ColourRGBA(ColourRGBA colour) {
      super(colour);
   }

   public int pack() {
      return pack(this);
   }

   public Colour copy() {
      return new ColourRGBA(this);
   }

   public static int pack(Colour colour) {
      return (colour.r & 255) << 24 | (colour.g & 255) << 16 | (colour.b & 255) << 8 | colour.a & 255;
   }

   public static int multiply(int c1, int c2) {
      if (c1 == -1) {
         return c2;
      } else if (c2 == -1) {
         return c1;
      } else {
         int r = ((c1 >>> 24) * (c2 >>> 24) & '\uff00') << 16;
         int g = ((c1 >> 16 & 255) * (c2 >> 16 & 255) & '\uff00') << 8;
         int b = (c1 >> 8 & 255) * (c2 >> 8 & 255) & '\uff00';
         int a = (c1 & 255) * (c2 & 255) >> 8;
         return r | g | b | a;
      }
   }

   public static int multiplyC(int c, float f) {
      int r = (int)((float)(c >>> 24) * f);
      int g = (int)((float)(c >> 16 & 255) * f);
      int b = (int)((float)(c >> 8 & 255) * f);
      return r << 24 | g << 16 | b << 8 | c & 255;
   }
}
