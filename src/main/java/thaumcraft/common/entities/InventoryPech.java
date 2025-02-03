package thaumcraft.common.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.monster.EntityPech;

public class InventoryPech implements IInventory {
   private final EntityPech theMerchant;
   private ItemStack[] theInventory = new ItemStack[5];
   private final EntityPlayer thePlayer;
   private Container eventHandler;

   public InventoryPech(EntityPlayer par1EntityPlayer, EntityPech par2IMerchant, Container par1Container) {
      this.thePlayer = par1EntityPlayer;
      this.theMerchant = par2IMerchant;
      this.eventHandler = par1Container;
   }

   public int getSizeInventory() {
      return this.theInventory.length;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.theInventory[par1];
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (this.theInventory[par1] != null) {
          ItemStack var3;
          if (this.theInventory[par1].stackSize <= par2) {
              var3 = this.theInventory[par1];
            this.theInventory[par1] = null;
          } else {
              var3 = this.theInventory[par1].splitStack(par2);
            if (this.theInventory[par1].stackSize == 0) {
               this.theInventory[par1] = null;
            }

          }
          this.eventHandler.onCraftMatrixChanged(this);
          return var3;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.theInventory[par1] != null) {
         ItemStack itemstack = this.theInventory[par1];
         this.theInventory[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.theInventory[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      this.eventHandler.onCraftMatrixChanged(this);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.theMerchant.isTamed();
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return par1 == 0;
   }

   public String getInventoryName() {
      return "entity.Pech.name";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public void markDirty() {
      this.eventHandler.onCraftMatrixChanged(this);
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }
}
