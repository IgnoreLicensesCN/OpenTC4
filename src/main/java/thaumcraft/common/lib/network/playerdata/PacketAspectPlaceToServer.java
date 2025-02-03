package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.tiles.TileResearchTable;

public class PacketAspectPlaceToServer implements IMessage, IMessageHandler<PacketAspectPlaceToServer,IMessage> {
   private int dim;
   private int playerid;
   private int x;
   private int y;
   private int z;
   Aspect aspect;
   byte q;
   byte r;

   public PacketAspectPlaceToServer() {
   }

   public PacketAspectPlaceToServer(EntityPlayer player, byte q, byte r, int x, int y, int z, Aspect aspect) {
      this.dim = player.worldObj.provider.dimensionId;
      this.playerid = player.getEntityId();
      this.x = x;
      this.y = y;
      this.z = z;
      this.aspect = aspect;
      this.q = q;
      this.r = r;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.dim);
      buffer.writeInt(this.playerid);
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      ByteBufUtils.writeUTF8String(buffer, this.aspect == null ? "null" : this.aspect.getTag());
      buffer.writeByte(this.q);
      buffer.writeByte(this.r);
   }

   public void fromBytes(ByteBuf buffer) {
      this.dim = buffer.readInt();
      this.playerid = buffer.readInt();
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.aspect = Aspect.getAspect(ByteBufUtils.readUTF8String(buffer));
      this.q = buffer.readByte();
      this.r = buffer.readByte();
   }

   public IMessage onMessage(PacketAspectPlaceToServer message, MessageContext ctx) {
      World world = DimensionManager.getWorld(message.dim);
      if (world != null && (ctx.getServerHandler().playerEntity == null || ctx.getServerHandler().playerEntity.getEntityId() == message.playerid)) {
         Entity player = world.getEntityByID(message.playerid);
          if (player != null) {
              TileEntity rt = world.getTileEntity(message.x, message.y, message.z);
              if (rt != null && rt instanceof TileResearchTable) {
                  ((TileResearchTable) rt).placeAspect(message.q, message.r, message.aspect, (EntityPlayer) player);
              }

          }
          return null;
      } else {
         return null;
      }
   }
}
