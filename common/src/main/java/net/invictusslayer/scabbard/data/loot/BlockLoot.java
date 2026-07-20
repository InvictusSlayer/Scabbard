package net.invictusslayer.scabbard.data.loot;

import net.invictusslayer.scabbard.Scabbard;
import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class BlockLoot extends BlockLootSubProvider {
    private final String modId;

    public BlockLoot(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider registries, String modId) {
        super(explosionResistant, enabledFeatures, registries);
        this.modId = modId;
    }

    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        generate();
        Set<ResourceKey<LootTable>> set = new HashSet<>();

        for (Block block : BuiltInRegistries.BLOCK) {
            if (block.isEnabled(enabledFeatures)) {
                block.getLootTable().ifPresent(key -> {
                    if (set.add(key)) {
                        LootTable.Builder builder = map.remove(key);

                        if (!key.location().getNamespace().equals(modId)) return;

                        if (builder == null) {
                            Scabbard.LOGGER.error("Missing loottable '{}' for '{}'", key.location(), BuiltInRegistries.BLOCK.getKey(block));
                            return;
                        }

                        output.accept(key, builder);
                    }
                });
            }
        }

        if (!map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + map.keySet());
        }
    }

    protected void generateBlockFamily(BlockFamily family) {
        dropSelf(family.getBaseBlock());
        family.getVariants().forEach((variant, block) -> {
            switch (variant) {
                case SLAB -> add(block, this::createSlabItemTable);
                case DOOR -> add(block, this::createDoorTable);
                case WALL_SIGN -> dropOther(block, family.get(BlockFamily.Variant.SIGN));
                default -> dropSelf(block);
            }
        });
    }

    protected void generateWoodFamily(WoodFamily family) {
        family.getVariants().forEach((variant, supplier) -> {
            if (!(supplier.get() instanceof Block block)) return;
            switch (variant) {
                case DOOR -> add(block, this::createDoorTable);
                case LEAVES -> family.getBlock(WoodFamily.Variant.SAPLING).ifPresent(sapling -> add(sapling, createLeavesDrops(block, sapling, 0.05F)));
                case POTTED_SAPLING -> dropPottedContents(block);
                case SLAB -> add(block, this::createSlabItemTable);
                case WALL_HANGING_SIGN -> family.getBlock(WoodFamily.Variant.HANGING_SIGN).ifPresent(sign -> dropOther(block, sign));
                case WALL_SIGN -> family.getBlock(WoodFamily.Variant.SIGN).ifPresent(sign -> dropOther(block, sign));
                default -> dropSelf(block);
            }
        });
    }
}
