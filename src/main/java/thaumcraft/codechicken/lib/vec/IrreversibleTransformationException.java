package thaumcraft.codechicken.lib.vec;

public class IrreversibleTransformationException extends RuntimeException {
   public ITransformation t;

   public IrreversibleTransformationException(ITransformation t) {
      this.t = t;
   }

   public String getMessage() {
      return "The following transformation is irreversible:\n" + this.t;
   }
}
