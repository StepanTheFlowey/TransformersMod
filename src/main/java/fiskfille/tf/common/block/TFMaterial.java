package fiskfille.tf.common.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class TFMaterial extends Material {
	public static final TFMaterial energon = new TFMaterial(MapColor.diamondColor).setBurning();
	public static final TFMaterial display = new TFMaterial(MapColor.stoneColor);

	protected boolean isTranslucent;

	public TFMaterial(final MapColor color) {
		super(color);
	}

	protected TFMaterial setTranslucent() {
		isTranslucent = true;
		return this;
	}

	@Override
	public boolean isOpaque() {
		return !isTranslucent && blocksMovement();
	}

	@Override
	protected TFMaterial setRequiresTool() {
		return (TFMaterial) super.setRequiresTool();
	}

	@Override
	protected TFMaterial setBurning() {
		return (TFMaterial) super.setBurning();
	}

	@Override
	protected TFMaterial setNoPushMobility() {
		return (TFMaterial) super.setNoPushMobility();
	}

	@Override
	protected TFMaterial setImmovableMobility() {
		return (TFMaterial) super.setImmovableMobility();
	}

	@Override
	protected TFMaterial setAdventureModeExempt() {
		return (TFMaterial) super.setAdventureModeExempt();
	}
}
