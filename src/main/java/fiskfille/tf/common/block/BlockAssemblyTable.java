package fiskfille.tf.common.block;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockAssemblyTable extends BlockMachineBase {
	public BlockAssemblyTable() {
		super(Material.iron);
		setHarvestLevel("pickaxe", 1);
		setStepSound(Block.soundTypeMetal);
		setHardness(6F);
		setResistance(10F);
		setLightLevel(0.5F);
		setBlockBounds(0, 0, 0, 1, 0.0625F * 13, 1);
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		if(!player.isSneaking()) {
			TFGui.ASSEMBLY_TABLE.open(player, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("iron_block");
	}
}
