package fiskfille.tf.common.network;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFDimensionHelper;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;

public class MessagePlayerJoin extends MessageSyncBase {
	private HashMap<Transformer, Boolean> canTransform;
	private HashMap<Integer, String> dimensionNames = new HashMap<>();
	private int[] dimensionIDs;

	public MessagePlayerJoin() {}

	public MessagePlayerJoin(final EntityPlayer player) {
		super(player);
		canTransform = TFConfig.canTransform;

		if(canTransform.isEmpty() || !canTransform.keySet().containsAll(TransformersAPI.getTransformers())) {
			for(final Transformer transformer : TransformersAPI.getTransformers()) {
				if(!canTransform.containsKey(transformer)) {
					canTransform.put(transformer, true);
				}
			}
		}

		dimensionNames = TFDimensionHelper.dimensionNames;

		final Integer[] ids = DimensionManager.getIDs();
		for(final int id : ids) {
			final WorldServer world = MinecraftServer.getServer().worldServerForDimension(id);

			if(world != null && world.provider != null) {
				dimensionNames.put(id, world.provider.getDimensionName());
			}
		}

		final IntArrayList list = new IntArrayList();
		for(final int id : ids) {
			if(DimensionManager.shouldLoadSpawn(id)) {
				list.add(id);
			}
		}

		list.sort(IntComparator.comparing(Double::valueOf));

		TFDimensionHelper.dimensionIDs = dimensionIDs = list.elements();
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);
		canTransform = Maps.newHashMap();

		for(final Transformer transformer : TransformersAPI.getTransformers()) {
			canTransform.put(transformer, buf.readBoolean());
		}

		final int length = buf.readInt();
		for(int i = 0; i < length; ++i) {
			dimensionNames.put(buf.readInt(), ByteBufUtils.readUTF8String(buf));
		}

		dimensionIDs = new int[buf.readInt()];
		for(int i = 0; i < dimensionIDs.length; ++i) {
			dimensionIDs[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);

		for(final Map.Entry<Transformer, Boolean> transformable : canTransform.entrySet()) {
			buf.writeBoolean(transformable.getValue());
		}

		buf.writeInt(dimensionNames.size());
		for(final Map.Entry<Integer, String> e : dimensionNames.entrySet()) {
			buf.writeInt(e.getKey());
			ByteBufUtils.writeUTF8String(buf, e.getValue());
		}

		buf.writeInt(dimensionIDs.length);
		for(final int dimensionID : dimensionIDs) {
			buf.writeInt(dimensionID);
		}
	}

	public static class Handler implements IMessageHandler<MessagePlayerJoin, IMessage> {
		@Override
		public IMessage onMessage(final MessagePlayerJoin message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final EntityPlayer player = TransformersMod.proxy.getPlayer();

				for(final Map.Entry<TFData, Object> e : message.playerData.entrySet()) {
					e.getKey().setWithoutNotify(player, e.getValue());
				}

				if(message.canTransform != null) {
					TFConfig.canTransform = message.canTransform;
				}

				TFDimensionHelper.dimensionNames = message.dimensionNames;
				TFDimensionHelper.dimensionIDs = message.dimensionIDs;
			}

			return null;
		}
	}
}
