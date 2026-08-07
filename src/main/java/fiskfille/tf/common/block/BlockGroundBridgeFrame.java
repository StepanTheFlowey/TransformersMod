package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.client.render.block.RenderBlockGroundBridgeFrame;
import fiskfille.tf.common.tileentity.TileEntityGroundBridgeFrame;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGroundBridgeFrame extends Block implements ITileEntityProvider {
	@SideOnly(Side.CLIENT) public IIcon centerIcon;

	public BlockGroundBridgeFrame() {
		super(Material.iron);
		setHarvestLevel(null, -1);
		setHardness(1);
		setResistance(5);
	}

	public static ForgeDirection getFrameDirection(final IBlockAccess world, final int x, final int y, final int z) {
		if(BlockGroundBridgeTeleporter.isNorthSouthFacingFramePresent(world, x, y, z)) {
			return ForgeDirection.NORTH;
		}
		else if(BlockGroundBridgeTeleporter.isEastWestFacingFramePresent(world, x, y, z)) {
			return ForgeDirection.EAST;
		}

		return null;
	}

	@Override
	public int getRenderType() {
		return RenderBlockGroundBridgeFrame.renderId;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		final int metadata = world.getBlockMetadata(x, y, z);

		if(getFrameDirection(world, x, y, z) != null) {
			if(metadata == 0) {
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);
			}
			else {
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
			}

			return true;
		}

		return false;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new TileEntityGroundBridgeFrame();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		return side == 1 && getFrameDirection(world, x, y, z) != null ? centerIcon : super.getIcon(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(getTextureName());
		centerIcon = iconRegister.registerIcon(getTextureName() + "_center");
	}
}
