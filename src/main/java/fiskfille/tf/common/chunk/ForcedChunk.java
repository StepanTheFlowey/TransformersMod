package fiskfille.tf.common.chunk;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class ForcedChunk extends ChunkCoordIntPair {
	public final World worldObj;

	public ForcedChunk(final World world, final int chunkX, final int chunkZ) {
		super(chunkX, chunkZ);
		worldObj = world;
	}

	public static ForcedChunk fromTile(final TileEntity tile) {
		return new ForcedChunk(tile.getWorldObj(), tile.xCoord >> 4, tile.zCoord >> 4);
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		else if(!(obj instanceof ForcedChunk)) {
			return false;
		}
		else {
			final ForcedChunk forcedChunk = (ForcedChunk) obj;
			return chunkXPos == forcedChunk.chunkXPos && chunkZPos == forcedChunk.chunkZPos && worldObj.equals(forcedChunk.worldObj);
		}
	}

	@Override
	public String toString() {
		return "[" + chunkXPos + ", " + chunkZPos + ", " + worldObj + "]";
	}
}
