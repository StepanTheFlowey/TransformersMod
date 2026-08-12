package fiskfille.tf.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public final class TFFormatHelper {
	public static String formatNumber(final float f) {
		final String s = (long) f + "";

		if(!s.contains("E")) {
			final StringBuilder s1 = new StringBuilder();

			for(int i = 0; i < s.length(); ++i) {
				s1.append(s.charAt(i));

				if((s.length() - i) % 3 == 1) {
					s1.append(",");
				}
			}

			return s1.substring(0, s1.length() - 1);
		}

		return s;
	}

	public static String formatNumberPrecise(final float f) {
		String s = formatNumber(f);
		final String s1 = ItemStack.field_111284_a.format(f);

		if(s1.contains(".")) {
			s += s1.substring(s1.lastIndexOf("."));
		}

		return s;
	}

	public static String getUnconventionalName(String s) {
		s = s.toLowerCase();

		for(int i = 0; i < s.length(); ++i) {
			if(i > 0 && s.charAt(i - 1) == '_') {
				s = s.substring(0, i) + s.substring(i, i + 1).toUpperCase() + s.substring(i + 1);
			}
		}

		s = s.replace(" ", "").replace("'", "").replace("/", "").replace("\\", "").replace("_", "").replace("-", "").replace("(", "").replace(")", "");
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static ArrayList<String> toString(final List<IChatComponent> list) {
		final ArrayList<String> list1 = new ArrayList<>();

		for(final IChatComponent component : list) {
			list1.add(component == null ? "" : component.getFormattedText());
		}

		return list1;
	}
}
