package fiskfille.tf.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.Map;

public final class TFMathHelper {
	public static int[] split(final int number, final int digits, final int m) {
		final int[] increments = new int[digits];
		final int[] aint = new int[digits];
		int incr = 1;
		int total = 0;

		for(int i = 0; i < digits; ++i) {
			increments[i] = incr;
			incr *= m;
		}

		for(int i = digits - 1; i >= 0; --i) {
			final int j = MathHelper.floor_double((float) number / increments[i]);

			aint[i] = j * increments[i] - total;
			total = j * increments[i];
		}

		return aint;
	}

	public static <T> Object getMinKey(final Map<T, Integer> map) {
		T minKey = null;
		int minValue = Integer.MAX_VALUE;

		for(final Map.Entry<T, Integer> e : map.entrySet()) {
			final int value = e.getValue();

			if(value < minValue) {
				minValue = value;
				minKey = e.getKey();
			}
		}

		return minKey;
	}

	public static MapColor getClosestMapColor(final int color) {
		final List<Integer> colors = Lists.newArrayList();

		for(int i = 0; i < MapColor.mapColorArray.length; ++i) {
			final MapColor mapColor = MapColor.mapColorArray[i];

			if(mapColor != null) {
				colors.add(mapColor.colorValue);
			}
		}

		final int color1 = getClosestColor(color, colors);

		for(int i = 0; i < MapColor.mapColorArray.length; ++i) {
			final MapColor mapColor = MapColor.mapColorArray[i];

			if(mapColor != null && mapColor.colorValue == color1) {
				return mapColor;
			}
		}

		return MapColor.airColor;
	}

	public static int getClosestColor(final int color, final List<Integer> colors) {
		final Map<Integer, Integer> distances = Maps.newHashMap();
		final Color color1 = new Color(color);

		for(final int color2 : colors) {
			distances.put(color2, getColorDistance(color1, new Color(color2)));
		}

		return (Integer) getMinKey(distances);
	}

	public static int getColorDistance(final Color a, final Color b) {
		return (int) Math.sqrt(Math.pow(a.getRed() - b.getRed(), 2) + Math.pow(a.getGreen() - b.getGreen(), 2) + Math.pow(a.getBlue() - b.getBlue(), 2));
	}
}
