package thaumcraft.codechicken.lib.lighting;

import thaumcraft.codechicken.lib.colour.ColourRGBA;
import thaumcraft.codechicken.lib.render.CCRenderState;

public class PlanarLightModel implements CCRenderState.IVertexOperation {
   public static PlanarLightModel standardLightModel;
   public int[] colours;

   public PlanarLightModel(int[] colours) {
      this.colours = colours;
   }

   public boolean load() {
      CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
      CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
      return true;
   }

   public void operate() {
      CCRenderState.setColour(ColourRGBA.multiply(CCRenderState.colour, this.colours[CCRenderState.side]));
   }

   public int operationID() {
      return LightModel.operationIndex;
   }

   static {
      standardLightModel = LightModel.standardLightModel.reducePlanar();
   }
}
