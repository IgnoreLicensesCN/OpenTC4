package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public class PickWarpEventContext {
    public PickWarpEventContext(int warp,
                                @Nullable WarpEvent e,
                                @Nullable EntityPlayer player,
                                int actualWarp,
                                int warpCounter
    ) {
        this.warp = warp;
        this.e = e;
        this.player = player;
        this.actualWarp = actualWarp;
        this.warpCounter = warpCounter;
    }
    public PickWarpEventContext() {}

    public int warp = 0;
    public WarpEvent e = null;
    public EntityPlayer player = null;
    public int actualWarp = 0;
    public int warpCounter = 0;
    public int randWithWarp = Integer.MIN_VALUE;
}
