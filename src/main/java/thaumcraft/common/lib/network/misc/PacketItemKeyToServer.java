package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.entities.golems.ItemGolemBell;
import thaumcraft.common.items.equipment.ItemElementalShovel;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;

public class PacketItemKeyToServer implements IMessage, IMessageHandler<PacketItemKeyToServer,IMessage> {
   private int dim;
   private int playerid;
   private byte key;

   public PacketItemKeyToServer() {
   }

   public PacketItemKeyToServer(EntityPlayer player, int key) {
      this.dim = player.worldObj.provider.dimensionId;
      this.playerid = player.getEntityId();
      this.key = (byte)key;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.dim);
      buffer.writeInt(this.playerid);
      buffer.writeByte(this.key);
   }

   public void fromBytes(ByteBuf buffer) {
      this.dim = buffer.readInt();
      this.playerid = buffer.readInt();
      this.key = buffer.readByte();
   }

   public IMessage onMessage(PacketItemKeyToServer message, MessageContext ctx) {
      World world = DimensionManager.getWorld(message.dim);
       if (world != null) {
           Entity player = world.getEntityByID(message.playerid);
           if (player != null && player instanceof EntityPlayer && ((EntityPlayer) player).getHeldItem() != null) {
               if (message.key == 0 && ((EntityPlayer) player).getHeldItem().getItem() instanceof ItemGolemBell) {
                   ItemGolemBell.resetMarkers(((EntityPlayer) player).getHeldItem(), world, (EntityPlayer) player);
               }

               if (message.key == 1 && ((EntityPlayer) player).getHeldItem().getItem() instanceof ItemWandCasting) {
                   WandManager.toggleMisc(((EntityPlayer) player).getHeldItem(), world, (EntityPlayer) player);
               }

               if (message.key == 1 && ((EntityPlayer) player).getHeldItem().getItem() instanceof ItemElementalShovel) {
                   ItemElementalShovel var10000 = (ItemElementalShovel) ((EntityPlayer) player).getHeldItem().getItem();
                   byte b = ItemElementalShovel.getOrientation(((EntityPlayer) player).getHeldItem());
                   var10000 = (ItemElementalShovel) ((EntityPlayer) player).getHeldItem().getItem();
                   ItemElementalShovel.setOrientation(((EntityPlayer) player).getHeldItem(), (byte) (b + 1));
               }
           }

       }
       return null;
   }
}
