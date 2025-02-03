package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWarp implements IMessage, IMessageHandler<PacketSyncWarp,IMessage> {
   protected int data = 0;
   protected byte type = 0;

   public PacketSyncWarp() {
   }

   public PacketSyncWarp(EntityPlayer player, byte type) {
      if (type == 0) {
         this.data = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());
      }

      if (type == 1) {
         this.data = Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
      }

      if (type == 2) {
         this.data = Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName());
      }

      this.type = type;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.data);
      buffer.writeByte(this.type);
   }

   public void fromBytes(ByteBuf buffer) {
      this.data = buffer.readInt();
      this.type = buffer.readByte();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketSyncWarp message, MessageContext ctx) {
      if (message.type == 0) {
         Thaumcraft.proxy.getPlayerKnowledge().setWarpPerm(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), message.data);
      } else if (message.type == 1) {
         Thaumcraft.proxy.getPlayerKnowledge().setWarpSticky(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), message.data);
      } else {
         Thaumcraft.proxy.getPlayerKnowledge().setWarpTemp(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), message.data);
      }

      return null;
   }
}
