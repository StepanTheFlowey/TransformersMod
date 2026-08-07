package fiskfille.tf.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import fiskfille.tf.Tags;
import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class NEITransformersModConfig implements IConfigureNEI {
	@Override
	public void loadConfig() {
		registerHandler(new EnergonProcessorRecipeHandler());
		registerHandler(new PowerSourceRecipeHandler());
		registerHandler(new AssemblyTableRecipeHandler());
		registerHandler(new AlloyCrucibleRecipeHandler());

		API.hideItem(new ItemStack(TFBlocks.groundBridgeTeleporter, 1, OreDictionary.WILDCARD_VALUE));
	}

	public void registerHandler(final Object obj) {
		if(obj instanceof ICraftingHandler) {
			API.registerRecipeHandler((ICraftingHandler) obj);
		}

		if(obj instanceof IUsageHandler) {
			API.registerUsageHandler((IUsageHandler) obj);
		}
	}

	@Override
	public String getName() {
		return "TransformersMod NEI Plugin";
	}

	@Override
	public String getVersion() {
		return Tags.VERSION;
	}
}
