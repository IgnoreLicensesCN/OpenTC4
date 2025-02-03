package thaumcraft.common.tiles;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.network.NetworkRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRepairableExtended;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandable;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.equipment.ItemElementalPickaxe;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketBoreDig;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.utils.TCVec3;
import thaumcraft.common.lib.utils.Utils;

public class TileArcaneBore extends TileThaumcraft implements IInventory, IWandable {
   public int spiral = 0;
   public float currentRadius = 0.0F;
   public int maxRadius = 2;
   public float vRadX = 0.0F;
   public float vRadZ = 0.0F;
   public float tRadX = 0.0F;
   public float tRadZ = 0.0F;
   public float mRadX = 0.0F;
   public float mRadZ = 0.0F;
   private int count = 0;
   public int topRotation = 0;
   long soundDelay = 0L;
   Object beam1 = null;
   Object beam2 = null;
   int beamlength = 0;
   TileArcaneBoreBase base = null;
   public ItemStack[] contents = new ItemStack[2];
   public int rotX = 0;
   public int rotZ = 0;
   public int tarX = 0;
   public int tarZ = 0;
   public int speedX = 0;
   public int speedZ = 0;
   public boolean hasFocus = false;
   public boolean hasPickaxe = false;
   int lastX = 0;
   int lastZ = 0;
   int lastY = 0;
   boolean toDig = false;
   int digX = 0;
   int digZ = 0;
   int digY = 0;
   Block digBlock;
   int digMd;
   float radInc;
   int paused;
   int maxPause;
   long repairCounter;
   boolean first;
   public ForgeDirection orientation;
   public ForgeDirection baseOrientation;
   FakePlayer fakePlayer;
   private AspectList repairCost;
   private AspectList currentRepairVis;
   public int fortune;
   public int speed;
   public int area;
   int blockCount;
   private float speedyTime;
   private final int itemsPerVis;

   public TileArcaneBore() {
      this.digBlock = Blocks.air;
      this.digMd = 0;
      this.radInc = 0.0F;
      this.paused = 100;
      this.maxPause = 100;
      this.repairCounter = 0L;
      this.first = true;
      this.orientation = ForgeDirection.getOrientation(1);
      this.baseOrientation = ForgeDirection.getOrientation(1);
      this.fakePlayer = null;
      this.repairCost = new AspectList();
      this.currentRepairVis = new AspectList();
      this.fortune = 0;
      this.speed = 0;
      this.area = 0;
      this.blockCount = 0;
      this.itemsPerVis = 20;
   }

   public boolean canUpdate() {
      return true;
   }

   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote && this.speedyTime < 20.0F) {
         this.speedyTime += (float)VisNetHandler.drainVis(this.worldObj, this.xCoord, this.yCoord, this.zCoord, Aspect.ENTROPY, 100) / 5.0F;
         if (this.speedyTime < 20.0F && this.base != null && this.base.drawEssentia()) {
            float var10001 = this.speedyTime;
            this.getClass();
            this.speedyTime = var10001 + 20.0F;
         }
      }

      if (!this.worldObj.isRemote && this.fakePlayer == null) {
         this.fakePlayer = FakePlayerFactory.get((WorldServer)this.worldObj, new GameProfile(null, "FakeThaumcraftBore"));
      }

      if (this.worldObj.isRemote && this.first) {
         this.setOrientation(this.orientation, true);
         this.first = false;
      }

      if (this.rotX < this.tarX) {
         this.rotX += this.speedX;
         if (this.rotX < this.tarX) {
            ++this.speedX;
         } else {
            this.speedX = (int)((float)this.speedX / 3.0F);
         }
      } else if (this.rotX > this.tarX) {
         this.rotX += this.speedX;
         if (this.rotX > this.tarX) {
            --this.speedX;
         } else {
            this.speedX = (int)((float)this.speedX / 3.0F);
         }
      } else {
         this.speedX = 0;
      }

      if (this.rotZ < this.tarZ) {
         this.rotZ += this.speedZ;
         if (this.rotZ < this.tarZ) {
            ++this.speedZ;
         } else {
            this.speedZ = (int)((float)this.speedZ / 3.0F);
         }
      } else if (this.rotZ > this.tarZ) {
         this.rotZ += this.speedZ;
         if (this.rotZ > this.tarZ) {
            --this.speedZ;
         } else {
            this.speedZ = (int)((float)this.speedZ / 3.0F);
         }
      } else {
         this.speedZ = 0;
      }

      if (this.gettingPower() && this.areItemsValid()) {
         this.dig();
      } else if (this.worldObj.isRemote) {
         if (this.topRotation % 90 != 0) {
            this.topRotation += Math.min(10, 90 - this.topRotation % 90);
         }

         this.vRadX *= 0.9F;
         this.vRadZ *= 0.9F;
      }

      if (!this.worldObj.isRemote && this.hasPickaxe && this.getStackInSlot(1) != null) {
         if (this.repairCounter++ % 40L == 0L && this.getStackInSlot(1).isItemDamaged()) {
            this.doRepair(this.getStackInSlot(1), this.fakePlayer);
         }

         if (this.repairCost != null && this.repairCost.size() > 0 && this.repairCounter % 5L == 0L) {
            for(Aspect a : this.repairCost.getAspects()) {
               if (this.currentRepairVis.getAmount(a) < this.repairCost.getAmount(a)) {
                  this.currentRepairVis.add(a, VisNetHandler.drainVis(this.worldObj, this.xCoord, this.yCoord, this.zCoord, a, this.repairCost.getAmount(a)));
               }
            }
         }

         this.fakePlayer.ticksExisted = (int)this.repairCounter;

         try {
            this.getStackInSlot(1).updateAnimation(this.worldObj, this.fakePlayer, 0, true);
         } catch (Exception ignored) {
         }
      }

   }

   private void doRepair(ItemStack is, EntityPlayer player) {
      int level = EnchantmentHelper.getEnchantmentLevel(Config.enchRepair.effectId, is);
      if (level > 0) {
         if (level > 2) {
            level = 2;
         }

         if (is.getItem() instanceof IRepairable) {
            AspectList cost = ThaumcraftCraftingManager.getObjectTags(is);
            if (cost == null || cost.size() == 0) {
               return;
            }

            cost = ResearchManager.reduceToPrimals(cost);

            for(Aspect a : cost.getAspects()) {
               if (a != null) {
                  this.repairCost.merge(a, (int)Math.sqrt(cost.getAmount(a) * 2) * level);
               }
            }

            boolean doIt = true;
            if (is.getItem() instanceof IRepairableExtended) {
               doIt = ((IRepairableExtended)is.getItem()).doRepair(is, player, level);
            }

            if (doIt) {
               for(Aspect a : this.repairCost.getAspects()) {
                  if (this.currentRepairVis.getAmount(a) < this.repairCost.getAmount(a)) {
                     doIt = false;
                     break;
                  }
               }
            }

            if (doIt) {
               for(Aspect a : this.repairCost.getAspects()) {
                  this.currentRepairVis.reduce(a, this.repairCost.getAmount(a));
               }

               is.damageItem(-level, player);
               this.markDirty();
            }
         } else {
            this.repairCost = new AspectList();
         }

      }
   }

   private boolean areItemsValid() {
      boolean notNearBroken = !this.hasPickaxe || this.getStackInSlot(1).getItemDamage() + 1 < this.getStackInSlot(1).getMaxDamage();

       return this.hasFocus && this.hasPickaxe && this.getStackInSlot(1).isItemStackDamageable() && notNearBroken;
   }

   public void markDirty() {
      super.markDirty();
      this.fortune = 0;
      this.area = 0;
      this.speed = 0;
      if (this.getStackInSlot(0) != null && this.getStackInSlot(0).getItem() instanceof ItemFocusExcavation) {
         this.fortune = ((ItemFocusExcavation)this.getStackInSlot(0).getItem()).getUpgradeLevel(this.getStackInSlot(0), FocusUpgradeType.treasure);
         this.area = ((ItemFocusExcavation)this.getStackInSlot(0).getItem()).getUpgradeLevel(this.getStackInSlot(0), FocusUpgradeType.enlarge);
         this.speed += ((ItemFocusExcavation)this.getStackInSlot(0).getItem()).getUpgradeLevel(this.getStackInSlot(0), FocusUpgradeType.potency);
         this.hasFocus = true;
      } else {
         this.hasFocus = false;
      }

      if (this.getStackInSlot(1) != null && this.getStackInSlot(1).getItem() instanceof ItemPickaxe) {
         this.hasPickaxe = true;
         int f = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, this.getStackInSlot(1));
         if (f > this.fortune) {
            this.fortune = f;
         }

         this.speed += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, this.getStackInSlot(1));
      } else {
         this.hasPickaxe = false;
      }

   }

   private void dig() {
      if (this.rotX == this.tarX && this.rotZ == this.tarZ) {
         if (!this.worldObj.isRemote) {
            boolean dug = false;
            if (this.base == null) {
               this.base = (TileArcaneBoreBase)this.worldObj.getTileEntity(this.xCoord, this.yCoord + this.baseOrientation.getOpposite().offsetY, this.zCoord);
            }

            if (--this.count > 0) {
               return;
            }

            if (this.toDig) {
               this.toDig = false;
               Block bi = this.worldObj.getBlock(this.digX, this.digY, this.digZ);
               int md = this.worldObj.getBlockMetadata(this.digX, this.digY, this.digZ);
               if (!bi.isAir(this.worldObj, this.digX, this.digY, this.digZ)) {
                  int tfortune = this.fortune;
                  boolean silktouch = false;
                  if (this.getStackInSlot(1) != null && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, this.getStackInSlot(1)) > 0 && bi.canSilkHarvest(this.worldObj, null, this.digX, this.digY, this.digZ, md)) {
                     silktouch = true;
                     tfortune = 0;
                  }

                  if (!silktouch && this.getStackInSlot(0) != null && ((ItemFocusExcavation)this.getStackInSlot(0).getItem()).isUpgradedWith(this.getStackInSlot(0), FocusUpgradeType.silktouch) && bi.canSilkHarvest(this.worldObj, null, this.digX, this.digY, this.digZ, md)) {
                     silktouch = true;
                     tfortune = 0;
                  }

                  this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockWoodenDevice, 99, Block.getIdFromBlock(bi) + (md << 12));
                  ArrayList<ItemStack> items = new ArrayList<>();
                  if (silktouch) {
                     ItemStack dropped = BlockUtils.createStackedBlock(bi, md);
                     if (dropped != null) {
                        items.add(dropped);
                     }
                  } else {
                     items = bi.getDrops(this.worldObj, this.digX, this.digY, this.digZ, md, tfortune);
                  }

                  List<EntityItem> targets = this.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(this.digX, this.digY, this.digZ, this.digX + 1, this.digY + 1, this.digZ + 1).expand(1.0F, 1.0F, 1.0F));
                  if (targets.size() > 0) {
                     for(EntityItem e : targets) {
                        items.add(e.getEntityItem().copy());
                        e.setDead();
                     }
                  }

                  if (items.size() > 0) {
                     for(ItemStack is : items) {
                        ItemStack dropped = is.copy();
                        if (!silktouch && (this.getStackInSlot(1) != null && this.getStackInSlot(1).getItem() instanceof ItemElementalPickaxe || this.getStackInSlot(0) != null && this.getStackInSlot(0).getItem() instanceof ItemFocusBasic && ((ItemFocusBasic)this.getStackInSlot(0).getItem()).isUpgradedWith(this.getStackInSlot(0), ItemFocusExcavation.dowsing))) {
                           dropped = Utils.findSpecialMiningResult(is, 0.2F + (float)tfortune * 0.075F, this.worldObj.rand);
                        }

                        if (this.base != null && this.base instanceof TileArcaneBoreBase) {
                           TileEntity inventory = this.worldObj.getTileEntity(this.base.xCoord + this.base.orientation.offsetX, this.base.yCoord, this.base.zCoord + this.base.orientation.offsetZ);
                           if (inventory instanceof IInventory) {
                              dropped = InventoryUtils.placeItemStackIntoInventory(dropped, (IInventory)inventory, this.base.orientation.getOpposite().ordinal(), true);
                           }

                           if (dropped != null) {
                              EntityItem ei = new EntityItem(this.worldObj, (double)this.xCoord + (double)0.5F + (double)this.base.orientation.offsetX * 0.66, (double)this.yCoord + 0.4 + (double)this.baseOrientation.getOpposite().offsetY, (double)this.zCoord + (double)0.5F + (double)this.base.orientation.offsetZ * 0.66, dropped.copy());
                              ei.motionX = 0.075F * (float)this.base.orientation.offsetX;
                              ei.motionY = 0.025F;
                              ei.motionZ = 0.075F * (float)this.base.orientation.offsetZ;
                              this.worldObj.spawnEntityInWorld(ei);
                           }
                        }
                     }
                  }
               }

               this.setInventorySlotContents(1, InventoryUtils.damageItem(1, this.getStackInSlot(1), this.worldObj));
               if (this.getStackInSlot(1).stackSize <= 0) {
                  this.setInventorySlotContents(1, null);
               }

               this.worldObj.setBlockToAir(this.digX, this.digY, this.digZ);
               if (this.base != null) {
                  for(int lb = 2; lb < 6; ++lb) {
                     ForgeDirection lbd = ForgeDirection.getOrientation(lb);
                     TileEntity lbte = this.worldObj.getTileEntity(this.base.xCoord + lbd.offsetX, this.base.yCoord, this.base.zCoord + lbd.offsetZ);
                     if (lbte instanceof TileArcaneLamp) {
                        int d = this.worldObj.rand.nextInt(32) * 2;
                        int xx = this.xCoord + this.orientation.offsetX + this.orientation.offsetX * d;
                        int yy = this.yCoord + this.orientation.offsetY + this.orientation.offsetY * d;
                        int zz = this.zCoord + this.orientation.offsetZ + this.orientation.offsetZ * d;
                        int p = d / 2 % 4;
                        if (this.orientation.offsetX != 0) {
                           zz += p == 0 ? 3 : (p != 1 && p != 3 ? -3 : 0);
                        } else {
                           xx += p == 0 ? 3 : (p != 1 && p != 3 ? -3 : 0);
                        }

                        if (p == 3 && this.orientation.offsetY == 0) {
                           yy -= 2;
                        }

                        if (this.worldObj.isAirBlock(xx, yy, zz) && this.worldObj.getBlock(xx, yy, zz) != ConfigBlocks.blockAiry && this.worldObj.getBlockLightValue(xx, yy, zz) < 15) {
                           this.worldObj.setBlock(xx, yy, zz, ConfigBlocks.blockAiry, 3, 3);
                        }
                        break;
                     }
                  }
               }

               dug = true;
            }

            this.findNextBlockToDig();
            if (dug && this.speedyTime > 0.0F) {
               --this.speedyTime;
            }
         } else {
            ++this.paused;
            if (this.worldObj.isAirBlock(this.xCoord, this.yCoord, this.zCoord)) {
               this.invalidate();
            }

            if (this.paused < this.maxPause && this.soundDelay < System.currentTimeMillis()) {
               this.soundDelay = System.currentTimeMillis() + 1200L + (long)this.worldObj.rand.nextInt(100);
               this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:rumble", 0.25F, 0.9F + this.worldObj.rand.nextFloat() * 0.2F, false);
            }

            if (this.beamlength > 0 && this.paused > this.maxPause) {
               --this.beamlength;
            }

            if (this.toDig) {
               this.paused = 0;
               this.beamlength = 64;
               Block block = this.worldObj.getBlock(this.digX, this.digY, this.digZ);
               int md = this.worldObj.getBlockMetadata(this.digX, this.digY, this.digZ);
               if (block != null) {
                  this.maxPause = 10 + Math.max(10 - this.speed, (int)(block.getBlockHardness(this.worldObj, this.digX, this.digY, this.digZ) * 2.0F) - this.speed * 2);
               } else {
                  this.maxPause = 20;
               }

               if (this.speedyTime <= 0.0F) {
                  this.maxPause *= 4;
               }

               this.toDig = false;
               double xd = (double)this.xCoord + (double)0.5F - ((double)this.digX + (double)0.5F);
               double yd = (double)this.yCoord + (double)0.5F - ((double)this.digY + (double)0.5F);
               double zd = (double)this.zCoord + (double)0.5F - ((double)this.digZ + (double)0.5F);
               double var12 = MathHelper.sqrt_double(xd * xd + zd * zd);
               float rx = (float)(Math.atan2(zd, xd) * (double)180.0F / Math.PI);
               float rz = (float)(-(Math.atan2(yd, var12) * (double)180.0F / Math.PI)) + 90.0F;
               this.tRadX = MathHelper.wrapAngleTo180_float((float)this.rotX) + rx;
               if (this.orientation.ordinal() == 5) {
                  if (this.tRadX > 180.0F) {
                     this.tRadX -= 360.0F;
                  }

                  if (this.tRadX < -180.0F) {
                     this.tRadX += 360.0F;
                  }
               }

               this.tRadZ = rz - (float)this.rotZ;
               if (this.orientation.ordinal() <= 1) {
                  this.tRadZ += 180.0F;
                  if (this.vRadX - this.tRadX >= 180.0F) {
                     this.vRadX -= 360.0F;
                  }

                  if (this.vRadX - this.tRadX <= -180.0F) {
                     this.vRadX += 360.0F;
                  }
               }

               this.mRadX = Math.abs((this.vRadX - this.tRadX) / 6.0F);
               this.mRadZ = Math.abs((this.vRadZ - this.tRadZ) / 6.0F);
               if (this.speedyTime > 0.0F) {
                  --this.speedyTime;
               }
            }

            if (this.paused < this.maxPause) {
               if (this.vRadX < this.tRadX) {
                  this.vRadX += this.mRadX;
               } else if (this.vRadX > this.tRadX) {
                  this.vRadX -= this.mRadX;
               }

               if (this.vRadZ < this.tRadZ) {
                  this.vRadZ += this.mRadZ;
               } else if (this.vRadZ > this.tRadZ) {
                  this.vRadZ -= this.mRadZ;
               }
            } else {
               this.vRadX *= 0.9F;
               this.vRadZ *= 0.9F;
            }

            this.mRadX *= 0.9F;
            this.mRadZ *= 0.9F;
            float vx = (float)(this.rotX + 90) - this.vRadX;
            float vz = (float)(this.rotZ + 90) - this.vRadZ;
            float var3 = 1.0F;
            float dX = MathHelper.sin(vx / 180.0F * (float)Math.PI) * MathHelper.cos(vz / 180.0F * (float)Math.PI) * var3;
            float dZ = MathHelper.cos(vx / 180.0F * (float)Math.PI) * MathHelper.cos(vz / 180.0F * (float)Math.PI) * var3;
            float dY = MathHelper.sin(vz / 180.0F * (float)Math.PI) * var3;
            Vec3 var13 = Vec3.createVectorHelper((double)this.xCoord + (double)0.5F + (double)dX, (double)this.yCoord + (double)0.5F + (double)dY, (double)this.zCoord + (double)0.5F + (double)dZ);
            Vec3 var14 = Vec3.createVectorHelper((double)this.xCoord + (double)0.5F + (double)(dX * (float)this.beamlength), (double)this.yCoord + (double)0.5F + (double)(dY * (float)this.beamlength), (double)this.zCoord + (double)0.5F + (double)(dZ * (float)this.beamlength));
            MovingObjectPosition mop = this.worldObj.func_147447_a(var13, var14, false, true, false);
            int impact = 0;
            float length = 64.0F;
            double bx = var14.xCoord;
            double by = var14.yCoord;
            double bz = var14.zCoord;
            if (mop != null) {
               double xd = (double)this.xCoord + (double)0.5F + (double)dX - mop.hitVec.xCoord;
               double yd = (double)this.yCoord + (double)0.5F + (double)dY - mop.hitVec.yCoord;
               double zd = (double)this.zCoord + (double)0.5F + (double)dZ - mop.hitVec.zCoord;
               bx = mop.hitVec.xCoord;
               by = mop.hitVec.yCoord;
               bz = mop.hitVec.zCoord;
               length = MathHelper.sqrt_double(xd * xd + yd * yd + zd * zd) + 0.5F;
               impact = 5;
               int x = MathHelper.floor_double(bx);
               int y = MathHelper.floor_double(by);
               int z = MathHelper.floor_double(bz);
               if (!this.worldObj.isAirBlock(x, y, z)) {
                  Thaumcraft.proxy.boreDigFx(this.worldObj, x, y, z, this.xCoord + this.orientation.offsetX, this.yCoord + this.orientation.offsetY, this.zCoord + this.orientation.offsetZ, this.worldObj.getBlock(x, y, z), this.worldObj.getBlockMetadata(x, y, z) >> 12 & 255);
               }
            }

            this.topRotation += this.beamlength / 6;
            this.beam1 = Thaumcraft.proxy.beamBore(this.worldObj, (double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, bx, by, bz, 1, 65382, true, impact > 0 ? 2.0F : 0.0F, this.beam1, impact);
            this.beam2 = Thaumcraft.proxy.beamBore(this.worldObj, (double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, bx, by, bz, 2, 16746581, false, impact > 0 ? 2.0F : 0.0F, this.beam2, impact);
            if (this.worldObj.isAirBlock(this.digX, this.digY, this.digZ) && this.digBlock != Blocks.air) {
               this.worldObj.playSound((float)this.digX + 0.5F, (float)this.digY + 0.5F, (float)this.digZ + 0.5F, this.digBlock.stepSound.getBreakSound(), (this.digBlock.stepSound.getVolume() + 1.0F) / 2.0F, this.digBlock.stepSound.getPitch() * 0.8F, false);

               for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
                  Thaumcraft.proxy.boreDigFx(this.worldObj, this.digX, this.digY, this.digZ, this.xCoord + this.orientation.offsetX, this.yCoord + this.orientation.offsetY, this.zCoord + this.orientation.offsetZ, this.digBlock, this.digMd >> 12 & 255);
               }

               this.digBlock = Blocks.air;
            }
         }

      } else {
         if (this.worldObj.isRemote) {
            if (this.topRotation % 90 != 0) {
               this.topRotation += Math.min(10, 90 - this.topRotation % 90);
            }

            this.vRadX *= 0.9F;
            this.vRadZ *= 0.9F;
         }

      }
   }

   private void findNextBlockToDig() {
      if (this.radInc == 0.0F) {
         this.radInc = (float)(this.maxRadius + this.area) / 360.0F;
      }

      int x = this.lastX;
      int z = this.lastZ;

      int y;
      TCVec3 vres;
      for(y = this.lastY; x == this.lastX && z == this.lastZ && y == this.lastY; z = MathHelper.floor_double(vres.zCoord)) {
         this.spiral += 2;
         if (this.spiral >= 360) {
            this.spiral -= 360;
         }

         this.currentRadius += this.radInc;
         if (this.currentRadius > (float)(this.maxRadius + this.area) || this.currentRadius < (float)(-(this.maxRadius + this.area))) {
            this.radInc *= -1.0F;
         }

         TCVec3 vsource = TCVec3.createVectorHelper((double)(this.xCoord + this.orientation.offsetX) + (double)0.5F, (double)(this.yCoord + this.orientation.offsetY) + (double)0.5F, (double)(this.zCoord + this.orientation.offsetZ) + (double)0.5F);
         TCVec3 vtar = TCVec3.createVectorHelper(0.0F, this.currentRadius, 0.0F);
         vtar.rotateAroundZ((float)this.spiral / 180.0F * (float)Math.PI);
         vtar.rotateAroundY(((float)Math.PI / 2F) * (float)this.orientation.offsetX);
         vtar.rotateAroundX(((float)Math.PI / 2F) * (float)this.orientation.offsetY);
         vres = vsource.addVector(vtar.xCoord, vtar.yCoord, vtar.zCoord);
         x = MathHelper.floor_double(vres.xCoord);
         y = MathHelper.floor_double(vres.yCoord);
      }

      this.lastX = x;
      this.lastZ = z;
      this.lastY = y;
      x += this.orientation.offsetX;
      y += this.orientation.offsetY;
      z += this.orientation.offsetZ;

      for(int depth = 0; depth < 64; ++depth) {
         x += this.orientation.offsetX;
         y += this.orientation.offsetY;
         z += this.orientation.offsetZ;
         Block block = this.worldObj.getBlock(x, y, z);
         int md = this.worldObj.getBlockMetadata(x, y, z);
         if (block != null && block.getBlockHardness(this.worldObj, x, y, z) < 0.0F) {
            break;
         }

         if (!this.worldObj.isAirBlock(x, y, z) && block != null && block.canCollideCheck(md, false) && block.getCollisionBoundingBoxFromPool(this.worldObj, x, y, z) != null) {
            this.digX = x;
            this.digY = y;
            this.digZ = z;
            if (++this.blockCount > 2) {
               this.blockCount = 0;
            }

            this.count = Math.max(10 - this.speed, (int)(block.getBlockHardness(this.worldObj, x, y, z) * 2.0F) - this.speed * 2);
            if (this.speedyTime < 1.0F) {
               this.count *= 4;
            }

            this.toDig = true;
            Vec3 var13 = Vec3.createVectorHelper((double)this.xCoord + (double)0.5F + (double)this.orientation.offsetX, (double)this.yCoord + (double)0.5F + (double)this.orientation.offsetY, (double)this.zCoord + (double)0.5F + (double)this.orientation.offsetZ);
            Vec3 var14 = Vec3.createVectorHelper((double)this.digX + (double)0.5F, (double)this.digY + (double)0.5F, (double)this.digZ + (double)0.5F);
            MovingObjectPosition mop = this.worldObj.func_147447_a(var13, var14, false, true, false);
            if (mop != null) {
               block = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
               this.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
               if (block.getBlockHardness(this.worldObj, mop.blockX, mop.blockY, mop.blockZ) > -1.0F && block.getCollisionBoundingBoxFromPool(this.worldObj, mop.blockX, mop.blockY, mop.blockZ) != null) {
                  this.count = Math.max(10 - this.speed, (int)(block.getBlockHardness(this.worldObj, mop.blockX, mop.blockY, mop.blockZ) * 2.0F) - this.speed * 2);
                  if (this.speedyTime < 1.0F) {
                     this.count *= 4;
                  }

                  this.digX = mop.blockX;
                  this.digY = mop.blockY;
                  this.digZ = mop.blockZ;
               }
            }

            this.sendDigEvent();
            break;
         }
      }

   }

   public boolean gettingPower() {
      return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord + this.baseOrientation.getOpposite().offsetY, this.zCoord);
   }

   public void setOrientation(ForgeDirection or, boolean initial) {
      this.orientation = or;
      this.lastX = 0;
      this.lastZ = 0;
      switch (or.ordinal()) {
         case 0:
            this.tarZ = 180;
            this.tarX = 0;
            break;
         case 1:
            this.tarZ = 0;
            this.tarX = 0;
            break;
         case 2:
            this.tarZ = 90;
            this.tarX = 270;
            break;
         case 3:
            this.tarZ = 90;
            this.tarX = 90;
            break;
         case 4:
            this.tarZ = 90;
            this.tarX = 0;
            break;
         case 5:
            this.tarZ = 90;
            this.tarX = 180;
      }

      if (initial) {
         this.rotX = this.tarX;
         this.rotZ = this.tarZ;
      }

      this.toDig = false;
      this.radInc = 0.0F;
      this.paused = 100;
      this.tRadX = 0.0F;
      this.tRadZ = 0.0F;
      this.mRadX = 0.0F;
      this.mRadZ = 0.0F;
      this.digX = 0;
      this.digY = 0;
      this.digZ = 0;
      if (this.worldObj != null) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.speedyTime = nbttagcompound.getShort("SpeedyTime");
      this.setOrientation(this.orientation, true);
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setShort("SpeedyTime", (short)((int)this.speedyTime));
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.orientation = ForgeDirection.getOrientation(nbttagcompound.getInteger("orientation"));
      this.baseOrientation = ForgeDirection.getOrientation(nbttagcompound.getInteger("baseOrientation"));
      NBTTagList var2 = nbttagcompound.getTagList("Inventory", 10);
      this.contents = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.contents.length) {
            this.contents[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

      this.markDirty();
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.orientation.ordinal());
      nbttagcompound.setInteger("baseOrientation", this.baseOrientation.ordinal());
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.contents.length; ++var3) {
         if (this.contents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.contents[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      nbttagcompound.setTag("Inventory", var2);
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i != 99) {
         return super.receiveClientEvent(i, j);
      } else {
         try {
            if (this.worldObj.isRemote && (j & 4095) > 0) {
               Block var40 = Block.getBlockById(j & 4095);
               if (var40 != null) {
                  this.worldObj.playSound((float)this.digX + 0.5F, (float)this.digY + 0.5F, (float)this.digZ + 0.5F, var40.stepSound.getBreakSound(), (var40.stepSound.getVolume() + 1.0F) / 2.0F, var40.stepSound.getPitch() * 0.8F, false);

                  for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
                     Thaumcraft.proxy.boreDigFx(this.worldObj, this.digX, this.digY, this.digZ, this.xCoord + this.orientation.offsetX, this.yCoord + this.orientation.offsetY, this.zCoord + this.orientation.offsetZ, var40, j >> 12 & 255);
                  }
               }
            }
         } catch (Exception ignored) {
         }

         return true;
      }
   }

   public void getDigEvent(int j) {
      int x = (j >> 16 & 255) - 64;
      int y = (j >> 8 & 255) - 64;
      int z = (j & 255) - 64;
      this.digX = this.xCoord + x;
      this.digY = this.yCoord + y;
      this.digZ = this.zCoord + z;
      this.toDig = true;
      this.digBlock = this.worldObj.getBlock(this.digX, this.digY, this.digZ);
      this.digMd = this.worldObj.getBlockMetadata(this.digX, this.digY, this.digZ);
   }

   public void sendDigEvent() {
      int x = this.digX - this.xCoord + 64;
      int y = this.digY - this.yCoord + 64;
      int z = this.digZ - this.zCoord + 64;
      int c = (x & 255) << 16 | (y & 255) << 8 | z & 255;
      PacketHandler.INSTANCE.sendToAllAround(new PacketBoreDig(this.xCoord, this.yCoord, this.zCoord, c), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord, 64.0F));
   }

   public int getSizeInventory() {
      return 2;
   }

   public ItemStack getStackInSlot(int var1) {
      return this.contents[var1];
   }

   public ItemStack decrStackSize(int var1, int var2) {
      if (this.contents[var1] != null) {
          ItemStack var3;
          if (this.contents[var1].stackSize <= var2) {
              var3 = this.contents[var1];
            this.contents[var1] = null;
          } else {
              var3 = this.contents[var1].splitStack(var2);
            if (this.contents[var1].stackSize == 0) {
               this.contents[var1] = null;
            }

          }
          this.markDirty();
          return var3;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.contents[var1] != null) {
         ItemStack var2 = this.contents[var1];
         this.contents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.contents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public String getInventoryName() {
      return "Arcane Bore";
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && var1.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
      return true;
   }

   public int onWandRightClick(World world, ItemStack wandstack, EntityPlayer player, int x, int y, int z, int side, int md) {
      this.setOrientation(ForgeDirection.getOrientation(side), false);
      player.worldObj.playSound((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:tool", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.2F, false);
      player.swingItem();
      this.markDirty();
      return 0;
   }

   public ItemStack onWandRightClick(World world, ItemStack wandstack, EntityPlayer player) {
      return null;
   }

   public void onUsingWandTick(ItemStack wandstack, EntityPlayer player, int count) {
   }

   public void onWandStoppedUsing(ItemStack wandstack, World world, EntityPlayer player, int count) {
   }
}
