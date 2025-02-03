package thaumcraft.common.blocks;

import net.minecraft.block.Block;

public class JarStepSound extends Block.SoundType {
   public JarStepSound(String par1Str, float par2, float par3) {
      super(par1Str, par2, par3);
   }

   public String getBreakSound() {
      return "thaumcraft:" + this.soundName;
   }

   public String getStepResourcePath() {
      return "thaumcraft:" + this.soundName;
   }
}
