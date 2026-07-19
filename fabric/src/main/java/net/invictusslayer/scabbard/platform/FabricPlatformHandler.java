package net.invictusslayer.scabbard.platform;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.invictusslayer.scabbard.Scabbard;
import net.invictusslayer.scabbard.world.biome.BiomeModifierHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
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
	public void addFlammableBlock(Block block, int flammability, int encouragement) {
		FlammableBlockRegistry.getDefaultInstance().add(block, flammability, encouragement);
	}

	@Override
	public void addStrippableBlock(Block block, Block stripped) {
		StrippableBlockRegistry.register(block, stripped);
	}

	@Override
	public void addFlattenableBlock(Block block, BlockState flattened) {
		FlattenableBlockRegistry.register(block, flattened);
	}

	@Override
	public void addCompostableItem(ItemLike item, float chance) {
		ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
	}

	@Override
	public void addFurnaceFuelItem(ItemLike item, int ticks) {
		FuelRegistry.INSTANCE.add(item, ticks);
	}

	@Override
	public void addSpawnBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) {
		spawners.forEach(spawner -> BiomeModifications.addSpawn(context -> context.hasTag(biomes), spawner.type.getCategory(), spawner.type, spawner.getWeight().asInt(), spawner.minCount, spawner.maxCount));
	}

	@Override
	public void addFeatureBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
		features.forEach(feature -> BiomeModifications.addFeature(context -> context.hasTag(biomes), step, BiomeModifierHandler.modifierKey(feature)));
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

	@Override
	public void registerBuiltinPack(String modId, String packId, String name, boolean enabled) {
		FabricLoader.getInstance().getModContainer(modId)
				.map(container -> ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.fromNamespaceAndPath(modId, packId), container, Component.literal(name),
						enabled ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL))
				.filter(success -> !success).ifPresent(success -> Scabbard.LOGGER.warn("Could not register built-in resource pack {} for {}.", packId, modId));
	}
}
