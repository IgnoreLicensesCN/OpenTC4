package thaumcraft.common.lib.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import org.apache.logging.log4j.Level;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.world.ChunkLoc;
import thaumcraft.common.tiles.TileSensor;

public class ServerTickEventsFML {
   public static Map swapList = new HashMap<>();
   public static HashMap chunksToGenerate = new HashMap<>();

   @SubscribeEvent
   public void serverWorldTick(TickEvent.WorldTickEvent event) {
      if (event.side != Side.CLIENT) {
         if (event.phase != Phase.START) {
            this.tickChunkRegeneration(event);
            this.tickBlockSwap(event.world);
            if (TileSensor.noteBlockEvents.get(event.world) != null) {
               ((ArrayList)TileSensor.noteBlockEvents.get(event.world)).clear();
            }
         }

      }
   }

   public void tickChunkRegeneration(TickEvent.WorldTickEvent event) {
      int dim = event.world.provider.dimensionId;
      int count = 0;
      ArrayList<ChunkLoc> chunks = (ArrayList)chunksToGenerate.get(dim);
      if (chunks != null && !chunks.isEmpty()) {
         for(int a = 0; a < 10; ++a) {
            chunks = (ArrayList)chunksToGenerate.get(dim);
            if (chunks == null || chunks.isEmpty()) {
               break;
            }

            ++count;
            ChunkLoc loc = chunks.get(0);
            long worldSeed = event.world.getSeed();
            Random fmlRandom = new Random(worldSeed);
            long xSeed = fmlRandom.nextLong() >> 3;
            long zSeed = fmlRandom.nextLong() >> 3;
            fmlRandom.setSeed(xSeed * (long)loc.chunkXPos + zSeed * (long)loc.chunkZPos ^ worldSeed);
            Thaumcraft.instance.worldGen.worldGeneration(fmlRandom, loc.chunkXPos, loc.chunkZPos, event.world, false);
            chunks.remove(0);
            chunksToGenerate.put(dim, chunks);
         }
      }

      if (count > 0) {
         FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[Thaumcraft] Regenerated {} chunks. {} chunks left",count, chunks.size());
      }

   }

   private void tickBlockSwap(World world) {
      int dim = world.provider.dimensionId;
      LinkedBlockingQueue<VirtualSwapper> queue = (LinkedBlockingQueue)swapList.get(dim);
      if (queue != null) {
         boolean didSomething = false;

         while(!didSomething) {
            VirtualSwapper vs = queue.poll();
            if (vs != null) {
               Block bi = world.getBlock(vs.x, vs.y, vs.z);
               int md = world.getBlockMetadata(vs.x, vs.y, vs.z);
               ItemWandCasting wand = null;
               ItemFocusBasic focus = null;
               ItemStack focusStack = null;
               if (vs.player.inventory.getStackInSlot(vs.wand) != null && vs.player.inventory.getStackInSlot(vs.wand).getItem() instanceof ItemWandCasting) {
                  wand = (ItemWandCasting)vs.player.inventory.getStackInSlot(vs.wand).getItem();
                  focusStack = wand.getFocusItem(vs.player.inventory.getStackInSlot(vs.wand));
                  focus = wand.getFocus(vs.player.inventory.getStackInSlot(vs.wand));
               }

               if (world.canMineBlock(vs.player, vs.x, vs.y, vs.z) && !vs.target.isItemEqual(new ItemStack(bi, 1, md)) && wand != null && focus != null && !ForgeEventFactory.onPlayerInteract(vs.player, Action.RIGHT_CLICK_BLOCK, vs.x, vs.y, vs.z, 1, world).isCanceled() && wand.consumeAllVis(vs.player.inventory.getStackInSlot(vs.wand), vs.player, focus.getVisCost(focusStack), false, false)) {
                  int slot = InventoryUtils.isPlayerCarrying(vs.player, vs.target);
                  if (vs.player.capabilities.isCreativeMode) {
                     slot = 1;
                  }

                  if (vs.bSource == bi && vs.mSource == md && slot >= 0) {
                     didSomething = true;
                     if (!vs.player.capabilities.isCreativeMode) {
                        int fortune = wand.getFocusTreasure(vs.player.inventory.getStackInSlot(vs.wand));
                        boolean silk = wand.getFocus(vs.player.inventory.getStackInSlot(vs.wand)).isUpgradedWith(wand.getFocusItem(vs.player.inventory.getStackInSlot(vs.wand)), FocusUpgradeType.silktouch);
                        vs.player.inventory.decrStackSize(slot, 1);
                        ArrayList<ItemStack> ret = new ArrayList<>();
                        if (silk && bi.canSilkHarvest(world, vs.player, vs.x, vs.y, vs.z, md)) {
                           ItemStack itemstack = BlockUtils.createStackedBlock(bi, md);
                           if (itemstack != null) {
                              ret.add(itemstack);
                           }
                        } else {
                           ret = bi.getDrops(world, vs.x, vs.y, vs.z, md, fortune);
                        }

                        if (!ret.isEmpty()) {
                           for(ItemStack is : ret) {
                              if (!vs.player.inventory.addItemStackToInventory(is)) {
                                 world.spawnEntityInWorld(new EntityItem(world, (double)vs.x + (double)0.5F, (double)vs.y + (double)0.5F, (double)vs.z + (double)0.5F, is));
                              }
                           }
                        }

                        wand.consumeAllVis(vs.player.inventory.getStackInSlot(vs.wand), vs.player, focus.getVisCost(focusStack), true, false);
                     }

                     world.setBlock(vs.x, vs.y, vs.z, Block.getBlockFromItem(vs.target.getItem()), vs.target.getItemDamage(), 3);
                     Block.getBlockFromItem(vs.target.getItem()).onBlockPlacedBy(world, vs.x, vs.y, vs.z, vs.player, vs.target);
                     PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(vs.x, vs.y, vs.z, 12632319), new NetworkRegistry.TargetPoint(world.provider.dimensionId, vs.x, vs.y, vs.z, 32.0F));
                     world.playAuxSFX(2001, vs.x, vs.y, vs.z, Block.getIdFromBlock(vs.bSource) + (vs.mSource << 12));
                     if (vs.lifespan > 0) {
                        for(int xx = -1; xx <= 1; ++xx) {
                           for(int yy = -1; yy <= 1; ++yy) {
                              for(int zz = -1; zz <= 1; ++zz) {
                                 if ((xx != 0 || yy != 0 || zz != 0) && world.getBlock(vs.x + xx, vs.y + yy, vs.z + zz) == vs.bSource && world.getBlockMetadata(vs.x + xx, vs.y + yy, vs.z + zz) == vs.mSource && BlockUtils.isBlockExposed(world, vs.x + xx, vs.y + yy, vs.z + zz)) {
                                    queue.offer(new VirtualSwapper(vs.x + xx, vs.y + yy, vs.z + zz, vs.bSource, vs.mSource, vs.target, vs.lifespan - 1, vs.player, vs.wand));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               didSomething = true;
            }
         }

         swapList.put(dim, queue);
      }

   }

   public static void addSwapper(World world, int x, int y, int z, Block bs, int ms, ItemStack target, int life, EntityPlayer player, int wand) {
      int dim = world.provider.dimensionId;
      if (bs != Blocks.air && !(bs.getBlockHardness(world, x, y, z) < 0.0F) && !target.isItemEqual(new ItemStack(bs, 1, ms))) {
         LinkedBlockingQueue<VirtualSwapper> queue = (LinkedBlockingQueue)swapList.get(dim);
         if (queue == null) {
            swapList.put(dim, new LinkedBlockingQueue());
            queue = (LinkedBlockingQueue)swapList.get(dim);
         }

         queue.offer(new VirtualSwapper(x, y, z, bs, ms, target, life, player, wand));
         world.playSoundAtEntity(player, "thaumcraft:wand", 0.25F, 1.0F);
         swapList.put(dim, queue);
      }
   }

   public static class VirtualSwapper {
      int lifespan = 0;
      int x = 0;
      int y = 0;
      int z = 0;
      Block bSource;
      int mSource = 0;
      ItemStack target;
      int wand = 0;
      EntityPlayer player = null;

      VirtualSwapper(int x, int y, int z, Block bs, int ms, ItemStack t, int life, EntityPlayer p, int wand) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.bSource = bs;
         this.mSource = ms;
         this.target = t;
         this.lifespan = life;
         this.player = p;
         this.wand = wand;
      }
   }

   public static class RestorableWardedBlock {
      int x = 0;
      int y = 0;
      int z = 0;
      Block bi;
      int md = 0;
      NBTTagCompound nbt = null;

      RestorableWardedBlock(World world, int x, int y, int z) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.bi = world.getBlock(x, y, z);
         this.md = world.getBlockMetadata(x, y, z);
         TileEntity te = world.getTileEntity(x, y, z);
         if (te != null) {
            this.nbt = new NBTTagCompound();
            te.writeToNBT(this.nbt);
         }

      }
   }
}
