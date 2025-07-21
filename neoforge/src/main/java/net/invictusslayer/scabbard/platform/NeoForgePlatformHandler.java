package net.invictusslayer.scabbard.platform;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.invictusslayer.scabbard.world.biome.BiomeModifierHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgePlatformHandler implements IPlatformHandler {
	@Override
	public Platform getPlatform() {
		return Platform.NEOFORGE;
	}

	@Override
	public Path configPath(String modId) {
		return FMLPaths.CONFIGDIR.get().resolve(modId);
	}

	@Override
	public void addSpawnBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) {
		handler.spawnModifiers.add(new BiomeModifierHandler.SpawnModifier(name, biomes, spawners));
	}

	@Override
	public void addFeatureBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
		handler.featureModifiers.add(new BiomeModifierHandler.FeatureModifier(name, biomes, step, features));
	}

	private static final Map<ResourceKey<?>, DeferredRegister> REGISTERS = new Reference2ObjectOpenHashMap<>();

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String modId, String name, Supplier<T> value) {
		return REGISTERS.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), modId)).register(name, value);
	}

	@Override
	public <T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String modId, String name, Supplier<T> value) {
		DeferredHolder<?, ?> registryObject = REGISTERS.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), modId)).register(name, value);
		return () -> (Holder.Reference<T>) registryObject.getDelegate();
	}

	public static void register(final IEventBus bus) {
		REGISTERS.values().forEach(deferredRegister -> deferredRegister.register(bus));
	}
}
