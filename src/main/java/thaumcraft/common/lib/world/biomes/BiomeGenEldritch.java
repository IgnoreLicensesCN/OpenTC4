package thaumcraft.common.lib.world.biomes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;

public class BiomeGenEldritch extends BiomeGenBase {
   public BiomeGenEldritch(int p_i1990_1_) {
      super(p_i1990_1_);
      this.spawnableMonsterList.clear();
      this.spawnableCreatureList.clear();
      this.spawnableWaterCreatureList.clear();
      this.spawnableCaveCreatureList.clear();
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityInhabitedZombie.class, 1, 1, 1));
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
      this.topBlock = Blocks.dirt;
      this.fillerBlock = Blocks.dirt;
      this.setBiomeName("Eldritch");
      this.setDisableRain();
   }

   @SideOnly(Side.CLIENT)
   public int getSkyColorByTemp(float p_76731_1_) {
      return 0;
   }

   public void decorate(World world, Random random, int x, int z) {
   }

   public BiomeGenBase createMutation() {
      return null;
   }
}
