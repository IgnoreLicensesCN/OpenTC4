package thaumcraft.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.common.lib.research.ScanManager;

public class MappingThread implements Runnable {
   Map<String,Integer> idMappings = null;

   public MappingThread(Map<String,Integer> idMappings) {
      this.idMappings = idMappings;
   }

   public void run() {
      for(Integer id : this.idMappings.values()) {
         try {
            Item i = Item.getItemById(id);
            if (i != null) {
               List<ItemStack> q = new ArrayList<>();
               i.getSubItems(i, i.getCreativeTab(), q);
               if (!q.isEmpty()) {
                  for(ItemStack stack : q) {
                     GuiResearchRecipe.putToCache(ScanManager.generateItemHash(i, stack.getItemDamage()), stack.copy());
                  }
               }
            } else {
               Block b = Block.getBlockById(id);

               for(int a = 0; a < 16; ++a) {
                  GuiResearchRecipe.putToCache(ScanManager.generateItemHash(Item.getItemFromBlock(b), a), new ItemStack(b, 1, a));
               }
            }
         } catch (Exception ignored) {
         }
      }

   }
}
