package thaumcraft.common.tiles;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.world.dim.TeleporterThaumcraft;

public class TileEldritchPortal extends TileEntity {
   public int opencount = -1;
   private int count = 0;

   public boolean canUpdate() {
      return true;
   }

   public double getMaxRenderDistanceSquared() {
      return (double)9216.0F;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox((double)(this.xCoord - 1), (double)(this.yCoord - 1), (double)(this.zCoord - 1), (double)(this.xCoord + 2), (double)(this.yCoord + 2), (double)(this.zCoord + 2));
   }

   public void updateEntity() {
      ++this.count;
      if (this.worldObj.isRemote && (this.count % 250 == 0 || this.count == 0)) {
         this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:evilportal", 1.0F, 1.0F, false);
      }

      if (this.worldObj.isRemote && this.opencount < 30) {
         ++this.opencount;
      }

      if (!this.worldObj.isRemote && this.count % 5 == 0) {
         List ents = this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getBoundingBox((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand((double)0.5F, (double)1.0F, (double)0.5F));
         if (ents.size() > 0) {
            for(Object e : ents) {
               EntityPlayerMP player = (EntityPlayerMP)e;
               if (player.ridingEntity == null && player.riddenByEntity == null) {
                  MinecraftServer mServer = FMLCommonHandler.instance().getMinecraftServerInstance();
                  if (player.timeUntilPortal > 0) {
                     player.timeUntilPortal = 100;
                  } else if (player.dimension != Config.dimensionOuterId) {
                     player.timeUntilPortal = 100;
                     player.mcServer.getConfigurationManager().transferPlayerToDimension(player, Config.dimensionOuterId, new TeleporterThaumcraft(mServer.worldServerForDimension(Config.dimensionOuterId)));
                     if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ENTEROUTER")) {
                        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ENTEROUTER"), player);
                        Thaumcraft.proxy.getResearchManager().completeResearch(player, "ENTEROUTER");
                     }
                  } else {
                     player.timeUntilPortal = 100;
                     player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterThaumcraft(mServer.worldServerForDimension(0)));
                  }
               }
            }
         }
      }

   }
}
