package fiskfille.tf.common.data;

import com.google.common.base.Predicate;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TFPredicates {
	public static Predicate<EntityPlayer> and(final Predicate... predicates) {
		return input -> {
			for(Predicate p : predicates) {
				if(!p.apply(input)) {
					return false;
				}
			}

			return true;
		};
	}

	public static Predicate<EntityPlayer> or(final Predicate... predicates) {
		return input -> {
			for(Predicate p : predicates) {
				if(p.apply(input)) {
					return true;
				}
			}

			return false;
		};
	}

	public static Predicate<EntityPlayer> not(final Predicate p) {
		return input -> !p.apply(input);
	}

	public static Predicate<EntityPlayer> isTransformer() {
		return input -> TFHelper.isTransformer(input);
	}

	public static Predicate<EntityPlayer> isTransformer(final Transformer transformer) {
		return input -> TFHelper.getTransformer(input) == transformer;
	}

	public static Predicate<EntityPlayer> isInVehicleMode() {
		return TFHelper::isFullyTransformed;
	}

	public static Predicate<EntityPlayer> hasStealthForce() {
		return input -> {
			Transformer transformer = TFHelper.getTransformer(input);
			return transformer != null && transformer.hasStealthForce();
		};
	}

	public static Predicate<EntityPlayer> isSneaking() {
		return Entity::isSneaking;
	}

	public static Predicate<EntityPlayer> isBacking() {
		return input -> input.moveForward < 0;
	}

	public static Predicate<EntityPlayer> isFlying() {
		return input -> input.capabilities.isFlying;
	}
}
