package fiskfille.tf.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockCompressedTF extends BlockBasic {
	private final MapColor mapColor;

	public BlockCompressedTF(final MapColor color) {
		super(Material.iron);
		mapColor = color;
	}

	@Override
	public MapColor getMapColor(final int metadata) {
		return mapColor;
	}
}
