package net.invictusslayer.scabbard.platform;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.loader.api.FabricLoader;
import net.invictusslayer.scabbard.Scabbard;
import net.invictusslayer.scabbard.world.biome.BiomeModifierHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

public class FabricPlatformHandler implements IPlatformHandler {
	@Override
	public Platform getPlatform() {
		return Platform.FABRIC;
	}

	@Override
	public Path configPath(String modId) {
		return FabricLoader.getInstance().getConfigDir().resolve(modId);
	}

	@Override
	public void addSpawnBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) {
		BiomeModification modification = BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(Scabbard.MOD_ID, name));
		spawners.forEach(spawner -> modification.add(ModificationPhase.ADDITIONS, context -> context.hasTag(biomes), context -> context.getSpawnSettings().addSpawn(spawner.type.getCategory(), spawner)));
	}

	@Override
	public void addFeatureBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
		BiomeModification modification = BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(Scabbard.MOD_ID, name));
		features.forEach(feature -> modification.add(ModificationPhase.ADDITIONS, context -> context.hasTag(biomes), context -> context.getGenerationSettings().addFeature(step, feature)));
	}

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String modId, String name, Supplier<T> value) {
		T registered = Registry.register(registry, ResourceLocation.fromNamespaceAndPath(modId, name), value.get());
		return () -> registered;
	}

	@Override
	public <T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String modId, String name, Supplier<T> value) {
		Holder.Reference<T> registered = Registry.registerForHolder(registry, ResourceLocation.fromNamespaceAndPath(modId, name), value.get());
		return () -> registered;
	}
}
