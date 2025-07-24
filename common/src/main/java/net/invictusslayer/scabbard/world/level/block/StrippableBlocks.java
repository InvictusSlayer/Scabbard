package net.invictusslayer.scabbard.world.level.block;

import com.google.common.collect.Maps;
import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class StrippableBlocks {
	protected static void registerWoodFamilies(Stream<WoodFamily> families) {
		families.forEach(family -> {
			register(family.get(WoodFamily.Variant.LOG), family.get(WoodFamily.Variant.STRIPPED_LOG));
			register(family.get(WoodFamily.Variant.WOOD), family.get(WoodFamily.Variant.STRIPPED_WOOD));
		});
	}

	protected static void register(Supplier<?> block, Supplier<?> result) {
		AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
		AxeItem.STRIPPABLES.put((Block) block.get(), (Block) result.get());
	}
}
