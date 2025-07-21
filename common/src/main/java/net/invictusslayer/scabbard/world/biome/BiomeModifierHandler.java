package net.invictusslayer.scabbard.world.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

public class BiomeModifierHandler {
	public List<FeatureModifier> featureModifiers = new ArrayList<>();
	public List<SpawnModifier> spawnModifiers = new ArrayList<>();
	public final String modId;

	public BiomeModifierHandler(String modId) {
		this.modId = modId;
	}

	public record SpawnModifier(String name, TagKey<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) {}

	public record FeatureModifier(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {}
}
