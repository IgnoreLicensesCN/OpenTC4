package thaumcraft.codechicken.lib.render.uv;

import java.util.ArrayList;
import java.util.Iterator;

public class UVTransformationList extends UVTransformation {
   private ArrayList<UVTransformation> transformations = new ArrayList<>();

   public UVTransformationList(UVTransformation... transforms) {
      for(UVTransformation t : transforms) {
         if (t instanceof UVTransformationList) {
            this.transformations.addAll(((UVTransformationList)t).transformations);
         } else {
            this.transformations.add(t);
         }
      }

      this.compact();
   }

   public void apply(UV uv) {
       for (UVTransformation transformation : this.transformations) {
           ((UVTransformation) transformation).apply(uv);
       }

   }

   public UVTransformationList with(UVTransformation t) {
      if (t.isRedundant()) {
         return this;
      } else {
         if (t instanceof UVTransformationList) {
            this.transformations.addAll(((UVTransformationList)t).transformations);
         } else {
            this.transformations.add(t);
         }

         this.compact();
         return this;
      }
   }

   public UVTransformationList prepend(UVTransformation t) {
      if (t.isRedundant()) {
         return this;
      } else {
         if (t instanceof UVTransformationList) {
            this.transformations.addAll(0, ((UVTransformationList)t).transformations);
         } else {
            this.transformations.add(0, t);
         }

         this.compact();
         return this;
      }
   }

   private void compact() {
      ArrayList<UVTransformation> newList = new ArrayList(this.transformations.size());
      Iterator<UVTransformation> iterator = this.transformations.iterator();
      UVTransformation prev = null;

      while(iterator.hasNext()) {
         UVTransformation t = iterator.next();
         if (!t.isRedundant()) {
            if (prev != null) {
               UVTransformation m = prev.merge(t);
               if (m == null) {
                  newList.add(prev);
               } else if (m.isRedundant()) {
                  t = null;
               } else {
                  t = m;
               }
            }

            prev = t;
         }
      }

      if (prev != null) {
         newList.add(prev);
      }

      if (newList.size() < this.transformations.size()) {
         this.transformations = newList;
      }

   }

   public boolean isRedundant() {
      return this.transformations.size() == 0;
   }

   public UVTransformation inverse() {
      UVTransformationList rev = new UVTransformationList();

      for(int i = this.transformations.size() - 1; i >= 0; --i) {
         rev.with(this.transformations.get(i).inverse());
      }

      return rev;
   }

   public String toString() {
      StringBuilder s = new StringBuilder();

      for(UVTransformation t : this.transformations) {
         s.append("\n").append(t.toString());
      }

      return s.toString().trim();
   }
}
