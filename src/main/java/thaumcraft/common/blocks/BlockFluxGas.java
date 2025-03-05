package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

import java.util.Random;

public class BlockFluxGas extends BlockFluidFinite {
   public IIcon iconStill;
   public IIcon iconFlow;

   public BlockFluxGas() {
      super(ConfigBlocks.FLUXGAS, Config.fluxGoomaterial);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.densityDir = 1;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconStill = ir.registerIcon("thaumcraft:fluxgas");
      this.iconFlow = ir.registerIcon("thaumcraft:fluxgas");
      ConfigBlocks.FLUXGAS.setIcons(this.iconStill, this.iconFlow);
   }

   public int getDensityDir() {
      return this.densityDir;
   }

   public int getRenderType() {
      return ConfigBlocks.blockFluxGasRI;
   }

   public void setDensityDir(int densityDir) {
      this.densityDir = densityDir;
   }

   public IIcon getIcon(int par1, int par2) {
      return this.iconStill;
   }

   public int getQuanta() {
      return this.quantaPerBlock;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      int md = world.getBlockMetadata(x, y, z);
      if (world.rand.nextInt(10) == 0 && entity instanceof EntityLivingBase && !(entity instanceof ITaintedMob) && !((EntityLivingBase)entity).isEntityUndead() && !((EntityLivingBase)entity).isPotionActive(Config.potionVisExhaustID) && !((EntityLivingBase)entity).isPotionActive(Potion.confusion.id)) {
         if (world.rand.nextBoolean()) {
            PotionEffect pe = new PotionEffect(Config.potionVisExhaustID, 1200, md / 3, true);
            pe.getCurativeItems().clear();
            ((EntityLivingBase)entity).addPotionEffect(pe);
         } else {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.confusion.id, 80 + md * 20, 0, false));
         }

         if (md > 0) {
            world.setBlockMetadataWithNotify(x, y, z, md - 1, 3);
         } else {
            world.setBlockToAir(x, y, z);
         }
      }

   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      super.updateTick(world, x, y, z, rand);
   }

   public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
      int meta = world.getBlockMetadata(x, y, z);
      return meta < 2;
   }

   public boolean isInsideOfMaterial(World worldObj, Entity entity) {
      double d0 = entity.posY + (double)entity.getEyeHeight();
      int i = MathHelper.floor_double(entity.posX);
      int j = MathHelper.floor_float((float)MathHelper.floor_double(d0));
      int k = MathHelper.floor_double(entity.posZ);
      Block l = worldObj.getBlock(i, j, k);
      if (l.getMaterial() == this.blockMaterial) {
         float f = this.getQuantaPercentage(worldObj, i, j, k) - 0.11111111F;
         float f1 = (float)(j + 1) - f;
         return d0 < (double)f1;
      } else {
         return false;
      }
   }
}
