package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ContainerHoverHarness extends Container {
   private World worldObj;
   private int posX;
   private int posY;
   private int posZ;
   public IInventory input = new InventoryHoverHarness(this);
   ItemStack armor = null;
   EntityPlayer player = null;
   private int blockSlot;

   public ContainerHoverHarness(InventoryPlayer iinventory, World par2World, int par3, int par4, int par5) {
      this.worldObj = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.player = iinventory.player;
      this.armor = iinventory.getCurrentItem();
      this.blockSlot = iinventory.currentItem + 28;
      this.addSlotToContainer(new Slot(this.input, 0, 80, 32));
      this.bindPlayerInventory(iinventory);
      if (!par2World.isRemote) {
         try {
            ItemStack jar = ItemStack.loadItemStackFromNBT(this.armor.stackTagCompound.getCompoundTag("jar"));
            this.input.setInventorySlotContents(0, jar);
         } catch (Exception ignored) {
         }
      }

      this.onCraftMatrixChanged(this.input);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
      if (slot == this.blockSlot) {
         return null;
      } else {
         ItemStack stack = null;
         Slot slotObject = (Slot)this.inventorySlots.get(slot);
         if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot == 0) {
               if (!this.input.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true, 64)) {
                  return null;
               }
            } else if (!this.input.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 0, 1, false, 1)) {
               return null;
            }

            if (stackInSlot.stackSize == 0) {
               slotObject.putStack(null);
            } else {
               slotObject.onSlotChanged();
            }
         }

         return stack;
      }
   }

   public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
      if (par1 == this.blockSlot) {
         return null;
      } else {
         InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
         return par1 == 0 && !this.input.isItemValidForSlot(par1, inventoryplayer.getItemStack()) && (par1 != 0 || inventoryplayer.getItemStack() != null) ? null : super.slotClick(par1, par2, par3, par4EntityPlayer);
      }
   }

   public boolean canInteractWith(EntityPlayer var1) {
      return true;
   }

   public void putStackInSlot(int par1, ItemStack par2ItemStack) {
      if (this.input.isItemValidForSlot(par1, par2ItemStack)) {
         super.putStackInSlot(par1, par2ItemStack);
      }

   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      if (!this.worldObj.isRemote) {
         ItemStack var3 = this.input.getStackInSlotOnClosing(0);
         if (var3 != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var3.writeToNBT(var4);
            this.armor.setTagInfo("jar", var4);
         } else {
            this.armor.setTagInfo("jar", new NBTTagCompound());
         }

         if (this.player == null) {
            return;
         }

         if (this.player.getHeldItem() != null && this.player.getHeldItem().isItemEqual(this.armor)) {
            this.player.setCurrentItemOrArmor(0, this.armor);
         }

         this.player.inventory.markDirty();
      }

   }

   protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, int limit) {
      boolean var5 = false;
      int var6 = par2;
      if (par4) {
         var6 = par3 - 1;
      }

      if (par1ItemStack.isStackable()) {
         while(par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
            Slot var7 = (Slot)this.inventorySlots.get(var6);
            ItemStack var8 = var7.getStack();
            if (var8 != null && var8.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8)) {
               int var9 = var8.stackSize + par1ItemStack.stackSize;
               if (var9 <= Math.min(par1ItemStack.getMaxStackSize(), limit)) {
                  par1ItemStack.stackSize = 0;
                  var8.stackSize = var9;
                  var7.onSlotChanged();
                  var5 = true;
               } else if (var8.stackSize < Math.min(par1ItemStack.getMaxStackSize(), limit)) {
                  par1ItemStack.stackSize -= Math.min(par1ItemStack.getMaxStackSize(), limit) - var8.stackSize;
                  var8.stackSize = Math.min(par1ItemStack.getMaxStackSize(), limit);
                  var7.onSlotChanged();
                  var5 = true;
               }
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if (par1ItemStack.stackSize > 0) {
         if (par4) {
            var6 = par3 - 1;
         } else {
            var6 = par2;
         }

         while(!par4 && var6 < par3 || par4 && var6 >= par2) {
            Slot var7 = (Slot)this.inventorySlots.get(var6);
            ItemStack var8 = var7.getStack();
            if (var8 == null) {
               ItemStack res = par1ItemStack.copy();
               res.stackSize = Math.min(res.stackSize, limit);
               var7.putStack(res);
               var7.onSlotChanged();
               par1ItemStack.stackSize -= res.stackSize;
               var5 = true;
               break;
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }
}
