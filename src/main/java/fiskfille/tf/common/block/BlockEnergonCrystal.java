package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.common.tileentity.TileEntityCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEnergonCrystal extends BlockBasic implements ITileEntityProvider, IEnergon {
	private final Energon energonType;

	public BlockEnergonCrystal(Energon type) {
		super(TFMaterial.energon);
		energonType = type;

		setHarvestLvl("pickaxe", 1);
		setStepSound(Block.soundTypeGlass);
		setHardness(6);
		setResistance(10);
		setLightLevel(0.5F);
	}

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return 255;
	}

	@Override
	public Energon getEnergonType() {
		return energonType;
	}

	@Override
	public int getMass() {
		return Energon.CRYSTAL_FULL;
	}

	@Override
	public MapColor getMapColor(int metadata) {
		return MapColor.airColor;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) + 2;
	}

	@Override
	public Item getItemDropped(int metadata, Random random, int fortune) {
		return energonType.getCrystalPiece();
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return ThreadLocalRandom.current().nextInt(0, 3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		float f = 0.21F;
		switch(ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)).getOpposite()) {
			case UP:
				f = 0.2F;
				setBlockBounds(0.5F - f, f * 2, 0.5F - f, 0.5F + f, 1, 0.5F + f);
				break;

			case DOWN:
				f = 0.2F;
				setBlockBounds(0.5F - f, 0, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
				break;

			case WEST:
				setBlockBounds(0, 0.2F, 0.5F - f, f * 2, 0.8F, 0.5F + f);
				break;

			case EAST:
				setBlockBounds(1 - f * 2, 0.2F, 0.5F - f, 1, 0.8F, 0.5F + f);
				break;

			case NORTH:
				setBlockBounds(0.5F - f, 0.2F, 0, 0.5F + f, 0.8F, f * 2);
				break;

			case SOUTH:
				setBlockBounds(0.5F - f, 0.2F, 1 - f * 2, 0.5F + f, 0.8F, 1);
				break;
		}
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
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if(world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite(), false)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		final ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();

		if(world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite(), false)) {
			return side;
		}

		for(ForgeDirection dir1 : ForgeDirection.VALID_DIRECTIONS) {
			if(world.isSideSolid(x + dir1.offsetX, y + dir1.offsetY, z + dir1.offsetZ, dir1.getOpposite(), false)) {
				return dir1.getOpposite().ordinal();
			}
		}

		return 0;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		final ForgeDirection dir = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)).getOpposite();

		if(!world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite(), false)) {
			final ThreadLocalRandom random = ThreadLocalRandom.current();

			if(random.nextInt(9) == 0) {
				// do not drop items while restoring blockstates, prevents item dupe
				if(!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) {
					final float f = 0.7F;
					final double motionX = random.nextFloat() * f + (1 - f) * 0.5D;
					final double motionY = random.nextFloat() * f + (1 - f) * 0.5D;
					final double motionZ = random.nextFloat() * f + (1 - f) * 0.5D;
					final EntityItem entityitem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, new ItemStack(energonType.getCrystal()));
					entityitem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(entityitem);
				}
			}
			else {
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			}

			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityCrystal();
	}
}
