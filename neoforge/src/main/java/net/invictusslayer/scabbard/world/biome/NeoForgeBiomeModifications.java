package net.invictusslayer.scabbard.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBiomeModifications {
	public static void bootstrap(BootstrapContext<BiomeModifier> context, BiomeModifierHandler handler) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);

		handler.spawnModifiers.forEach(modifier ->
				context.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(handler.modId, modifier.name())),
						new BiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(modifier.biomes()), modifier.spawners())));

		handler.featureModifiers.forEach(modifier -> {
			List<Holder<PlacedFeature>> features = new ArrayList<>();
			modifier.features().forEach(feature -> features.add(placed.getOrThrow(feature)));
			context.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(handler.modId, modifier.name())),
					new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(modifier.biomes()), HolderSet.direct(features), modifier.step()));
		});
	}
}
