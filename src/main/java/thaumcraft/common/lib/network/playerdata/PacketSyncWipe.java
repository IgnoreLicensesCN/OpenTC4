package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWipe implements IMessage, IMessageHandler<PacketSyncWipe,IMessage> {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketSyncWipe message, MessageContext ctx) {
      Thaumcraft.proxy.getPlayerKnowledge().wipePlayerKnowledge(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
      return null;
   }
}
