package fiskfille.tf.common.recipe;

import net.minecraft.item.ItemStack;

public class AssemblyTable {
	public static void addRecipe(ItemStack result, Dyes dyes, Object... ingredients) {
		AssemblyTableCraftingManager.getInstance().addRecipe(result, dyes.compile(), ingredients);
	}

	public static void addRecipe(ItemStack[] result, int amount, Dyes dyes, Object... ingredients) {
		addRecipe(result[amount], dyes, ingredients);
	}

	public static void addRecipe(ItemStack[] result, Dyes dyes, Object... ingredients) {
		addRecipe(result, 1, dyes, ingredients);
	}
}
