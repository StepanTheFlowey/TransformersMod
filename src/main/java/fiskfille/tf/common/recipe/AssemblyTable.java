package fiskfille.tf.common.recipe;

import net.minecraft.item.ItemStack;

public class AssemblyTable {
	public static void addRecipe(final ItemStack result, final Dyes dyes, final Object... ingredients) {
		AssemblyTableCraftingManager.getInstance().addRecipe(result, dyes.compile(), ingredients);
	}

	public static void addRecipe(final ItemStack[] result, final int amount, final Dyes dyes, final Object... ingredients) {
		addRecipe(result[amount], dyes, ingredients);
	}

	public static void addRecipe(final ItemStack[] result, final Dyes dyes, final Object... ingredients) {
		addRecipe(result, 1, dyes, ingredients);
	}
}
