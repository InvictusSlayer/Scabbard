package net.invictusslayer.scabbard.platform;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.invictusslayer.scabbard.resource.BuiltInPackHandler;
import net.invictusslayer.scabbard.world.biome.BiomeModifierHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
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
	public void addFlammableBlock(Block block, int flammability, int encouragement) {
		((FireBlock) Blocks.FIRE).setFlammable(block, encouragement, flammability);
	}

	@Override
	public void addStrippableBlock(Block block, Block stripped) {
		AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
		AxeItem.STRIPPABLES.put(block, stripped);
	}

	@Override
	public void addFlattenableBlock(Block block, BlockState flattened) {
		ShovelItem.FLATTENABLES = Maps.newHashMap(ShovelItem.FLATTENABLES);
		ShovelItem.FLATTENABLES.put(block, flattened);
	}

	@Override
	public void addCompostableItem(ItemLike item, float chance) {
	}

	@Override
	public void addFurnaceFuelItem(ItemLike item, int ticks) {
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

	@Override
	public void registerBuiltinPack(String modId, String packId, String name, boolean enabled) {
		BuiltInPackHandler.PACKS.add(new BuiltInPackHandler.PackData(modId, packId, name, enabled));
	}

	public static void register(final IEventBus bus) {
		REGISTERS.values().forEach(deferredRegister -> deferredRegister.register(bus));
	}
}
