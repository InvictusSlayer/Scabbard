package net.invictusslayer.scabbard.world.level.item;

import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.stream.Stream;

public abstract class DispensableItems {
	public abstract void register();

	protected static void registerWoodFamilies(Stream<WoodFamily> families) {
		families.forEach(family -> {
			if (family.getBoatLayer(false) != null) DispenserBlock.registerBehavior((ItemLike) family.get(WoodFamily.Variant.BOAT_ITEM).get(), new BoatDispenseItemBehavior((EntityType<Boat>) family.get(WoodFamily.Variant.BOAT).get()));
			if (family.getBoatLayer(true) != null) DispenserBlock.registerBehavior((ItemLike) family.get(WoodFamily.Variant.CHEST_BOAT_ITEM).get(), new BoatDispenseItemBehavior((EntityType<ChestBoat>) family.get(WoodFamily.Variant.CHEST_BOAT).get()));
		});
	}
}
