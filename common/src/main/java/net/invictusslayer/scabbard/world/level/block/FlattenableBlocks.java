package net.invictusslayer.scabbard.world.level.block;

import com.google.common.collect.Maps;
import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public abstract class FlattenableBlocks {
	public abstract void register();

	protected static void register(Supplier<?> block, BlockState result) {
		ShovelItem.FLATTENABLES = Maps.newHashMap(ShovelItem.FLATTENABLES);
		ShovelItem.FLATTENABLES.put((Block) block.get(), result);
	}
}
