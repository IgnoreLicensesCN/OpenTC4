package thaumcraft.common.lib.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import thaumcraft.common.lib.network.fx.PacketFXBeamPulse;
import thaumcraft.common.lib.network.fx.PacketFXBeamPulseGolemBoss;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXBlockBubble;
import thaumcraft.common.lib.network.fx.PacketFXBlockDig;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;
import thaumcraft.common.lib.network.fx.PacketFXBlockZap;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSource;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.network.fx.PacketFXVisDrain;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;
import thaumcraft.common.lib.network.fx.PacketFXZap;
import thaumcraft.common.lib.network.misc.PacketBiomeChange;
import thaumcraft.common.lib.network.misc.PacketBoreDig;
import thaumcraft.common.lib.network.misc.PacketConfig;
import thaumcraft.common.lib.network.misc.PacketFlyToServer;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
import thaumcraft.common.lib.network.misc.PacketItemKeyToServer;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.misc.PacketNote;
import thaumcraft.common.lib.network.playerdata.PacketAspectCombinationToServer;
import thaumcraft.common.lib.network.playerdata.PacketAspectDiscovery;
import thaumcraft.common.lib.network.playerdata.PacketAspectPlaceToServer;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.network.playerdata.PacketPlayerCompleteToServer;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.network.playerdata.PacketRunicCharge;
import thaumcraft.common.lib.network.playerdata.PacketScannedToServer;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedEntities;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedItems;
import thaumcraft.common.lib.network.playerdata.PacketSyncScannedPhenomena;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketSyncWipe;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;

public class PacketHandler {
   public static final SimpleNetworkWrapper INSTANCE;

   public static void init() {
      int idx = 0;
      INSTANCE.registerMessage(PacketBiomeChange.class, PacketBiomeChange.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketConfig.class, PacketConfig.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketMiscEvent.class, PacketMiscEvent.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncWipe.class, PacketSyncWipe.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncAspects.class, PacketSyncAspects.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncResearch.class, PacketSyncResearch.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncScannedItems.class, PacketSyncScannedItems.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncScannedEntities.class, PacketSyncScannedEntities.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncScannedPhenomena.class, PacketSyncScannedPhenomena.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketResearchComplete.class, PacketResearchComplete.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketAspectPool.class, PacketAspectPool.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketAspectDiscovery.class, PacketAspectDiscovery.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketScannedToServer.class, PacketScannedToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketAspectCombinationToServer.class, PacketAspectCombinationToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketPlayerCompleteToServer.class, PacketPlayerCompleteToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketAspectPlaceToServer.class, PacketAspectPlaceToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketRunicCharge.class, PacketRunicCharge.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketBoreDig.class, PacketBoreDig.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketNote.class, PacketNote.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketSyncWarp.class, PacketSyncWarp.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketWarpMessage.class, PacketWarpMessage.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketNote.class, PacketNote.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketItemKeyToServer.class, PacketItemKeyToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketFocusChangeToServer.class, PacketFocusChangeToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketFlyToServer.class, PacketFlyToServer.class, idx++, Side.SERVER);
      INSTANCE.registerMessage(PacketFXBlockBubble.class, PacketFXBlockBubble.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockDig.class, PacketFXBlockDig.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockSparkle.class, PacketFXBlockSparkle.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockArc.class, PacketFXBlockArc.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBlockZap.class, PacketFXBlockZap.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXEssentiaSource.class, PacketFXEssentiaSource.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXInfusionSource.class, PacketFXInfusionSource.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXShield.class, PacketFXShield.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXSonic.class, PacketFXSonic.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXWispZap.class, PacketFXWispZap.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXZap.class, PacketFXZap.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXVisDrain.class, PacketFXVisDrain.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBeamPulse.class, PacketFXBeamPulse.class, idx++, Side.CLIENT);
      INSTANCE.registerMessage(PacketFXBeamPulseGolemBoss.class, PacketFXBeamPulseGolemBoss.class, idx++, Side.CLIENT);
   }

   static {
      INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Thaumcraft".toLowerCase());
   }
}
