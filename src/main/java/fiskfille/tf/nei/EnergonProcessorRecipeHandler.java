package fiskfille.tf.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.gui.GuiEnergonProcessor;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.common.fluid.FluidEnergon;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.common.fluid.TFFluids;
import fiskfille.tf.common.item.ItemFuelCanister;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.recipe.PowerManager;
import fiskfille.tf.helper.TFFluidRenderHelper;
import fiskfille.tf.helper.TFFormatHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnergonProcessorRecipeHandler extends TemplateRecipeHandler {
	public static ArrayList<PowerSourcePair> powerSources;
	public static ArrayList<CrystalPair> crystals;

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiEnergonProcessor.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("recipe.energon_processor");
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		if(powerSources == null || powerSources.isEmpty()) {
			findPowerSources();
		}
		if(crystals == null || crystals.isEmpty()) {
			findCrystals();
		}

		return super.newInstance();
	}

	private void findPowerSources() {
		powerSources = new ArrayList<>();

		for(final Map.Entry<ItemStack, Integer> e : PowerManager.powerSources.entrySet()) {
			if(e.getValue() > 0) {
				powerSources.add(new PowerSourcePair(e.getKey().copy(), e.getValue()));
			}
		}
	}

	private void findCrystals() {
		crystals = new ArrayList<>();

		for(final ItemStack itemstack : ItemList.items) {
			final Item item = itemstack.getItem();

			if(item instanceof IEnergon || item instanceof ItemBlock && Block.getBlockFromItem(item) instanceof IEnergon) {
				final IEnergon ienergon = (IEnergon) (item instanceof ItemBlock ? Block.getBlockFromItem(item) : item);
				crystals.add(new CrystalPair(itemstack, ienergon));
			}
		}
	}

	@Override
	public void loadCraftingRecipes(final ItemStack result) {
		final Item item = result.getItem();

		if(item instanceof IFluidContainerItem) {
			final IFluidContainerItem container = (IFluidContainerItem) item;
			final FluidStack stack = container.getFluid(result);

			final int amount = stack != null ? stack.amount : 0;

			if(!ItemFuelCanister.isEmpty(result) && stack.getFluid() == TFFluids.energon) {
				final Map<String, Float> ratios = FluidEnergon.getRatios(stack);

				for(final CrystalPair crystal : crystals) {
					final String id = crystal.energon.getEnergonType().getId();
					final int mass = crystal.energon.getMass();

					if(ratios.get(id) > 0 && amount >= mass) {
						result.stackSize = 1;
						final CachedProcessorRecipe recipe = new CachedProcessorRecipe(crystal.stack, result);
						final FluidStack stack1 = new FluidStack(TFFluids.energon, 0);

						for(final Map.Entry<String, Float> e : ratios.entrySet()) {
							final Energon energon = TransformersAPI.getEnergonTypeByName(e.getKey());
							int amount1 = Math.round(e.getValue() * amount);

							if(e.getKey().equals(id)) {
								amount1 -= mass;
							}

							if(amount1 > 0) {
								stack1.amount += amount1;
								FluidEnergon.merge(stack1, FluidEnergon.create(energon, 0), amount1);
							}
						}

						recipe.tank.fill(stack1, true);
						recipe.computeVisuals();
						arecipes.add(recipe);
					}
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(final ItemStack ingredient) {
		final Item item = ingredient.getItem();

		if(item instanceof IEnergon || item instanceof ItemBlock && Block.getBlockFromItem(item) instanceof IEnergon) {
			final CachedProcessorRecipe recipe = new CachedProcessorRecipe(new ItemStack(ingredient.getItem(), 1, ingredient.getItemDamage()), new ItemStack(TFItems.fuelCanister));
			final FluidStack stack = FluidEnergon.create(ingredient);

			((ItemFuelCanister) recipe.result.item.getItem()).fill(recipe.result.item, stack, true);

			recipe.computeVisuals();
			arecipes.add(recipe);
		}
	}

	@Override
	public String getGuiTexture() {
		return TransformersMod.MODID + ":textures/gui/container/energon_processor.png";
	}

	@Override
	public void drawExtras(final int recipe) {
		drawProgressBar(20, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(42, 24, 176, 14, 24, 17, 48, 0);
		drawProgressBar(130, 25, 176, 31, 13, 12, 48, 0);

		GL11.glEnable(GL11.GL_BLEND);
		TFFluidRenderHelper.renderIntoGUI(getProcessorRecipes().get(recipe).tank, 74, 8, 48, 48, GuiDraw.gui.getZLevel());
		GL11.glDisable(GL11.GL_BLEND);

		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(72, 6, 204, 0, 52, 52);
	}

	@Override
	public List<String> handleTooltip(final GuiRecipe<?> gui, List<String> currenttip, final int recipe) {
		currenttip = super.handleTooltip(gui, currenttip, recipe);
		final int guiLeft = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 4);
		final int guiTop = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, 5);

		final Point mousepos = GuiDraw.getMousePosition();
		final Point relMouse = new Point(mousepos.x - guiLeft, mousepos.y - guiTop);
		final Point recipepos = gui.getRecipePosition(recipe);

		if(currenttip.isEmpty() && GuiContainerManager.getStackMouseOver(gui) == null && new Rectangle(recipepos.x + 72, recipepos.y + 6, 52, 52).contains(relMouse)) {
			currenttip.addAll(TFFormatHelper.toString(getProcessorRecipes().get(recipe).tank.format()));
		}

		return currenttip;
	}

	public List<CachedProcessorRecipe> getProcessorRecipes() {
		final List<CachedProcessorRecipe> list = new ArrayList<>();

		for(final CachedRecipe recipe : arecipes) {
			if(recipe instanceof CachedProcessorRecipe) {
				list.add((CachedProcessorRecipe) recipe);
			}
		}

		return list;
	}

	public static class PowerSourcePair {
		public final PositionedStack stack;
		public final int burnTime;

		public PowerSourcePair(final ItemStack ingred, final int burnTime) {
			this.stack = new PositionedStack(ingred, 19, 42, false);
			this.burnTime = burnTime;
		}
	}

	public static class CrystalPair {
		public final ItemStack stack;
		public final IEnergon energon;

		public CrystalPair(final ItemStack ingred, final IEnergon energon) {
			this.stack = ingred;
			this.energon = energon;
		}
	}

	public class CachedProcessorRecipe extends CachedRecipe {
		public final PositionedStack ingredient;
		public final PositionedStack result;

		public final FluidTankTF tank = new FluidTankTF(2000);

		public CachedProcessorRecipe(final ItemStack in, final ItemStack out) {
			ingredient = new PositionedStack(in, 19, 6);
			result = new PositionedStack(out, 133, 42);
		}

		@Override
		public PositionedStack getIngredient() {
			return ingredient;
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public PositionedStack getOtherStack() {
			return powerSources.get(cycleticks / 48 % powerSources.size()).stack;
		}

		public void computeVisuals() {
			ingredient.generatePermutations();
		}
	}
}
