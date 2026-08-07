package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.fluid.FluidEnergon;
import fiskfille.tf.common.fluid.IFluidHandlerTF;
import fiskfille.tf.common.fluid.TFFluids;
import fiskfille.tf.common.item.ItemFuelCanister;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BlockMachineBase extends Block implements ITileEntityProvider {
	public Class<? extends TileEntity> tileClass;

	protected BlockMachineBase(final Material material) {
		super(material);
	}

	public int getBlockHeight() {
		return 1;
	}

	public int getPlacedRotation(final EntityLivingBase entity) {
		return MathHelper.floor_double(entity.rotationYaw * 4F / 360F + 2.5D) & 3;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));
		final ItemStack heldItem = player.getHeldItem();

		if(tile instanceof IFluidHandlerTF && heldItem != null && heldItem.getItem() instanceof IFluidContainerItem) {
			final IFluidHandlerTF fluidHandler = (IFluidHandlerTF) tile;
			final IFluidContainerItem item = (IFluidContainerItem) heldItem.getItem();

			if(!ItemFuelCanister.isEmpty(heldItem)) {
				final FluidStack stack = item.getFluid(heldItem);

				if(stack != null && fluidHandler.canFill(ForgeDirection.UNKNOWN, stack.getFluid())) {
					final int amount = fluidHandler.fill(ForgeDirection.UNKNOWN, item.drain(heldItem, stack.amount, false), true);

					if(amount > 0) {
						final ItemStack newItem = new ItemStack(heldItem.getItem(), 1, heldItem.getItemDamage());
						item.fill(newItem, stack, true);
						item.drain(newItem, amount, true);

						player.setCurrentItemOrArmor(0, addItem(player, heldItem, newItem));

						return true;
					}
				}
			}

			final FluidStack stack = fluidHandler.getTank().getFluid();

			if(stack != null && fluidHandler.canDrain(ForgeDirection.UNKNOWN, item.getFluid(heldItem) == null ? stack.getFluid() : item.getFluid(heldItem).getFluid())) {
				final FluidStack drained = fluidHandler.drain(ForgeDirection.UNKNOWN, item.getCapacity(heldItem) - ItemFuelCanister.getFluidAmount(heldItem), false);

				if(drained != null && drained.amount > 0) {
					final ItemStack newItem = new ItemStack(heldItem.getItem(), 1, heldItem.getItemDamage());
					item.fill(newItem, item.getFluid(heldItem), true);

					final int amount = drained.amount;
					FluidStack stack1 = item.getFluid(newItem);

					if(stack1 == null) {
						stack1 = new FluidStack(TFFluids.energon, 0);
					}

					final FluidStack stack2 = new FluidStack(TFFluids.energon, amount);
					FluidEnergon.setRatios(stack2, FluidEnergon.getRatios(stack));
					final NBTTagCompound prevNBT = stack2.tag;

					stack2.tag = stack1.tag;
					final int i = item.fill(newItem, stack2, true);
					fluidHandler.drain(ForgeDirection.UNKNOWN, amount, true);
					stack1.amount += i;
					stack2.tag = prevNBT;

					FluidEnergon.merge(stack1, stack2, amount);

					if(!newItem.hasTagCompound()) {
						newItem.setTagCompound(new NBTTagCompound());
					}

					newItem.getTagCompound().setTag("Fluid", stack1.writeToNBT(new NBTTagCompound()));

					player.setCurrentItemOrArmor(0, addItem(player, heldItem, newItem));

					return true;
				}
			}
		}

		return false;
	}

	public ItemStack addItem(final EntityPlayer player, final ItemStack itemstack, final ItemStack newItem) {
		if(player.capabilities.isCreativeMode) {
			return itemstack;
		}
		else if(itemstack.stackSize - 1 <= 0) {
			return newItem;
		}
		else {
			--itemstack.stackSize;

			if(!player.inventory.addItemStackToInventory(newItem)) {
				player.dropPlayerItemWithRandomChoice(newItem, false);
			}

			return itemstack;
		}
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final int x, final int y, final int z) {
		boolean flag = super.canPlaceBlockAt(world, x, y, z);

		final int height = getBlockHeight();
		for(int i = 1; i < height; ++i) {
			flag &= super.canPlaceBlockAt(world, x, y + i, z);
		}

		return y + height - 1 < world.getHeight() && flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	public void addBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ, final int x, final int y, final int z, final AxisAlignedBB aabb, final List list) {
		final AxisAlignedBB aabb1 = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ).offset(x, y, z);

		if(aabb1.intersectsWith(aabb)) {
			list.add(aabb1);
		}
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return tileClass != null && (IFluidHandler.class.isAssignableFrom(tileClass) || IInventory.class.isAssignableFrom(tileClass));
	}

	@Override
	public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int metadata) {
		final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

		if(tile instanceof IFluidHandler) {
			final IFluidHandler fluidHandler = (IFluidHandler) tile;
			float amount = 0;
			float capacity = 0;

			for(final ForgeDirection dir : ForgeDirection.values()) {
				final FluidTankInfo[] info = fluidHandler.getTankInfo(dir);

				for(final FluidTankInfo fluidTankInfo : info) {
					capacity += fluidTankInfo.capacity;

					if(fluidTankInfo.fluid != null) {
						amount += fluidTankInfo.fluid.amount;
					}
				}
			}

			return Math.round(amount / capacity * 15);
		}
		else if(tile instanceof IInventory) {
			return Container.calcRedstoneFromInventory((IInventory) tile);
		}

		return 0;
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block block, final int metadata) {
		final TileEntity tile = world.getTileEntity(x, y, z);

		if(tile instanceof IInventory) {
			final IInventory inventory = (IInventory) tile;

			for(int i = 0; i < inventory.getSizeInventory(); ++i) {
				final ItemStack itemstack = inventory.getStackInSlot(i);

				if(itemstack != null) {
					final ThreadLocalRandom rand = ThreadLocalRandom.current();
					final float f = rand.nextFloat() * 0.8F + 0.1F;
					final float f1 = rand.nextFloat() * 0.8F + 0.1F;
					final float f2 = rand.nextFloat() * 0.8F + 0.1F;

					while(itemstack.stackSize > 0) {
						int j = rand.nextInt(21) + 10;

						if(j > itemstack.stackSize) {
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						final EntityItem entity = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

						if(itemstack.hasTagCompound()) {
							entity.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						final float f3 = 0.05F;
						entity.motionX = (float) rand.nextGaussian() * f3;
						entity.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
						entity.motionZ = (float) rand.nextGaussian() * f3;
						world.spawnEntityInWorld(entity);
					}
				}
			}

			world.func_147453_f(x, y, z, block);
		}

		final TileEntity tileBase = TFTileHelper.getTileBase(tile);

		if(tileBase != null && tile != tileBase && getBlockHeight() > 0) {
			world.setBlockToAir(tileBase.xCoord, tileBase.yCoord, tileBase.zCoord);
		}

		super.breakBlock(world, x, y, z, block, metadata);
	}

	@Override
	public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
		if(!world.isRemote && (player == null || !player.capabilities.isCreativeMode)) {
			final ItemStack itemstack = new ItemStack(this, 1, damageDropped(world.getBlockMetadata(x, y, z)));
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile != null) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				tile.writeToNBT(nbttagcompound);

				if(nbttagcompound.hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
					final NBTTagCompound config = nbttagcompound.getCompoundTag("ConfigDataTF");

					if(!itemstack.hasTagCompound()) {
						itemstack.setTagCompound(new NBTTagCompound());
					}

					itemstack.getTagCompound().setTag("ConfigDataTF", config);
				}
			}

			final double f = 0.7D;
			final double d0 = world.rand.nextFloat() * f + (1 - f) * 0.5D;
			final double d1 = world.rand.nextFloat() * f + (1 - f) * 0.5D;
			final double d2 = world.rand.nextFloat() * f + (1 - f) * 0.5D;
			final EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, itemstack);

			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
		}

		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}

	@Override
	public int quantityDropped(final Random random) {
		return 0;
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemstack) {
		super.onBlockPlacedBy(world, x, y, z, entity, itemstack);
		final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

		if(tile != null) {
			final NBTTagCompound nbttagcompound = new NBTTagCompound();
			tile.writeToNBT(nbttagcompound);

			if(itemstack != null && itemstack.hasTagCompound()) {
				if(itemstack.getTagCompound().hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
					final NBTTagCompound config = itemstack.getTagCompound().getCompoundTag("ConfigDataTF");
					nbttagcompound.setTag("ConfigDataTF", config);

					tile.readFromNBT(nbttagcompound);
				}
			}
		}

		for(int i = 0; i < getBlockHeight(); ++i) {
			final int metadata = getPlacedRotation(entity) + i * 4;

			if(metadata > 0) {
				if(i == 0) {
					world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
				}
				else {
					world.setBlock(x, y + i, z, this, metadata, 2);
				}
			}
		}

		if(!world.isRemote) {
			world.markBlockForUpdate(x, y, z);
		}
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
		final TileEntity tile = world.getTileEntity(x, y, z);
		final int metadata = world.getBlockMetadata(x, y, z);

		if(tile != null && getBlockHeight() > 0) {
			final int[] offsets = TFTileHelper.getTileBaseOffsets(tile, metadata);

			if(world.getBlock(x + offsets[0], y + offsets[1], z + offsets[2]) != this) {
				world.setBlockToAir(x, y, z);
			}
		}
	}

	@Override
	public boolean hasTileEntity(final int metadata) {
		return tileClass != null;
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		if(tileClass != null) {
			try {
				return tileClass.newInstance();
			}
			catch(final Exception e) {
				TransformersMod.log.error("Could not create tile entity for block '{}' from class {}", delegate.name(), tileClass);
			}
		}

		return null;
	}
}
