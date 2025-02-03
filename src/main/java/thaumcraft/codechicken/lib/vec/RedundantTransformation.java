package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RedundantTransformation extends Transformation {
   public void apply(Vector3 vec) {
   }

   public void apply(Matrix4 mat) {
   }

   public void applyN(Vector3 normal) {
   }

   public Transformation at(Vector3 point) {
      return this;
   }

   @SideOnly(Side.CLIENT)
   public void glApply() {
   }

   public Transformation inverse() {
      return this;
   }

   public Transformation merge(Transformation next) {
      return next;
   }

   public boolean isRedundant() {
      return true;
   }

   public String toString() {
      return "Nothing()";
   }
}
