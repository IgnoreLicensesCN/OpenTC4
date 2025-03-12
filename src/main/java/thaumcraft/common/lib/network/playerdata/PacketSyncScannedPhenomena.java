package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.common.Thaumcraft;

public class PacketSyncScannedPhenomena implements IMessage, IMessageHandler<PacketSyncScannedPhenomena,IMessage> {
   protected ArrayList<String> data = new ArrayList<>();

   public PacketSyncScannedPhenomena() {
   }

   public void toBytes(ByteBuf buffer) {
      if (this.data != null && !this.data.isEmpty()) {
         buffer.writeShort(this.data.size());

         for(String s : this.data) {
            if (s != null) {
               ByteBufUtils.writeUTF8String(buffer, s);
            }
         }
      } else {
         buffer.writeShort(0);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      short size = buffer.readShort();
      this.data = new ArrayList<>();

      for(int a = 0; a < size; ++a) {
         this.data.add(ByteBufUtils.readUTF8String(buffer));
      }

   }

   public PacketSyncScannedPhenomena(EntityPlayer player) {
      this.data = Thaumcraft.proxy.getScannedPhenomena().get(player.getCommandSenderName());
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketSyncScannedPhenomena message, MessageContext ctx) {
      for(String key : message.data) {
         Thaumcraft.proxy.getResearchManager().completeScannedPhenomena(Minecraft.getMinecraft().thePlayer, key);
      }

      return null;
   }
}
