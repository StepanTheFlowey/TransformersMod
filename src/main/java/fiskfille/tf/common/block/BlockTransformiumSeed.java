package fiskfille.tf.common.block;

import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.entity.EntityTransformiumSeed;
import fiskfille.tf.common.tileentity.TileEntityTransformiumSeed;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockTransformiumSeed extends BlockBasic implements ITileEntityProvider {
	public BlockTransformiumSeed() {
		super(Material.circuits);
		setResistance(1000000F);
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
			ignite(world, x, y, z, world.getBlockMetadata(x, y, z), null);
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
		if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
			ignite(world, x, y, z, world.getBlockMetadata(x, y, z), null);
			world.setBlockToAir(x, y, z);
		}
	}

	public void ignite(final World world, final int x, final int y, final int z, final int metadata, final EntityLivingBase entity) {
		if(!world.isRemote) {
			final EntityTransformiumSeed seed = new EntityTransformiumSeed(world, x + 0.5F, y, z + 0.5F);
			world.spawnEntityInWorld(seed);
			world.playSoundAtEntity(seed, "note.pling", 1F, 0.5F);
		}

		if(entity instanceof EntityPlayer) {
			((EntityPlayer) entity).addStat(TFAchievements.detonateSeed, 1);
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int metadata, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
		ignite(world, x, y, z, 1, player);
		world.setBlockToAir(x, y, z);
		return true;
	}

	@Override
	public MovingObjectPosition collisionRayTrace(final World world, final int x, final int y, final int z, final Vec3 src, final Vec3 dst) {
		final float f = 0.2F;
		setBlockBounds(0.5F - f, 0F, 0.5F - f, 0.5F + f, 1F, 0.5F + f);
		return super.collisionRayTrace(world, x, y, z, src, dst);
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
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new TileEntityTransformiumSeed();
	}
}
