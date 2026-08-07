package fiskfille.tf.common.groundbridge;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.StatCollector;

import java.util.Arrays;

public enum GroundBridgeError {
	INVALID_COORDS,
	NOT_ENOUGH_SPACE,
	NOT_ENOUGH_ENERGY,
	NO_PORTAL_LINKED,
	PORTAL_OBSTRUCTED,
	OUT_OF_BOUNDS;

	public static class ErrorContainer {
		private final GroundBridgeError error;
		private final Integer[] arguments;

		public ErrorContainer(GroundBridgeError error, Integer... arguments) {
			this.error = error;
			this.arguments = arguments;
		}

		public static ErrorContainer fromBytes(ByteBuf buf) {
			try {
				final byte index = buf.readByte();
				final byte length = buf.readByte();

				Integer[] list = new Integer[length];
				for(byte i = 0; i < length; ++i) {
					list[i] = buf.readInt();
				}

				return new ErrorContainer(GroundBridgeError.values()[index], list);
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		public GroundBridgeError getError() {
			return error;
		}

		public String translate() {
			return StatCollector.translateToLocalFormatted(
							"ground_bridge.error." + error.name().toLowerCase(),
							(Object[]) arguments
			);
		}

		public void toBytes(ByteBuf buf) {
			buf.writeByte(error.ordinal());
			buf.writeByte(arguments.length);

			for(final int argument : arguments) {
				buf.writeInt(argument);
			}
		}

		@Override
		public String toString() {
			return getError().toString();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof ErrorContainer) {
				final ErrorContainer container = (ErrorContainer) obj;
				return container.error == error && Arrays.equals(container.arguments, arguments);
			}

			return false;
		}
	}
}
