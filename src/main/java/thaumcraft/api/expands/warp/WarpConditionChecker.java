package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.EntityPlayer;

public abstract class WarpConditionChecker implements Comparable<WarpConditionChecker> {
    public WarpConditionChecker(int priority) {
        this.priority = priority;
    }

    public final int priority;

    /**
     *
     * @param context
     * @param player
     * @return true if can trigger wrap event
     */
    public abstract boolean check(PickWarpEventContext context, EntityPlayer player);

    @Override
    public int compareTo(WarpConditionChecker o) {
        return Integer.compare(priority, o.priority);
    }
}
