package thaumcraft.codechicken.lib.vec;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import thaumcraft.codechicken.lib.render.CCRenderState;

public abstract class Transformation extends ITransformation<Vector3, Transformation>
        implements CCRenderState.IVertexOperation {
   public static final int operationIndex = CCRenderState.registerOperation();

   public abstract void applyN(Vector3 var1);

   public abstract void apply(Matrix4 var1);

   public Transformation at(Vector3 point) {
      return new TransformationList(new Translation(-point.x, -point.y, -point.z), this, point.translation());
   }

   public TransformationList with(Transformation t) {
      return new TransformationList(this, t);
   }

   @SideOnly(Side.CLIENT)
   public abstract void glApply();

   public boolean load() {
      CCRenderState.pipeline.addRequirement(CCRenderState.normalAttrib.operationID());
      return !this.isRedundant();
   }

   public void operate() {
      this.apply(CCRenderState.vert.vec);
      if (CCRenderState.normalAttrib.active) {
         this.applyN(CCRenderState.normal);
      }

   }

   public int operationID() {
      return operationIndex;
   }
}
