package fiskfille.tf.common.data;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.event.PlayerTransformEvent;
import fiskfille.tf.common.network.MessagePlayerData;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFFormatHelper;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static fiskfille.tf.common.data.TFPredicates.*;

public class TFData<T> {
	public static final TFData<Integer> ALT_MODE = new TFData<>(-1, isTransformer());
	public static final TFData<Float> TRANSFORM_PROGRESS = new TFData<>(0F, isTransformer());
	public static final TFData<Double> FORWARD_VELOCITY = new TFData<>(0D, Predicates.alwaysTrue());
	public static final TFData<Double> HORIZONTAL_VELOCITY = new TFData<>(0D, Predicates.alwaysTrue());
	public static final TFData<Boolean> BOOSTING = new TFData<>(false, Predicates.alwaysTrue());
	public static final TFData<Float> NITRO = new TFData<>(1F, Predicates.alwaysTrue());
	public static final TFData<Integer> PREV_ALT_MODE = new TFDataPrev(ALT_MODE);
	public static final TFData<Float> PREV_TRANSFORM_PROGRESS = new TFData<>(0F, isTransformer());
	public static final TFData<Boolean> STEALTH_FORCE = new TFData<>(false, and(isInVehicleMode(), hasStealthForce()));
	public static final TFData<Float> STEALTH_FORCE_PROGRESS = new TFData<>(0F, isInVehicleMode());
	public static final TFData<Float> PREV_STEALTH_FORCE_PROGRESS = new TFData<>(0F, isInVehicleMode());
	public static final TFData<Transformer> PREV_TRANSFORMER = new TFData<>(null, Predicates.alwaysTrue());
	public static final TFData<Float> PREV_NITRO = new TFData<>(1F, Predicates.alwaysTrue());

	public static final List<TFData<?>> VALUES = Lists.newArrayList();

	static {
		for(final Field field : TFData.class.getFields()) {
			final String s = field.getType().getName();

			if(s.equals(TFData.class.getName())) {
				try {
					final TFData<?> data = (TFData<?>) field.get(null);
					data.id = TFFormatHelper.getUnconventionalName(field.getName());
					TFData.VALUES.add(data);
				}
				catch(final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public final boolean save;
	public final T defaultValue;
	public String id;

	protected TFData(final T defaultValue, final Predicate<EntityPlayer> canSet) {
		this(true, defaultValue, canSet);
	}

	protected TFData(final boolean save, final T defaultValue, final Predicate<EntityPlayer> canSet) {
		this.save = save;
		this.defaultValue = defaultValue;
	}

	public static void writeToNBT(final NBTTagCompound nbt, final Map<TFData, Object> data) {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();

		for(final Map.Entry<TFData, Object> e : data.entrySet()) {
			if(e.getKey().save) {
				Object obj = e.getValue();

				if(obj instanceof Transformer) {
					obj = ((Transformer) obj).getName();
				}

				e.getKey().writeDataToNBT(nbttagcompound, obj);
			}
		}

		nbt.setTag("DataArray", nbttagcompound);
	}

	public static Map<TFData, Object> readFromNBT(final NBTTagCompound nbt, final Map<TFData, Object> data) {
		final NBTTagCompound nbttagcompound = nbt.getCompoundTag("DataArray");

		for(final TFData<?> type : TFData.VALUES) {
			if(type.save) {
				Object obj = type.readDataFromNBT(nbttagcompound);

				if(obj != null) {
					if(type.defaultValue instanceof Transformer && obj instanceof String) {
						obj = TransformersAPI.getTransformerByName((String) obj);
					}

					data.put(type, obj);
				}
			}
		}

		return data;
	}

	public Predicate<EntityPlayer> predicate(final TFData data, final T value) {
		return input -> value == null ? data.get(input) == value : data.get(input) == value || data.get(input).equals(value);
	}

	public boolean set(final EntityPlayer player, final T value) {
		if(get(player) == null || !get(player).equals(value)) {
//            if (canSet.apply(player)) // TODO: Gegy-proof
			{
				if(setWithoutNotify(player, value)) {
					if(player.worldObj.isRemote) {
						TFNetworkManager.networkWrapper.sendToServer(new MessagePlayerData(player, this, value));
					}
					else {
						TFNetworkManager.networkWrapper.sendToDimension(new MessagePlayerData(player, this, value), player.dimension);
					}

					return true;
				}
			}
//            else
//            {
//                // reset to default
//            }
		}

		return false;
	}

	public boolean setWithoutNotify(final EntityPlayer player, final T value) {
		if(get(player) == null || !get(player).equals(value)) {
//            if (canSet.apply(player))
			{
				if(this == ALT_MODE) {
					if(!MinecraftForge.EVENT_BUS.post(new PlayerTransformEvent(player, TFHelper.getTransformer(player), (Integer) value, STEALTH_FORCE.get(player)))) {
						player.triggerAchievement(TFAchievements.transform);

						if((Integer) value == -1) {
							STEALTH_FORCE.setWithoutNotify(player, false);
						}
					}
					else {
						return false;
					}
				}

				for(final TFData type : TFData.VALUES) {
					if(type instanceof TFDataPrev && ((TFDataPrev) type).tracking == this) {
						TFPlayerData.getData(player).putData(type, get(player));
					}
				}

				TFPlayerData.getData(player).putData(this, value);

				return true;
			}
//            else
//            {
//                // reset to default
//            }
		}

		return false;
	}

	public void incr(final EntityPlayer player, final T value) {
		if(value instanceof Integer) {
			final Integer i = (Integer) get(player) + (Integer) value;
			set(player, (T) i);
		}
		else if(value instanceof Float) {
			final Float f = (Float) get(player) + (Float) value;
			set(player, (T) f);
		}
		else if(value instanceof Double) {
			final Double d = (Double) get(player) + (Double) value;
			set(player, (T) d);
		}
		else if(value instanceof String) {
			final String s = get(player) + (String) value;
			set(player, (T) s);
		}
		else {
			throw new RuntimeException("Cannot increment a non-numerical data type unless a String!");
		}
	}

	public void incrWithoutNotify(final EntityPlayer player, final T value) {
		if(value instanceof Integer) {
			final Integer i = (Integer) get(player) + (Integer) value;
			setWithoutNotify(player, (T) i);
		}
		else if(value instanceof Float) {
			final Float f = (Float) get(player) + (Float) value;
			setWithoutNotify(player, (T) f);
		}
		else if(value instanceof Double) {
			final Double d = (Double) get(player) + (Double) value;
			setWithoutNotify(player, (T) d);
		}
		else if(value instanceof String) {
			final String s = get(player) + (String) value;
			setWithoutNotify(player, (T) s);
		}
		else {
			throw new RuntimeException("Cannot increment a non-numerical data type unless a String!");
		}
	}

	public void clamp(final EntityPlayer player, final T min, final T max) {
		if(min instanceof Integer) {
			if((Integer) get(player) < (Integer) min) {
				set(player, min);
			}
			else if((Integer) get(player) > (Integer) max) {
				set(player, max);
			}
		}
		else if(min instanceof Float) {
			if((Float) get(player) < (Float) min) {
				set(player, min);
			}
			else if((Float) get(player) > (Float) max) {
				set(player, max);
			}
		}
		else if(min instanceof Double) {
			if((Double) get(player) < (Double) min) {
				set(player, min);
			}
			else if((Double) get(player) > (Double) max) {
				set(player, max);
			}
		}
		else {
			throw new RuntimeException("Cannot clamp a non-numerical data type!");
		}
	}

	public void clampWithoutNotify(final EntityPlayer player, final T min, final T max) {
		if(min instanceof Integer) {
			if((Integer) get(player) < (Integer) min) {
				setWithoutNotify(player, min);
			}
			else if((Integer) get(player) > (Integer) max) {
				setWithoutNotify(player, max);
			}
		}
		else if(min instanceof Float) {
			if((Float) get(player) < (Float) min) {
				setWithoutNotify(player, min);
			}
			else if((Float) get(player) > (Float) max) {
				setWithoutNotify(player, max);
			}
		}
		else if(min instanceof Double) {
			if((Double) get(player) < (Double) min) {
				setWithoutNotify(player, min);
			}
			else if((Double) get(player) > (Double) max) {
				setWithoutNotify(player, max);
			}
		}
		else {
			throw new RuntimeException("Cannot clamp a non-numerical data type!");
		}
	}

	public T get(final EntityPlayer player) {
		T value = TFPlayerData.getData(player).getData(this);

		if(this == ALT_MODE) {
			if(!TFHelper.isTransformer(player) && (Integer) value != -1) {
				TFPlayerData.getData(player).putData(this, (T) Integer.valueOf(-1));
				value = (T) Integer.valueOf(-1);
			}
		}

		return value;
	}

	public NBTTagCompound writeDataToNBT(final NBTTagCompound nbt, final Object obj) {
		if(obj instanceof Integer) {
			nbt.setInteger(id, (Integer) obj);
		}
		else if(obj instanceof Float) {
			nbt.setFloat(id, (Float) obj);
		}
		else if(obj instanceof Double) {
			nbt.setDouble(id, (Double) obj);
		}
		else if(obj instanceof Boolean) {
			nbt.setBoolean(id, (Boolean) obj);
		}
		else if(obj instanceof String) {
			final String s = (String) obj;
			if(s != null && !s.isEmpty()) {
				nbt.setString(id, s);
			}
		}

		return nbt;
	}

	public Object readDataFromNBT(final NBTTagCompound nbt) {
		final NBTBase tag = nbt.getTag(id);

		if(tag instanceof NBTTagInt) {
			return ((NBTTagInt) tag).func_150287_d();
		}
		else if(tag instanceof NBTTagFloat) {
			return ((NBTTagFloat) tag).func_150288_h();
		}
		else if(tag instanceof NBTTagDouble) {
			return ((NBTTagDouble) tag).func_150286_g();
		}
		else if(tag instanceof NBTTagByte) {
			return ((NBTTagByte) tag).func_150290_f() == 1;
		}
		else if(tag instanceof NBTTagString) {
			return ((NBTTagString) tag).func_150285_a_();
		}

		return null;
	}
}
