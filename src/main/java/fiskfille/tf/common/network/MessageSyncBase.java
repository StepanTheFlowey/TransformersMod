package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.data.TFPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageSyncBase implements IMessage {
	public Map<TFData, Object> playerData;

	public MessageSyncBase() {

	}

	public MessageSyncBase(EntityPlayer player) {
		playerData = TFPlayerData.getData(player).data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		playerData = TFData.readFromNBT(ByteBufUtils.readTag(buf), new HashMap<>());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();

		TFData.writeToNBT(nbttagcompound, playerData);
		ByteBufUtils.writeTag(buf, nbttagcompound);
	}
}
