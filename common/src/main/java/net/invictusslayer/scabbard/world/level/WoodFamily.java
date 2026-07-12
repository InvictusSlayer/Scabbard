package net.invictusslayer.scabbard.world.level;

import net.invictusslayer.scabbard.Scabbard;
import net.invictusslayer.scabbard.platform.IPlatformHandler;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.*;
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

	public boolean hasBoatType() {
		return boatType != null;
	}

	public Map<Variant, Supplier<?>> getVariants() {
		return variants;
	}

	@Deprecated
	public Supplier<?> get(Variant variant) {
		return variants.get(variant);
	}

	public Optional<Item> getItem(Variant variant) {
		Supplier<?> supplier = variants.get(variant);
		if (supplier == null || !(supplier.get() instanceof Item item)) {
			Scabbard.LOGGER.warn("WoodFamily.Variant {} does not contain an Item", variant.name());
			return Optional.empty();
		}
		return Optional.of(item);
	}

	public Optional<Block> getBlock(Variant variant) {
		Supplier<?> supplier = variants.get(variant);
		if (supplier == null || !(supplier.get() instanceof Block block)) {
			Scabbard.LOGGER.warn("WoodFamily.Variant {} does not contain an Block", variant.name());
			return Optional.empty();
		}
		return Optional.of(block);
	}

	public boolean isFlammable() {
		return isFlammable;
	}

	public boolean isStrippable() {
		return isStrippable;
	}

	public void registerFlammability(IPlatformHandler platform) {
		if (!isFlammable) return;
		getVariants().forEach((variant, supplier) -> {
			switch (variant) {
				case LOG, WOOD, STRIPPED_LOG, STRIPPED_WOOD -> platform.addFlammableBlock((Block) supplier.get(), 5, 5);
				case LEAVES -> platform.addFlammableBlock((Block) supplier.get(), 60, 30);
				case PLANKS, STAIRS, SLAB, FENCE, FENCE_GATE -> platform.addFlammableBlock((Block) supplier.get(), 20, 5);
			}
		});
	}

	public void registerStrippability(IPlatformHandler platform) {
		if (!isStrippable) return;
		getBlock(Variant.LOG).ifPresent(b -> getBlock(Variant.STRIPPED_LOG).ifPresent(b1 -> platform.addStrippableBlock(b, b1)));
		getBlock(Variant.WOOD).ifPresent(b -> getBlock(Variant.STRIPPED_WOOD).ifPresent(b1 -> platform.addStrippableBlock(b, b1)));
	}

	public void registerDispensability() {
		if (boatType == null) return;
		getItem(WoodFamily.Variant.BOAT).ifPresent(boat -> DispenserBlock.registerBehavior(boat, new BoatDispenseItemBehavior(getBoatType())));
		getItem(WoodFamily.Variant.CHEST_BOAT).ifPresent(boat -> DispenserBlock.registerBehavior(boat, new BoatDispenseItemBehavior(getBoatType(), true)));
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
