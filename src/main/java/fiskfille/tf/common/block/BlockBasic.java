package fiskfille.tf.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBasic extends Block {
	public BlockBasic(final Material material) {
		super(material);
	}

	public BlockBasic setHarvestLvl(final String tool, final int level) {
		setHarvestLevel(tool, level);
		return this;
	}
}
