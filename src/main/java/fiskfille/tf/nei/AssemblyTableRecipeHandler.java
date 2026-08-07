package fiskfille.tf.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.gui.GuiAssemblyTable;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.recipe.AssemblyTableCraftingManager;
import fiskfille.tf.common.recipe.AssemblyTableRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public final class AssemblyTableRecipeHandler extends TemplateRecipeHandler {
	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(97, 14, 22, 15), "assembly_table"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiAssemblyTable.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("recipe.assembly_table");
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void loadCraftingRecipes(final String outputId, final Object... results) {
		if(outputId.equals("assembly_table") && getClass() == AssemblyTableRecipeHandler.class) {
			for(final IRecipe irecipe : (List<IRecipe>) AssemblyTableCraftingManager.getInstance().getRecipeList()) {
				CachedAssemblyRecipe recipe = null;

				if(irecipe instanceof AssemblyTableRecipe) {
					recipe = new CachedAssemblyRecipe((AssemblyTableRecipe) irecipe);
				}

				if(recipe == null) {
					continue;
				}

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
		else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(final ItemStack result) {
		if(result.getItem() == TFItems.groundBridgeRemote && result.getItemDamage() > 0) {
			result.setItemDamage(0);
		}

		for(final IRecipe irecipe : (List<IRecipe>) AssemblyTableCraftingManager.getInstance().getRecipeList()) {
			if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
				CachedAssemblyRecipe recipe = null;

				if(irecipe instanceof AssemblyTableRecipe) {
					recipe = new CachedAssemblyRecipe((AssemblyTableRecipe) irecipe);
				}

				if(recipe == null) {
					continue;
				}

				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(final ItemStack ingredient) {
		for(final IRecipe irecipe : (List<IRecipe>) AssemblyTableCraftingManager.getInstance().getRecipeList()) {
			CachedAssemblyRecipe recipe = null;

			if(irecipe instanceof AssemblyTableRecipe) {
				recipe = new CachedAssemblyRecipe((AssemblyTableRecipe) irecipe);
			}

			if(recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem())) {
				continue;
			}

			recipe.computeVisuals();

			if(recipe.contains(recipe.ingredients, ingredient)) {
				recipe.setIngredientPermutation(recipe.ingredients, ingredient);
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public String getGuiTexture() {
		return TransformersMod.MODID + ":textures/gui/container/assembly_table.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "assembly_table";
	}

	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor3f(1F, 1F, 1F);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(0, 0, 5, 11, 166, 122);
	}

	public class CachedAssemblyRecipe extends CachedRecipe {
		public final ArrayList<PositionedStack> ingredients;
		public final PositionedStack result;

		public CachedAssemblyRecipe(final int width, final int height, final Object[] items, final Object[] dyes, final ItemStack out) {
			result = new PositionedStack(out, 131, 14);
			ingredients = new ArrayList<>();
			setIngredients(width, height, items, dyes);
		}

		public CachedAssemblyRecipe(final AssemblyTableRecipe recipe) {
			this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.recipeDyes, recipe.getRecipeOutput());
		}

		public void setIngredients(final int width, final int height, final Object[] items, final Object[] dyes) {
			addSlotToContainer(1, 42, 25, items);
			addSlotToContainer(2, 60, 25, items);
			addSlotToContainer(3, 78, 25, items);
			addSlotToContainer(21, 42, 105, items);
			addSlotToContainer(22, 60, 105, items);
			addSlotToContainer(23, 78, 105, items);
			addSlotToContainer(5, 20, 47, items);
			addSlotToContainer(10, 20, 65, items);
			addSlotToContainer(15, 20, 83, items);
			addSlotToContainer(9, 100, 47, items);
			addSlotToContainer(14, 100, 65, items);
			addSlotToContainer(19, 100, 83, items);

			for(int i = 0; i < 3; ++i) {
				for(int j = 0; j < 3; ++j) {
					addSlotToContainer(6 + i + j * 2 + j * 3, 42 + i * 18, 47 + j * 18, items);
				}
			}

			addSlotToContainer(0, 136, 56, dyes[0]);
			addSlotToContainer(4, 136, 74, dyes[1]);
			addSlotToContainer(20, 136, 92, dyes[2]);
		}

		private void addSlotToContainer(final int id, final int x, final int y, final Object item) {
			if(item != null) {
				final PositionedStack stack = new PositionedStack(item, x - 5, y - 11, false);
				ingredients.add(stack);
			}
		}

		private void addSlotToContainer(final int id, final int x, final int y, final Object[] items) {
			if(items[id] != null) {
				final PositionedStack stack = new PositionedStack(items[id], x - 5, y - 11, false);
				ingredients.add(stack);
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		public void computeVisuals() {
			for(final PositionedStack p : ingredients) {
				p.generatePermutations();
			}
		}
	}
}
