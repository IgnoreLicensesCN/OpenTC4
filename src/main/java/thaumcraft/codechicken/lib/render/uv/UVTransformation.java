package thaumcraft.codechicken.lib.render.uv;

import thaumcraft.codechicken.lib.render.CCRenderState;
import thaumcraft.codechicken.lib.vec.ITransformation;

public abstract class UVTransformation extends ITransformation<UV, UVTransformation> implements CCRenderState.IVertexOperation {
   public static final int operationIndex = CCRenderState.registerOperation();

   public UVTransformation at(UV point) {
      return new UVTransformationList(new UVTransformation[]{new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v)});
   }

   public UVTransformationList with(UVTransformation t) {
      return new UVTransformationList(new UVTransformation[]{this, t});
   }

   public boolean load() {
      return !this.isRedundant();
   }

   public void operate() {
      this.apply(CCRenderState.vert.uv);
   }

   public int operationID() {
      return operationIndex;
   }
}
