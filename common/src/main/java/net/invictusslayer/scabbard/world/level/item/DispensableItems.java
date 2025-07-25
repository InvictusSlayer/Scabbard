package net.invictusslayer.scabbard.world.level.item;

import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.stream.Stream;

public abstract class DispensableItems {
	protected static void registerWoodFamilies(Stream<WoodFamily> families) {
		families.forEach(family -> {
			Boat.Type type = family.getBoatType();
			if (type == null) return;
			DispenserBlock.registerBehavior((ItemLike) family.get(WoodFamily.Variant.BOAT).get(), new BoatDispenseItemBehavior(type));
			DispenserBlock.registerBehavior((ItemLike) family.get(WoodFamily.Variant.CHEST_BOAT).get(), new BoatDispenseItemBehavior(type, true));
		});
	}
}
