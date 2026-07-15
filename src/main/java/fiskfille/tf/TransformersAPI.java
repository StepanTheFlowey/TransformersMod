package fiskfille.tf;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fiskfille.tf.client.displayable.Displayable;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author gegy1000, FiskFille
 */
public class TransformersAPI {
	private static final ArrayList<Transformer> transformers = new ArrayList<>();
	private static final ArrayList<Energon> energonTypes = new ArrayList<>();
	private static final ArrayList<Item> displayablesServer = new ArrayList<>();
	private static final HashMap<Item, Displayable> displayables = new HashMap<>();

	/**
	 * Used to register the specified Transformer.
	 *
	 * @param transformer The Transformer registered.
	 */
	public static void registerTransformer(Transformer transformer) {
		if(!transformers.contains(transformer)) {
			transformers.add(transformer);
		}
		else {
			System.err.println("[TransformersAPI] " + transformer.getName() + " has already been registered!");
		}
	}

	/**
	 * @return a list of registered Transformers.
	 */
	public static ArrayList<Transformer> getTransformers() {
		return transformers;
	}

	/**
	 * Gets an instance of a Transformer by name.
	 *
	 * @param name The name of the Transformer
	 * @return the Transformer with the specified name, or null if there is none.
	 */
	public static Transformer getTransformerByName(String name) {
		for(Transformer transformer : transformers) {
			if(transformer.getName().equals(name)) {
				return transformer;
			}
		}

		return null;
	}

	/**
	 * Used to register the specified energon type.
	 *
	 * @param energon The energon type being registered.
	 */
	public static void registerEnergonType(Energon energon) {
		if(!energonTypes.contains(energon)) {
			energonTypes.add(energon);
		}
		else {
			System.err.println("[TransformersAPI] A mod is trying to register an energon type twice!");
		}
	}

	/**
	 * @return a list of registered Energon Types.
	 */
	public static ArrayList<Energon> getEnergonTypes() {
		return energonTypes;
	}

	/**
	 * @param name The name of the energon type
	 * @return an instance of an energon type with the specified name
	 */
	public static Energon getEnergonTypeByName(String name) {
		for(Energon energon : energonTypes) {
			if(energon.getId().equals(name)) {
				return energon;
			}
		}

		return null;
	}

	/**
	 * Used to register the specified Displayable.
	 *
	 * @param item             The item to be assigned to.
	 * @param displayableClass The Displayable registered.
	 */
	public static void registerDisplayable(Item item, Class<? extends Displayable> displayableClass) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			try {
				displayables.put(item, displayableClass.newInstance());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			displayablesServer.add(item);
		}
	}

	/**
	 * @return a map of registered Displayables.
	 */
	public static HashMap<Item, Displayable> getDisplayables() {
		return displayables;
	}

	/**
	 * @param item The item to get the Displayable for.
	 * @return the Displayable for the specific item.
	 */
	public static Displayable getDisplayableFor(Item item) {
		for(HashMap.Entry<Item, Displayable> e : displayables.entrySet()) {
			if(e.getKey() == item) {
				return e.getValue();
			}
		}

		return null;
	}

	/**
	 * @param item The item to query
	 * @return if the specific item has a Displayable on the client side.
	 */
	public static boolean hasDisplayable(Item item) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			return getDisplayableFor(item) != null;
		}
		else {
			return displayablesServer.contains(item);
		}
	}
}
