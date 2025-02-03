package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSlimyBubble;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvanced;
import thaumcraft.common.tiles.TileAlchemyFurnaceAdvancedNozzle;

public class BlockAlchemyFurnace extends BlockContainer {
   public IIcon icon;

   public BlockAlchemyFurnace() {
      super(Material.iron);
      this.setHardness(3.0F);
      this.setResistance(17.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:metalbase");
   }

   public IIcon getIcon(int i, int md) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
   }

   public int getRenderType() {
      return -1;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return super.getSelectedBoundingBoxFromPool(w, i, j, k);
   }

   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity entity) {
      int md = world.getBlockMetadata(i, j, k);
      if (md == 0 && !(entity instanceof EntityLivingBase)) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.7F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
      }

   }

   public int getLightValue(IBlockAccess world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      if (metadata == 0) {
         TileAlchemyFurnaceAdvanced tile = (TileAlchemyFurnaceAdvanced)world.getTileEntity(x, y, z);
         if (tile != null && tile.heat > 100) {
            return (int)((float)tile.heat / (float)tile.maxPower * 12.0F);
         }
      }

      return super.getLightValue(world, x, y, z);
   }

   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote) {
         int metadata = world.getBlockMetadata(i, j, k);
         if (metadata == 0) {
            TileAlchemyFurnaceAdvanced tile = (TileAlchemyFurnaceAdvanced)world.getTileEntity(i, j, k);
            if (tile != null && entity instanceof EntityItem && tile.process(((EntityItem)entity).getEntityItem())) {
               ItemStack s = ((EntityItem)entity).getEntityItem();
               --s.stackSize;
               world.playSoundAtEntity(entity, "thaumcraft:bubble", 0.2F, 1.0F + world.rand.nextFloat() * 0.4F);
               if (s.stackSize <= 0) {
                  entity.setDead();
               } else {
                  ((EntityItem)entity).setEntityItemStack(s);
               }
            }
         }
      }

   }

   public Item getItemDropped(int md, Random par2Random, int par3) {
      return md == 0 ? Item.getItemFromBlock(ConfigBlocks.blockStoneDevice) : (md != 1 && md != 2 && md != 3 && md != 4 ? Item.getItemById(0) : Item.getItemFromBlock(ConfigBlocks.blockMetalDevice));
   }

   public int damageDropped(int metadata) {
      if (metadata != 1 && metadata != 4) {
         if (metadata == 3) {
            return 9;
         } else {
            return metadata == 2 ? 1 : 0;
         }
      } else {
         return 3;
      }
   }

   public TileEntity createTileEntity(World world, int metadata) {
      if (metadata == 0) {
         return new TileAlchemyFurnaceAdvanced();
      } else {
         return (TileEntity)(metadata == 1 ? new TileAlchemyFurnaceAdvancedNozzle() : super.createTileEntity(world, metadata));
      }
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te != null && te instanceof TileAlchemyFurnaceAdvancedNozzle) {
         if (((TileAlchemyFurnaceAdvancedNozzle)te).furnace != null) {
            float r = (float)((TileAlchemyFurnaceAdvancedNozzle)te).furnace.vis / (float)((TileAlchemyFurnaceAdvancedNozzle)te).furnace.maxVis;
            return MathHelper.floor_float(r * 14.0F) + ((TileAlchemyFurnaceAdvancedNozzle)te).furnace.vis > 0 ? 1 : 0;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public TileEntity createNewTileEntity(World var1, int md) {
      return null;
   }

   public void breakBlock(World world, int x, int y, int z, Block bl, int md) {
      if (!world.isRemote) {
         if (md != 0) {
            for(int a = -1; a <= 1; ++a) {
               for(int b = -1; b <= 1; ++b) {
                  for(int c = -1; c <= 1; ++c) {
                     if (world.getBlock(x + a, y + b, z + c) == this && world.getBlockMetadata(x + a, y + b, z + c) == 0) {
                        TileAlchemyFurnaceAdvanced tile = (TileAlchemyFurnaceAdvanced)world.getTileEntity(x + a, y + b, z + c);
                        if (tile != null) {
                           tile.destroy = true;
                           return;
                        }
                     }
                  }
               }
            }
         } else {
            for(int a = -1; a <= 1; ++a) {
               for(int b = 0; b <= 1; ++b) {
                  for(int c = -1; c <= 1; ++c) {
                     if ((a != 0 || b != 0 || c != 0) && world.getBlock(x + a, y + b, z + c) == this) {
                        int m = world.getBlockMetadata(x + a, y + b, z + c);
                        world.setBlock(x + a, y + b, z + c, Block.getBlockFromItem(this.getItemDropped(m, world.rand, 0)), this.damageDropped(m), 3);
                     }
                  }
               }
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int meta = world.getBlockMetadata(x, y, z);
      if (meta == 0) {
         TileAlchemyFurnaceAdvanced tile = (TileAlchemyFurnaceAdvanced)world.getTileEntity(x, y, z);
         if (tile != null && tile.vis > 0) {
            FXSlimyBubble ef = new FXSlimyBubble(world, (double)((float)x + rand.nextFloat()), (double)(y + 1), (double)((float)z + rand.nextFloat()), 0.06F + rand.nextFloat() * 0.06F);
            ef.setAlphaF(0.8F);
            ef.setRBGColorF(0.6F - rand.nextFloat() * 0.2F, 0.0F, 0.6F + rand.nextFloat() * 0.2F);
            ParticleEngine.instance.addEffect(world, ef);
            if (rand.nextInt(50) == 0) {
               double var21 = (double)((float)x + rand.nextFloat());
               double var22 = (double)y + this.maxY;
               double var23 = (double)((float)z + rand.nextFloat());
               world.playSound(var21, var22, var23, "liquid.lavapop", 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            int q = rand.nextInt(2);
            int w = rand.nextInt(2);
            FXSlimyBubble ef2 = new FXSlimyBubble(world, (double)x - 0.6 + (double)rand.nextFloat() * 0.2 + (double)(q * 2), (double)(y + 2), (double)z - 0.6 + (double)rand.nextFloat() * 0.2 + (double)(w * 2), 0.06F + rand.nextFloat() * 0.06F);
            ef2.setAlphaF(0.8F);
            ef2.setRBGColorF(0.6F - rand.nextFloat() * 0.2F, 0.0F, 0.6F + rand.nextFloat() * 0.2F);
            ParticleEngine.instance.addEffect(world, ef2);
         }
      }

      super.randomDisplayTick(world, x, y, z, rand);
   }
}
