package fiskfille.tf.common.recipe;

import cpw.mods.fml.common.Loader;
import fiskfille.tf.TFLog;
import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.item.TFSubItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.Map.Entry;

public class AlloyRecipes {
	private static final AlloyRecipes instance = new AlloyRecipes();

	private final HashMap<AlloyIngredients, ItemStack> smeltingMap = new HashMap<>();
	private final HashMap<ItemStack, Integer> durationMap = new HashMap<>();
	private final HashMap<ItemStack, Float> experienceMap = new HashMap<>();

	public static AlloyRecipes getInstance() {
		return instance;
	}

	public static void register() {
		getInstance().smeltingMap.clear();
		getInstance().durationMap.clear();
		getInstance().experienceMap.clear();
		getInstance().registerRecipes();
	}

	public static boolean matches(ItemStack itemstack, String oreDict) {
		List<ItemStack> aliases = OreDictionary.getOres(oreDict);

		for(ItemStack itemstack1 : aliases) {
			if(matches(itemstack, itemstack1)) {
				return true;
			}
		}

		return false;
	}

	public static boolean matches(ItemStack itemstack, ItemStack itemstack1) {
		return itemstack1.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack1.getItemDamage() == itemstack.getItemDamage());
	}

	private void registerRecipes() {
		addRecipe(new AlloyIngredients("transformium", "ingotIron", "ingotIron"), new ItemStack(TFItems.transformiumAlloy, 2), 400, 1F);
		addRecipe(new AlloyIngredients(TFBlocks.transformiumStone, Blocks.clay, "gemQuartz"), new ItemStack(TFItems.transformiumFragment), 600, 0.35F);
		addRecipe(new AlloyIngredients("ingotIron", "ingotGold", "dustRedstone"), new ItemStack(TFItems.crudeFluxAlloy), 0.4F);
		addRecipe(new AlloyIngredients("ingotFluxAlloyCrude", "ingotFluxAlloyCrude", "dustRedstone"), new ItemStack(TFItems.refinedFluxAlloy), 300, 0.6F);
		addRecipe(new AlloyIngredients("ingotIron", "dustEnergon"), new ItemStack(TFItems.energonAlloy), 800, 0.4F);
		addRecipe(new AlloyIngredients("dustEnergon", "transformium", "dustEnergon"), TFSubItems.focusing_crystal[1], 0.1F);
		addRecipe(new AlloyIngredients("dustEnergon", "dustEnergon", "dustEnergon"), new ItemStack(TFItems.energonCrystalShard), 100, 0);

		addRecipe(new AlloyIngredients("stone", Items.ender_pearl), new ItemStack(Blocks.end_stone), 0.2F);
		addRecipe(new AlloyIngredients("blockGlass"), new ItemStack(Blocks.glass), 0);
		addRecipe(new AlloyIngredients("paneGlass"), new ItemStack(Blocks.glass_pane), 0);
		addRecipe(new AlloyIngredients("blockClayHardened"), new ItemStack(Blocks.hardened_clay), 0);
	}

	public void addRecipe(AlloyIngredients alloy, ItemStack result, float xp) {
		addRecipe(alloy, result, 200, xp);
	}

	public void addRecipe(AlloyIngredients alloy, ItemStack result, int duration, float xp) {
		if(alloy == null || alloy.getIngredients()[0] == null) {
			TFLog.warn("Mod '%s' attempted to register unknown or empty AlloyIngredients for item %s!", Loader.instance().activeModContainer().getModId(), Item.itemRegistry.getNameForObject(result.getItem()));
			return;
		}

		smeltingMap.put(alloy, result);
		durationMap.put(result, duration);
		experienceMap.put(result, xp);
	}

	public ItemStack getSmeltingResult(AlloyIngredients ingredients) {
		ItemStack[] itemstacks = ingredients.getIngredients();
		return getSmeltingResult(itemstacks[0], itemstacks[1], itemstacks[2]);
	}

	public ItemStack getSmeltingResult(ItemStack input1, ItemStack input2, ItemStack input3) {
		Iterator<Entry<AlloyIngredients, ItemStack>> iterator = smeltingMap.entrySet().iterator();
		Entry<AlloyIngredients, ItemStack> entry;

		do {
			if(!iterator.hasNext()) {
				return null;
			}

			entry = iterator.next();
		}
		while(!entry.getKey().matches(input1, input2, input3));

		return entry.getValue();
	}

	public HashMap<AlloyIngredients, ItemStack> getSmeltingList() {
		return smeltingMap;
	}

	public int getSmeltTime(ItemStack itemstack) {
		Iterator<Entry<ItemStack, Integer>> iterator = durationMap.entrySet().iterator();
		Entry<ItemStack, Integer> entry;

		do {
			if(!iterator.hasNext()) {
				return 200;
			}

			entry = iterator.next();
		}
		while(!matches(itemstack, entry.getKey()));

		return entry.getValue();
	}

	public float getXpYield(ItemStack itemstack) {
		float xp = itemstack.getItem().getSmeltingExperience(itemstack);

		if(xp != -1) {
			return xp;
		}

		Iterator<Entry<ItemStack, Float>> iterator = experienceMap.entrySet().iterator();
		Entry<ItemStack, Float> entry;

		do {
			if(!iterator.hasNext()) {
				return 0F;
			}

			entry = iterator.next();
		}
		while(!matches(itemstack, entry.getKey()));

		return entry.getValue();
	}

	public static class AlloyIngredients {
		private final HashMap<Integer, List<String>> oreDictNames = new HashMap<>();
		private final ItemStack[] ingredients;

		public AlloyIngredients(Object... objects) {
			LinkedList<ItemStack> list = new LinkedList<>();

			for(int i = 0; i < objects.length; ++i) {
				final Object obj = objects[i];

				if(obj instanceof List) {
					List list1 = (List) obj;

					for(Object o : list1) {
						List<ItemStack> list2 = OreDictionary.getOres((String) o);

						if(!list2.isEmpty()) {
							list.add(list2.get(0));
							break;
						}
					}

					oreDictNames.put(i, list1);
				}
				else if(obj instanceof String) {
					final List<ItemStack> list1 = OreDictionary.getOres((String) obj);

					if(!list1.isEmpty()) {
						list.add(list1.get(0));
					}

					oreDictNames.put(i, Collections.singletonList((String) obj));
				}
				else {
					ItemStack itemstack = getItemStack(obj);

					if(itemstack != null) {
						list.add(itemstack);
					}
				}
			}

			ingredients = list.toArray(new ItemStack[3]);
		}

		public boolean matches(ItemStack input1, ItemStack input2, ItemStack input3) {
			final ItemStack[] ingredients = new ItemStack[]{input1, input2, input3};

			for(int i = 0; i < getIngredients().length; ++i) {
				final ItemStack itemstack = getIngredients()[i];

				if(itemstack == null && ingredients[i] == null) {
					continue;
				}
				else if(itemstack == null || ingredients[i] == null) {
					return false;
				}

				List<String> list = getOreDictNames(i);
				boolean oreDictMatch = false;

				for(String oreDict : list) {
					if(AlloyRecipes.matches(ingredients[i], oreDict)) {
						oreDictMatch = true;
						break;
					}
				}

				if(!oreDictMatch) {
					if(!AlloyRecipes.matches(itemstack, ingredients[i])) {
						return false;
					}
				}
			}

			return true;
		}

		private ItemStack getItemStack(Object obj) {
			if(obj instanceof ItemStack) {
				return (ItemStack) obj;
			}
			else if(obj instanceof Item) {
				return new ItemStack((Item) obj, 1, OreDictionary.WILDCARD_VALUE);
			}
			else if(obj instanceof Block) {
				return new ItemStack((Block) obj, 1, OreDictionary.WILDCARD_VALUE);
			}

			return null;
		}

		public ItemStack[] getIngredients() {
			return ingredients;
		}

		public List<String> getOreDictNames(int index) {
			return oreDictNames.get(index) != null ? oreDictNames.get(index) : new ArrayList<>();
		}

		@Override
		public String toString() {
			return String.format("Alloy{%s}", Arrays.asList(ingredients));
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof AlloyIngredients) {
				AlloyIngredients alloy = (AlloyIngredients) obj;

				if(getIngredients().length == alloy.getIngredients().length) {
					for(int i = 0; i < getIngredients().length; ++i) {
						ItemStack itemstack = getIngredients()[i];
						ItemStack itemstack1 = alloy.getIngredients()[i];

						if(itemstack == null && itemstack1 == null) {
							continue;
						}
						else if(itemstack == null || itemstack1 == null) {
							return false;
						}

						if(!AlloyRecipes.matches(itemstack, itemstack1)) {
							return false;
						}
					}

					return true;
				}
			}

			return false;
		}
	}
}
