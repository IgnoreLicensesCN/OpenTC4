package thaumcraft.codechicken.lib.colour;

public class ColourARGB extends Colour {
   public ColourARGB(int colour) {
      super(colour >> 16 & 255, colour >> 8 & 255, colour & 255, colour >> 24 & 255);
   }

   public ColourARGB(int a, int r, int g, int b) {
      super(r, g, b, a);
   }

   public ColourARGB(ColourARGB colour) {
      super(colour);
   }

   public ColourARGB copy() {
      return new ColourARGB(this);
   }

   public int pack() {
      return pack(this);
   }

   public static int pack(Colour colour) {
      return (colour.a & 255) << 24 | (colour.r & 255) << 16 | (colour.g & 255) << 8 | colour.b & 255;
   }
}
