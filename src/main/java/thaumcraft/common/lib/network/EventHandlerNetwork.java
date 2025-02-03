package thaumcraft.common.lib.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.misc.PacketConfig;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedEntities;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedPhenomena;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketSyncWipe;

public class EventHandlerNetwork {
   @SubscribeEvent
   public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
      Side side = FMLCommonHandler.instance().getEffectiveSide();
      if (side == Side.SERVER) {
         EntityPlayer p = event.player;
         PacketHandler.INSTANCE.sendTo(new PacketSyncWipe(), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedItems(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedEntities(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncScannedPhenomena(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(p), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)0), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)1), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(p, (byte)2), (EntityPlayerMP)p);
         PacketHandler.INSTANCE.sendTo(new PacketConfig(), (EntityPlayerMP)p);
      }

   }

   @SubscribeEvent
   public void clientLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
      if (Thaumcraft.proxy.getClientWorld() != null && Minecraft.getMinecraft().thePlayer != null) {
         GuiResearchBrowser.completedResearch.put(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), new ArrayList());
         Thaumcraft.log.info("Resetting research to defaults.");
      }

   }

   @SubscribeEvent
   public void clientLogsOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
      if (Thaumcraft.proxy.getClientWorld() != null) {
         Config.allowCheatSheet = Config.CallowCheatSheet;
         Config.wardedStone = Config.CwardedStone;
         Config.allowMirrors = Config.CallowMirrors;
         Config.hardNode = Config.ChardNode;
         Config.wuss = Config.Cwuss;
         Config.researchDifficulty = Config.CresearchDifficulty;
         Config.aspectTotalCap = Config.CaspectTotalCap;
         Thaumcraft.log.info("Restoring client configs.");
      }

   }
}
