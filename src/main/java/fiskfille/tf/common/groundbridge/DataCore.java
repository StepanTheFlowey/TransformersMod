package fiskfille.tf.common.groundbridge;

import net.minecraft.util.StatCollector;

import java.util.ArrayList;

public class DataCore {
	public static final ArrayList<DataCore> dataCores = new ArrayList<>(3);
	public static final DataCore spaceBridge = new DataCore("space_bridge", 0x5FEEEE);
	public static final DataCore leveler = new DataCore("leveler", 0x61C37B);
	public static final DataCore range = new DataCore("range", 0x7C65EA);

	private final String id;
	private final int color;

	public final int index;

	public DataCore(String s, int i) {
		id = s;
		color = i;
		index = dataCores.size();
		dataCores.add(this);
	}

	public static DataCore get(int index) {
		return dataCores.get(index);
	}

	public String getTranslatedName() {
		return StatCollector.translateToLocal("ground_bridge.upgrade." + id);
	}

	public int getColor() {
		return color;
	}
}
