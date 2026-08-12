package fiskfille.tf.common.block;

import fiskfille.tf.client.render.block.RenderBlockDisplayPedestal;
import fiskfille.tf.common.tileentity.TileEntityDisplayPedestal;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockDisplayPedestal extends BlockMachineBase {
	private static ArrayList<BlockIcon[]> textures = null;

	public BlockDisplayPedestal() {
		super(TFMaterial.display);
		setHardness(2F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 0);
	}

	public static ArrayList<BlockIcon[]> getTextures() {
	if(textures != null){
		return textures;
	}

		textures = new ArrayList<>();
		textures.add(new BlockIcon[]{new BlockIcon(Blocks.cobblestone, 0, 0), new BlockIcon(Blocks.stonebrick, 0, 0), new BlockIcon(Blocks.stone, 0, 0)});
		textures.add(new BlockIcon[]{new BlockIcon(Blocks.stonebrick, 0, 3), new BlockIcon(Blocks.stonebrick, 0, 1), new BlockIcon(Blocks.stonebrick, 0, 2)});
		textures.add(new BlockIcon[]{new BlockIcon(Blocks.sandstone, 2, 0), new BlockIcon(Blocks.sandstone, 2, 1), new BlockIcon(Blocks.sandstone, 1, 0)});
		textures.add(new BlockIcon[]{new BlockIcon(Blocks.quartz_block, 1, 1), new BlockIcon(Blocks.quartz_block, 2, 2), new BlockIcon(Blocks.quartz_block, 1, 2)});
		textures.add(new BlockIcon[]{new BlockIcon(Blocks.nether_brick, 0, 0), new BlockIcon(Blocks.netherrack, 0, 0), new BlockIcon(Blocks.nether_brick, 0, 0)});

		for(int i = 0; i < 6; ++i) {
			final Block log = i < 4 ? Blocks.log : Blocks.log2;
			textures.add(new BlockIcon[]{new BlockIcon(Blocks.planks, 0, i), new BlockIcon(log, 2, i % 4), new BlockIcon(Blocks.planks, 0, i)});
		}

		return textures;
	}

	public static BlockIcon[] getTexture(final int metadata) {
		return getTextures().get(MathHelper.clamp_int(metadata, 0, getTextures().size() - 1));
	}

	public static AxisAlignedBB[] getBounds() {
		final float f = 0.0625F;
		return new AxisAlignedBB[]{AxisAlignedBB.getBoundingBox(f * 2, 0, f * 2, f * 14, f * 2, f * 14), AxisAlignedBB.getBoundingBox(f * 5, f * 2, f * 5, 1 - f * 5, f * 7, 1 - f * 5), AxisAlignedBB.getBoundingBox(f * 4, f * 7, f * 4, 1 - f * 4, f * 9, 1 - f * 4)};
	}

	@Override
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for(int i = 0; i < getTextures().size(); ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getHarvestTool(final int metadata) {
		final BlockIcon icon = getTexture(metadata)[0];
		return icon.block.getHarvestTool(icon.metadata);
	}

	@Override
	public boolean isToolEffective(final String type, final int metadata) {
		final BlockIcon icon = getTexture(metadata)[0];
		return icon.block.isToolEffective(type, icon.metadata);
	}

	@Override
	public int getFlammability(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
		final BlockIcon[] icons = getTexture(world.getBlockMetadata(x, y, z));
		int fire = 0;

		for(final BlockIcon icon : icons) {
			fire += Blocks.fire.getFlammability(icon.block);
		}

		return fire / icons.length;
	}

	@Override
	public int getFireSpreadSpeed(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
		final BlockIcon[] icons = getTexture(world.getBlockMetadata(x, y, z));
		int fire = 0;

		for(final BlockIcon icon : icons) {
			fire += Blocks.fire.getEncouragement(icon.block);
		}

		return fire / icons.length;
	}

	@Override
	public int getPlacedRotation(final EntityLivingBase entity) {
		return 0;
	}

	@Override
	public int damageDropped(final int metadata) {
		return metadata;
	}

	@Override
	public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB aabb, final List list, final Entity entity) {
		for(final AxisAlignedBB aabb1 : getBounds()) {
			addBox(aabb1.minX, aabb1.minY, aabb1.minZ, aabb1.maxX, aabb1.maxY, aabb1.maxZ, x, y, z, aabb, list);
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		final TileEntityDisplayPedestal tile = (TileEntityDisplayPedestal) world.getTileEntity(x, y, z);
		if(tile != null) {
			final ItemStack heldItem = player.getHeldItem();
			final ItemStack displayItem = tile.getDisplayItem();

			if(heldItem != null) {
				if(tile.isItemValidForSlot(0, heldItem)) {
					if(displayItem != null) {
						if(displayItem.isStackable() && heldItem.getItem() == displayItem.getItem() && heldItem.getItemDamage() == displayItem.getItemDamage() && ItemStack.areItemStackTagsEqual(heldItem, displayItem)) {
							if(displayItem.stackSize < displayItem.getMaxStackSize()) {
								final int amount = Math.min(heldItem.stackSize, displayItem.getMaxStackSize() - displayItem.stackSize);
								displayItem.stackSize += amount;

								if((heldItem.stackSize -= amount) <= 0) {
									player.setCurrentItemOrArmor(0, null);
								}

								return true;
							}
						}
						else {
							tile.setDisplayItem(heldItem, true);
							player.setCurrentItemOrArmor(0, displayItem);

							return true;
						}
					}
					else {
						tile.setDisplayItem(heldItem, true);
						player.setCurrentItemOrArmor(0, null);

						return true;
					}
				}
			}
			else if(displayItem != null) {
				player.setCurrentItemOrArmor(0, displayItem);
				tile.setDisplayItem(null, true);

				return true;
			}
		}

		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
		AxisAlignedBB aabb = null;

		for(final AxisAlignedBB aabb1 : getBounds()) {
			if(aabb == null) {
				aabb = aabb1;
			}
			else {
				aabb = aabb.func_111270_a(aabb1);
			}
		}

		setBlockBounds((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ);
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
		return RenderBlockDisplayPedestal.renderId;
	}

	@Override
	public IIcon getIcon(final int side, final int metadata) {
		final BlockIcon icon = getTexture(metadata)[side % getTexture(metadata).length];
		return icon.block.getIcon(icon.side, icon.metadata);
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
	}

	public static class BlockIcon {
		public final Block block;
		public final int side;
		public final int metadata;

		public BlockIcon(final Block block, final int side, final int metadata) {
			this.block = block;
			this.side = side;
			this.metadata = metadata;
		}
	}
}
