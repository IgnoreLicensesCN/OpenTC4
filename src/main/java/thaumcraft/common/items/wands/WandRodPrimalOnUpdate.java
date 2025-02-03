package thaumcraft.common.items.wands;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;

public class WandRodPrimalOnUpdate implements IWandRodOnUpdate {
   Aspect aspect;
   ArrayList<Aspect> primals;

   public WandRodPrimalOnUpdate(Aspect aspect) {
      this.aspect = aspect;
   }

   public WandRodPrimalOnUpdate() {
      this.aspect = null;
      this.primals = Aspect.getPrimalAspects();
   }

   public void onUpdate(ItemStack itemstack, EntityPlayer player) {
      if (this.aspect != null) {
         if (player.ticksExisted % 200 == 0 && ((ItemWandCasting)itemstack.getItem()).getVis(itemstack, this.aspect) < ((ItemWandCasting)itemstack.getItem()).getMaxVis(itemstack) / 10) {
            ((ItemWandCasting)itemstack.getItem()).addVis(itemstack, this.aspect, 1, true);
         }
      } else if (player.ticksExisted % 50 == 0) {
         ArrayList<Aspect> q = new ArrayList<>();

         for(Aspect as : this.primals) {
            if (((ItemWandCasting)itemstack.getItem()).getVis(itemstack, as) < ((ItemWandCasting)itemstack.getItem()).getMaxVis(itemstack) / 10) {
               q.add(as);
            }
         }

         if (!q.isEmpty()) {
            ((ItemWandCasting)itemstack.getItem()).addVis(itemstack, q.get(player.worldObj.rand.nextInt(q.size())), 1, true);
         }
      }

   }
}
