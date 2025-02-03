package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

public class PacketAspectDiscovery implements IMessage, IMessageHandler<PacketAspectDiscovery,IMessage> {
   private String key;

   public PacketAspectDiscovery() {
   }

   public PacketAspectDiscovery(String key) {
      this.key = key;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketAspectDiscovery message, MessageContext ctx) {
      if (Aspect.getAspect(message.key) != null) {
         Thaumcraft.proxy.getPlayerKnowledge().addDiscoveredAspect(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), Aspect.getAspect(message.key));
         String text = StatCollector.translateToLocal("tc.addaspectdiscovery");
         text = text.replaceAll("%n", Aspect.getAspect(message.key).getName());
         PlayerNotifications.addNotification("§6" + text, Aspect.getAspect(message.key));
         Minecraft.getMinecraft().thePlayer.playSound("random.orb", 0.2F, 0.5F + Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.2F);
         GuiResearchBrowser.highlightedItem.add("ASPECTS");
         GuiResearchBrowser.highlightedItem.add("BASICS");
      }

      return null;
   }
}
