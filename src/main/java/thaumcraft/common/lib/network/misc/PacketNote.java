package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.tiles.TileSensor;

public class PacketNote implements IMessage, IMessageHandler<PacketNote,IMessage> {
   private int x;
   private int y;
   private int z;
   private int dim;
   private byte note;

   public PacketNote() {
   }

   public PacketNote(int x, int y, int z, int dim) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dim = dim;
      this.note = -1;
   }

   public PacketNote(int x, int y, int z, int dim, byte note) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dim = dim;
      this.note = note;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.dim);
      buffer.writeByte(this.note);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.dim = buffer.readInt();
      this.note = buffer.readByte();
   }

   public IMessage onMessage(PacketNote message, MessageContext ctx) {
      if (ctx.side == Side.CLIENT) {
         if (message.note >= 0) {
            TileEntity tile = Thaumcraft.proxy.getClientWorld().getTileEntity(message.x, message.y, message.z);
            if (tile != null && tile instanceof TileEntityNote) {
               ((TileEntityNote)tile).note = message.note;
            } else if (tile != null && tile instanceof TileSensor) {
               ((TileSensor)tile).note = message.note;
            }
         }
      } else if (message.note == -1) {
         World world = DimensionManager.getWorld(message.dim);
         if (world == null) {
            return null;
         }

         TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
         byte note = -1;
         if (tile != null && tile instanceof TileEntityNote) {
            note = ((TileEntityNote)tile).note;
         } else if (tile != null && tile instanceof TileSensor) {
            note = ((TileSensor)tile).note;
         }

         if (note >= 0) {
            PacketHandler.INSTANCE.sendToAllAround(new PacketNote(message.x, message.y, message.z, message.dim, note), new NetworkRegistry.TargetPoint(message.dim, (double)message.x, (double)message.y, (double)message.z, (double)8.0F));
         }
      }

      return null;
   }
}
