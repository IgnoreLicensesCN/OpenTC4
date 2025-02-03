package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import thaumcraft.client.lib.PlayerNotifications;

public class PacketWarpMessage implements IMessage, IMessageHandler<PacketWarpMessage,IMessage> {
   protected int data = 0;
   protected byte type = 0;

   public PacketWarpMessage() {
   }

   public PacketWarpMessage(EntityPlayer player, byte type, int change) {
      this.data = change;
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
   public IMessage onMessage(PacketWarpMessage message, MessageContext ctx) {
      if (message.data != 0) {
         if (message.type == 0 && message.data > 0) {
            String text = StatCollector.translateToLocal("tc.addwarp");
            if (message.data < 0) {
               text = StatCollector.translateToLocal("tc.removewarp");
            } else {
               Minecraft.getMinecraft().thePlayer.playSound("thaumcraft:whispers", 0.5F, 1.0F);
            }

            PlayerNotifications.addNotification(text);
         } else if (message.type == 1) {
            String text = StatCollector.translateToLocal("tc.addwarpsticky");
            if (message.data < 0) {
               text = StatCollector.translateToLocal("tc.removewarpsticky");
            } else {
               Minecraft.getMinecraft().thePlayer.playSound("thaumcraft:whispers", 0.5F, 1.0F);
            }

            PlayerNotifications.addNotification(text);
         } else if (message.data > 0) {
            String text = StatCollector.translateToLocal("tc.addwarptemp");
            if (message.data < 0) {
               text = StatCollector.translateToLocal("tc.removewarptemp");
            }

            PlayerNotifications.addNotification(text);
         }
      }

      return null;
   }
}
