package fiskfille.tf.waila;

import fiskfille.tf.common.tileentity.*;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistrar {
	public static void wailaCallback(final IWailaRegistrar registrar) {
		registrar.addConfig("Transformers Mod", "tf.energon_processor");
		registrar.addConfig("Transformers Mod", "tf.alloy_crucible");
		registrar.addConfig("Transformers Mod", "tf.energon_tank");
		registrar.addConfig("Transformers Mod", "tf.transmitter");
		registrar.addConfig("Transformers Mod", "tf.relay_tower");
		registrar.addConfig("Transformers Mod", "tf.relay_torch");
		registrar.addConfig("Transformers Mod", "tf.energy_column");
		registrar.addConfig("Transformers Mod", "tf.isotopic_condenser");
		registrar.addConfig("Transformers Mod", "tf.control_panel");

		registrar.registerBodyProvider(new DataProviderMachine("tf.energon_processor", TileEntityEnergonProcessor.class), TileEntityEnergonProcessor.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.alloy_crucible", TileEntityAlloyCrucible.class), TileEntityAlloyCrucible.class);
		registrar.registerBodyProvider(new DataProviderEnergonTank("tf.energon_tank"), TileEntityEnergonTank.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.transmitter", TileEntityTransmitter.class), TileEntityTransmitter.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.relay_tower", TileEntityRelayTower.class), TileEntityRelayTower.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.relay_torch", TileEntityRelayTorch.class), TileEntityRelayTorch.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.energy_column", TileEntityColumn.class), TileEntityColumn.class);
		registrar.registerBodyProvider(new DataProviderMachine("tf.isotopic_condenser", TileEntityIsoCondenser.class), TileEntityIsoCondenser.class);
		registrar.registerBodyProvider(new DataProviderControlPanel("tf.control_panel"), TileEntityControlPanel.class);
	}
}
