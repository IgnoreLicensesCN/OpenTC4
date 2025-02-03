package thaumcraft.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import thaumcraft.common.config.Config;

public class ThaumcraftGuiConfig extends GuiConfig {
   public ThaumcraftGuiConfig(GuiScreen parent) {
      super(parent, getConfigElements(), "Thaumcraft", false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
   }

   private static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList<>();
      list.addAll((new ConfigElement(Config.config.getCategory("general"))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory("Monster_Spawning"))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory("World_Generation"))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory("World_Regeneration"))).getChildElements());
      list.addAll((new ConfigElement(Config.config.getCategory("Research"))).getChildElements());
      return list;
   }
}
