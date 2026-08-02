package fiskfille.tf.common.block;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.tileentity.TileEntityColumn;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockColumn extends BlockMachineBase {
	public BlockColumn() {
		super(Material.iron);
		setHardness(5F);
		setResistance(10F);
	}

	@Override
	public int getBlockHeight() {
		return 2;
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
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		final float width = 0.0625F * 3.125F;

		if(world.getBlockMetadata(x, y, z) < 4) {
			setBlockBounds(width, 0, width, 1 - width, 2, 1 - width);
		}
		else {
			setBlockBounds(width, -1, width, 1 - width, 1, 1 - width);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		if(!player.isSneaking()) {
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile instanceof TileEntityColumn) {
				TFGui.ENERGY_COLUMN.open(player, tile);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
//        return side == ForgeDirection.UP || side == ForgeDirection.DOWN;
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("iron_block");
	}
}
