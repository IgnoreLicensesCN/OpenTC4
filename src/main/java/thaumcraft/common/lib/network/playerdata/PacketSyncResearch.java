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
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

public class PacketSyncResearch implements IMessage, IMessageHandler<PacketSyncResearch,IMessage> {
   protected ArrayList<String> data = new ArrayList<>();

   public PacketSyncResearch() {
   }

   public PacketSyncResearch(EntityPlayer player) {
      this.data = ResearchManager.getResearchForPlayer(player.getCommandSenderName());
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

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketSyncResearch message, MessageContext ctx) {
      for(String key : message.data) {
         Thaumcraft.proxy.getResearchManager().completeResearch(Minecraft.getMinecraft().thePlayer, key);
      }

      GuiResearchBrowser.completedResearch.put(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), message.data);
      return null;
   }
}
