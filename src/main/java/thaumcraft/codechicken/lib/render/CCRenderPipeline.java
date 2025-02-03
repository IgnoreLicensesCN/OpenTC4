package thaumcraft.codechicken.lib.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CCRenderPipeline {
   private ArrayList<CCRenderState.VertexAttribute> attribs = new ArrayList<>();
   private ArrayList<CCRenderState.IVertexOperation> ops = new ArrayList<>();
   private ArrayList<PipelineNode> nodes = new ArrayList<>();
   private ArrayList<CCRenderState.IVertexOperation> sorted = new ArrayList<>();
   private PipelineNode loading;
   private PipelineBuilder builder = new PipelineBuilder();

   public void setPipeline(CCRenderState.IVertexOperation... ops) {
      this.ops.clear();

       this.ops.addAll(Arrays.asList(ops));

      this.rebuild();
   }

   public void reset() {
      this.ops.clear();
      this.unbuild();
   }

   private void unbuild() {
      for(int i = 0; i < this.attribs.size(); ++i) {
         ((CCRenderState.VertexAttribute)this.attribs.get(i)).active = false;
      }

      this.attribs.clear();
      this.sorted.clear();
   }

   public void rebuild() {
      if (!this.ops.isEmpty() && CCRenderState.model != null) {
         while(this.nodes.size() < CCRenderState.operationCount()) {
            this.nodes.add(new PipelineNode());
         }

         this.unbuild();
         if (CCRenderState.useNormals) {
            this.addAttribute(CCRenderState.normalAttrib);
         }

         if (CCRenderState.baseColour == -1 && CCRenderState.alphaOverride < 0) {
            if (CCRenderState.hasColour) {
               CCRenderState.setColour(-1);
            }
         } else {
            this.addAttribute(CCRenderState.colourAttrib);
         }

         for(int i = 0; i < this.ops.size(); ++i) {
            CCRenderState.IVertexOperation op = (CCRenderState.IVertexOperation)this.ops.get(i);
            this.loading = (PipelineNode)this.nodes.get(op.operationID());
            boolean loaded = op.load();
            if (loaded) {
               this.loading.op = op;
            }

            if (op instanceof CCRenderState.VertexAttribute) {
               if (loaded) {
                  this.attribs.add((CCRenderState.VertexAttribute)op);
               } else {
                  ((CCRenderState.VertexAttribute)op).active = false;
               }
            }
         }

         for(int i = 0; i < this.nodes.size(); ++i) {
            ((PipelineNode)this.nodes.get(i)).add();
         }

      }
   }

   public void addRequirement(int opRef) {
      this.loading.deps.add(this.nodes.get(opRef));
   }

   public void addDependency(CCRenderState.VertexAttribute attrib) {
      this.loading.deps.add(this.nodes.get(attrib.operationID()));
      this.addAttribute(attrib);
   }

   public void addAttribute(CCRenderState.VertexAttribute attrib) {
      if (!attrib.active) {
         this.ops.add(attrib);
         attrib.active = true;
      }

   }

   public void operate() {
      for(int i = 0; i < this.sorted.size(); ++i) {
         ((CCRenderState.IVertexOperation)this.sorted.get(i)).operate();
      }

   }

   public PipelineBuilder builder() {
      this.ops.clear();
      return this.builder;
   }

   public class PipelineBuilder {
      public PipelineBuilder add(CCRenderState.IVertexOperation op) {
         CCRenderPipeline.this.ops.add(op);
         return this;
      }

      public PipelineBuilder add(CCRenderState.IVertexOperation... ops) {
          Collections.addAll(CCRenderPipeline.this.ops, ops);
         return this;
      }

      public void build() {
         CCRenderPipeline.this.rebuild();
      }

      public void render() {
         CCRenderPipeline.this.rebuild();
         CCRenderState.render();
      }
   }

   private class PipelineNode {
      public ArrayList<PipelineNode> deps;
      public CCRenderState.IVertexOperation op;

      private PipelineNode() {
         this.deps = new ArrayList<>();
      }

      public void add() {
         if (this.op != null) {
            for(int i = 0; i < this.deps.size(); ++i) {
               ((PipelineNode)this.deps.get(i)).add();
            }

            this.deps.clear();
            CCRenderPipeline.this.sorted.add(this.op);
            this.op = null;
         }
      }
   }
}
