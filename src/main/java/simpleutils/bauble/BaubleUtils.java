package simpleutils.bauble;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

import static baubles.api.expanded.BaubleExpandedSlots.getCurrentlyRegisteredTypes;
import static baubles.api.expanded.BaubleExpandedSlots.getIndexesOfAssignedSlotsOfType;

@ParametersAreNonnullByDefault
public class BaubleUtils {

    public static boolean forEachBauble(EntityPlayer player,BaubleConsumer<Item> operation) {
        for(String baubleType:getCurrentlyRegisteredTypes()) {
            if (forEachBaubleWithBaubleType(baubleType,player,operation)){
                return true;
            }
        }
        return false;
    }

    public static <T> boolean forEachBauble(EntityPlayer player,Class<T> expectedItemType, BaubleConsumer<T> operation) {
        for(String baubleType:getCurrentlyRegisteredTypes()) {
            if (forEachBaubleWithBaubleType(baubleType,player,expectedItemType,operation)){
                return true;
            }
        }
        return false;
    }
    public static boolean forEachBaubleWithBaubleType(String baubleType, EntityPlayer player, BaubleConsumer<Item> operation) {
        IInventory baubles = BaublesApi.getBaubles(player);

        for (int a:getIndexesOfAssignedSlotsOfType(baubleType)){
            ItemStack stack = baubles.getStackInSlot(a);
            if (stack == null) {continue;}
            if (operation.accept(a,stack, stack.getItem())){
                return true;
            }
        }
        return false;
    }
    public static <T> boolean forEachBaubleWithBaubleType(String baubleType,EntityPlayer player,Class<T> expectedItemType, BaubleConsumer<T> operation) {
        IInventory baubles = BaublesApi.getBaubles(player);

        for (int a:getIndexesOfAssignedSlotsOfType(baubleType)){
            ItemStack stack = baubles.getStackInSlot(a);
            if (stack == null) {continue;}
            if (expectedItemType.isAssignableFrom(stack.getClass())) {
                if (operation.accept(a,stack, (T) stack.getItem())){
                    return true;
                }
            }
        }
        return false;
    }
}
