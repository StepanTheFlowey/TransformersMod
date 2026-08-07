package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.client.render.block.RenderBlockAlloyCrucible;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockAlloyCrucible extends BlockMachineBase {
	public static final int FLAG_TOP = 4;
	public static final int FLAG_FRONT = 8;
	public static int renderPass;
	private IIcon bottomIcon;
	private IIcon[] topIcons;
	private IIcon[] frontIcons;

	public BlockAlloyCrucible() {
		super(Material.iron);
		setHarvestLevel("pickaxe", 1);
		setHardness(6F);
		setResistance(10F);
	}

	public static int getRotation(final int metadata) {
		return metadata & 3;
	}

	public static boolean getFlag(final int metadata, final int flag) {
		return (metadata & flag) == flag;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		if(!player.isSneaking()) {
			TFGui.ALLOY_CRUCIBLE.open(player, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		final IIcon topIcon;
		final IIcon frontIcon;

		if(renderPass == 1) {
			topIcon = topIcons[getFlag(metadata, FLAG_TOP) ? 2 : 0];
			frontIcon = frontIcons[getFlag(metadata, FLAG_FRONT) ? 2 : 0];
		}
		else {
			topIcon = topIcons[getFlag(metadata, FLAG_TOP) ? 0 : 1];
			frontIcon = frontIcons[getFlag(metadata, FLAG_FRONT) ? 0 : 1];
		}

		return side == 1 ? topIcon : side == 0 ? bottomIcon : side != new int[]{3, 4, 2, 5}[getRotation(metadata)] ? blockIcon : frontIcon;
	}

	@Override
	public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		if(renderPass == 1) {
			final ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
			final int metadata = world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
			boolean flag = side == 1 && getFlag(metadata, FLAG_TOP);

			if(getFlag(metadata, FLAG_FRONT)) {
				flag |= side == new int[]{3, 4, 2, 5}[getRotation(metadata)];
			}

			return flag;
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public int getMixedBrightnessForBlock(final IBlockAccess world, final int x, final int y, final int z) {
		if(renderPass == 1) {
			return 0xF000F0;
		}

		return super.getMixedBrightnessForBlock(world, x, y, z);
	}

	@Override
	public int getRenderType() {
		return RenderBlockAlloyCrucible.renderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister) {
		frontIcons = new IIcon[3];
		topIcons = new IIcon[3];

		for(int i = 0; i < 3; ++i) {
			final String[] astring = {"", "_off", "_on"};
			topIcons[i] = iconRegister.registerIcon(getTextureName() + "_top" + astring[i]);
			frontIcons[i] = iconRegister.registerIcon(getTextureName() + "_front" + astring[i]);
		}

		bottomIcon = iconRegister.registerIcon(getTextureName() + "_bottom");
		blockIcon = iconRegister.registerIcon(getTextureName() + "_side");
	}
}
