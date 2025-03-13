package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import simpleutils.AutoSortSynchronizedList;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerAfter;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.api.expands.warp.listeners.WarpEventListenerAfter;
import thaumcraft.api.expands.warp.listeners.WarpEventListenerBefore;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.research.PlayerKnowledge;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static thaumcraft.api.expands.warp.consts.AfterPickEventListeners.SPAWN_GUARD_IF_NO_EVENT;
import static thaumcraft.api.expands.warp.consts.AfterWarpEventListeners.*;
import static thaumcraft.api.expands.warp.consts.BeforePickEventListeners.CALCULATE_WARP_AND_COUNTER;
import static thaumcraft.api.expands.warp.consts.BeforePickEventListeners.THAUMIC_FORTRESS_MASK_DISCOUNT;
import static thaumcraft.api.expands.warp.consts.WarpConditions.NO_WARP_WARD;
import static thaumcraft.api.expands.warp.consts.WarpConditions.WARP_AND_COUNTER;
import static thaumcraft.api.expands.warp.consts.WarpEvents.*;
import static thaumcraft.common.lib.WarpEvents.*;

@ParametersAreNonnullByDefault
public class WarpEventManager {
    public static final List<WarpEvent> warpEvents = new AutoSortSynchronizedList<>();
    /**
     * I may update those register methods,so please don't access these lists directly.
     */
    public static final List<WarpEventListenerBefore> warpEventListenersBefore = new AutoSortSynchronizedList<>();
    public static final List<WarpEventListenerAfter> warpEventListenersAfter = new AutoSortSynchronizedList<>();
    public static final List<PickWarpEventListenerBefore> pickWarpEventListenersBefore = new AutoSortSynchronizedList<>();
    public static final List<PickWarpEventListenerAfter> pickWarpEventListenersAfter = new AutoSortSynchronizedList<>();
    public static final List<WarpConditionChecker> WARP_CONDITION_CHECKERS = new AutoSortSynchronizedList<>();
    public static void init(){
        registerWarpEvent(GRANT_RESEARCH_LOW);
        registerWarpEvent(NOISE_AND_FOLLOWING);
        registerWarpEvent(VIS_EXHAUST);
        registerWarpEvent(THAUMARHIA);
        registerWarpEvent(STRANGE_HUNGER);
        registerWarpEvent(FOLLOWING);
        registerWarpEvent(SPAWN_A_GUARD);
        registerWarpEvent(BLURRED);
        registerWarpEvent(SUN_SCORNED);
        registerWarpEvent(SLOW_DIGGING);
        registerWarpEvent(INF_VIS_EXHAUST);
        registerWarpEvent(NIGHT_VISION);
        registerWarpEvent(DEATH_GAZE);
        registerWarpEvent(FAKE_SPIDERS);
        registerWarpEvent(BEING_WATCHED);
        registerWarpEvent(SPAWN_SOME_GUARDS);
        registerWarpEvent(BLINDNESS);
        registerWarpEvent(DECREASE_A_STICKY_WARP);
        registerWarpEvent(STRANGE_HUNGER_2);
        registerWarpEvent(GRANT_RESEARCH_HIGH);
        registerWarpEvent(REAL_SPIDERS);
        registerPickWarpEventListenerBefore(THAUMIC_FORTRESS_MASK_DISCOUNT);
        registerPickWarpEventListenerBefore(CALCULATE_WARP_AND_COUNTER);
        registerPickWarpEventListenerAfter(SPAWN_GUARD_IF_NO_EVENT);
        
        registerWarpEventListenerAfter(CHECK_RESEARCH);
        registerWarpEventListenerAfter(DECREASE_A_TEMP_WARP);
        registerWarpEventListenerAfter(DONT_SEND_MISC_FOR_EMPTY);
        
        registerWarpConditionChecker(WARP_AND_COUNTER);
        registerWarpConditionChecker(NO_WARP_WARD);

    }
    public static void registerWarpEvent(WarpEvent e) {
        warpEvents.add(e);
    }
    public static void unregisterWarpEvent(WarpEvent e) {
        warpEvents.remove(e);
    }
    public static void registerWarpEventListenerBefore( WarpEventListenerBefore o) {
        warpEventListenersBefore.add(o);
    }
    public static void unregisterWarpEventListenerBefore( WarpEventListenerBefore o) {
        warpEventListenersBefore.remove(o);
    }
    public static void registerWarpEventListenerAfter( WarpEventListenerAfter o) {
        warpEventListenersAfter.add(o);
    }
    public static void unregisterWarpEventListenerAfter( WarpEventListenerAfter o) {
        warpEventListenersAfter.remove(o);
    }
    public static void registerPickWarpEventListenerBefore( PickWarpEventListenerBefore o) {
        pickWarpEventListenersBefore.add(o);
    }
    public static void unregisterPickWarpEventListenerBefore( PickWarpEventListenerBefore o) {
        pickWarpEventListenersBefore.remove(o);
    }
    public static void registerPickWarpEventListenerAfter( PickWarpEventListenerAfter o) {
        pickWarpEventListenersAfter.add(o);
    }
    public static void unregisterPickWarpEventListenerAfter( PickWarpEventListenerAfter o) {
        pickWarpEventListenersAfter.remove(o);
    }
    public static void registerWarpConditionChecker(WarpConditionChecker o) {
        WARP_CONDITION_CHECKERS.add(o);
    }
    public static void unregisterWarpConditionChecker(WarpConditionChecker o) {
        WARP_CONDITION_CHECKERS.remove(o);
    }

    public static  WarpEvent pickWarpEventWithListener(PickWarpEventContext warpContext,EntityPlayer player) {
        for (PickWarpEventListenerBefore listener : pickWarpEventListenersBefore) {
            listener.beforePickEvent(warpContext, player);
        }
        if (warpContext.warp <= 0 || warpContext.actualWarp <= 0) {
            return WarpEvent.EMPTY;
        }
        warpContext.randWithWarp = player.worldObj.rand.nextInt(warpContext.warp);
        WarpEvent picked = WarpEvent.EMPTY;

        for (WarpEvent pickEvent : warpEvents) {
            if (warpContext.actualWarp >= pickEvent.warpRequired) {
                if (warpContext.randWithWarp >= pickEvent.weight) {
                    warpContext.randWithWarp -= pickEvent.weight;
                } else {
                    picked = pickEvent;
                    break;
                }
            } else {
                break;
            }
        }

        for (PickWarpEventListenerAfter listener : pickWarpEventListenersAfter) {
            picked = listener.afterPickEvent(warpContext,picked,player);
        }
        return picked;
    }

    public static void triggerRandomWarpEvent(PickWarpEventContext warpContext, EntityPlayer player) {
        triggerWarpEvent(warpContext, pickWarpEventWithListener(warpContext,player),player);
    }

    public static void triggerWarpEvent(PickWarpEventContext warpContext,WarpEvent e,EntityPlayer player) {
        e.enable();
        for (WarpEventListenerBefore listener : warpEventListenersBefore) {
            listener.onWarpEvent(warpContext,e,player);
        }
        if (e.enabledFlag) {
            e.onEventTriggered(warpContext,player);
            if (e.retryAnotherFlag){
                triggerRandomWarpEvent(warpContext,player);
                return;
            }
            for (WarpEventListenerAfter listener : warpEventListenersAfter) {
                listener.onWarpEvent(warpContext,e,player);
            }
            if (e.sendMiscPacket){
                if (player instanceof EntityPlayerMP) {
                    PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((short)0), (EntityPlayerMP)player);
                }
            }
            if (player instanceof EntityPlayerMP) {
                PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), (EntityPlayerMP)player);
            }
        }
    }

    public static void tryTriggerRandomWarpEvent(EntityPlayer player) {
        PlayerKnowledge knowledge = Thaumcraft.proxy.getPlayerKnowledge();
        PickWarpEventContext warpContext = new PickWarpEventContext(
                knowledge.getWarpTotal(player.getCommandSenderName())
                        + getWarpFromGear(player),
                null,
                player,
                knowledge.getWarpPerm(player.getCommandSenderName())
                        + knowledge.getWarpSticky(player.getCommandSenderName()),
                knowledge.getWarpCounter(player.getCommandSenderName())
        );
        for (WarpConditionChecker condition : WARP_CONDITION_CHECKERS) {
            if (!condition.check(warpContext,player)) {
                return;
            }
        }
        triggerRandomWarpEvent(warpContext,player);
    }
}
