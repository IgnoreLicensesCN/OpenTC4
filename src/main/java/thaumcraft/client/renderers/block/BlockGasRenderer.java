package thaumcraft.client.renderers.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import thaumcraft.common.blocks.BlockFluxGas;
import thaumcraft.common.config.ConfigBlocks;

public class BlockGasRenderer implements ISimpleBlockRenderingHandler {
   public static BlockGasRenderer instance = new BlockGasRenderer();
   static final float LIGHT_Y_NEG = 0.5F;
   static final float LIGHT_Y_POS = 1.0F;
   static final float LIGHT_XZ_NEG = 0.8F;
   static final float LIGHT_XZ_POS = 0.6F;
   static final double RENDER_OFFSET = (double)0.001F;

   public float getFluidHeightAverage(float[] flow) {
      float total = 0.0F;
      int count = 0;
      float end = 0.0F;

      for(int i = 0; i < flow.length; ++i) {
         if (flow[i] >= 0.875F && end != 1.0F) {
            end = flow[i];
         }

         if (flow[i] >= 0.0F) {
            total += flow[i];
            ++count;
         }
      }

      if (end == 0.0F) {
         end = total / (float)count;
      }

      return end;
   }

   public float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockFluxGas block) {
      if (world.getBlock(x, y, z) == block) {
         if (world.getBlock(x, y - block.getDensityDir(), z).getMaterial().isLiquid()) {
            return 1.0F;
         }

         if (world.getBlockMetadata(x, y, z) == block.getMaxRenderHeightMeta()) {
            return 0.875F;
         }
      }

      return !world.getBlock(x, y, z).getMaterial().isSolid() && world.getBlock(x, y - block.getDensityDir(), z) == block ? 1.0F : block.getQuantaPercentage(world, x, y, z) * 0.875F;
   }

   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      if (!(block instanceof BlockFluxGas)) {
         return false;
      } else {
         Tessellator tessellator = Tessellator.instance;
         int color = block.colorMultiplier(world, x, y, z);
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         BlockFluxGas theFluid = (BlockFluxGas)block;
         int bMeta = world.getBlockMetadata(x, y, z);
         if (!world.isSideSolid(x, y + theFluid.getDensityDir(), z, ForgeDirection.DOWN, false)) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            tessellator.setColorOpaque_F(red, green, blue);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.clearOverrideBlockTexture();
            renderer.setRenderBoundsFromBlock(block);
            return true;
         } else {
            boolean renderTop = world.getBlock(x, y - theFluid.getDensityDir(), z) != theFluid;
            boolean renderBottom = block.shouldSideBeRendered(world, x, y + theFluid.getDensityDir(), z, 0) && world.getBlock(x, y + theFluid.getDensityDir(), z) != theFluid;
            boolean[] renderSides = new boolean[]{block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4), block.shouldSideBeRendered(world, x + 1, y, z, 5)};
            if (!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3]) {
               return false;
            } else {
               boolean rendered = false;
               float flow11 = this.getFluidHeightForRender(world, x, y, z, theFluid);
               double heightNW;
               double heightSW;
               double heightSE;
               double heightNE;
               if (flow11 != 1.0F) {
                  float flow00 = this.getFluidHeightForRender(world, x - 1, y, z - 1, theFluid);
                  float flow01 = this.getFluidHeightForRender(world, x - 1, y, z, theFluid);
                  float flow02 = this.getFluidHeightForRender(world, x - 1, y, z + 1, theFluid);
                  float flow10 = this.getFluidHeightForRender(world, x, y, z - 1, theFluid);
                  float flow12 = this.getFluidHeightForRender(world, x, y, z + 1, theFluid);
                  float flow20 = this.getFluidHeightForRender(world, x + 1, y, z - 1, theFluid);
                  float flow21 = this.getFluidHeightForRender(world, x + 1, y, z, theFluid);
                  float flow22 = this.getFluidHeightForRender(world, x + 1, y, z + 1, theFluid);
                  heightNW = (double)this.getFluidHeightAverage(new float[]{flow00, flow01, flow10, flow11});
                  heightSW = (double)this.getFluidHeightAverage(new float[]{flow01, flow02, flow12, flow11});
                  heightSE = (double)this.getFluidHeightAverage(new float[]{flow12, flow21, flow22, flow11});
                  heightNE = (double)this.getFluidHeightAverage(new float[]{flow10, flow20, flow21, flow11});
               } else {
                  heightNW = (double)flow11;
                  heightSW = (double)flow11;
                  heightSE = (double)flow11;
                  heightNE = (double)flow11;
               }

               boolean rises = theFluid.getDensityDir() == 1;
               if (renderer.renderAllFaces || renderTop) {
                  rendered = true;
                  IIcon iconStill = block.getIcon(1, bMeta);
                  float flowDir = (float)BlockFluidBase.getFlowDirection(world, x, y, z);
                  if (flowDir > -999.0F) {
                     iconStill = block.getIcon(2, bMeta);
                  }

                  heightNW -= (double)0.001F;
                  heightSW -= (double)0.001F;
                  heightSE -= (double)0.001F;
                  heightNE -= (double)0.001F;
                  double u4;
                  double v1;
                  double v2;
                  double v3;
                  double v4;
                  double u1;
                  double u2;
                  double u3;
                  if (flowDir < -999.0F) {
                     u2 = (double)iconStill.getInterpolatedU((double)0.0F);
                     v2 = (double)iconStill.getInterpolatedV((double)0.0F);
                     u1 = u2;
                     v1 = (double)iconStill.getInterpolatedV((double)16.0F);
                     u4 = (double)iconStill.getInterpolatedU((double)16.0F);
                     v4 = v1;
                     u3 = u4;
                     v3 = v2;
                  } else {
                     float xFlow = MathHelper.sin(flowDir) * 0.25F;
                     float zFlow = MathHelper.cos(flowDir) * 0.25F;
                     u2 = (double)iconStill.getInterpolatedU((double)(8.0F + (-zFlow - xFlow) * 16.0F));
                     v2 = (double)iconStill.getInterpolatedV((double)(8.0F + (-zFlow + xFlow) * 16.0F));
                     u1 = (double)iconStill.getInterpolatedU((double)(8.0F + (-zFlow + xFlow) * 16.0F));
                     v1 = (double)iconStill.getInterpolatedV((double)(8.0F + (zFlow + xFlow) * 16.0F));
                     u4 = (double)iconStill.getInterpolatedU((double)(8.0F + (zFlow + xFlow) * 16.0F));
                     v4 = (double)iconStill.getInterpolatedV((double)(8.0F + (zFlow - xFlow) * 16.0F));
                     u3 = (double)iconStill.getInterpolatedU((double)(8.0F + (zFlow - xFlow) * 16.0F));
                     v3 = (double)iconStill.getInterpolatedV((double)(8.0F + (-zFlow - xFlow) * 16.0F));
                  }

                  tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
                  tessellator.setColorOpaque_F(red, green, blue);
                  if (!rises) {
                     tessellator.addVertexWithUV((double)(x), (double)y + heightNW, (double)(z), u2, v2);
                     tessellator.addVertexWithUV((double)(x), (double)y + heightSW, (double)(z + 1), u1, v1);
                     tessellator.addVertexWithUV((double)(x + 1), (double)y + heightSE, (double)(z + 1), u4, v4);
                     tessellator.addVertexWithUV((double)(x + 1), (double)y + heightNE, (double)(z), u3, v3);
                  } else {
                     tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightNE, (double)(z), u3, v3);
                     tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightSE, (double)(z + 1), u4, v4);
                     tessellator.addVertexWithUV((double)(x), (double)(y + 1) - heightSW, (double)(z + 1), u1, v1);
                     tessellator.addVertexWithUV((double)(x), (double)(y + 1) - heightNW, (double)(z), u2, v2);
                  }
               }

               if (renderer.renderAllFaces || renderBottom) {
                  rendered = true;
                  tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
                  if (!rises) {
                     tessellator.setColorOpaque_F(0.5F * red, 0.5F * green, 0.5F * blue);
                     renderer.renderFaceYNeg(block, (double)x, (double)y + (double)0.001F, (double)z, block.getIcon(0, bMeta));
                  } else {
                     tessellator.setColorOpaque_F(red, green, blue);
                     renderer.renderFaceYPos(block, (double)x, (double)y + (double)0.001F, (double)z, block.getIcon(1, bMeta));
                  }
               }

               for(int side = 0; side < 4; ++side) {
                  int x2 = x;
                  int z2 = z;
                  switch (side) {
                     case 0:
                        z2 = z - 1;
                        break;
                     case 1:
                        z2 = z + 1;
                        break;
                     case 2:
                        x2 = x - 1;
                        break;
                     case 3:
                        x2 = x + 1;
                  }

                  IIcon iconFlow = block.getIcon(side + 2, bMeta);
                  if (renderer.renderAllFaces || renderSides[side]) {
                     rendered = true;
                     double ty1;
                     double tx1;
                     double ty2;
                     double tx2;
                     double tz1;
                     double tz2;
                     if (side == 0) {
                        ty1 = heightNW;
                        ty2 = heightNE;
                        tx1 = (double)x;
                        tx2 = (double)(x + 1);
                        tz1 = (double)z + (double)0.001F;
                        tz2 = (double)z + (double)0.001F;
                     } else if (side == 1) {
                        ty1 = heightSE;
                        ty2 = heightSW;
                        tx1 = (double)(x + 1);
                        tx2 = (double)x;
                        tz1 = (double)(z + 1) - (double)0.001F;
                        tz2 = (double)(z + 1) - (double)0.001F;
                     } else if (side == 2) {
                        ty1 = heightSW;
                        ty2 = heightNW;
                        tx1 = (double)x + (double)0.001F;
                        tx2 = (double)x + (double)0.001F;
                        tz1 = (double)(z + 1);
                        tz2 = (double)z;
                     } else {
                        ty1 = heightNE;
                        ty2 = heightSE;
                        tx1 = (double)(x + 1) - (double)0.001F;
                        tx2 = (double)(x + 1) - (double)0.001F;
                        tz1 = (double)z;
                        tz2 = (double)(z + 1);
                     }

                     float u1Flow = iconFlow.getInterpolatedU((double)0.0F);
                     float u2Flow = iconFlow.getInterpolatedU((double)8.0F);
                     float v1Flow = iconFlow.getInterpolatedV(((double)1.0F - ty1) * (double)16.0F * (double)0.5F);
                     float v2Flow = iconFlow.getInterpolatedV(((double)1.0F - ty2) * (double)16.0F * (double)0.5F);
                     float v3Flow = iconFlow.getInterpolatedV((double)8.0F);
                     tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x2, y, z2));
                     float sideLighting = 1.0F;
                     if (side < 2) {
                        sideLighting = 0.8F;
                     } else {
                        sideLighting = 0.6F;
                     }

                     tessellator.setColorOpaque_F(1.0F * sideLighting * red, 1.0F * sideLighting * green, 1.0F * sideLighting * blue);
                     if (!rises) {
                        tessellator.addVertexWithUV(tx1, (double)y + ty1, tz1, (double)u1Flow, (double)v1Flow);
                        tessellator.addVertexWithUV(tx2, (double)y + ty2, tz2, (double)u2Flow, (double)v2Flow);
                        tessellator.addVertexWithUV(tx2, (double)(y), tz2, (double)u2Flow, (double)v3Flow);
                        tessellator.addVertexWithUV(tx1, (double)(y), tz1, (double)u1Flow, (double)v3Flow);
                     } else {
                        tessellator.addVertexWithUV(tx1, (double)(y + 1), tz1, (double)u1Flow, (double)v3Flow);
                        tessellator.addVertexWithUV(tx2, (double)(y + 1), tz2, (double)u2Flow, (double)v3Flow);
                        tessellator.addVertexWithUV(tx2, (double)(y + 1) - ty2, tz2, (double)u2Flow, (double)v2Flow);
                        tessellator.addVertexWithUV(tx1, (double)(y + 1) - ty1, tz1, (double)u1Flow, (double)v1Flow);
                     }
                  }
               }

               renderer.renderMinY = (double)0.0F;
               renderer.renderMaxY = (double)1.0F;
               return rendered;
            }
         }
      }
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return ConfigBlocks.blockFluxGasRI;
   }
}
