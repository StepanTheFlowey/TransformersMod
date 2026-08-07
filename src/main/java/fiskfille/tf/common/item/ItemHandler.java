package fiskfille.tf.common.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fiskfille.tf.common.data.TFWorldData;
import fiskfille.tf.common.event.ItemHandlerEvent;
import fiskfille.tf.common.event.ItemStitchEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ItemHandler {
	private static final Map<Class, String> itemHandlers = Maps.newHashMap();
	public static boolean hasInit = false;

	public static void init() {
		final List<String> names = Lists.newLinkedList();
		final List<String> domains = Lists.newLinkedList();

		MinecraftForge.EVENT_BUS.post(new ItemHandlerEvent.Init(itemHandlers));
		hasInit = true;

		for(final Map.Entry<Class, String> e : itemHandlers.entrySet()) {
			for(final Field field : e.getKey().getFields()) {
				final String s = field.getType().getName();

				if(s.equals(ItemStack[].class.getName())) {
					names.add(field.getName());
					domains.add(e.getValue());
				}
			}
		}

		ItemMetaBasic.iconNames = names.toArray(new String[0]);
		ItemMetaBasic.iconDomains = domains.toArray(new String[0]);
	}

	public static void load(final World world) {
		final TFWorldData data = TFWorldData.get(world);
		MinecraftForge.EVENT_BUS.post(new ItemStitchEvent.Pre(world));

		for(final Map.Entry<Class, String> e : itemHandlers.entrySet()) {
			for(final Field field : e.getKey().getFields()) {
				final String s = field.getType().getName();

				if(s.equals(ItemStack[].class.getName())) {
					try {
						final ItemStack[] itemstacks = new ItemStack[65];
						final String name = field.getName();
						int id = data.getNextAvailableId();

						if(data.subItems.containsKey(name)) {
							id = data.subItems.get(name);
						}
						else {
							data.subItems.put(name, id);
						}

						for(int amount = 0; amount < itemstacks.length; ++amount) {
							itemstacks[amount] = new ItemStack(TFItems.craftingMaterial, amount, id);
						}

						field.set(null, itemstacks);
					}
					catch(final Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		}

		ItemMetaBasic.subItems = data.subItems;
		MinecraftForge.EVENT_BUS.post(new ItemStitchEvent.Post(world));
	}

	public static boolean matches(final ItemStack itemstack, final ItemStack[] item) {
		return itemstack != null && item[1].getItem() == itemstack.getItem() && item[1].getItemDamage() == itemstack.getItemDamage();
	}
}
