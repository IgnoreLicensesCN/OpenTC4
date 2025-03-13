package thaumcraft.api.expands.wandconsumption;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import simpleutils.AutoSortSynchronizedList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.listeners.CalculateWandConsumptionListener;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.List;

import static thaumcraft.api.expands.wandconsumption.consts.CalculateWandConsumptionListeners.*;

public class ConsumptionModifierCalculator {
    public static final List<CalculateWandConsumptionListener> calculateWandConsumptionListeners = new AutoSortSynchronizedList<>();


    public static void registerCalculateWandConsumptionListener(CalculateWandConsumptionListener listener) {
        calculateWandConsumptionListeners.add(listener);
    }
    public static void unregisterCalculateWandConsumptionListener(CalculateWandConsumptionListener listener) {
        calculateWandConsumptionListeners.remove(listener);
    }
    public static void init(){
        registerCalculateWandConsumptionListener(CASTING_MODIFIER);
        registerCalculateWandConsumptionListener(PLAYER_DISCOUNT);
        registerCalculateWandConsumptionListener(FOCUS_DISCOUNT);
        registerCalculateWandConsumptionListener(SCEPTRE);
        registerCalculateWandConsumptionListener(SET_MIN);
    }

    /**
     * {@link CalculateWandConsumptionListener#onCalculation(ItemWandCasting, ItemStack, EntityPlayer, Aspect, boolean, float)}
     */
    public static float getConsumptionModifier(ItemWandCasting casting, ItemStack is, EntityPlayer player, Aspect aspect, boolean crafting) {
        float consumptionModifier = 1.0F;
        for (CalculateWandConsumptionListener listener : calculateWandConsumptionListeners) {
            consumptionModifier = listener.onCalculation(casting,is,player,aspect,crafting,consumptionModifier);
        }
        return consumptionModifier;
    }
}
