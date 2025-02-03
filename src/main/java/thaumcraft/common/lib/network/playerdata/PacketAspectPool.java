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
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

public class PacketAspectPool implements IMessage, IMessageHandler<PacketAspectPool,IMessage> {
   private String key;
   private Short amount;
   private Short total;
   private static long lastSound = 0L;

   public PacketAspectPool() {
   }

   public PacketAspectPool(String key, Short amount, Short total) {
      this.key = key;
      this.amount = amount;
      this.total = total;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
      buffer.writeShort(this.amount);
      buffer.writeShort(this.total);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
      this.amount = buffer.readShort();
      this.total = buffer.readShort();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketAspectPool message, MessageContext ctx) {
      if (Aspect.getAspect(message.key) != null) {
         boolean success = Thaumcraft.proxy.getPlayerKnowledge().setAspectPool(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), Aspect.getAspect(message.key), message.total);
         if (success && message.amount > 0) {
            String text = StatCollector.translateToLocal("tc.addaspectpool");
            text = text.replaceAll("%s", message.amount + "");
            text = text.replaceAll("%n", Aspect.getAspect(message.key).getName());
            PlayerNotifications.addNotification(text, Aspect.getAspect(message.key));

            for(int a = 0; a < message.amount; ++a) {
               PlayerNotifications.addAspectNotification(Aspect.getAspect(message.key));
            }

            if (System.currentTimeMillis() > lastSound) {
               Minecraft.getMinecraft().thePlayer.playSound("random.orb", 0.1F, 0.9F + Minecraft.getMinecraft().thePlayer.worldObj.rand.nextFloat() * 0.2F);
               lastSound = System.currentTimeMillis() + 100L;
            }
         }
      }

      return null;
   }
}
