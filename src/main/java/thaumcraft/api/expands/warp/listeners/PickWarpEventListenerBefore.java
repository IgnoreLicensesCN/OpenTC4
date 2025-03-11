package thaumcraft.api.expands.warp.listeners;

import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.api.expands.warp.PickWarpEventContext;

import javax.annotation.Nonnull;

public abstract class PickWarpEventListenerBefore implements Comparable<PickWarpEventListenerBefore> {

    public final int priority;

    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    public PickWarpEventListenerBefore(int priority) {
        this.priority = priority;
    }

    /**
     *
     * @param e the context stores warp to calculate which event to pickup.
     * @param player victim
     */
    public abstract void beforePickEvent(PickWarpEventContext e, EntityPlayer player);

    @Override
    public int compareTo(@Nonnull PickWarpEventListenerBefore o) {
        return Integer.compare(priority, o.priority);
    }
}
