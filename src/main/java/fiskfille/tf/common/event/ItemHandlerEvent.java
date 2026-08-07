package fiskfille.tf.common.event;

import cpw.mods.fml.common.eventhandler.Event;

import java.util.Map;

public abstract class ItemHandlerEvent extends Event {
	public static class Init extends ItemHandlerEvent {
		public final Map<Class, String> itemHandlers;

		public Init(final Map<Class, String> handlers) {
			super();
			itemHandlers = handlers;
		}

		public void registerItemHandler(final String modid, final Class handlerClass) {
			itemHandlers.put(handlerClass, modid);
		}
	}
}
