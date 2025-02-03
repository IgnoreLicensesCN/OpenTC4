package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.IScribeTools;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileResearchTable;
import thaumcraft.common.tiles.TileTable;

public class ItemInkwell extends Item implements IScribeTools {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemInkwell() {
      this.maxStackSize = 1;
      this.canRepair = true;
      this.setMaxDamage(100);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:inkwell");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      TileEntity tile = world.getTileEntity(x, y, z);
      int md = world.getBlockMetadata(x, y, z);
      Block bi = world.getBlock(x, y, z);
      if (tile != null && tile instanceof TileTable && md != 6) {
         if (world.isRemote) {
            return false;
         }

         for(int a = 2; a < 6; ++a) {
            TileEntity tile2 = world.getTileEntity(x + ForgeDirection.getOrientation(a).offsetX, y + ForgeDirection.getOrientation(a).offsetY, z + ForgeDirection.getOrientation(a).offsetZ);
            int md2 = world.getBlockMetadata(x + ForgeDirection.getOrientation(a).offsetX, y + ForgeDirection.getOrientation(a).offsetY, z + ForgeDirection.getOrientation(a).offsetZ);
            if (tile2 != null && tile2 instanceof TileTable && md2 < 6) {
               world.setBlock(x, y, z, bi, a, 0);
               world.setTileEntity(x, y, z, new TileResearchTable());
               world.setBlock(x + ForgeDirection.getOrientation(a).offsetX, y + ForgeDirection.getOrientation(a).offsetY, z + ForgeDirection.getOrientation(a).offsetZ, bi, ForgeDirection.getOrientation(a).getOpposite().ordinal() + 4, 0);
               world.markBlockForUpdate(x, y, z);
               world.markBlockForUpdate(x + ForgeDirection.getOrientation(a).offsetX, y + ForgeDirection.getOrientation(a).offsetY, z + ForgeDirection.getOrientation(a).offsetZ);
               TileEntity tile3 = world.getTileEntity(x, y, z);
               if (tile3 != null && tile3 instanceof TileResearchTable) {
                  ((TileResearchTable)tile3).setInventorySlotContents(0, stack.copy());
                  if (!player.capabilities.isCreativeMode) {
                     player.inventory.decrStackSize(player.inventory.currentItem, 1);
                     player.inventory.markDirty();
                  }

                  world.markBlockForUpdate(x, y, z);
               }

               return true;
            }
         }
      }

      return false;
   }
}
