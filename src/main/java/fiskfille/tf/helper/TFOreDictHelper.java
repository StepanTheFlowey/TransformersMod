package fiskfille.tf.helper;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TFOreDictHelper {
	public static List<String> getAliases(final ItemStack itemstack) {
		final List<String> list = Lists.newArrayList();
		final int[] ids = OreDictionary.getOreIDs(itemstack);

		for(final int id : ids) {
			list.add(OreDictionary.getOreName(id));
		}

		return list;
	}
}
