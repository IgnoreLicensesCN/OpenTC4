package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXBubble;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityThaumicSlime;
import thaumcraft.common.lib.CustomSoundType;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.Random;

public class BlockFluxGoo extends BlockFluidFinite {
   public IIcon iconStill;
   public IIcon iconFlow;

   public BlockFluxGoo() {
      super(ConfigBlocks.FLUXGOO, Config.fluxGoomaterial);
      this.setStepSound(new CustomSoundType("gore", 1.0F, 1.0F));
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister ir) {
      this.iconStill = ir.registerIcon("thaumcraft:fluxgoo");
      this.iconFlow = ir.registerIcon("thaumcraft:fluxgoo");
      ConfigBlocks.FLUXGOO.setIcons(this.iconStill, this.iconFlow);
   }

   public IIcon getIcon(int par1, int par2) {
      return this.iconStill;
   }

   public int getQuanta() {
      return this.quantaPerBlock;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      int md = world.getBlockMetadata(x, y, z);
      if (entity instanceof EntityThaumicSlime) {
         EntityThaumicSlime slime = (EntityThaumicSlime)entity;
         if (slime.getSlimeSize() < md && world.rand.nextBoolean()) {
            slime.setSlimeSize(slime.getSlimeSize() + 1);
            if (md > 1) {
               world.setBlockMetadataWithNotify(x, y, z, md - 1, 3);
            } else {
               world.setBlockToAir(x, y, z);
            }
         }
      } else {
         entity.motionX *= 1.0F - this.getQuantaPercentage(world, x, y, z);
         entity.motionZ *= 1.0F - this.getQuantaPercentage(world, x, y, z);
         if (entity instanceof EntityLivingBase) {
            PotionEffect pe = new PotionEffect(Config.potionVisExhaustID, 600, md / 3, true);
            pe.getCurativeItems().clear();
            ((EntityLivingBase)entity).addPotionEffect(pe);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int meta = world.getBlockMetadata(x, y, z);
      if (rand.nextInt(50 - Thaumcraft.proxy.particleCount(10)) <= meta) {
         FXBubble fb = new FXBubble(world, (float)x + rand.nextFloat(), (float)y + 0.125F * (float)meta, (float)z + rand.nextFloat(), 0.0F, 0.0F, 0.0F, 0);
         fb.setAlphaF(0.25F);
         ParticleEngine.instance.addEffect(world, fb);
      }

      super.randomDisplayTick(world, x, y, z, rand);
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      super.updateTick(world, x, y, z, rand);
      int meta = world.getBlockMetadata(x, y, z);
      if (meta >= 2 && meta < 6 && world.isAirBlock(x, y + 1, z) && rand.nextInt(25) == 0) {
         world.setBlockToAir(x, y, z);
         EntityThaumicSlime slime = new EntityThaumicSlime(world);
         slime.setLocationAndAngles((float)x + 0.5F, y, (float)z + 0.5F, 0.0F, 0.0F);
         slime.setSlimeSize(1);
         world.spawnEntityInWorld(slime);
         world.playSoundAtEntity(slime, "thaumcraft:gore", 1.0F, 1.0F);
      } else if (meta >= 6 && world.isAirBlock(x, y + 1, z)) {
         if (rand.nextInt(25) == 0) {
            world.setBlockToAir(x, y, z);
            EntityThaumicSlime slime = new EntityThaumicSlime(world);
            slime.setLocationAndAngles((float)x + 0.5F, y, (float)z + 0.5F, 0.0F, 0.0F);
            slime.setSlimeSize(2);
            world.spawnEntityInWorld(slime);
            world.playSoundAtEntity(slime, "thaumcraft:gore", 1.0F, 1.0F);
         } else if (Config.taintFromFlux && rand.nextInt(50) == 0) {
            Utils.setBiomeAt(world, x, z, ThaumcraftWorldGenerator.biomeTaint);
            world.setBlock(x, y, z, ConfigBlocks.blockTaintFibres, 0, 3);
            world.addBlockEvent(x, y, z, ConfigBlocks.blockTaintFibres, 1, 0);
         }
      } else if (rand.nextInt(30) == 0) {
         if (meta == 0) {
            world.setBlockToAir(x, y, z);
         } else {
            world.setBlockMetadataWithNotify(x, y, z, meta - 1, 3);
            if (rand.nextBoolean() && world.isAirBlock(x, y + 1, z)) {
               world.setBlock(x, y + 1, z, ConfigBlocks.blockFluxGas, 0, 3);
            }
         }
      }

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

   static {
      defaultDisplacements.put(ConfigBlocks.blockTaintFibres, true);
   }
}
