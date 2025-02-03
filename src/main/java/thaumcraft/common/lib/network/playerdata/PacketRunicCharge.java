package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import thaumcraft.common.Thaumcraft;

public class PacketRunicCharge implements IMessage, IMessageHandler<PacketRunicCharge,IMessage> {
   private int id;
   private short amount;
   private short max;

   public PacketRunicCharge() {
   }

   public PacketRunicCharge(EntityPlayer player, Short amount, int max) {
      this.id = player.getEntityId();
      this.amount = amount;
      this.max = (short)max;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.id);
      buffer.writeShort(this.amount);
      buffer.writeShort(this.max);
   }

   public void fromBytes(ByteBuf buffer) {
      this.id = buffer.readInt();
      this.amount = buffer.readShort();
      this.max = buffer.readShort();
   }

   public IMessage onMessage(PacketRunicCharge message, MessageContext ctx) {
      Thaumcraft.instance.runicEventHandler.runicCharge.put(message.id, (int) message.amount);
      Thaumcraft.instance.runicEventHandler.runicInfo.put(message.id, new Integer[]{(int) message.max, 0, 0, 0, 0});
      return null;
   }
}
