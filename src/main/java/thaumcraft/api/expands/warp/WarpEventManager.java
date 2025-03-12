package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerAfter;
import thaumcraft.api.expands.warp.listeners.PickWarpEventListenerBefore;
import thaumcraft.api.expands.warp.listeners.WarpEventListenerAfter;
import thaumcraft.api.expands.warp.listeners.WarpEventListenerBefore;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;
import thaumcraft.common.lib.research.PlayerKnowledge;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static thaumcraft.common.lib.WarpEvents.*;

@ParametersAreNonnullByDefault
public class WarpEventManager {
    public static final List<WarpEvent> warpEvents = Collections.synchronizedList(new ArrayList<WarpEvent>(){
        @Override
        public boolean add(WarpEvent o) {
            boolean result = super.add(o);
            Collections.sort(this);
            return result;
        }
    });

    public static final List<WarpEventListenerBefore> warpEventListenersBefore = Collections.synchronizedList(
            new ArrayList<WarpEventListenerBefore>(){
        @Override
        public boolean add(WarpEventListenerBefore o) {
            boolean result = super.add(o);
            Collections.sort(this);
            return result;
        }
    });
    public static final List<WarpEventListenerAfter> warpEventListenersAfter = Collections.synchronizedList(
            new ArrayList<WarpEventListenerAfter>(){
                @Override
                public boolean add(WarpEventListenerAfter o) {
                    boolean result = super.add(o);
                    Collections.sort(this);
                    return result;
                }
            });

    public static final List<PickWarpEventListenerBefore> pickWarpEventListenersBefore = Collections.synchronizedList(
            new ArrayList<PickWarpEventListenerBefore>(){
                @Override
                public boolean add(PickWarpEventListenerBefore o) {
                    boolean result = super.add(o);
                    Collections.sort(this);
                    return result;
                }
            });
    public static final List<PickWarpEventListenerAfter> pickWarpEventListenersAfter = Collections.synchronizedList(
            new ArrayList<PickWarpEventListenerAfter>(){
                @Override
                public boolean add(PickWarpEventListenerAfter o) {
                    boolean result = super.add(o);
                    Collections.sort(this);
                    return result;
                }
            });
    public static final List<WarpCondition> warpConditions = Collections.synchronizedList(new ArrayList<WarpCondition>(){
        @Override
        public boolean add(WarpCondition o) {
            boolean result = super.add(o);
            Collections.sort(this);
            return result;
        }
    });
    public static final WarpEvent eventSpawnMist = new WarpEvent(4,96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, EntityPlayer player) {
            spawnMist(player, context.warp, context.warp / 15);
        }
    };
    public static void init(){
        pickWarpEventListenersBefore.add(new PickWarpEventListenerBefore(0) {
            @Override
            public void beforePickEvent(PickWarpEventContext e, EntityPlayer player) {
                ItemStack helm = player.inventory.armorInventory[3];
                if (helm != null
                        && helm.getItem() instanceof ItemFortressArmor
                        && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask")
                        && helm.stackTagCompound.getInteger("mask") == 0) {
                    e.warp -=  2 + player.worldObj.rand.nextInt(4);
                }
            }
        });
        registerWarpEvent(new WarpEvent(4,4) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext,EntityPlayer player) {
                grantResearch(player, 1);
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
            }
        });
        registerWarpEvent(new WarpEvent(4,8) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext,EntityPlayer player) {
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.11")));
            }
        });
        registerWarpEvent(new WarpEvent(4,16) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext,EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionVisExhaustID, 5000, Math.min(3, warpContext.warp / 15), true);
                pe.getCurativeItems().clear();

                try {
                    player.addPotionEffect(pe);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
            }
        });
        registerWarpEvent(new WarpEvent(4,20) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionThaumarhiaID, Math.min(32000, 10 * warpContext.warp), 0, true);
                pe.getCurativeItems().clear();

                try {
                    player.addPotionEffect(pe);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.15")));
            }
        });
        registerWarpEvent(new WarpEvent(4,24) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 5000, Math.min(3, warpContext.warp / 15), true);
                pe.getCurativeItems().clear();
                pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
                pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

                try {
                    player.addPotionEffect(pe);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
            }
        });
        registerWarpEvent(new WarpEvent(4,28) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.12")));
            }
        });
        registerWarpEvent(new WarpEvent(4,32) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                spawnMist(player, warpContext.warp, 1);
            }
        });
        registerWarpEvent(new WarpEvent(4,36) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                try {
                    player.addPotionEffect(new PotionEffect(Config.potionBlurredID, Math.min(32000, 10 * warpContext.warp), 0, true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        registerWarpEvent(new WarpEvent(4,40) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionSunScornedID, 5000, Math.min(3, warpContext.warp / 15), true);
                pe.getCurativeItems().clear();

                try {
                    player.addPotionEffect(pe);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.5")));
            }
        });
        registerWarpEvent(new WarpEvent(4,44) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {

                try {
                    player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1200, Math.min(3, warpContext.warp / 15), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.9")));
            }
        });
        registerWarpEvent(new WarpEvent(4,48) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionInfVisExhaustID, 6000, Math.min(3, warpContext.warp / 15), false);
                pe.getCurativeItems().clear();

                try {
                    player.addPotionEffect(pe);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
            }
        });
        registerWarpEvent(new WarpEvent(4,52) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, Math.min(40 * warpContext.warp, 6000), 0, true));
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.10")));
            }
        });
        registerWarpEvent(new WarpEvent(4,56) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionDeathGazeID, 6000, Math.min(3, warpContext.warp / 15), true);
                pe.getCurativeItems().clear();

                try {
                    player.addPotionEffect(pe);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.4")));
            }
        });
        registerWarpEvent(new WarpEvent(4,60) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                suddenlySpiders(player, warpContext.warp, false);
            }
        });
        registerWarpEvent(new WarpEvent(4,64) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.13")));
            }
        });
        registerWarpEvent(new WarpEvent(4,68) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                spawnMist(player, warpContext.warp, warpContext.warp / 30);
            }
        });
        registerWarpEvent(new WarpEvent(4,72) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                try {
                    player.addPotionEffect(new PotionEffect(Potion.blindness.id, Math.min(32000, 5 * warpContext.warp), 0, true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        registerWarpEvent(new WarpEvent(1,76) {//anazor may get something wrong.
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {

                if (Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName()) > 0) {
                    Thaumcraft.proxy.getPlayerKnowledge().addWarpSticky(player.getCommandSenderName(), -1);
                    if (player instanceof EntityPlayerMP) {
                        PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 1), (EntityPlayerMP) player);
                        PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte) 1, -1), (EntityPlayerMP) player);
                    }
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.14")));
            }
        });
        registerWarpEvent(new WarpEvent(4,80) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 6000, Math.min(3, warpContext.warp / 15), true);
                pe.getCurativeItems().clear();
                pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
                pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

                try {
                    player.addPotionEffect(pe);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
            }
        });
        registerWarpEvent(new WarpEvent(4,84) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                grantResearch(player, warpContext.warp / 10);
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
            }
        });
        registerWarpEvent(new WarpEvent(4,92) {
            @Override
            public void onEventTriggered(PickWarpEventContext warpContext, EntityPlayer player) {
                suddenlySpiders(player, warpContext.warp, true);
            }
        });
        registerPickWarpEventListenerBefore(new PickWarpEventListenerBefore(0) {
            @Override
            public void beforePickEvent(PickWarpEventContext e, EntityPlayer player) {
                e.warp = Math.min(100, (e.warp + e.warp + e.warpCounter) / 3);
                e.warpCounter = (int)((double)e.warpCounter - Math.max(5.0F, Math.sqrt(e.warpCounter) * (double)2.0F));
                Thaumcraft.proxy.getPlayerKnowledge().setWarpCounter(player.getCommandSenderName(), e.warpCounter);
            }
        });
        registerPickWarpEventListenerAfter(new PickWarpEventListenerAfter(0) {
            @Nonnull
            @Override
            public WarpEvent afterPickEvent(PickWarpEventContext context, WarpEvent e, EntityPlayer player) {
                if (context.warp >= 92 && e == WarpEvent.EMPTY) {
                    return eventSpawnMist;
                }
                return e;
            }
        });
        registerWarpEventListenerAfter(new WarpEventListenerAfter(0) {
            @Override
            public void onWarpEvent(PickWarpEventContext warpContext, WarpEvent e,  EntityPlayer player) {
                if (warpContext.actualWarp > 10 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "BATHSALTS") && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "@BATHSALTS")) {
                    player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.8")));
                    if (player instanceof EntityPlayerMP) {
                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("@BATHSALTS"), (EntityPlayerMP) player);
                    }
                    Thaumcraft.proxy.getResearchManager().completeResearch(player, "@BATHSALTS");
                }

                if (warpContext.actualWarp > 25 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMINOR")) {
                    grantResearch(player, 10);
                    if (player instanceof EntityPlayerMP) {
                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMINOR"), (EntityPlayerMP) player);
                    }
                    Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMINOR");
                }

                if (warpContext.actualWarp > 50 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMAJOR")) {
                    grantResearch(player, 20);
                    if (player instanceof EntityPlayerMP) {
                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMAJOR"), (EntityPlayerMP) player);
                    }
                    Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMAJOR");
                }
            }
        });
        registerWarpEventListenerAfter(new WarpEventListenerAfter(0) {
            @Override
            public void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull EntityPlayer player) {
                Thaumcraft.proxy.getPlayerKnowledge().addWarpTemp(player.getCommandSenderName(), -1);
            }
        });
        registerWarpEventListenerAfter(new WarpEventListenerAfter(0) {
            @Override
            public void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull EntityPlayer player) {
                if (e == WarpEvent.EMPTY){
                    e.sendMiscPacket = false;
                }
            }
        });
        registerWarpCondition(new WarpCondition(0) {
            @Override
            public boolean check(PickWarpEventContext context, EntityPlayer player) {
                if (player.worldObj == null) {
                    return false;
                }
                return context.warpCounter > 0 && context.warp > 0 && (double)player.worldObj.rand.nextInt(100)
                        <= Math.sqrt(context.warpCounter);
            }
        });
        registerWarpCondition(new WarpCondition(0) {
            @Override
            public boolean check(PickWarpEventContext context, EntityPlayer player) {
                return !player.isPotionActive(Config.potionWarpWardID);
            }
        });

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
    public static void registerWarpCondition(WarpCondition o) {
        warpConditions.add(o);
    }
    public static void unregisterWarpCondition(WarpCondition o) {
        warpConditions.remove(o);
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
        for (WarpCondition condition : warpConditions) {
            if (!condition.check(warpContext,player)) {
                return;
            }
        }
        triggerRandomWarpEvent(warpContext,player);
    }
}
