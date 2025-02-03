package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;

public class PacketSyncAspects implements IMessage, IMessageHandler<PacketSyncAspects,IMessage> {
   protected AspectList data = new AspectList();

   public PacketSyncAspects() {
   }

   public PacketSyncAspects(EntityPlayer player) {
      this.data = Thaumcraft.proxy.getPlayerKnowledge().getAspectsDiscovered(player.getCommandSenderName());
   }

   public void toBytes(ByteBuf buffer) {
      if (this.data != null && this.data.size() > 0) {
         buffer.writeShort(this.data.size());

         for(Aspect a : this.data.getAspects()) {
            if (a != null) {
               ByteBufUtils.writeUTF8String(buffer, a.getTag());
               buffer.writeShort(this.data.getAmount(a));
            }
         }
      } else {
         buffer.writeShort(0);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      short size = buffer.readShort();
      this.data = new AspectList();

      for(int a = 0; a < size; ++a) {
         String tag = ByteBufUtils.readUTF8String(buffer);
         short amount = buffer.readShort();
         this.data.add(Aspect.getAspect(tag), amount);
      }

   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketSyncAspects message, MessageContext ctx) {
      for(Aspect key : message.data.getAspects()) {
         Thaumcraft.proxy.getResearchManager().completeAspect(Minecraft.getMinecraft().thePlayer, key, (short)message.data.getAmount(key));
      }

      return null;
   }
}
