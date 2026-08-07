package fiskfille.tf.common.registry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.block.BlockMachineBase;
import fiskfille.tf.common.item.ItemBlockWithMetadata;
import fiskfille.tf.common.item.ItemMachine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class TFBlockRegistry {
	public static void registerBlock(final Block block, final String name) {
		final String unlocalizedName = name.toLowerCase().replace(' ', '_').replace("'", "");

		block.setBlockName(unlocalizedName);
		block.setBlockTextureName(getMod() + ":" + unlocalizedName);
		GameRegistry.registerBlock(block, unlocalizedName);

		if(FMLCommonHandler.instance().getSide().isClient() && block.getCreativeTabToDisplayOn() == null) {
			block.setCreativeTab(TransformersMod.tabTransformers);
		}
	}

	public static void registerItemBlock(final Block block, final String name, final Class clazz) {
		final String unlocalizedName = name.toLowerCase().replace(' ', '_').replace("'", "");

		block.setBlockName(unlocalizedName);
		block.setBlockTextureName(getMod() + ":" + unlocalizedName);
		GameRegistry.registerBlock(block, clazz, unlocalizedName);

		if(FMLCommonHandler.instance().getSide().isClient() && block.getCreativeTabToDisplayOn() == null) {
			block.setCreativeTab(TransformersMod.tabTransformers);
		}
	}

	public static void registerItemBlock(final Block block, final String name) {
		registerItemBlock(block, name, ItemBlockWithMetadata.class);
	}

	public static void registerItemBlockAsTileEntity(final Block block, final String name, final Class clazz, final Class clazz1) {
		registerItemBlock(block, name, clazz1);
		GameRegistry.registerTileEntity(clazz, name);

		if(block instanceof BlockMachineBase) {
			((BlockMachineBase) block).tileClass = clazz;
		}
	}

	public static void registerTileEntity(final Block block, final String name, final Class clazz) {
		registerItemBlockAsTileEntity(block, name, clazz, ItemBlock.class);
	}

	public static void registerMachine(final Block block, final String name, final Class clazz) {
		registerItemBlockAsTileEntity(block, name, clazz, ItemMachine.class);
	}

	private static String getMod() {
		return Loader.instance().activeModContainer().getModId();
	}
}
