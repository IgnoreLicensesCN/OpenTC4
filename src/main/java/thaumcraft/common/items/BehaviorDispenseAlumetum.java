package thaumcraft.common.items;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityAlumentum;

public class BehaviorDispenseAlumetum extends BehaviorProjectileDispense {
   public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
      if (par2ItemStack.getItemDamage() != 0) {
         BehaviorDefaultDispenseItem def = new BehaviorDefaultDispenseItem();
         return def.dispense(par1IBlockSource, par2ItemStack);
      } else {
         World var3 = par1IBlockSource.getWorld();
         IPosition var4 = BlockDispenser.func_149939_a(par1IBlockSource);
         EnumFacing var5 = BlockDispenser.func_149937_b(par1IBlockSource.getBlockMetadata());
         IProjectile var6 = this.getProjectileEntity(var3, var4);
         var6.setThrowableHeading(var5.getFrontOffsetX(), (float)var5.getFrontOffsetY() + 0.1F, var5.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
         var3.spawnEntityInWorld((Entity)var6);
         par2ItemStack.splitStack(1);
         return par2ItemStack;
      }
   }

   protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition) {
      return new EntityAlumentum(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
   }

   protected void playDispenseSound(IBlockSource par1IBlockSource) {
      par1IBlockSource.getWorld().playAuxSFX(1009, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
   }
}
