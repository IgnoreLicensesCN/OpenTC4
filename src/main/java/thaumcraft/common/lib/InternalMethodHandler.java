package thaumcraft.common.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.internal.IInternalMethodHandler;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.tiles.TileMagicWorkbench;

public class InternalMethodHandler implements IInternalMethodHandler {
   public void generateVisEffect(int dim, int x, int y, int z, int x2, int y2, int z2, int color) {
      Utils.generateVisEffect(dim, x, y, z, x2, y2, z2, color);
   }

   public boolean isResearchComplete(String username, String researchkey) {
      return ResearchManager.isResearchComplete(username, researchkey);
   }

   public boolean hasDiscoveredAspect(String username, Aspect aspect) {
      return Thaumcraft.proxy.getPlayerKnowledge().hasDiscoveredAspect(username, aspect);
   }

   public AspectList getDiscoveredAspects(String username) {
      return Thaumcraft.proxy.getPlayerKnowledge().getAspectsDiscovered(username);
   }

   public ItemStack getStackInRowAndColumn(Object instance, int row, int column) {
      return ((TileMagicWorkbench)instance).getStackInRowAndColumn(row, column);
   }

   public AspectList getObjectAspects(ItemStack is) {
      return ThaumcraftCraftingManager.getObjectTags(is);
   }

   public AspectList getBonusObjectTags(ItemStack is, AspectList ot) {
      return ThaumcraftCraftingManager.getBonusTags(is, ot);
   }

   public AspectList generateTags(Item item, int meta) {
      return ThaumcraftCraftingManager.generateTags(item, meta);
   }

   public boolean consumeVisFromWand(ItemStack wand, EntityPlayer player, AspectList cost, boolean doit, boolean crafting) {
      return wand.getItem() instanceof ItemWandCasting ? ((ItemWandCasting)wand.getItem()).consumeAllVis(wand, player, cost, doit, crafting) : false;
   }

   public boolean consumeVisFromWandCrafting(ItemStack wand, EntityPlayer player, AspectList cost, boolean doit) {
      return wand.getItem() instanceof ItemWandCasting ? ((ItemWandCasting)wand.getItem()).consumeAllVisCrafting(wand, player, cost, doit) : false;
   }

   public boolean consumeVisFromInventory(EntityPlayer player, AspectList cost) {
      return WandManager.consumeVisFromInventory(player, cost);
   }

   public void addWarpToPlayer(EntityPlayer player, int amount, boolean temporary) {
      Thaumcraft.addWarpToPlayer(player, amount, temporary);
   }

   public void addStickyWarpToPlayer(EntityPlayer player, int amount) {
      Thaumcraft.addStickyWarpToPlayer(player, amount);
   }
}
