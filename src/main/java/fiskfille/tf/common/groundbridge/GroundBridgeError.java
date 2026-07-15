package fiskfille.tf.common.groundbridge;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.StatCollector;

import java.util.List;

public enum GroundBridgeError {
	INVALID_COORDS, NOT_ENOUGH_SPACE, NOT_ENOUGH_ENERGY, NO_PORTAL_LINKED, PORTAL_OBSTRUCTED, OUT_OF_BOUNDS;

	public static class ErrorContainer {
		private final GroundBridgeError error;
		private final Integer[] arguments;

		public ErrorContainer(GroundBridgeError error, Integer... arguments) {
			this.error = error;
			this.arguments = arguments;
		}

		public static ErrorContainer fromBytes(ByteBuf buf) {
			try {
				GroundBridgeError error = GroundBridgeError.values()[buf.readByte()];
				List<Integer> list = Lists.newArrayList();

				int length = buf.readByte() & 0xFF;

				for(int i = 0; i < length; ++i) {
					list.add(buf.readInt());
				}

				return new ErrorContainer(error, list.toArray(new Integer[0]));
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
			return StatCollector.translateToLocalFormatted("ground_bridge.error." + error.name().toLowerCase(), (Object[]) arguments);
		}

		public void toBytes(ByteBuf buf) {
			buf.writeByte(error.ordinal() & 0xFF);
			buf.writeByte(arguments.length & 0xFF);

			for(Integer argument : arguments) {
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
				ErrorContainer container = (ErrorContainer) obj;
				boolean flag = container.arguments.length == arguments.length;

				if(flag) {
					for(int i = 0; i < arguments.length; ++i) {
						if(!container.arguments[i].equals(arguments[i])) {
							return false;
						}
					}
				}

				return container.error == error && flag;
			}

			return false;
		}
	}
}
