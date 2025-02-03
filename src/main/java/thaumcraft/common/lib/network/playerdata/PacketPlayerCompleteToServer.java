package thaumcraft.common.lib.network.playerdata;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.research.ResearchManager;

public class PacketPlayerCompleteToServer implements IMessage, IMessageHandler<PacketPlayerCompleteToServer,IMessage> {
   private String key;
   private int dim;
   private String username;
   private byte type;

   public PacketPlayerCompleteToServer() {
   }

   public PacketPlayerCompleteToServer(String key, String username, int dim, byte type) {
      this.key = key;
      this.dim = dim;
      this.username = username;
      this.type = type;
   }

   public void toBytes(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.key);
      buffer.writeInt(this.dim);
      ByteBufUtils.writeUTF8String(buffer, this.username);
      buffer.writeByte(this.type);
   }

   public void fromBytes(ByteBuf buffer) {
      this.key = ByteBufUtils.readUTF8String(buffer);
      this.dim = buffer.readInt();
      this.username = ByteBufUtils.readUTF8String(buffer);
      this.type = buffer.readByte();
   }

   public IMessage onMessage(PacketPlayerCompleteToServer message, MessageContext ctx) {
      World world = DimensionManager.getWorld(message.dim);
      if (world != null && (ctx.getServerHandler().playerEntity == null || ctx.getServerHandler().playerEntity.getCommandSenderName().equals(message.username))) {
         EntityPlayer player = world.getPlayerEntityByName(message.username);
         if (player != null && !ResearchManager.isResearchComplete(message.username, message.key)) {
            if (ResearchManager.doesPlayerHaveRequisites(message.username, message.key)) {
               if (message.type != 0) {
                  if (message.type == 1) {
                     ResearchManager.createResearchNoteForPlayer(world, player, message.key);
                  }
               } else {
                  for(Aspect a : ResearchCategories.getResearch(message.key).tags.getAspects()) {
                     Thaumcraft.proxy.playerKnowledge.addAspectPool(message.username, a, (short)(-ResearchCategories.getResearch(message.key).tags.getAmount(a)));
                     ResearchManager.scheduleSave(player);
                     PacketHandler.INSTANCE.sendTo(new PacketAspectPool(a.getTag(), (short)(-ResearchCategories.getResearch(message.key).tags.getAmount(a)), Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(message.username, a)), (EntityPlayerMP)player);
                  }

                  PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(message.key), (EntityPlayerMP)player);
                  Thaumcraft.proxy.getResearchManager().completeResearch(player, message.key);
                  if (ResearchCategories.getResearch(message.key).siblings != null) {
                     for(String sibling : ResearchCategories.getResearch(message.key).siblings) {
                        if (!ResearchManager.isResearchComplete(message.username, sibling) && ResearchManager.doesPlayerHaveRequisites(message.username, sibling)) {
                           PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(sibling), (EntityPlayerMP)player);
                           Thaumcraft.proxy.getResearchManager().completeResearch(player, sibling);
                        }
                     }
                  }
               }

               world.playSoundAtEntity(player, "thaumcraft:learn", 0.75F, 1.0F);
            } else {
               player.addChatMessage(new ChatComponentTranslation(StatCollector.translateToLocal("tc.researcherror"), new Object[0]));
            }
         }

         return null;
      } else {
         return null;
      }
   }
}
