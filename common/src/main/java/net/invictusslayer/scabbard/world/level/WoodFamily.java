package net.invictusslayer.scabbard.world.level;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
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
	private ModelLayerLocation boatLayer = null;
	private ModelLayerLocation chestBoatLayer = null;
	private final Map<Variant, Supplier<?>> variants = new HashMap<>();
	private boolean isFlammable = true;

	WoodFamily() {}

	public TagKey<Block> getLogBlocks() {
		return logBlocks;
	}

	public TagKey<Item> getLogItems() {
		return logItems;
	}

	public ModelLayerLocation getBoatLayer(boolean chestBoat) {
		return chestBoat ? chestBoatLayer : boatLayer;
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

		public Builder boat(Supplier<EntityType<Boat>> boat, Supplier<Item> boatItem, ModelLayerLocation layer) {
			family.variants.put(Variant.BOAT, boat);
			family.variants.put(Variant.BOAT_ITEM, boatItem);
			family.boatLayer = layer;
			return this;
		}

		public Builder chestBoat(Supplier<EntityType<ChestBoat>> boat, Supplier<Item> boatItem, ModelLayerLocation layer) {
			family.variants.put(Variant.CHEST_BOAT, boat);
			family.variants.put(Variant.CHEST_BOAT_ITEM, boatItem);
			family.chestBoatLayer = layer;
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
	}

	public enum Variant {
		BUTTON("Button"),
		BOAT(null),
		BOAT_ITEM("Boat"),
		CHEST_BOAT(null),
		CHEST_BOAT_ITEM("Boat with Chest"),
		DOOR("Door", true),
		FENCE("Fence"),
		FENCE_GATE("Fence Gate"),
		HANGING_SIGN("Hanging Sign"),
		HANGING_SIGN_ITEM(null),
		LEAVES("Leaves"),
		LOG("Log"),
		SAPLING("Sapling", true),
		SIGN("Sign"),
		SIGN_ITEM(null),
		SLAB("Slab"),
		STAIRS("Stairs"),
		STRIPPED_LOG("Stripped Log"),
		STRIPPED_WOOD("Stripped Wood"),
		PLANKS("Planks"),
		POTTED_SAPLING(null, true),
		PRESSURE_PLATE("Pressure Plate"),
		TRAPDOOR("Trapdoor", true),
		WALL_HANGING_SIGN(null),
		WALL_SIGN(null),
		WOOD("Wood");

		private final String name;
		private final boolean isCutout;

		Variant(String name) {
			this(name, false);
		}

		Variant(String name, boolean isCutout) {
			this.name = name;
			this.isCutout = isCutout;
		}

		public String getName() {
			return name;
		}

		public boolean isCutout() {
			return isCutout;
		}
	}
}
