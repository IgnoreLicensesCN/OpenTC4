package thaumcraft.api.crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CrucibleRecipe {

	private ItemStack recipeOutput;
	
	public Object catalyst;
	public AspectList aspects;
	public String key;
	
	public int hash;
	
	public CrucibleRecipe(String researchKey, ItemStack result, Object cat, AspectList tags) {
		recipeOutput = result;
		this.aspects = tags;
		this.key = researchKey;
		this.catalyst = cat;
		if (cat instanceof String) {
			this.catalyst = OreDictionary.getOres((String) cat);
		}
		StringBuilder hc = new StringBuilder(researchKey + result.toString());
		for (Aspect tag:tags.getAspects()) {
			hc.append(tag.getTag()).append(tags.getAmount(tag));
		}
		if (cat instanceof ItemStack) {
			hc.append(((ItemStack) cat).toString());
		} else
		if (cat instanceof ArrayList && !((ArrayList<ItemStack>) catalyst).isEmpty()) {
			for (ItemStack is :(ArrayList<ItemStack>)catalyst) {
				hc.append(is.toString());
			}
		}
		
		hash = hc.toString().hashCode();
	}
	
		

	public boolean matches(AspectList itags, ItemStack cat) {
		if (catalyst instanceof ItemStack &&
				!ThaumcraftApiHelper.itemMatches((ItemStack) catalyst,cat,false)) {
			return false;
		} else 
		if (catalyst instanceof ArrayList && !((ArrayList<ItemStack>) catalyst).isEmpty()) {
			ItemStack[] ores = ((ArrayList<ItemStack>)catalyst).toArray(new ItemStack[]{});
			if (!ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat},ores)) return false;
		}
		if (itags==null) return false;
		for (Aspect tag:aspects.getAspects()) {
			if (itags.getAmount(tag)<aspects.getAmount(tag)) return false;
		}
		return true;
	}
	
	public boolean catalystMatches(ItemStack cat) {
		if (catalyst instanceof ItemStack && ThaumcraftApiHelper.itemMatches((ItemStack) catalyst,cat,false)) {
			return true;
		} else 
		if (catalyst instanceof ArrayList && !((ArrayList<ItemStack>) catalyst).isEmpty()) {
			ItemStack[] ores = ((ArrayList<ItemStack>)catalyst).toArray(new ItemStack[]{});
            return ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat}, ores);
		}
		return false;
	}
	
	public AspectList removeMatching(AspectList itags) {
		AspectList temptags = new AspectList();
		temptags.aspects.putAll(itags.aspects);
		
		for (Aspect tag:aspects.getAspects()) {
			temptags.remove(tag, aspects.getAmount(tag));
		}
		
		itags = temptags;
		return itags;
	}
	
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}
	

}
