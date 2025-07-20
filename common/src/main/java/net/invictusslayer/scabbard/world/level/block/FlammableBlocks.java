package net.invictusslayer.scabbard.world.level.block;

import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class FlammableBlocks {
	public abstract void register();

	protected static void registerWoodFamilies(Stream<WoodFamily> families) {
		families.filter(WoodFamily::isFlammable).forEach(family -> family.getVariants().forEach((variant, supplier) -> {
			switch (variant) {
				case LOG, WOOD, STRIPPED_LOG, STRIPPED_WOOD -> register(supplier, 5, 5);
				case LEAVES -> register(supplier, 60, 30);
				case PLANKS, STAIRS, SLAB, FENCE, FENCE_GATE -> register(supplier, 20, 5);
			}
		}));
	}

	protected static void register(Supplier<?> block, int flammability, int encouragement) {
		((FireBlock) Blocks.FIRE).setFlammable((Block) block.get(), encouragement, flammability);
	}
}
