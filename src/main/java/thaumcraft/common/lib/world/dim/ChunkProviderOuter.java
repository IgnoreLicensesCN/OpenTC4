package thaumcraft.common.lib.world.dim;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class ChunkProviderOuter implements IChunkProvider {
   private Random rand;
   private World worldObj;
   private WorldType worldType;
   private BiomeGenBase[] biomesForGeneration;

   public ChunkProviderOuter(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
      this.worldObj = p_i2006_1_;
      this.worldType = p_i2006_1_.getWorldInfo().getTerrainType();
      this.rand = new Random(p_i2006_2_);
   }

   public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
      return this.provideChunk(p_73158_1_, p_73158_2_);
   }

   public Chunk provideChunk(int p_73154_1_, int p_73154_2_) {
      this.rand.setSeed((long)p_73154_1_ * 341873128712L + (long)p_73154_2_ * 132897987541L);
      Block[] ablock = new Block['耀'];
      byte[] meta = new byte[ablock.length];

      for(int k = 0; k < 16; ++k) {
         for(int l = 0; l < 16; ++l) {
            for(int j1 = 127; j1 >= 0; --j1) {
               int k1 = (l * 16 + k) * 128 + j1;
               ablock[k1] = null;
               meta[k1] = 0;
            }
         }
      }

      this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
      Chunk chunk = new Chunk(this.worldObj, ablock, meta, p_73154_1_, p_73154_2_);
      byte[] abyte = chunk.getBiomeArray();

      for(int k = 0; k < abyte.length; ++k) {
         abyte[k] = (byte)this.biomesForGeneration[k].biomeID;
      }

      chunk.generateSkylightMap();
      return chunk;
   }

   public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
      return true;
   }

   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
      BlockFalling.fallInstantly = true;
      MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(p_73153_1_, this.worldObj, this.worldObj.rand, p_73153_2_, p_73153_3_, false));
      int k = p_73153_2_ * 16;
      int l = p_73153_3_ * 16;
      BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
      biomegenbase.decorate(this.worldObj, this.worldObj.rand, k, l);
      MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, this.worldObj, this.worldObj.rand, p_73153_2_, p_73153_3_, false));
      BlockFalling.fallInstantly = false;
   }

   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
      return true;
   }

   public void saveExtraData() {
   }

   public boolean unloadQueuedChunks() {
      return false;
   }

   public boolean canSave() {
      return true;
   }

   public String makeString() {
      return "RandomLevelSource";
   }

   public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
      BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
      return biomegenbase.getSpawnableList(p_73155_1_);
   }

   public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
      return null;
   }

   public int getLoadedChunkCount() {
      return 0;
   }

   public void recreateStructures(int p_82695_1_, int p_82695_2_) {
   }
}
