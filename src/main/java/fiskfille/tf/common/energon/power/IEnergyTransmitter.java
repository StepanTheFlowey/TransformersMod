package fiskfille.tf.common.energon.power;

import net.minecraft.util.Vec3;

public interface IEnergyTransmitter extends IEnergyContainer {
	TransmissionHandler getTransmissionHandler();

	int getTransmissionRate();

	int getRange();

	Vec3 getEnergyOutputOffset();
}
