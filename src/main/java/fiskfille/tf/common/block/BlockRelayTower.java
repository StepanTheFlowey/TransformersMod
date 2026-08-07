package fiskfille.tf.common.block;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.tileentity.TileEntityRelayTower;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockRelayTower extends BlockMachineBase {
	public BlockRelayTower() {
		super(Material.rock);
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
	public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB aabb, final List list, final Entity entity) {
		final int metadata = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;
		float width = f * 2;

		if(metadata < 4) {
			addBox(width, 0, width, 1 - width, f * 2, 1 - width, x, y, z, aabb, list);
			width = f * 4;
			addBox(width, f * 2, width, 1 - width, f * 14, 1 - width, x, y, z, aabb, list);
			width = f * 5;
			addBox(width, f * 14, width, 1 - width, f * 22, 1 - width, x, y, z, aabb, list);
			width = f * 6;
			addBox(width, f * 22, width, 1 - width, 2, 1 - width, x, y, z, aabb, list);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
		final int metadata = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;
		final float width = f * 4;

		if(metadata < 4) {
			setBlockBounds(width, 0, width, 1 - width, 2, 1 - width);
		}
		else {
			setBlockBounds(width, -1, width, 1 - width, 1, 1 - width);
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		if(!player.isSneaking()) {
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile instanceof TileEntityRelayTower) {
				TFGui.RECEIVER_NETWORK.open(player, tile);
			}

			return true;
		}

		return false;
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("stone");
	}
}
