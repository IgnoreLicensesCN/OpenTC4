package simpleutils.bauble;

import net.minecraft.item.ItemStack;

public interface BaubleConsumer<T> {
    /**
     * @param stack stack in slot,skip if null.
     * @param item item of the stack
     * @return break flag
     */
    boolean accept(int slot,ItemStack stack,T item);
}
