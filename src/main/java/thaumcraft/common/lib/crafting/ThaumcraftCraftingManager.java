package thaumcraft.common.lib.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.Utils;

public class ThaumcraftCraftingManager {
   public static ShapedRecipes createFakeRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj) {
      StringBuilder var3 = new StringBuilder();
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      if (par2ArrayOfObj[var4] instanceof String[]) {
         String[] var7 = (String[])par2ArrayOfObj[var4++];

         for(String var11 : var7) {
            ++var6;
            var5 = var11.length();
            var3.append(var11);
         }
      } else {
         while(par2ArrayOfObj[var4] instanceof String) {
            String var13 = (String)par2ArrayOfObj[var4++];
            ++var6;
            var5 = var13.length();
            var3.append(var13);
         }
      }

      HashMap var14;
      for(var14 = new HashMap<>(); var4 < par2ArrayOfObj.length; var4 += 2) {
         Character var16 = (Character)par2ArrayOfObj[var4];
         ItemStack var17 = null;
         if (par2ArrayOfObj[var4 + 1] instanceof Item) {
            var17 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
         } else if (par2ArrayOfObj[var4 + 1] instanceof Block) {
            var17 = new ItemStack((Block)par2ArrayOfObj[var4 + 1]);
         } else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack) {
            var17 = (ItemStack)par2ArrayOfObj[var4 + 1];
         }

         var14.put(var16, var17);
      }

      ItemStack[] var15 = new ItemStack[var5 * var6];

      for(int var9 = 0; var9 < var5 * var6; ++var9) {
         char var18 = var3.charAt(var9);
         if (var14.containsKey(var18)) {
            var15[var9] = ((ItemStack)var14.get(var18)).copy();
         } else {
            var15[var9] = null;
         }
      }

      return new ShapedRecipes(var5, var6, var15, par1ItemStack);
   }

   public static CrucibleRecipe findMatchingCrucibleRecipe(String username, AspectList aspects, ItemStack lastDrop) {
      int highest = 0;
      int index = -1;

      for(int a = 0; a < ThaumcraftApi.getCraftingRecipes().size(); ++a) {
         if (ThaumcraftApi.getCraftingRecipes().get(a) instanceof CrucibleRecipe) {
            CrucibleRecipe recipe = (CrucibleRecipe)ThaumcraftApi.getCraftingRecipes().get(a);
            ItemStack temp = lastDrop.copy();
            temp.stackSize = 1;
            if (ResearchManager.isResearchComplete(username, recipe.key) && recipe.matches(aspects, temp)) {
               int result = recipe.aspects.size();
               if (result > highest) {
                  highest = result;
                  index = a;
               }
            }
         }
      }

      if (index < 0) {
         return null;
      } else {
         new AspectList();
         return (CrucibleRecipe)ThaumcraftApi.getCraftingRecipes().get(index);
      }
   }

   public static ItemStack findMatchingArcaneRecipe(IInventory awb, EntityPlayer player) {
      int var2 = 0;
      ItemStack var3 = null;
      ItemStack var4 = null;

      for(int var5 = 0; var5 < 9; ++var5) {
         ItemStack var6 = awb.getStackInSlot(var5);
         if (var6 != null) {
            if (var2 == 0) {
            }

            if (var2 == 1) {
            }

            ++var2;
         }
      }

      IArcaneRecipe var13 = null;

      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
         if (var11 instanceof IArcaneRecipe && ((IArcaneRecipe)var11).matches(awb, player.worldObj, player)) {
            var13 = (IArcaneRecipe)var11;
            break;
         }
      }

      return var13 == null ? null : var13.getCraftingResult(awb);
   }

   public static AspectList findMatchingArcaneRecipeAspects(IInventory awb, EntityPlayer player) {
      int var2 = 0;
      ItemStack var3 = null;
      ItemStack var4 = null;

      for(int var5 = 0; var5 < 9; ++var5) {
         ItemStack var6 = awb.getStackInSlot(var5);
         if (var6 != null) {
            if (var2 == 0) {
            }

            if (var2 == 1) {
            }

            ++var2;
         }
      }

      IArcaneRecipe var13 = null;

      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
         if (var11 instanceof IArcaneRecipe && ((IArcaneRecipe)var11).matches(awb, player.worldObj, player)) {
            var13 = (IArcaneRecipe)var11;
            break;
         }
      }

      return var13 == null ? new AspectList() : (var13.getAspects() != null ? var13.getAspects() : var13.getAspects(awb));
   }

   public static InfusionRecipe findMatchingInfusionRecipe(ArrayList items, ItemStack input, EntityPlayer player) {
      InfusionRecipe var13 = null;

      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
         if (var11 instanceof InfusionRecipe && ((InfusionRecipe)var11).matches(items, input, player.worldObj, player)) {
            var13 = (InfusionRecipe)var11;
            break;
         }
      }

      return var13;
   }

   public static InfusionEnchantmentRecipe findMatchingInfusionEnchantmentRecipe(ArrayList items, ItemStack input, EntityPlayer player) {
      InfusionEnchantmentRecipe var13 = null;

      for(Object var11 : ThaumcraftApi.getCraftingRecipes()) {
         if (var11 instanceof InfusionEnchantmentRecipe && ((InfusionEnchantmentRecipe)var11).matches(items, input, player.worldObj, player)) {
            var13 = (InfusionEnchantmentRecipe)var11;
            break;
         }
      }

      return var13;
   }

   public static AspectList getObjectTags(ItemStack itemstack) {
      Item item;
      int meta;
      try {
         item = itemstack.getItem();
         meta = itemstack.getItemDamage();
      } catch (Exception var8) {
         return null;
      }

      AspectList tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, meta));
      if (tmp == null) {
         for(List l : ThaumcraftApi.objectTags.keySet()) {
            if (l.get(0) == item && l.get(1) instanceof int[]) {
               int[] range = (int[])l.get(1);
               Arrays.sort(range);
               if (Arrays.binarySearch(range, meta) >= 0) {
                  tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, range));
                  return tmp;
               }
            }
         }

         tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, 32767));
         if (tmp == null) {
            if (meta == 32767) {
               int index = 0;

               do {
                  tmp = ThaumcraftApi.objectTags.get(Arrays.asList(item, index));
                  ++index;
               } while(index < 16 && tmp == null);
            }

            if (tmp == null) {
               tmp = generateTags(item, meta);
            }
         }
      }

      if (itemstack.getItem() instanceof ItemWandCasting) {
         ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
         if (tmp == null) {
            tmp = new AspectList();
         }

         tmp.merge(Aspect.MAGIC, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 2);
         tmp.merge(Aspect.TOOL, (wand.getRod(itemstack).getCraftCost() + wand.getCap(itemstack).getCraftCost()) / 3);
      }

      if (item != null && item == Items.potionitem) {
         if (tmp == null) {
            tmp = new AspectList();
         }

         tmp.merge(Aspect.WATER, 1);
         ItemPotion ip = (ItemPotion)item;
         List<PotionEffect> effects = (List<PotionEffect>)ip.getEffects(itemstack.getItemDamage());
         if (effects != null) {
            if (ItemPotion.isSplash(itemstack.getItemDamage())) {
               tmp.merge(Aspect.ENTROPY, 2);
            }

            for(PotionEffect var6 : effects) {
               tmp.merge(Aspect.MAGIC, (var6.getAmplifier() + 1) * 2);
               if (var6.getPotionID() == Potion.blindness.id) {
                  tmp.merge(Aspect.DARKNESS, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.confusion.id) {
                  tmp.merge(Aspect.ELDRITCH, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.damageBoost.id) {
                  tmp.merge(Aspect.WEAPON, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.digSlowdown.id) {
                  tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.digSpeed.id) {
                  tmp.merge(Aspect.TOOL, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.fireResistance.id) {
                  tmp.merge(Aspect.ARMOR, var6.getAmplifier() + 1);
                  tmp.merge(Aspect.FIRE, (var6.getAmplifier() + 1) * 2);
               } else if (var6.getPotionID() == Potion.harm.id) {
                  tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.heal.id) {
                  tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.hunger.id) {
                  tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.invisibility.id) {
                  tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.jump.id) {
                  tmp.merge(Aspect.FLIGHT, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.moveSlowdown.id) {
                  tmp.merge(Aspect.TRAP, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.moveSpeed.id) {
                  tmp.merge(Aspect.MOTION, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.nightVision.id) {
                  tmp.merge(Aspect.SENSES, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.poison.id) {
                  tmp.merge(Aspect.POISON, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.regeneration.id) {
                  tmp.merge(Aspect.HEAL, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.resistance.id) {
                  tmp.merge(Aspect.ARMOR, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.waterBreathing.id) {
                  tmp.merge(Aspect.AIR, (var6.getAmplifier() + 1) * 3);
               } else if (var6.getPotionID() == Potion.weakness.id) {
                  tmp.merge(Aspect.DEATH, (var6.getAmplifier() + 1) * 3);
               }
            }
         }
      }

      return capAspects(tmp, 64);
   }

   private static AspectList capAspects(AspectList sourcetags, int amount) {
      if (sourcetags == null) {
         return sourcetags;
      } else {
         AspectList out = new AspectList();

         for(Aspect aspect : sourcetags.getAspects()) {
            out.merge(aspect, Math.min(amount, sourcetags.getAmount(aspect)));
         }

         return out;
      }
   }

   public static AspectList getBonusTags(ItemStack itemstack, AspectList sourcetags) {
      AspectList tmp = new AspectList();
      Item item = itemstack.getItem();
      if (item instanceof IEssentiaContainerItem) {
         tmp = ((IEssentiaContainerItem)item).getAspects(itemstack);
         if (tmp != null && tmp.size() > 0) {
            for(Aspect tag : tmp.copy().getAspects()) {
               if (tmp.getAmount(tag) <= 0) {
                  tmp.remove(tag);
               }
            }
         }
      }

      if (tmp == null) {
         tmp = new AspectList();
      }

      if (sourcetags != null) {
         for(Aspect tag : sourcetags.getAspects()) {
            if (tag != null) {
               tmp.add(tag, sourcetags.getAmount(tag));
            }
         }
      }

      int id = Item.getIdFromItem(itemstack.getItem());
      int meta = itemstack.getItemDamage();
      if (item != null && (tmp != null || item == Items.potionitem)) {
         if (item instanceof ItemArmor) {
            tmp.merge(Aspect.ARMOR, ((ItemArmor)item).damageReduceAmount);
         } else if (item instanceof ItemSword && ((ItemSword)item).func_150931_i() + 1.0F > 0.0F) {
            tmp.merge(Aspect.WEAPON, (int)(((ItemSword)item).func_150931_i() + 1.0F));
         } else if (item instanceof ItemBow) {
            tmp.merge(Aspect.WEAPON, 3).merge(Aspect.FLIGHT, 1);
         } else if (item instanceof ItemPickaxe) {
            String mat = ((ItemTool)item).getToolMaterialName();

            for(Item.ToolMaterial tm : ToolMaterial.values()) {
               if (tm.toString().equals(mat)) {
                  tmp.merge(Aspect.MINE, tm.getHarvestLevel() + 1);
               }
            }
         } else if (item instanceof ItemTool) {
            String mat = ((ItemTool)item).getToolMaterialName();

            for(Item.ToolMaterial tm : ToolMaterial.values()) {
               if (tm.toString().equals(mat)) {
                  tmp.merge(Aspect.TOOL, tm.getHarvestLevel() + 1);
               }
            }
         } else if (item instanceof ItemShears || item instanceof ItemHoe) {
            if (item.getMaxDamage() <= ToolMaterial.WOOD.getMaxUses()) {
               tmp.merge(Aspect.HARVEST, 1);
            } else if (item.getMaxDamage() > ToolMaterial.STONE.getMaxUses() && item.getMaxDamage() > ToolMaterial.GOLD.getMaxUses()) {
               if (item.getMaxDamage() <= ToolMaterial.IRON.getMaxUses()) {
                  tmp.merge(Aspect.HARVEST, 3);
               } else {
                  tmp.merge(Aspect.HARVEST, 4);
               }
            } else {
               tmp.merge(Aspect.HARVEST, 2);
            }
         }

         NBTTagList ench = itemstack.getEnchantmentTagList();
         if (item instanceof ItemEnchantedBook) {
            ench = ((ItemEnchantedBook)item).func_92110_g(itemstack);
         }

         if (ench != null) {
            int var5 = 0;

            for(int var3 = 0; var3 < ench.tagCount(); ++var3) {
               short eid = ench.getCompoundTagAt(var3).getShort("id");
               short lvl = ench.getCompoundTagAt(var3).getShort("lvl");
               if (eid == Enchantment.aquaAffinity.effectId) {
                  tmp.merge(Aspect.WATER, lvl);
               } else if (eid == Enchantment.baneOfArthropods.effectId) {
                  tmp.merge(Aspect.BEAST, lvl);
               } else if (eid == Enchantment.blastProtection.effectId) {
                  tmp.merge(Aspect.ARMOR, lvl);
               } else if (eid == Enchantment.efficiency.effectId) {
                  tmp.merge(Aspect.TOOL, lvl);
               } else if (eid == Enchantment.featherFalling.effectId) {
                  tmp.merge(Aspect.FLIGHT, lvl);
               } else if (eid == Enchantment.fireAspect.effectId) {
                  tmp.merge(Aspect.FIRE, lvl);
               } else if (eid == Enchantment.fireProtection.effectId) {
                  tmp.merge(Aspect.ARMOR, lvl);
               } else if (eid == Enchantment.flame.effectId) {
                  tmp.merge(Aspect.FIRE, lvl);
               } else if (eid == Enchantment.fortune.effectId) {
                  tmp.merge(Aspect.GREED, lvl);
               } else if (eid == Enchantment.infinity.effectId) {
                  tmp.merge(Aspect.CRAFT, lvl);
               } else if (eid == Enchantment.knockback.effectId) {
                  tmp.merge(Aspect.AIR, lvl);
               } else if (eid == Enchantment.looting.effectId) {
                  tmp.merge(Aspect.GREED, lvl);
               } else if (eid == Enchantment.power.effectId) {
                  tmp.merge(Aspect.WEAPON, lvl);
               } else if (eid == Enchantment.projectileProtection.effectId) {
                  tmp.merge(Aspect.ARMOR, lvl);
               } else if (eid == Enchantment.protection.effectId) {
                  tmp.merge(Aspect.ARMOR, lvl);
               } else if (eid == Enchantment.punch.effectId) {
                  tmp.merge(Aspect.AIR, lvl);
               } else if (eid == Enchantment.respiration.effectId) {
                  tmp.merge(Aspect.AIR, lvl);
               } else if (eid == Enchantment.sharpness.effectId) {
                  tmp.merge(Aspect.WEAPON, lvl);
               } else if (eid == Enchantment.silkTouch.effectId) {
                  tmp.merge(Aspect.EXCHANGE, lvl);
               } else if (eid == Enchantment.thorns.effectId) {
                  tmp.merge(Aspect.WEAPON, lvl);
               } else if (eid == Enchantment.smite.effectId) {
                  tmp.merge(Aspect.ENTROPY, lvl);
               } else if (eid == Enchantment.unbreaking.effectId) {
                  tmp.merge(Aspect.EARTH, lvl);
               } else if (eid == Enchantment.field_151370_z.effectId) {
                  tmp.merge(Aspect.GREED, lvl);
               } else if (eid == Enchantment.field_151369_A.effectId) {
                  tmp.merge(Aspect.BEAST, lvl);
               } else if (eid == Config.enchHaste.effectId) {
                  tmp.merge(Aspect.MOTION, lvl);
               } else if (eid == Config.enchRepair.effectId) {
                  tmp.merge(Aspect.TOOL, lvl);
               }

               var5 += lvl;
            }

            if (var5 > 0) {
               tmp.merge(Aspect.MAGIC, var5);
            }
         }
      }

      return ThaumcraftApiHelper.cullTags(tmp);
   }

   public static AspectList generateTags(Item item, int meta) {
      AspectList temp = generateTags(item, meta, new ArrayList());
      return temp;
   }

   public static AspectList generateTags(Item item, int meta, ArrayList history) {
      int tmeta = meta;

      try {
         tmeta = !(new ItemStack(item, 1, meta)).getItem().isDamageable() && (new ItemStack(item, 1, meta)).getItem().getHasSubtypes() ? meta : 32767;
      } catch (Exception ignored) {
      }

      if (ThaumcraftApi.exists(item, tmeta)) {
         return getObjectTags(new ItemStack(item, 1, tmeta));
      } else if (history.contains(Arrays.asList(item, tmeta))) {
         return null;
      } else {
         history.add(Arrays.asList(item, tmeta));
         if (history.size() < 100) {
            AspectList ret = generateTagsFromRecipes(item, tmeta == 32767 ? 0 : meta, history);
            ret = capAspects(ret, 64);
            ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, tmeta), ret);
            return ret;
         } else {
            return null;
         }
      }
   }

   private static AspectList generateTagsFromCrucibleRecipes(Item item, int meta, ArrayList history) {
      CrucibleRecipe cr = ThaumcraftApi.getCrucibleRecipe(new ItemStack(item, 1, meta));
      if (cr != null) {
         AspectList ot = cr.aspects.copy();
         int ss = cr.getRecipeOutput().stackSize;
         ItemStack cat = null;
         if (cr.catalyst instanceof ItemStack) {
            cat = (ItemStack)cr.catalyst;
         } else if (cr.catalyst instanceof ArrayList && ((ArrayList)cr.catalyst).size() > 0) {
            cat = (ItemStack)((ArrayList)cr.catalyst).get(0);
         }

         AspectList ot2 = generateTags(cat.getItem(), cat.getItemDamage());
         AspectList out = new AspectList();
         if (ot2 != null && ot2.size() > 0) {
            for(Aspect tt : ot2.getAspects()) {
               out.add(tt, ot2.getAmount(tt));
            }
         }

         for(Aspect tt : ot.getAspects()) {
            int amt = (int)(Math.sqrt(ot.getAmount(tt)) / (double)ss);
            out.add(tt, amt);
         }

         for(Aspect as : out.getAspects()) {
            if (out.getAmount(as) <= 0) {
               out.remove(as);
            }
         }

         return out;
      } else {
         return null;
      }
   }

   private static AspectList generateTagsFromArcaneRecipes(Item item, int meta, ArrayList history) {
      AspectList ret = null;
      int value = 0;
      List recipeList = ThaumcraftApi.getCraftingRecipes();

      label173:
      for (Object o : recipeList) {
          if (o instanceof IArcaneRecipe) {
              IArcaneRecipe recipe = (IArcaneRecipe) o;
              if (recipe.getRecipeOutput() != null) {
                  int idR = recipe.getRecipeOutput().getItemDamage() == 32767 ? 0 : recipe.getRecipeOutput().getItemDamage();
                  int idS = meta < 0 ? 0 : meta;
                  if (recipe.getRecipeOutput().getItem() == item && idR == idS) {
                      ArrayList<ItemStack> ingredients = new ArrayList<>();
                      new AspectList();
                      int cval = 0;

                      try {
                          if (o instanceof ShapedArcaneRecipe) {
                              int width = ((ShapedArcaneRecipe) o).width;
                              int height = ((ShapedArcaneRecipe) o).height;
                              Object[] items = ((ShapedArcaneRecipe) o).getInput();

                              for (int i = 0; i < width && i < 3; ++i) {
                                  for (int j = 0; j < height && j < 3; ++j) {
                                      if (items[i + j * width] != null) {
                                          if (items[i + j * width] instanceof ArrayList) {
                                              for (ItemStack it : (ArrayList<ItemStack>) items[i + j * width]) {
                                                  if (Utils.isEETransmutionItem(it.getItem())) {
                                                      continue label173;
                                                  }

                                                  AspectList obj = generateTags(it.getItem(), it.getItemDamage(), history);
                                                  if (obj != null && obj.size() > 0) {
                                                      ItemStack is = it.copy();
                                                      is.stackSize = 1;
                                                      ingredients.add(is);
                                                      break;
                                                  }
                                              }
                                          } else {
                                              ItemStack it = (ItemStack) items[i + j * width];
                                              if (Utils.isEETransmutionItem(it.getItem())) {
                                                  continue label173;
                                              }

                                              ItemStack is = it.copy();
                                              is.stackSize = 1;
                                              ingredients.add(is);
                                          }
                                      }
                                  }
                              }
                          } else if (o instanceof ShapelessArcaneRecipe) {
                              ArrayList items = ((ShapelessArcaneRecipe) o).getInput();

                              for (int i = 0; i < items.size() && i < 9; ++i) {
                                  if (items.get(i) != null) {
                                      if (items.get(i) instanceof ArrayList) {
                                          for (ItemStack it : (ArrayList<ItemStack>) items.get(i)) {
                                              if (Utils.isEETransmutionItem(it.getItem())) {
                                                  continue label173;
                                              }

                                              AspectList obj = generateTags(it.getItem(), it.getItemDamage(), history);
                                              if (obj != null && obj.size() > 0) {
                                                  ItemStack is = it.copy();
                                                  is.stackSize = 1;
                                                  ingredients.add(is);
                                                  break;
                                              }
                                          }
                                      } else {
                                          ItemStack it = (ItemStack) items.get(i);
                                          if (Utils.isEETransmutionItem(it.getItem())) {
                                              continue label173;
                                          }

                                          ItemStack is = it.copy();
                                          is.stackSize = 1;
                                          ingredients.add(is);
                                      }
                                  }
                              }
                          }

                          AspectList ph = getAspectsFromIngredients(ingredients, recipe.getRecipeOutput(), history);
                          if (recipe.getAspects() != null) {
                              for (Aspect a : recipe.getAspects().getAspects()) {
                                  ph.add(a, (int) (Math.sqrt(recipe.getAspects().getAmount(a)) / (double) ((float) recipe.getRecipeOutput().stackSize)));
                              }
                          }

                          for (Aspect as : ph.copy().getAspects()) {
                              if (ph.getAmount(as) <= 0) {
                                  ph.remove(as);
                              }
                          }

                          if (cval >= value) {
                              ret = ph;
                              value = cval;
                          }
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                  }
              }
          }
      }

      return ret;
   }

   private static AspectList generateTagsFromInfusionRecipes(Item item, int meta, ArrayList history) {
      InfusionRecipe cr = ThaumcraftApi.getInfusionRecipe(new ItemStack(item, 1, meta));
      if (cr == null) {
         return null;
      } else {
         AspectList ot = cr.getAspects().copy();
         ArrayList<ItemStack> ingredients = new ArrayList<>();
         ItemStack is = cr.getRecipeInput().copy();
         is.stackSize = 1;
         ingredients.add(is);

         for(ItemStack cat : cr.getComponents()) {
            ItemStack is2 = cat.copy();
            is2.stackSize = 1;
            ingredients.add(is2);
         }

         AspectList out = new AspectList();
         AspectList ot2 = getAspectsFromIngredients(ingredients, (ItemStack)cr.getRecipeOutput(), history);

         for(Aspect tt : ot2.getAspects()) {
            out.add(tt, ot2.getAmount(tt));
         }

         for(Aspect tt : ot.getAspects()) {
            int amt = (int)(Math.sqrt(ot.getAmount(tt)) / (double)((ItemStack)cr.getRecipeOutput()).stackSize);
            out.add(tt, amt);
         }

         for(Aspect as : out.getAspects()) {
            if (out.getAmount(as) <= 0) {
               out.remove(as);
            }
         }

         return out;
      }
   }

   private static AspectList generateTagsFromCraftingRecipes(Item item, int meta, ArrayList history) {
      AspectList ret = null;
      int value = Integer.MAX_VALUE;
      List recipeList = CraftingManager.getInstance().getRecipeList();

      label216:
      for (Object o : recipeList) {
          IRecipe recipe = (IRecipe) o;
          if (recipe != null && recipe.getRecipeOutput() != null && Item.getIdFromItem(recipe.getRecipeOutput().getItem()) > 0 && recipe.getRecipeOutput().getItem() != null) {
              int idR = recipe.getRecipeOutput().getItemDamage() == 32767 ? 0 : recipe.getRecipeOutput().getItemDamage();
              int idS = meta == 32767 ? 0 : meta;
              if (recipe.getRecipeOutput().getItem() == item && idR == idS) {
                  ArrayList<ItemStack> ingredients = new ArrayList<>();
                  new AspectList();
                  int cval = 0;

                  try {
                      if (o instanceof ShapedRecipes) {
                          int width = ((ShapedRecipes) o).recipeWidth;
                          int height = ((ShapedRecipes) o).recipeHeight;
                          ItemStack[] items = ((ShapedRecipes) o).recipeItems;

                          for (int i = 0; i < width && i < 3; ++i) {
                              for (int j = 0; j < height && j < 3; ++j) {
                                  if (items[i + j * width] != null) {
                                      if (Utils.isEETransmutionItem(items[i + j * width].getItem())) {
                                          continue label216;
                                      }

                                      ItemStack is = items[i + j * width].copy();
                                      is.stackSize = 1;
                                      ingredients.add(is);
                                  }
                              }
                          }
                      } else if (o instanceof ShapelessRecipes) {
                          List<ItemStack> items = ((ShapelessRecipes) o).recipeItems;

                          for (int i = 0; i < items.size() && i < 9; ++i) {
                              if (items.get(i) != null) {
                                  if (Utils.isEETransmutionItem(((ItemStack) items.get(i)).getItem())) {
                                      continue label216;
                                  }

                                  ItemStack is = ((ItemStack) items.get(i)).copy();
                                  is.stackSize = 1;
                                  ingredients.add(is);
                              }
                          }
                      } else if (o instanceof ShapedOreRecipe) {
                          int size = ((ShapedOreRecipe) o).getRecipeSize();
                          Object[] items = ((ShapedOreRecipe) o).getInput();

                          for (int i = 0; i < size && i < 9; ++i) {
                              if (items[i] != null) {
                                  if (items[i] instanceof ArrayList) {
                                      for (ItemStack it : (ArrayList<ItemStack>) items[i]) {
                                          if (Utils.isEETransmutionItem(it.getItem())) {
                                              continue label216;
                                          }

                                          AspectList obj = generateTags(it.getItem(), it.getItemDamage(), history);
                                          if (obj != null && obj.size() > 0) {
                                              ItemStack is = it.copy();
                                              is.stackSize = 1;
                                              ingredients.add(is);
                                              break;
                                          }
                                      }
                                  } else {
                                      ItemStack it = (ItemStack) items[i];
                                      if (Utils.isEETransmutionItem(it.getItem())) {
                                          continue label216;
                                      }

                                      ItemStack is = it.copy();
                                      is.stackSize = 1;
                                      ingredients.add(is);
                                  }
                              }
                          }
                      } else if (o instanceof ShapelessOreRecipe) {
                          ArrayList items = ((ShapelessOreRecipe) o).getInput();

                          for (int i = 0; i < items.size() && i < 9; ++i) {
                              if (items.get(i) != null) {
                                  if (items.get(i) instanceof ArrayList) {
                                      for (ItemStack it : (ArrayList<ItemStack>) items.get(i)) {
                                          if (Utils.isEETransmutionItem(it.getItem())) {
                                              continue label216;
                                          }

                                          AspectList obj = generateTags(it.getItem(), it.getItemDamage(), history);
                                          if (obj != null && obj.size() > 0) {
                                              ItemStack is = it.copy();
                                              is.stackSize = 1;
                                              ingredients.add(is);
                                              break;
                                          }
                                      }
                                  } else {
                                      ItemStack it = (ItemStack) items.get(i);
                                      if (Utils.isEETransmutionItem(it.getItem())) {
                                          continue label216;
                                      }

                                      ItemStack is = it.copy();
                                      is.stackSize = 1;
                                      ingredients.add(is);
                                  }
                              }
                          }
                      }

                      AspectList ph = getAspectsFromIngredients(ingredients, recipe.getRecipeOutput(), history);

                      for (Aspect as : ph.copy().getAspects()) {
                          if (ph.getAmount(as) <= 0) {
                              ph.remove(as);
                          }
                      }

                      if (ph.visSize() < value && ph.visSize() > 0) {
                          ret = ph;
                          value = ph.visSize();
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          }
      }

      return ret;
   }

   private static AspectList getAspectsFromIngredients(ArrayList<ItemStack> ingredients, ItemStack recipeOut, ArrayList<ItemStack> history) {
      AspectList out = new AspectList();
      AspectList mid = new AspectList();
      Iterator<ItemStack> i$ = ingredients.iterator();

      while(true) {
         AspectList obj;
         label57:
         while(true) {
            if (!i$.hasNext()) {
               for(Aspect as : mid.getAspects()) {
                  if (as != null) {
                     out.add(as, (int)((float)mid.getAmount(as) * 0.75F / (float)recipeOut.stackSize));
                  }
               }

               for(Aspect as : out.getAspects()) {
                  if (out.getAmount(as) <= 0) {
                     out.remove(as);
                  }
               }

               return out;
            }

            ItemStack is = i$.next();
            obj = generateTags(is.getItem(), is.getItemDamage(), history);
            if (is.getItem().getContainerItem() == null) {
               break;
            }

            if (is.getItem().getContainerItem() != is.getItem()) {
               AspectList objC = generateTags(is.getItem().getContainerItem(), 32767, history);
               Aspect[] arr$ = objC.getAspects();
               int len$ = arr$.length;
               int counter = 0;

               while(true) {
                  if (counter >= len$) {
                     break label57;
                  }

                  Aspect as = arr$[counter];
                  out.reduce(as, objC.getAmount(as));
                  ++counter;
               }
            }
         }

         if (obj != null) {
            for(Aspect as : obj.getAspects()) {
               if (as != null) {
                  mid.add(as, obj.getAmount(as));
               }
            }
         }
      }
   }

   private static AspectList generateTagsFromRecipes(Item item, int meta, ArrayList<ItemStack> history) {
      AspectList ret = null;
      int value = 0;
      ret = generateTagsFromCrucibleRecipes(item, meta, history);
      if (ret != null) {
         return ret;
      } else {
         ret = generateTagsFromArcaneRecipes(item, meta, history);
          if (ret == null) {
              ret = generateTagsFromInfusionRecipes(item, meta, history);
              if (ret == null) {
                  ret = generateTagsFromCraftingRecipes(item, meta, history);
              }
          }
          return ret;
      }
   }
}
