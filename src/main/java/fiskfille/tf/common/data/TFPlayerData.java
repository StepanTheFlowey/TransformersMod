package fiskfille.tf.common.data;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Map;

public class TFPlayerData implements IExtendedEntityProperties {
	public static final String IDENTIFIER = "TFPlayer";

	public Map<TFData, Object> data = createDataMap();

	public static TFPlayerData getData(final EntityPlayer player) {
		return (TFPlayerData) player.getExtendedProperties(IDENTIFIER);
	}

	private Map<TFData, Object> createDataMap() {
		final Map<TFData, Object> map = Maps.newHashMap();

		for(final TFData data : TFData.VALUES) {
			map.put(data, data.defaultValue);
		}

		return map;
	}

	public void onUpdate() {
	}

	@Override
	public void saveNBTData(final NBTTagCompound compound) {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setBoolean("Saved", true);

		TFData.writeToNBT(nbttagcompound, data);
		compound.setTag(IDENTIFIER, nbttagcompound);
	}

	@Override
	public void loadNBTData(final NBTTagCompound compound) {
		final NBTTagCompound nbttagcompound = compound.getCompoundTag(IDENTIFIER);

		if(nbttagcompound.getBoolean("Saved")) {
			data = TFData.readFromNBT(nbttagcompound, data);
		}
	}

	@Override
	public void init(final Entity entity, final World world) {

	}

	public void copy(final TFPlayerData props) {
		data = props.data;
	}

	public <T> void putData(final TFData<T> type, final T value) {
		data.put(type, value);
	}

	public <T> T getData(final TFData<T> type) {
		return (T) data.get(type);
	}
}
