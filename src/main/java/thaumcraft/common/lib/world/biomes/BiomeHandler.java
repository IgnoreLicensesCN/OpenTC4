package thaumcraft.common.lib.world.biomes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.aspects.Aspect;

public class BiomeHandler {
   public static HashMap biomeInfo = new HashMap<>();

   public static void registerBiomeInfo(BiomeDictionary.Type type, int auraLevel, Aspect tag, boolean greatwood, float greatwoodchance) {
      biomeInfo.put(type, Arrays.asList(auraLevel, tag, greatwood, greatwoodchance));
   }

   public static int getBiomeAura(BiomeGenBase biome) {
      try {
         BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
         int average = 0;
         int count = 0;

         for(BiomeDictionary.Type type : types) {
            average += (Integer)((List)biomeInfo.get(type)).get(0);
            ++count;
         }

         return average / count;
      } catch (Exception var8) {
         return 100;
      }
   }

   public static Aspect getRandomBiomeTag(int biomeId, Random random) {
      try {
         BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(BiomeGenBase.getBiome(biomeId));
         BiomeDictionary.Type type = types[random.nextInt(types.length)];
         return (Aspect)((List)biomeInfo.get(type)).get(1);
      } catch (Exception var4) {
         return null;
      }
   }

   public static float getBiomeSupportsGreatwood(int biomeId) {
      try {
         BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(BiomeGenBase.getBiome(biomeId));

         for(BiomeDictionary.Type type : types) {
            if ((Boolean)((List)biomeInfo.get(type)).get(2)) {
               return (Float)((List)biomeInfo.get(type)).get(3);
            }
         }
      } catch (Exception ignored) {
      }

      return 0.0F;
   }
}
