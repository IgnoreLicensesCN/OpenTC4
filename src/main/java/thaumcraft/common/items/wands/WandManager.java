package thaumcraft.common.items.wands;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.network.NetworkRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.IArchitect;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandTriggerManager;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.foci.ItemFocusTrade;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileEldritchAltar;
import thaumcraft.common.tiles.TileInfusionMatrix;
import thaumcraft.common.tiles.TileInfusionPillar;
import thaumcraft.common.tiles.TileJarNode;
import thaumcraft.common.tiles.TileNode;
import thaumcraft.common.tiles.TilePedestal;
import thaumcraft.common.tiles.TileThaumatorium;

public class WandManager implements IWandTriggerManager {
   static HashMap<Integer,Long> cooldownServer = new HashMap<>();
   static HashMap<Integer,Long> cooldownClient = new HashMap<>();

   public static float getTotalVisDiscount(EntityPlayer player, Aspect aspect) {
      int total = 0;
      if (player == null) {
         return 0.0F;
      } else {
         IInventory baubles = BaublesApi.getBaubles(player);

         for(int a = 0; a < 4; ++a) {
            if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).getItem() instanceof IVisDiscountGear) {
               total += ((IVisDiscountGear)baubles.getStackInSlot(a).getItem()).getVisDiscount(baubles.getStackInSlot(a), player, aspect);
            }
         }

         for(int a = 0; a < 4; ++a) {
            if (player.inventory.armorItemInSlot(a) != null && player.inventory.armorItemInSlot(a).getItem() instanceof IVisDiscountGear) {
               total += ((IVisDiscountGear)player.inventory.armorItemInSlot(a).getItem()).getVisDiscount(player.inventory.armorItemInSlot(a), player, aspect);
            }
         }

         if (player.isPotionActive(Config.potionVisExhaustID) || player.isPotionActive(Config.potionInfVisExhaustID)) {
            int level1 = 0;
            int level2 = 0;
            if (player.isPotionActive(Config.potionVisExhaustID)) {
               level1 = player.getActivePotionEffect(Potion.potionTypes[Config.potionVisExhaustID]).getAmplifier();
            }

            if (player.isPotionActive(Config.potionInfVisExhaustID)) {
               level2 = player.getActivePotionEffect(Potion.potionTypes[Config.potionInfVisExhaustID]).getAmplifier();
            }

            total -= (Math.max(level1, level2) + 1) * 10;
         }

         return (float)total / 100.0F;
      }
   }

   public static boolean consumeVisFromInventory(EntityPlayer player, AspectList cost) {
      IInventory baubles = BaublesApi.getBaubles(player);

      for(int a = 0; a < 4; ++a) {
         if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).getItem() instanceof ItemAmuletVis) {
            boolean done = ((ItemAmuletVis)baubles.getStackInSlot(a).getItem()).consumeAllVis(baubles.getStackInSlot(a), player, cost, true, true);
            if (done) {
               return true;
            }
         }
      }

      for(int a = player.inventory.mainInventory.length - 1; a >= 0; --a) {
         ItemStack item = player.inventory.mainInventory[a];
         if (item != null && item.getItem() instanceof ItemWandCasting) {
            boolean done = ((ItemWandCasting)item.getItem()).consumeAllVis(item, player, cost, true, true);
            if (done) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean createCrucible(ItemStack is, EntityPlayer player, World world, int x, int y, int z) {
      ItemWandCasting wand = (ItemWandCasting)is.getItem();
      if (!world.isRemote) {
         world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
         world.setBlockToAir(x, y, z);
         world.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, 0, 3);
         world.notifyBlocksOfNeighborChange(x, y, z, world.getBlock(x, y, z));
         world.markBlockForUpdate(x, y, z);
         world.addBlockEvent(x, y, z, ConfigBlocks.blockMetalDevice, 1, 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean createInfusionAltar(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();

      for(int xx = x - 2; xx <= x; ++xx) {
         for(int yy = y - 2; yy <= y; ++yy) {
            for(int zz = z - 2; zz <= z; ++zz) {
               if (fitInfusionAltar(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 25).add(Aspect.EARTH, 25).add(Aspect.ORDER, 25).add(Aspect.AIR, 25).add(Aspect.ENTROPY, 25).add(Aspect.WATER, 25), true)) {
                  if (!world.isRemote) {
                     replaceInfusionAltar(world, xx, yy, zz);
                     return true;
                  }

                  return false;
               }
            }
         }
      }

      return false;
   }

   public static boolean fitInfusionAltar(World world, int x, int y, int z) {
      ItemStack br1 = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 6);
      ItemStack br2 = new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 7);
      ItemStack bs = new ItemStack(ConfigBlocks.blockStoneDevice, 1, 2);
      new ItemStack(ConfigBlocks.blockStoneDevice, 1, 1);
      ItemStack[][][] blueprint = new ItemStack[][][]{{{null, null, null}, {null, bs, null}, {null, null, null}}, {{br1, null, br1}, {null, null, null}, {br1, null, br1}}, {{br2, null, br2}, {null, null, null}, {br2, null, br2}}};

      for(int yy = 0; yy < 3; ++yy) {
         for(int xx = 0; xx < 3; ++xx) {
            for(int zz = 0; zz < 3; ++zz) {
               if (blueprint[yy][xx][zz] == null) {
                  if (xx == 1 && zz == 1 && yy == 2) {
                     TileEntity t = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                     if (t == null || !(t instanceof TilePedestal)) {
                        return false;
                     }
                  } else if (!world.isAirBlock(x + xx, y - yy + 2, z + zz)) {
                     return false;
                  }
               } else {
                  Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
                  int md = world.getBlockMetadata(x + xx, y - yy + 2, z + zz);
                  if (!(new ItemStack(block, 1, md)).isItemEqual(blueprint[yy][xx][zz])) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   public static void replaceInfusionAltar(World world, int x, int y, int z) {
      int[][][] blueprint = new int[][][]{{{0, 0, 0}, {0, 9, 0}, {0, 0, 0}}, {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}}, {{2, 0, 3}, {0, 0, 0}, {4, 0, 5}}};

      for(int yy = 0; yy < 3; ++yy) {
         for(int xx = 0; xx < 3; ++xx) {
            for(int zz = 0; zz < 3; ++zz) {
               if (blueprint[yy][xx][zz] != 0) {
                  if (blueprint[yy][xx][zz] == 1) {
                     world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 4, 3);
                     world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 1, 0);
                  }

                  if (blueprint[yy][xx][zz] > 1 && blueprint[yy][xx][zz] < 9) {
                     world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 3, 3);
                     TileInfusionPillar tip = (TileInfusionPillar)world.getTileEntity(x + xx, y - yy + 2, z + zz);
                     tip.orientation = (byte)blueprint[yy][xx][zz];
                     world.markBlockForUpdate(x + xx, y - yy + 2, z + zz);
                     world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockStoneDevice, 1, 0);
                  }

                  if (blueprint[yy][xx][zz] == 9) {
                     TileInfusionMatrix tis = (TileInfusionMatrix)world.getTileEntity(x + xx, y - yy + 2, z + zz);
                     tis.active = true;
                     world.markBlockForUpdate(x + xx, y - yy + 2, z + zz);
                  }
               }
            }
         }
      }

      world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
   }

   public static boolean createNodeJar(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();

      for(int xx = x - 2; xx <= x; ++xx) {
         for(int yy = y - 3; yy <= y; ++yy) {
            for(int zz = z - 2; zz <= z; ++zz) {
               if (fitNodeJar(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 70).add(Aspect.EARTH, 70).add(Aspect.ORDER, 70).add(Aspect.AIR, 70).add(Aspect.ENTROPY, 70).add(Aspect.WATER, 70), true)) {
                  if (!world.isRemote) {
                     replaceNodeJar(world, xx, yy, zz);
                     return true;
                  }

                  return false;
               }
            }
         }
      }

      return false;
   }

   public static boolean createThaumatorium(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      if (world.getBlock(x, y + 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y + 1, z) != 9 || world.getBlock(x, y - 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 1, z) != 0) {
         if (world.getBlock(x, y - 1, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 1, z) != 9 || world.getBlock(x, y - 2, z) != ConfigBlocks.blockMetalDevice || world.getBlockMetadata(x, y - 2, z) != 0) {
            return false;
         }

         --y;
      }

      if (wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 15).add(Aspect.ORDER, 30).add(Aspect.WATER, 30), true) && !world.isRemote) {
         world.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, 10, 0);
         world.setBlock(x, y + 1, z, ConfigBlocks.blockMetalDevice, 11, 0);
         TileEntity tile = world.getTileEntity(x, y, z);
         if (tile != null && tile instanceof TileThaumatorium) {
            ((TileThaumatorium)tile).facing = ForgeDirection.getOrientation(side);
         }

         world.markBlockForUpdate(x, y, z);
         world.markBlockForUpdate(x, y + 1, z);
         world.notifyBlockChange(x, y, z, ConfigBlocks.blockMetalDevice);
         world.notifyBlockChange(x, y + 1, z, ConfigBlocks.blockMetalDevice);
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y, z, -9999), new NetworkRegistry.TargetPoint(world.provider.dimensionId, (double)x, (double)y, (double)z, (double)32.0F));
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y + 1, z, -9999), new NetworkRegistry.TargetPoint(world.provider.dimensionId, (double)x, (double)y, (double)z, (double)32.0F));
         world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
         return true;
      } else {
         return false;
      }
   }

   static boolean containsMatch(boolean strict, List<ItemStack> inputs, ItemStack... targets) {
      for(ItemStack input : inputs) {
         for(ItemStack target : targets) {
            if (OreDictionary.itemMatches(input, target, strict)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean fitNodeJar(World world, int x, int y, int z) {
      int[][][] blueprint = new int[][][]{{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};

      for(int yy = 0; yy < 4; ++yy) {
         for(int xx = 0; xx < 3; ++xx) {
            for(int zz = 0; zz < 3; ++zz) {
               Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
               int md = world.getBlockMetadata(x + xx, y - yy + 2, z + zz);
               if (blueprint[yy][xx][zz] == 1 && !containsMatch(false, OreDictionary.getOres("slabWood"), new ItemStack(block, 1, md))) {
                  return false;
               }

               if (blueprint[yy][xx][zz] == 2 && block != Blocks.glass) {
                  return false;
               }

               if (blueprint[yy][xx][zz] == 3) {
                  TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                  if (tile == null || !(tile instanceof INode) || tile instanceof TileJarNode) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   public static void replaceNodeJar(World world, int x, int y, int z) {
      if (!world.isRemote) {
         int[][][] blueprint = new int[][][]{{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 3, 2}, {2, 2, 2}}, {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}}};

         for(int yy = 0; yy < 4; ++yy) {
            for(int xx = 0; xx < 3; ++xx) {
               for(int zz = 0; zz < 3; ++zz) {
                  if (blueprint[yy][xx][zz] == 3) {
                     TileEntity tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                     INode node = (INode)tile;
                     AspectList na = node.getAspects().copy();
                     int nt = node.getNodeType().ordinal();
                     int nm = -1;
                     if (node.getNodeModifier() != null) {
                        nm = node.getNodeModifier().ordinal();
                     }

                     if (world.rand.nextFloat() < 0.75F) {
                        if (node.getNodeModifier() == null) {
                           nm = NodeModifier.PALE.ordinal();
                        } else if (node.getNodeModifier() == NodeModifier.BRIGHT) {
                           nm = -1;
                        } else if (node.getNodeModifier() == NodeModifier.PALE) {
                           nm = NodeModifier.FADING.ordinal();
                        }
                     }

                     String nid = node.getId();
                     node.setAspects(new AspectList());
                     world.removeTileEntity(x + xx, y - yy + 2, z + zz);
                     world.setBlock(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 2, 3);
                     tile = world.getTileEntity(x + xx, y - yy + 2, z + zz);
                     TileJarNode jar = (TileJarNode)tile;
                     jar.setAspects(na);
                     if (nm >= 0) {
                        jar.setNodeModifier(NodeModifier.values()[nm]);
                     }

                     jar.setNodeType(NodeType.values()[nt]);
                     jar.setId(nid);
                     world.addBlockEvent(x + xx, y - yy + 2, z + zz, ConfigBlocks.blockJar, 9, 0);
                  } else {
                     world.setBlockToAir(x + xx, y - yy + 2, z + zz);
                  }
               }
            }
         }

         world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
      }
   }

   public static boolean createArcaneFurnace(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z) {
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();

      for(int xx = x - 2; xx <= x; ++xx) {
         for(int yy = y - 2; yy <= y; ++yy) {
            for(int zz = z - 2; zz <= z; ++zz) {
               if (fitArcaneFurnace(world, xx, yy, zz) && wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.EARTH, 50), true)) {
                  if (!world.isRemote) {
                     replaceArcaneFurnace(world, xx, yy, zz);
                     return true;
                  }

                  return false;
               }
            }
         }
      }

      return false;
   }

   public static boolean fitArcaneFurnace(World world, int x, int y, int z) {
      Block bo = Blocks.obsidian;
      Block bn = Blocks.nether_brick;
      Block bf = Blocks.iron_bars;
      Block bl = Blocks.lava;
      Block[][][] blueprint = new Block[][][]{{{bn, bo, bn}, {bo, Blocks.air, bo}, {bn, bo, bn}}, {{bn, bo, bn}, {bo, bl, bo}, {bn, bo, bn}}, {{bn, bo, bn}, {bo, bo, bo}, {bn, bo, bn}}};
      boolean fencefound = false;

      for(int yy = 0; yy < 3; ++yy) {
         for(int xx = 0; xx < 3; ++xx) {
            for(int zz = 0; zz < 3; ++zz) {
               Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
               if (world.isAirBlock(x + xx, y - yy + 2, z + zz)) {
                  block = Blocks.air;
               }

               if (block != blueprint[yy][xx][zz]) {
                  if (yy != 1 || fencefound || block != bf || xx == zz || xx != 1 && zz != 1) {
                     return false;
                  }

                  fencefound = true;
               }
            }
         }
      }

      return fencefound;
   }

   public static boolean replaceArcaneFurnace(World world, int x, int y, int z) {
      boolean fencefound = false;

      for(int yy = 0; yy < 3; ++yy) {
         int step = 1;

         for(int zz = 0; zz < 3; ++zz) {
            for(int xx = 0; xx < 3; ++xx) {
               int md = step;
               if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.lava || world.getBlock(x + xx, y + yy, z + zz) == Blocks.flowing_lava) {
                  md = 0;
               }

               if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.iron_bars) {
                  md = 10;
               }

               if (!world.isAirBlock(x + xx, y + yy, z + zz)) {
                  world.setBlock(x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, md, 0);
                  world.addBlockEvent(x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, 1, 4);
               }

               ++step;
            }
         }
      }

      for(int yy = 0; yy < 3; ++yy) {
         for(int zz = 0; zz < 3; ++zz) {
            for(int xx = 0; xx < 3; ++xx) {
               world.markBlockForUpdate(x + xx, y + yy, z + zz);
            }
         }
      }

      world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
      return fencefound;
   }

   public static boolean createThaumonomicon(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z) {
      if (!world.isRemote) {
         ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
         if (wand.getFocus(itemstack) != null) {
            return false;
         } else {
            world.setBlockToAir(x, y, z);
            EntitySpecialItem entityItem = new EntitySpecialItem(world, (double)((float)x + 0.5F), (double)((float)y + 0.3F), (double)((float)z + 0.5F), new ItemStack(ConfigItems.itemThaumonomicon));
            entityItem.motionY = (double)0.0F;
            entityItem.motionX = (double)0.0F;
            entityItem.motionZ = (double)0.0F;
            world.spawnEntityInWorld(entityItem);
            PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x, y, z, -9999), new NetworkRegistry.TargetPoint(world.provider.dimensionId, (double)x, (double)y, (double)z, (double)32.0F));
            world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
            return true;
         }
      } else {
         return false;
      }
   }

   private static boolean createOculus(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side) {
      if (!world.isRemote) {
         TileEntity tile = world.getTileEntity(x, y, z);
         TileEntity node = world.getTileEntity(x, y + 1, z);
         if (tile != null && node != null && tile instanceof TileEldritchAltar && ((TileEldritchAltar)tile).getEyes() == 4 && !((TileEldritchAltar)tile).isOpen() && node instanceof TileNode && ((TileNode)node).getNodeType() == NodeType.DARK && ((TileEldritchAltar)tile).checkForMaze()) {
            ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
            if (wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.AIR, 100).add(Aspect.FIRE, 100).add(Aspect.EARTH, 100).add(Aspect.WATER, 100).add(Aspect.ORDER, 100).add(Aspect.ENTROPY, 100), true)) {
               world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 1.0F, 1.0F);
               ((TileEldritchAltar)tile).setOpen(true);
               world.setBlockToAir(x, y + 1, z);
               world.setBlock(x, y + 1, z, ConfigBlocks.blockEldritchPortal);
               tile.markDirty();
               world.markBlockForUpdate(x, y, z);
            }
         }
      }

      return false;
   }

   public static void changeFocus(ItemStack is, World w, EntityPlayer player, String focus) {
      ItemWandCasting wand = (ItemWandCasting)is.getItem();
      TreeMap<String, Integer> foci = new TreeMap<>();
      HashMap<Integer, Integer> pouches = new HashMap<>();
      int pouchcount = 0;
      ItemStack item = null;
      IInventory baubles = BaublesApi.getBaubles(player);

      for(int a = 0; a < 4; ++a) {
         if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).getItem() instanceof ItemFocusPouch) {
            ++pouchcount;
            item = baubles.getStackInSlot(a);
            pouches.put(pouchcount, a - 4);
            ItemStack[] inv = ((ItemFocusPouch)item.getItem()).getInventory(item);

            for(int q = 0; q < inv.length; ++q) {
               item = inv[q];
               if (item != null && item.getItem() instanceof ItemFocusBasic) {
                  foci.put(((ItemFocusBasic)item.getItem()).getSortingHelper(item), q + pouchcount * 1000);
               }
            }
         }
      }

      for(int a = 0; a < 36; ++a) {
         item = player.inventory.mainInventory[a];
         if (item != null && item.getItem() instanceof ItemFocusBasic) {
            foci.put(((ItemFocusBasic)item.getItem()).getSortingHelper(item), a);
         }

         if (item != null && item.getItem() instanceof ItemFocusPouch) {
            ++pouchcount;
            pouches.put(pouchcount, a);
            ItemStack[] inv = ((ItemFocusPouch)item.getItem()).getInventory(item);

            for(int q = 0; q < inv.length; ++q) {
               item = inv[q];
               if (item != null && item.getItem() instanceof ItemFocusBasic) {
                  foci.put(((ItemFocusBasic)item.getItem()).getSortingHelper(item), q + pouchcount * 1000);
               }
            }
         }
      }

      if (!focus.equals("REMOVE") && foci.size() != 0) {
         if (foci != null && foci.size() > 0 && focus != null) {
            String newkey = focus;
            if (foci.get(focus) == null) {
               newkey = (String)foci.higherKey(focus);
            }

            if (newkey == null || foci.get(newkey) == null) {
               newkey = (String)foci.firstKey();
            }

            if ((Integer)foci.get(newkey) < 1000 && (Integer)foci.get(newkey) >= 0) {
               item = player.inventory.mainInventory[(Integer)foci.get(newkey)].copy();
            } else {
               int pid = (Integer)foci.get(newkey) / 1000;
               if (pouches.containsKey(pid)) {
                  int pouchslot = (Integer)pouches.get(pid);
                  int focusslot = (Integer)foci.get(newkey) - pid * 1000;
                  ItemStack tmp = null;
                  if (pouchslot >= 0) {
                     tmp = player.inventory.mainInventory[pouchslot].copy();
                  } else {
                     tmp = baubles.getStackInSlot(pouchslot + 4).copy();
                  }

                  item = fetchFocusFromPouch(player, focusslot, tmp, pouchslot);
               }
            }

            if (item == null) {
               return;
            }

            if ((Integer)foci.get(newkey) < 1000 && (Integer)foci.get(newkey) >= 0) {
               player.inventory.setInventorySlotContents((Integer)foci.get(newkey), (ItemStack)null);
            }

            w.playSoundAtEntity(player, "thaumcraft:cameraticks", 0.3F, 1.0F);
            if (wand.getFocus(is) != null && (addFocusToPouch(player, wand.getFocusItem(is).copy(), pouches) || player.inventory.addItemStackToInventory(wand.getFocusItem(is).copy()))) {
               wand.setFocus(is, (ItemStack)null);
            }

            if (wand.getFocus(is) == null) {
               wand.setFocus(is, item);
            } else if (!addFocusToPouch(player, item, pouches)) {
               player.inventory.addItemStackToInventory(item);
            }
         }

      } else {
         if (wand.getFocus(is) != null && (addFocusToPouch(player, wand.getFocusItem(is).copy(), pouches) || player.inventory.addItemStackToInventory(wand.getFocusItem(is).copy()))) {
            wand.setFocus(is, (ItemStack)null);
            w.playSoundAtEntity(player, "thaumcraft:cameraticks", 0.3F, 0.9F);
         }

      }
   }

   private static ItemStack fetchFocusFromPouch(EntityPlayer player, int focusid, ItemStack pouch, int pouchslot) {
      ItemStack focus = null;
      ItemStack[] inv = ((ItemFocusPouch)pouch.getItem()).getInventory(pouch);
      ItemStack contents = inv[focusid];
      if (contents != null && contents.getItem() instanceof ItemFocusBasic) {
         focus = contents.copy();
         inv[focusid] = null;
         ((ItemFocusPouch)pouch.getItem()).setInventory(pouch, inv);
         if (pouchslot >= 0) {
            player.inventory.setInventorySlotContents(pouchslot, pouch);
            player.inventory.markDirty();
         } else {
            IInventory baubles = BaublesApi.getBaubles(player);
            baubles.setInventorySlotContents(pouchslot + 4, pouch);
            baubles.markDirty();
         }
      }

      return focus;
   }

   private static boolean addFocusToPouch(EntityPlayer player, ItemStack focus, HashMap<Integer, Integer> pouches) {
      IInventory baubles = BaublesApi.getBaubles(player);

      for(Integer pouchslot : pouches.values()) {
         ItemStack pouch;
         if (pouchslot >= 0) {
            pouch = player.inventory.mainInventory[pouchslot];
         } else {
            pouch = baubles.getStackInSlot(pouchslot + 4);
         }

         ItemStack[] inv = ((ItemFocusPouch)pouch.getItem()).getInventory(pouch);

         for(int q = 0; q < inv.length; ++q) {
            ItemStack contents = inv[q];
            if (contents == null) {
               inv[q] = focus.copy();
               ((ItemFocusPouch)pouch.getItem()).setInventory(pouch, inv);
               if (pouchslot >= 0) {
                  player.inventory.setInventorySlotContents(pouchslot, pouch);
                  player.inventory.markDirty();
               } else {
                  baubles.setInventorySlotContents(pouchslot + 4, pouch);
                  baubles.markDirty();
               }

               return true;
            }
         }
      }

      return false;
   }

   public static void toggleMisc(ItemStack itemstack, World world, EntityPlayer player) {
      if (itemstack.getItem() instanceof ItemWandCasting) {
         ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
         if (wand.getFocus(itemstack) != null && wand.getFocus(itemstack) instanceof IArchitect && wand.getFocus(itemstack).isUpgradedWith(wand.getFocusItem(itemstack), FocusUpgradeType.architect)) {
            int dim = getAreaDim(itemstack);
            IArchitect fa = (IArchitect)wand.getFocus(itemstack);
            if (player.isSneaking()) {
               ++dim;
               if (dim > (wand.getFocusItem(itemstack).getItem() instanceof ItemFocusTrade ? 2 : 3)) {
                  dim = 0;
               }

               setAreaDim(itemstack, dim);
            } else {
               int areax = getAreaX(itemstack);
               int areay = getAreaY(itemstack);
               int areaz = getAreaZ(itemstack);
               if (dim == 0) {
                  ++areax;
                  ++areaz;
                  ++areay;
               } else if (dim == 1) {
                  ++areax;
               } else if (dim == 2) {
                  ++areaz;
               } else if (dim == 3) {
                  ++areay;
               }

               if (areax > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                  areax = 0;
               }

               if (areaz > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                  areaz = 0;
               }

               if (areay > wand.getFocus(itemstack).getMaxAreaSize(wand.getFocusItem(itemstack))) {
                  areay = 0;
               }

               setAreaX(itemstack, areax);
               setAreaY(itemstack, areay);
               setAreaZ(itemstack, areaz);
            }
         }

      }
   }

   public static int getAreaDim(ItemStack stack) {
      return stack.hasTagCompound() && stack.stackTagCompound.hasKey("aread") ? stack.stackTagCompound.getInteger("aread") : 0;
   }

   public static int getAreaX(ItemStack stack) {
      ItemWandCasting wand = (ItemWandCasting)stack.getItem();
      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areax")) {
         int a = stack.stackTagCompound.getInteger("areax");
         if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
            a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
         }

         return a;
      } else {
         return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
      }
   }

   public static int getAreaY(ItemStack stack) {
      ItemWandCasting wand = (ItemWandCasting)stack.getItem();
      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areay")) {
         int a = stack.stackTagCompound.getInteger("areay");
         if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
            a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
         }

         return a;
      } else {
         return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
      }
   }

   public static int getAreaZ(ItemStack stack) {
      ItemWandCasting wand = (ItemWandCasting)stack.getItem();
      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("areaz")) {
         int a = stack.stackTagCompound.getInteger("areaz");
         if (a > wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack))) {
            a = wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
         }

         return a;
      } else {
         return wand.getFocus(stack).getMaxAreaSize(wand.getFocusItem(stack));
      }
   }

   public static void setAreaX(ItemStack stack, int area) {
      if (stack.hasTagCompound()) {
         stack.stackTagCompound.setInteger("areax", area);
      }

   }

   public static void setAreaY(ItemStack stack, int area) {
      if (stack.hasTagCompound()) {
         stack.stackTagCompound.setInteger("areay", area);
      }

   }

   public static void setAreaZ(ItemStack stack, int area) {
      if (stack.hasTagCompound()) {
         stack.stackTagCompound.setInteger("areaz", area);
      }

   }

   public static void setAreaDim(ItemStack stack, int dim) {
      if (stack.hasTagCompound()) {
         stack.stackTagCompound.setInteger("aread", dim);
      }

   }

   static boolean isOnCooldown(EntityLivingBase entityLiving) {
      if (entityLiving.worldObj.isRemote && cooldownClient.containsKey(entityLiving.getEntityId())) {
         return (Long)cooldownClient.get(entityLiving.getEntityId()) > System.currentTimeMillis();
      } else if (!entityLiving.worldObj.isRemote && cooldownServer.containsKey(entityLiving.getEntityId())) {
         return (Long)cooldownServer.get(entityLiving.getEntityId()) > System.currentTimeMillis();
      } else {
         return false;
      }
   }

   public static float getCooldown(EntityLivingBase entityLiving) {
      return entityLiving.worldObj.isRemote && cooldownClient.containsKey(entityLiving.getEntityId()) ? (float)((Long)cooldownClient.get(entityLiving.getEntityId()) - System.currentTimeMillis()) / 1000.0F : 0.0F;
   }

   public static void setCooldown(EntityLivingBase entityLiving, int cd) {
      if (cd == 0) {
         cooldownClient.remove(entityLiving.getEntityId());
         cooldownServer.remove(entityLiving.getEntityId());
      } else if (entityLiving.worldObj.isRemote) {
         cooldownClient.put(entityLiving.getEntityId(), System.currentTimeMillis() + (long)cd);
      } else {
         cooldownServer.put(entityLiving.getEntityId(), System.currentTimeMillis() + (long)cd);
      }

   }

   public boolean performTrigger(World world, ItemStack wand, EntityPlayer player, int x, int y, int z, int side, int event) {
      switch (event) {
         case 0:
            return createThaumonomicon(wand, player, world, x, y, z);
         case 1:
            return createCrucible(wand, player, world, x, y, z);
         case 2:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "INFERNALFURNACE")) {
               return createArcaneFurnace(wand, player, world, x, y, z);
            }
            break;
         case 3:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "INFUSION")) {
               return createInfusionAltar(wand, player, world, x, y, z);
            }
            break;
         case 4:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "NODEJAR")) {
               return createNodeJar(wand, player, world, x, y, z);
            }
            break;
         case 5:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "THAUMATORIUM")) {
               return createThaumatorium(wand, player, world, x, y, z, side);
            }
            break;
         case 6:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "OCULUS")) {
               return createOculus(wand, player, world, x, y, z, side);
            }
            break;
         case 7:
            if (ResearchManager.isResearchComplete(player.getCommandSenderName(), "ADVALCHEMYFURNACE")) {
               return createAdvancedAlchemicalFurnace(wand, player, world, x, y, z, side);
            }
      }

      return false;
   }

   private static boolean createAdvancedAlchemicalFurnace(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side) {
       if (!world.isRemote) {
           int[][][] blueprint = new int[][][]{{{4, 4, 4}, {4, 3, 4}, {4, 4, 4}}, {{1, 2, 1}, {2, 0, 2}, {1, 2, 1}}};

           for (int a = -1; a <= 1; ++a) {
               for (int b = -1; b <= 1; ++b) {
                   for (int c = -1; c <= 1; ++c) {
                       if (world.getBlock(x + a, y + b, z + c) == ConfigBlocks.blockStoneDevice && world.getBlockMetadata(x + a, y + b, z + c) == 0) {
                           for (int aa = -1; aa <= 1; ++aa) {
                               for (int bb = 0; bb <= 1; ++bb) {
                                   int cc = -1;

                                   while (cc <= 1) {
                                       if (blueprint[bb][aa + 1][cc + 1] != 1 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 1) {
                                           if (blueprint[bb][aa + 1][cc + 1] != 2 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 9) {
                                               if (blueprint[bb][aa + 1][cc + 1] != 4 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockMetalDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 3) {
                                                   if (blueprint[bb][aa + 1][cc + 1] != 3 || world.getBlock(x + a + aa, y + b + bb, z + c + cc) == ConfigBlocks.blockStoneDevice && world.getBlockMetadata(x + a + aa, y + b + bb, z + c + cc) == 0) {
                                                       ++cc;
                                                       continue;
                                                   }

                                                   return false;
                                               }

                                               return false;
                                           }

                                           return false;
                                       }

                                       return false;
                                   }
                               }
                           }

                           ItemWandCasting wand = (ItemWandCasting) itemstack.getItem();
                           if (!wand.consumeAllVisCrafting(itemstack, player, (new AspectList()).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.ORDER, 50), true)) {
                               return false;
                           }

                           world.setBlock(x + a, y + b, z + c, ConfigBlocks.blockAlchemyFurnace);
                           world.setBlock(x + a - 1, y + b, z + c, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                           world.setBlock(x + a + 1, y + b, z + c, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                           world.setBlock(x + a, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                           world.setBlock(x + a, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 1, 3);
                           world.setBlock(x + a - 1, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                           world.setBlock(x + a + 1, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                           world.setBlock(x + a + 1, y + b, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                           world.setBlock(x + a - 1, y + b, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 4, 3);
                           world.setBlock(x + a - 1, y + b + 1, z + c, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                           world.setBlock(x + a + 1, y + b + 1, z + c, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                           world.setBlock(x + a, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                           world.setBlock(x + a, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 3, 3);
                           world.setBlock(x + a - 1, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                           world.setBlock(x + a + 1, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                           world.setBlock(x + a + 1, y + b + 1, z + c - 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                           world.setBlock(x + a - 1, y + b + 1, z + c + 1, ConfigBlocks.blockAlchemyFurnace, 2, 3);
                           world.playSoundEffect((double) (x + a) + (double) 0.5F, (double) (y + b) + (double) 0.5F, (double) (z + c) + (double) 0.5F, "thaumcraft:wand", 1.0F, 1.0F);

                           for (int aa = -1; aa <= 1; ++aa) {
                               for (int bb = 0; bb <= 1; ++bb) {
                                   for (int cc = -1; cc <= 1; ++cc) {
                                       PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockSparkle(x + a + aa, y + b + bb, z + c + cc, -9999), new NetworkRegistry.TargetPoint(world.provider.dimensionId, (double) (x + a), (double) (y + b), (double) (z + c), (double) 32.0F));
                                   }
                               }
                           }

                           return true;
                       }
                   }
               }
           }

       }
       return false;
   }
}
