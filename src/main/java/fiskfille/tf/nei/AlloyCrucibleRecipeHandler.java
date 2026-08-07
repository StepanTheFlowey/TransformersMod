package fiskfille.tf.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.gui.GuiAlloyCrucible;
import fiskfille.tf.common.recipe.AlloyRecipes;
import fiskfille.tf.common.recipe.AlloyRecipes.AlloyIngredients;
import fiskfille.tf.common.tileentity.TileEntityAlloyCrucible;
import fiskfille.tf.helper.TFFormatHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public final class AlloyCrucibleRecipeHandler extends TemplateRecipeHandler {
	public static TileEntityAlloyCrucible tileentity;

	@Override
	public TemplateRecipeHandler newInstance() {
		if(tileentity == null) {
			tileentity = new TileEntityAlloyCrucible();
			tileentity.alloyResult = true;
		}

		return super.newInstance();
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(102, 38, 16, 16), "alloy_crucible"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiAlloyCrucible.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("recipe.alloy_crucible");
	}

	@Override
	public void loadCraftingRecipes(final String outputId, final Object... results) {
		if(outputId.equals("alloy_crucible")) {
			final Map<AlloyIngredients, ItemStack> recipes = AlloyRecipes.getInstance().getSmeltingList();

			for(final Entry<AlloyIngredients, ItemStack> e : recipes.entrySet()) {
				final AlloyPair recipe = new AlloyPair(e.getKey(), e.getValue());
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
		final Map<AlloyIngredients, ItemStack> recipes = AlloyRecipes.getInstance().getSmeltingList();

		for(final Entry<AlloyIngredients, ItemStack> e : recipes.entrySet()) {
			if(NEIServerUtils.areStacksSameTypeCrafting(e.getValue(), result)) {
				final AlloyPair recipe = new AlloyPair(e.getKey(), e.getValue());
				recipe.computeVisuals();
				arecipes.add(recipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(final ItemStack ingredient) {
		final Map<AlloyIngredients, ItemStack> recipes = AlloyRecipes.getInstance().getSmeltingList();

		for(final Entry<AlloyIngredients, ItemStack> e : recipes.entrySet()) {
			final AlloyPair recipe = new AlloyPair(e.getKey(), e.getValue());

			for(int i = 0; i < recipe.getIngredients().size(); ++i) {
				if(recipe.getIngredients().get(i).contains(ingredient)) {
					recipe.computeVisuals();
					arecipes.add(recipe);
					break;
				}
			}
		}
	}

	@Override
	public String getGuiTexture() {
		return TransformersMod.MODID + ":textures/gui/container/alloy_crucible.png";
	}

	@Override
	public void drawExtras(final int recipe) {
		drawProgressBar(102, 40, 192, 0, 14, 14, 48, 3);
		drawProgressBar(44, 8, 176, 0, 16, 52, 52 * 48, 7);
	}

	@Override
	public void drawBackground(final int recipe) {
		GL11.glColor3f(1F, 1F, 1F);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(0, 0, 5, 11, 166, 65);
	}

	@Override
	public List<String> handleTooltip(final GuiRecipe<?> gui, List<String> currenttip, final int recipe) {
		currenttip = super.handleTooltip(gui, currenttip, recipe);

		final int guiLeft = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 4);
		final int guiTop = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 5);

		final Point pos = GuiDraw.getMousePosition();
		final Point offset = gui.getRecipePosition(recipe);
		final Point relMouse = new Point(pos.x - guiLeft - offset.x, pos.y - guiTop - offset.y);

		if(new Rectangle(44, 8, 16, 52).contains(relMouse)) {
			if(currenttip.isEmpty()) {
				tileentity.smeltingResult = getResultStack(recipe).item;
				currenttip.add(StatCollector.translateToLocalFormatted("gui.emb.amount", TFFormatHelper.formatNumber(tileentity.getSmeltTimeMax() * tileentity.getConsumptionRate())));
			}
		}

		return currenttip;
	}

	@Override
	public String getOverlayIdentifier() {
		return "alloy_crucible";
	}

	public class AlloyPair extends CachedRecipe {
		public final ArrayList<PositionedStack> ingredients;
		public final PositionedStack result;

		public AlloyPair(final AlloyIngredients alloy, final ItemStack out) {
			result = new PositionedStack(out, 107 - 5, 28 - 11);
			ingredients = new ArrayList<>();

			for(int i = 0; i < alloy.getIngredients().length; ++i) {
				final LinkedList<ItemStack> ingredients = Lists.newLinkedList();
				final List<String> list = alloy.getOreDictNames(i);

				for(final String s : list) {
					ingredients.addAll(OreDictionary.getOres(s));
				}

				Object items = ingredients;

				if(ingredients.isEmpty()) {
					items = alloy.getIngredients()[i];
				}

				addSlotToContainer(73, 19 + i * 18, items);
			}
		}

		private void addSlotToContainer(final int x, final int y, final Object item) {
			if(item != null) {
				final PositionedStack stack = new PositionedStack(item, x - 5, y - 11, false);
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
