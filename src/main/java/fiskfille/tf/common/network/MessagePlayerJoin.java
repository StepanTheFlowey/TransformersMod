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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class MessagePlayerJoin extends MessageSyncBase {
	private Map<Transformer, Boolean> canTransform;
	private Map<Integer, String> dimensionNames = Maps.newHashMap();
	private Integer[] dimensionIDs;

	public MessagePlayerJoin() {}

	public MessagePlayerJoin(EntityPlayer player) {
		super(player);
		canTransform = TFConfig.canTransform;

		if(canTransform.isEmpty() || !canTransform.keySet().containsAll(TransformersAPI.getTransformers())) {
			for(Transformer transformer : TransformersAPI.getTransformers()) {
				if(!canTransform.containsKey(transformer)) {
					canTransform.put(transformer, true);
				}
			}
		}

		dimensionNames = TFDimensionHelper.dimensionNames;

		final Integer[] ids = DimensionManager.getIDs();
		for(int id : ids) {
			final WorldServer world = MinecraftServer.getServer().worldServerForDimension(id);

			if(world != null && world.provider != null) {
				dimensionNames.put(id, world.provider.getDimensionName());
			}
		}

		final ArrayList<Integer> list = new ArrayList<>();
		for(int id : ids) {
			if(DimensionManager.shouldLoadSpawn(id)) {
				list.add(id);
			}
		}

		list.sort(Comparator.comparing(Double::valueOf));

		TFDimensionHelper.dimensionIDs = dimensionIDs = list.toArray(new Integer[0]);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		canTransform = Maps.newHashMap();

		for(Transformer transformer : TransformersAPI.getTransformers()) {
			canTransform.put(transformer, buf.readBoolean());
		}

		final int length = buf.readInt();
		for(int i = 0; i < length; ++i) {
			dimensionNames.put(buf.readInt(), ByteBufUtils.readUTF8String(buf));
		}

		dimensionIDs = new Integer[buf.readInt()];
		for(int i = 0; i < dimensionIDs.length; ++i) {
			dimensionIDs[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);

		for(Map.Entry<Transformer, Boolean> transformable : canTransform.entrySet()) {
			buf.writeBoolean(transformable.getValue());
		}

		buf.writeInt(dimensionNames.size());
		for(Map.Entry<Integer, String> e : dimensionNames.entrySet()) {
			buf.writeInt(e.getKey());
			ByteBufUtils.writeUTF8String(buf, e.getValue());
		}

		buf.writeInt(dimensionIDs.length);
		for(Integer dimensionID : dimensionIDs) {
			buf.writeInt(dimensionID);
		}
	}

	public static class Handler implements IMessageHandler<MessagePlayerJoin, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayerJoin message, MessageContext ctx) {
			if(ctx.side.isClient()) {
				final EntityPlayer player = TransformersMod.proxy.getPlayer();

				for(Map.Entry<TFData, Object> e : message.playerData.entrySet()) {
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
