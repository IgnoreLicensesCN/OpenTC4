package thaumcraft.common.container;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileThaumatorium;

public class ContainerThaumatorium extends Container {
   private TileThaumatorium thaumatorium;
   private EntityPlayer player = null;
   public ArrayList<CrucibleRecipe> recipes = new ArrayList<>();

   public ContainerThaumatorium(InventoryPlayer par1InventoryPlayer, TileThaumatorium tileEntity) {
      this.player = par1InventoryPlayer.player;
      this.thaumatorium = tileEntity;
      this.thaumatorium.eventHandler = this;
      this.addSlotToContainer(new Slot(tileEntity, 0, 48, 16));

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, 8 + var5 * 18, 142));
      }

      this.onCraftMatrixChanged(this.thaumatorium);
   }

   public void onCraftMatrixChanged(IInventory par1iInventory) {
      super.onCraftMatrixChanged(par1iInventory);
      this.updateRecipes();
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      if (!this.thaumatorium.getWorldObj().isRemote) {
         this.thaumatorium.eventHandler = null;
      }

   }

   public void updateRecipes() {
      this.recipes.clear();
      if (this.thaumatorium.inputStack != null || this.thaumatorium.recipeHash != null) {
         for(Object r : ThaumcraftApi.getCraftingRecipes()) {
            if (r instanceof CrucibleRecipe) {
               if (ResearchManager.isResearchComplete(this.player.getCommandSenderName(), ((CrucibleRecipe)r).key) && ((CrucibleRecipe)r).catalystMatches(this.thaumatorium.inputStack)) {
                  this.recipes.add((CrucibleRecipe)r);
               } else if (this.thaumatorium.recipeHash != null && !this.thaumatorium.recipeHash.isEmpty()) {
                  for(Integer hash : this.thaumatorium.recipeHash) {
                     if (((CrucibleRecipe)r).hash == hash) {
                        this.recipes.add((CrucibleRecipe)r);
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   public boolean enchantItem(EntityPlayer par1EntityPlayer, int button) {
      if (this.recipes.size() > 0 && button >= 0 && button < this.recipes.size()) {
         boolean found = false;

         for(int a = 0; a < this.thaumatorium.recipeHash.size(); ++a) {
            if (((CrucibleRecipe)this.recipes.get(button)).hash == (Integer)this.thaumatorium.recipeHash.get(a)) {
               found = true;
               this.thaumatorium.recipeEssentia.remove(a);
               this.thaumatorium.recipePlayer.remove(a);
               this.thaumatorium.recipeHash.remove(a);
               this.thaumatorium.currentCraft = -1;
               break;
            }
         }

         if (!found) {
            this.thaumatorium.recipeEssentia.add(((CrucibleRecipe)this.recipes.get(button)).aspects.copy());
            this.thaumatorium.recipePlayer.add(par1EntityPlayer.getCommandSenderName());
            this.thaumatorium.recipeHash.add(((CrucibleRecipe)this.recipes.get(button)).hash);
         }

         this.thaumatorium.markDirty();
         this.thaumatorium.getWorldObj().markBlockForUpdate(this.thaumatorium.xCoord, this.thaumatorium.yCoord, this.thaumatorium.zCoord);
         return true;
      } else {
         return false;
      }
   }

   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.thaumatorium.isUseableByPlayer(par1EntityPlayer);
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(par2);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (par2 != 0) {
            if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
               return null;
            }
         } else if (par2 >= 1 && par2 < 28) {
            if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
               return null;
            }
         } else {
            if (par2 >= 28 && par2 < 37 && !this.mergeItemStack(itemstack1, 1, 28, false)) {
               return null;
            }

            if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
               return null;
            }
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }

         if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
      }

      return itemstack;
   }
}
