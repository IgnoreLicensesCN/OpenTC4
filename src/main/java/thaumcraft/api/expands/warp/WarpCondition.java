package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.EntityPlayer;

public abstract class WarpCondition implements Comparable<WarpCondition> {
    public WarpCondition(int priority) {
        this.priority = priority;
    }

    public int priority;

    /**
     *
     * @param context
     * @param player
     * @return true if can trigger wrap event
     */
    public abstract boolean check(PickWarpEventContext context, EntityPlayer player);

    @Override
    public int compareTo(WarpCondition o) {
        return Integer.compare(priority, o.priority);
    }
}
