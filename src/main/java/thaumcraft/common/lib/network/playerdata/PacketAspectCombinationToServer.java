package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.tiles.TileResearchTable;

public class PacketAspectCombinationToServer implements IMessage, IMessageHandler<PacketAspectCombinationToServer,IMessage> {
   private int dim;
   private int playerid;
   private int x;
   private int y;
   private int z;
   Aspect aspect1;
   Aspect aspect2;
   boolean ab1;
   boolean ab2;

   public PacketAspectCombinationToServer() {
   }

   public PacketAspectCombinationToServer(EntityPlayer player, int x, int y, int z, Aspect aspect1, Aspect aspect2, boolean ab1, boolean ab2, boolean ret) {
      this.dim = player.worldObj.provider.dimensionId;
      this.playerid = player.getEntityId();
      this.x = x;
      this.y = y;
      this.z = z;
      this.aspect1 = aspect1;
      this.aspect2 = aspect2;
      this.ab1 = ab1;
      this.ab2 = ab2;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.dim);
      buffer.writeInt(this.playerid);
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      ByteBufUtils.writeUTF8String(buffer, this.aspect1.getTag());
      ByteBufUtils.writeUTF8String(buffer, this.aspect2.getTag());
      buffer.writeBoolean(this.ab1);
      buffer.writeBoolean(this.ab2);
   }

   public void fromBytes(ByteBuf buffer) {
      this.dim = buffer.readInt();
      this.playerid = buffer.readInt();
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.aspect1 = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
      this.aspect2 = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
      this.ab1 = buffer.readBoolean();
      this.ab2 = buffer.readBoolean();
   }

   private static boolean sanityCheckAspectCombination0(PacketAspectCombinationToServer packet) {
      TileResearchTable table = packet.table();
      if (table == null) return false;
      EntityPlayerMP player = packet.player();
      if (player == null) return false;
      return hasAspect(table, player, packet.lhs()) && hasAspect(table, player, packet.rhs());
   }
   private static boolean hasAspect(TileResearchTable table, EntityPlayerMP player, Aspect aspect) {
      return hasAspect(player, aspect, 0) || table.bonusAspects.getAmount(aspect) > 0;
   }

   private static boolean hasAspect(EntityPlayerMP player, Aspect aspect, int threshold) {
      return Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect) > threshold;
   }

   public IMessage onMessage(PacketAspectCombinationToServer message, MessageContext ctx) {
      if (!sanityCheckAspectCombination0(message)) {return null;}
      World world = DimensionManager.getWorld(message.dim);

      if (world != null && (ctx.getServerHandler().playerEntity == null
              || ctx.getServerHandler().playerEntity.getEntityId() == message.playerid)) {
         Entity player = world.getEntityByID(message.playerid);
         if (player instanceof EntityPlayer && message.aspect1 != null) {
            Aspect combo = ResearchManager.getCombinationResult(message.aspect1, message.aspect2);
            if ((Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect1) > 0 || message.ab1) && (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect2) > 0 || message.ab2)) {
               TileEntity rt = player.worldObj.getTileEntity(message.x, message.y, message.z);
               if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect1) <= 0 && message.ab1) {
                  if (rt instanceof TileResearchTable) {
                     ((TileResearchTable)rt).bonusAspects.remove(message.aspect1, 1);
                     player.worldObj.markBlockForUpdate(message.x, message.y, message.z);
                     rt.markDirty();
                  }
               } else {
                  Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), message.aspect1, (short)-1);
                  PacketHandler.INSTANCE.sendTo(new PacketAspectPool(message.aspect1.getTag(), (short) 0, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect1)), (EntityPlayerMP)player);
               }

               if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect2) <= 0 && message.ab2) {
                  if (rt instanceof TileResearchTable) {
                     ((TileResearchTable)rt).bonusAspects.remove(message.aspect2, 1);
                     player.worldObj.markBlockForUpdate(message.x, message.y, message.z);
                     rt.markDirty();
                  }
               } else {
                  Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), message.aspect2, (short)-1);
                  PacketHandler.INSTANCE.sendTo(new PacketAspectPool(message.aspect2.getTag(), (short) 0, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), message.aspect2)), (EntityPlayerMP)player);
               }

               if (combo != null) {
                  ScanManager.checkAndSyncAspectKnowledge((EntityPlayer)player, combo, 1);
               }

               ResearchManager.scheduleSave((EntityPlayer)player);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public TileResearchTable table(){
      WorldServer worldServer = DimensionManager.getWorld(dim);
      if (worldServer == null) {return null;}
      TileEntity probablyTable = worldServer.getTileEntity(x, y, z);
      if (probablyTable instanceof TileResearchTable) {
         return (TileResearchTable)probablyTable;
      }
      return null;
   };
   public EntityPlayerMP player(){
      WorldServer worldServer = DimensionManager.getWorld(dim);
      if (worldServer == null) {return null;}
      Entity probablyMP = worldServer.getEntityByID(playerid);
      if (!(probablyMP instanceof EntityPlayerMP)) {return null;}
      return (EntityPlayerMP)probablyMP;


   };
   public Aspect lhs(){
      return aspect1;
   };
   public Aspect rhs(){
      return aspect2;
   };
}
