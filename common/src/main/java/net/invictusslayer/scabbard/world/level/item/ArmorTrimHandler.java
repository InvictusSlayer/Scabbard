package net.invictusslayer.scabbard.world.level.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorTrimHandler {
	private static final Map<String, ResourceLocation> TRIM_MATERIALS = new HashMap<>();
	private static final List<ResourceLocation> TRIM_PATTERNS = new ArrayList<>();
	private static final List<ResourceLocation> ITEM_OVERLAYS = new ArrayList<>();

	public static Map<String, ResourceLocation> getTrimMaterials() {
		return TRIM_MATERIALS;
	}

	public static void addMaterial(ResourceLocation loc, String name) {
		TRIM_MATERIALS.put(name, loc);
	}

	public static void addMaterial(ResourceKey<TrimMaterial> key) {
		addMaterial(key.location().withPrefix("trims/color_palettes/"), key.location().getPath());
	}

	public static void addMaterial(ResourceKey<TrimMaterial> key, String suffix) {
		addMaterial(key.location().withPrefix("trims/color_palettes/"), key.location().withSuffix(suffix).getPath());
	}

	public static List<ResourceLocation> getTrimPatterns() {
		return TRIM_PATTERNS;
	}

	public static void addPattern(ResourceLocation loc) {
		TRIM_PATTERNS.add(loc);
	}

	public static void addPattern(ResourceKey<TrimPattern> key) {
		addPattern(key.location().withPrefix("trims/models/armor/"));
		addPattern(key.location().withPrefix("trims/models/armor/").withSuffix("_leggings"));
	}

	public static List<ResourceLocation> getItemOverlays() {
		return ITEM_OVERLAYS;
	}

	public static void addOverlay(ResourceLocation loc) {
		ITEM_OVERLAYS.add(loc);
	}

	public static void addOverlay(String modId, String name) {
		addOverlay(new ResourceLocation(modId, "trims/items/" + name));
	}
}
