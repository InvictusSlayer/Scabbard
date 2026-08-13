package net.invictusslayer.scabbard.platform;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.invictusslayer.scabbard.resource.BuiltInPackHandler;
import net.invictusslayer.scabbard.world.biome.BiomeModifierHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgePlatformHandler implements IPlatformHandler {
	@Override
	public Platform getPlatform() {
		return Platform.FORGE;
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
		ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
	}

	@Override
	public void addFurnaceFuelItem(ItemLike item, int ticks) {
		MinecraftForge.EVENT_BUS.addListener((FurnaceFuelBurnTimeEvent event) -> {
			if (event.getItemStack().is(item.asItem())) event.setBurnTime(ticks);
		});
	}

	@Override
	public void addSpawnBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, List<MobSpawnSettings.SpawnerData> spawners) {
		handler.spawnModifiers.add(new BiomeModifierHandler.SpawnModifier(name, biomes, spawners));
	}

	@Override
	public void addFeatureBiomeModifier(BiomeModifierHandler handler, String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features) {
		handler.featureModifiers.add(new BiomeModifierHandler.FeatureModifier(name, biomes, step, features));
	}

	private final Map<ResourceKey<?>, DeferredRegister> REGISTERS = new Reference2ObjectOpenHashMap<>();

	@Override
	public Supplier<SpawnEggItem> registerSpawnEgg(String modId, String name, Supplier<EntityType<? extends Mob>> entity, int bgColor, int fgColor, Item.Properties props) {
		return register(BuiltInRegistries.ITEM, modId, name, () -> new ForgeSpawnEggItem(entity, bgColor, fgColor, props));
	}

	@Override
	public Supplier<ArmorItem> registerCustomArmorItem(String modId, String name, ArmorMaterial material, ArmorItem.Type type, Item.Properties props, CustomArmorRenderer renderer) {
		return register(BuiltInRegistries.ITEM, modId, name, () -> new ArmorItem(material, type, props) {
			@Override
			public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
				return ResourceLocation.fromNamespaceAndPath(modId, "textures/models/armor/" + name + ".png").toString();
			}

			@Override
			public void initializeClient(Consumer<IClientItemExtensions> consumer) {
				consumer.accept(new IClientItemExtensions() {
					@Override
					public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> original) {
						return renderer.resolve(livingEntity, itemStack, slot, original);
					}
				});
			}
		});
	}

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String modId, String name, Supplier<T> value) {
		return REGISTERS.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), modId)).register(name, value);
	}

	@Override
	public <T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String modId, String name, Supplier<T> value) {
		RegistryObject<?> registryObject = REGISTERS.computeIfAbsent(registry.key(), key -> DeferredRegister.create(registry.key().location(), modId)).register(name, value);
		return () -> (Holder.Reference<T>) registryObject.getHolder().get();
	}

	@Override
	public void registerBuiltinPack(String modId, String packId, String name, boolean enabled) {
		BuiltInPackHandler.PACKS.add(new BuiltInPackHandler.PackData(modId, packId, name, enabled));
	}

	public void register(final IEventBus bus) {
		REGISTERS.values().forEach(deferredRegister -> deferredRegister.register(bus));
	}
}
