package thaumcraft.codechicken.lib.render;

import thaumcraft.codechicken.lib.vec.Matrix4;
import thaumcraft.codechicken.lib.vec.Quat;
import thaumcraft.codechicken.lib.vec.Rotation;
import thaumcraft.codechicken.lib.vec.Scale;
import thaumcraft.codechicken.lib.vec.Transformation;
import thaumcraft.codechicken.lib.vec.Vector3;

public class CCModelLibrary {
   public static CCModel icosahedron4;
   public static CCModel icosahedron7;
   private static int i;

   private static void generateIcosahedron() {
      Vector3[] verts = new Vector3[]{new Vector3(-1.0F, 1.618033988749894, 0.0F), new Vector3(1.0F, 1.618033988749894, 0.0F), new Vector3(1.0F, -1.618033988749894, 0.0F), new Vector3(-1.0F, -1.618033988749894, 0.0F), new Vector3(0.0F, -1.0F, 1.618033988749894), new Vector3(0.0F, 1.0F, 1.618033988749894), new Vector3(0.0F, 1.0F, -1.618033988749894), new Vector3(0.0F, -1.0F, -1.618033988749894), new Vector3(1.618033988749894, 0.0F, -1.0F), new Vector3(1.618033988749894, 0.0F, 1.0F), new Vector3(-1.618033988749894, 0.0F, 1.0F), new Vector3(-1.618033988749894, 0.0F, -1.0F)};
      Quat quat = Quat.aroundAxis(0.0F, 0.0F, 1.0F, Math.atan(0.6180339887498951));

      for(Vector3 vec : verts) {
         quat.rotate(vec);
      }

      icosahedron4 = CCModel.newModel(4, 60);
      icosahedron7 = CCModel.newModel(7, 80);
      i = 0;
      addIcosahedronTriangle(verts[1], 0.5F, 0.0F, verts[0], 0.0F, 0.25F, verts[5], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[1], 0.5F, 0.0F, verts[5], 0.0F, 0.25F, verts[9], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[1], 0.5F, 0.0F, verts[9], 0.0F, 0.25F, verts[8], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[1], 0.5F, 0.0F, verts[8], 0.0F, 0.25F, verts[6], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[1], 0.5F, 0.0F, verts[6], 0.0F, 0.25F, verts[0], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[0], 0.5F, 0.25F, verts[11], 0.0F, 0.75F, verts[10], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[5], 0.5F, 0.25F, verts[10], 0.0F, 0.75F, verts[4], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[9], 0.5F, 0.25F, verts[4], 0.0F, 0.75F, verts[2], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[8], 0.5F, 0.25F, verts[2], 0.0F, 0.75F, verts[7], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[6], 0.5F, 0.25F, verts[7], 0.0F, 0.75F, verts[11], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[2], 0.5F, 0.75F, verts[8], 0.0F, 0.25F, verts[9], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[7], 0.5F, 0.75F, verts[6], 0.0F, 0.25F, verts[8], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[11], 0.5F, 0.75F, verts[0], 0.0F, 0.25F, verts[6], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[10], 0.5F, 0.75F, verts[5], 0.0F, 0.25F, verts[0], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[4], 0.5F, 0.75F, verts[9], 0.0F, 0.25F, verts[5], 1.0F, 0.25F);
      addIcosahedronTriangle(verts[3], 0.5F, 1.0F, verts[2], 0.0F, 0.75F, verts[4], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[3], 0.5F, 1.0F, verts[7], 0.0F, 0.75F, verts[2], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[3], 0.5F, 1.0F, verts[11], 0.0F, 0.75F, verts[7], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[3], 0.5F, 1.0F, verts[10], 0.0F, 0.75F, verts[11], 1.0F, 0.75F);
      addIcosahedronTriangle(verts[3], 0.5F, 1.0F, verts[4], 0.0F, 0.75F, verts[10], 1.0F, 0.75F);
      icosahedron4.computeNormals().smoothNormals();
      icosahedron7.computeNormals().smoothNormals();
   }

   private static void addIcosahedronTriangle(Vector3 vec1, double u1, double v1, Vector3 vec2, double u2, double v2, Vector3 vec3, double u3, double v3) {
      icosahedron4.verts[i * 3] = icosahedron7.verts[i * 4] = new Vertex5(vec1, u1, v1);
      icosahedron4.verts[i * 3 + 1] = icosahedron7.verts[i * 4 + 1] = new Vertex5(vec2, u2, v2);
      icosahedron4.verts[i * 3 + 2] = icosahedron7.verts[i * 4 + 2] = icosahedron7.verts[i * 4 + 3] = new Vertex5(vec3, u3, v3);
      ++i;
   }

   public static Matrix4 getRenderMatrix(Vector3 position, Rotation rotation, double scale) {
      return (new Matrix4()).translate(position).apply(new Scale(scale)).apply(rotation);
   }

   static {
      generateIcosahedron();
   }
}
