package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.client.render.block.RenderBlockEnergonTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockEnergonTank extends BlockMachineBase {
	@SideOnly(Side.CLIENT) public static boolean renderingInside;
	public static int renderMetadata;
	public static int renderSide;

	private IIcon[] iconSides;
	private IIcon iconFront;
	private IIcon iconBack;

	public BlockEnergonTank() {
		super(Material.iron);
		setHarvestLevel("pickaxe", 1);
		setHardness(6F);
		setResistance(10F);
	}

	@Override
	public int getBlockHeight() {
		return 0;
	}

	@Override
	public int getPlacedRotation(final EntityLivingBase entity) {
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderBlockEnergonTank.renderId;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		final int metadata = world.getBlockMetadata(x, y, z);
		boolean flag = true;

		if((side == 0 || side == 1) && (metadata == 0 || metadata == 1)) {
			flag = player.getHeldItem() == null || player.getHeldItem().getItem() != Item.getItemFromBlock(this);
		}

		if(!player.isSneaking() && flag) {
			TFGui.ENERGON_TANK.open(player, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public int onBlockPlaced(final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ, final int metadata) {
		return Facing.oppositeSide[side];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		final Block block = world.getBlock(x, y, z);
		boolean flag = renderingInside || !block.isOpaqueCube();

		if(side == renderMetadata || Facing.oppositeSide[side] == renderMetadata) {
			final ForgeDirection dir = ForgeDirection.getOrientation(side);

			if(dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
				final int metadata = world.getBlockMetadata(x, y, z);

				if(block == this && (metadata == side || metadata == Facing.oppositeSide[side])) {
					flag = false;
				}
			}
		}

		return side == 0 && minY > 0 || side == 1 && maxY < 1 || side == 2 && minZ > 0 || side == 3 && maxZ < 1 || side == 4 && minX > 0 || side == 5 && maxX < 1 || flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		final IIcon icon = side == metadata ? iconFront : Facing.oppositeSide[side] == metadata ? iconBack : super.getIcon(side, metadata);

		if(renderSide > 0) {
			if(icon == super.getIcon(side, metadata)) {
				return iconSides[renderSide - 1];
			}

			return iconSides[3];
		}

		return icon;
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		super.registerBlockIcons(iconRegister);
		iconSides = new IIcon[4];

		for(int i = 0; i < iconSides.length; ++i) {
			iconSides[i] = iconRegister.registerIcon(getTextureName() + "_side_" + i);
		}

		iconFront = iconRegister.registerIcon(getTextureName() + "_front");
		iconBack = iconRegister.registerIcon(getTextureName() + "_back");
	}
}
