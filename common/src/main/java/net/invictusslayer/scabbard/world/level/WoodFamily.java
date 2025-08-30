package net.invictusslayer.scabbard.world.level;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WoodFamily {
	private static final List<WoodFamily> FAMILIES = new ArrayList<>();
	private TagKey<Block> logBlocks = null;
	private TagKey<Item> logItems = null;
	private Boat.Type boatType = null;
	private final Map<Variant, Supplier<?>> variants = new HashMap<>();
	private boolean isFlammable = true;
	private boolean isStrippable = true;

	WoodFamily() {}

	public TagKey<Block> getLogBlocks() {
		return logBlocks;
	}

	public TagKey<Item> getLogItems() {
		return logItems;
	}

	public Boat.Type getBoatType() {
		return boatType;
	}

	public Map<Variant, Supplier<?>> getVariants() {
		return variants;
	}

	public Supplier<?> get(Variant variant) {
		return variants.get(variant);
	}

	public boolean isFlammable() {
		return isFlammable;
	}

	public boolean isStrippable() {
		return isStrippable;
	}

	protected static Builder builder() {
		Builder builder = new Builder();
		FAMILIES.add(builder.getFamily());
		return builder;
	}

	public static Stream<WoodFamily> getAllFamilies() {
		return FAMILIES.stream();
	}

	public static class Builder {
		private final WoodFamily family;

		public Builder() {
			family = new WoodFamily();
		}

		public WoodFamily getFamily() {
			return family;
		}

		public Builder button(Supplier<Block> button) {
			family.variants.put(Variant.BUTTON, button);
			return this;
		}

		public Builder boat(Supplier<Item> boat, Boat.Type type) {
			family.variants.put(Variant.BOAT, boat);
			family.boatType = type;
			return this;
		}

		public Builder chestBoat(Supplier<Item> boat) {
			family.variants.put(Variant.CHEST_BOAT, boat);
			return this;
		}

		public Builder door(Supplier<Block> door) {
			family.variants.put(Variant.DOOR, door);
			return this;
		}

		public Builder fence(Supplier<Block> fence) {
			family.variants.put(Variant.FENCE, fence);
			return this;
		}

		public Builder fenceGate(Supplier<Block> fenceGate) {
			family.variants.put(Variant.FENCE_GATE, fenceGate);
			return this;
		}

		public Builder hangingSign(Supplier<Block> ceiling, Supplier<Block> wall, Supplier<Item> item) {
			family.variants.put(Variant.HANGING_SIGN, ceiling);
			family.variants.put(Variant.WALL_HANGING_SIGN, wall);
			family.variants.put(Variant.HANGING_SIGN_ITEM, item);
			return this;
		}

		public Builder leaves(Supplier<Block> leaves) {
			family.variants.put(Variant.LEAVES, leaves);
			return this;
		}

		public Builder log(Supplier<Block> log, TagKey<Block> blockTag, TagKey<Item> itemTag) {
			family.variants.put(Variant.LOG, log);
			family.logBlocks = blockTag;
			family.logItems = itemTag;
			return this;
		}

		public Builder sapling(Supplier<Block> sapling, Supplier<Block> pottedSapling) {
			family.variants.put(Variant.SAPLING, sapling);
			family.variants.put(Variant.POTTED_SAPLING, pottedSapling);
			return this;
		}

		public Builder sign(Supplier<Block> standing, Supplier<Block> wall, Supplier<Item> item) {
			family.variants.put(Variant.SIGN, standing);
			family.variants.put(Variant.WALL_SIGN, wall);
			family.variants.put(Variant.SIGN_ITEM, item);
			return this;
		}

		public Builder slab(Supplier<Block> slab) {
			family.variants.put(Variant.SLAB, slab);
			return this;
		}

		public Builder stairs(Supplier<Block> stairs) {
			family.variants.put(Variant.STAIRS, stairs);
			return this;
		}

		public Builder strippedLog(Supplier<Block> strippedLog) {
			family.variants.put(Variant.STRIPPED_LOG, strippedLog);
			return this;
		}

		public Builder strippedWood(Supplier<Block> strippedWood) {
			family.variants.put(Variant.STRIPPED_WOOD, strippedWood);
			return this;
		}

		public Builder planks(Supplier<Block> planks) {
			family.variants.put(Variant.PLANKS, planks);
			return this;
		}

		public Builder pressurePlate(Supplier<Block> pressurePlate) {
			family.variants.put(Variant.PRESSURE_PLATE, pressurePlate);
			return this;
		}

		public Builder trapdoor(Supplier<Block> trapdoor) {
			family.variants.put(Variant.TRAPDOOR, trapdoor);
			return this;
		}

		public Builder wood(Supplier<Block> wood) {
			family.variants.put(Variant.WOOD, wood);
			return this;
		}

		public Builder notFlammable() {
			family.isFlammable = false;
			return this;
		}

		public Builder notStrippable() {
			family.isStrippable = false;
			return this;
		}
	}

	public enum Variant {
		BUTTON,
		BOAT,
		CHEST_BOAT,
		DOOR(true),
		FENCE,
		FENCE_GATE,
		HANGING_SIGN,
		HANGING_SIGN_ITEM,
		LEAVES,
		LOG,
		SAPLING(true),
		SIGN,
		SIGN_ITEM,
		SLAB,
		STAIRS,
		STRIPPED_LOG,
		STRIPPED_WOOD,
		PLANKS,
		POTTED_SAPLING(true),
		PRESSURE_PLATE,
		TRAPDOOR(true),
		WALL_HANGING_SIGN,
		WALL_SIGN,
		WOOD;

		private final boolean isCutout;

		Variant() {
			this(false);
		}

		Variant(boolean isCutout) {
			this.isCutout = isCutout;
		}

		public boolean isCutout() {
			return isCutout;
		}
	}
}
