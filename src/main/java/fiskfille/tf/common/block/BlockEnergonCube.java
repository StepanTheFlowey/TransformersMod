package fiskfille.tf.common.block;

import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.helper.TFMathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.IBlockAccess;

public class BlockEnergonCube extends BlockBasic implements IEnergon {
	private final Energon energonType;

	public BlockEnergonCube(final Energon type) {
		super(TFMaterial.energon);
		energonType = type;

		setHarvestLvl("pickaxe", 1);
		setStepSound(Block.soundTypeMetal);
		setHardness(6F);
		setResistance(10F);
		setLightLevel(0.5F);
	}

	@Override
	public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z) {
		return 0xF000F0;
	}

	@Override
	public Energon getEnergonType() {
		return energonType;
	}

	@Override
	public int getMass() {
		return Energon.CRYSTAL_BLOCK;
	}

	@Override
	public MapColor getMapColor(final int metadata) {
		return TFMathHelper.getClosestMapColor(getEnergonType().getColor());
	}
}
