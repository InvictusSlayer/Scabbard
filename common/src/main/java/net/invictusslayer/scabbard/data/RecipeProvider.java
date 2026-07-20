package net.invictusslayer.scabbard.data;

import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public abstract class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    protected final String modId;
	private final HolderGetter<Item> items;

    public RecipeProvider(HolderLookup.Provider provider, RecipeOutput output, String modId) {
        super(provider, output);
        this.modId = modId;
		this.items = provider.lookupOrThrow(Registries.ITEM);
    }

	protected void generateBlockFamily(RecipeOutput output, BlockFamily family) {
        if (family.shouldGenerateRecipe()) generateRecipes(family, FeatureFlagSet.of(FeatureFlags.VANILLA));
	}

	protected void generateWoodFamily(RecipeOutput output, WoodFamily family) {
        family.getBlock(WoodFamily.Variant.PLANKS).ifPresent(planks -> {
            planksFromLog(planks, family.getLogItems(), 4);
            Ingredient ingredient = Ingredient.of(planks);

            family.getVariants().forEach((variant, supplier) -> {
                if (!(supplier.get() instanceof ItemLike item)) return;
                switch (variant) {
                    case BOAT -> woodenBoat(item, planks);
                    case BUTTON -> woodenRecipe(output, buttonRecipe(item, ingredient), planks, "button");
                    case DOOR -> woodenRecipe(output, doorBuilder(item, ingredient), planks, "door");
                    case FENCE -> woodenRecipe(output, fenceRecipe(item, ingredient), planks, "fence");
                    case FENCE_GATE -> woodenRecipe(output, fenceGateRecipe(item, ingredient), planks, "fence_gate");
                    case PRESSURE_PLATE -> woodenRecipe(output, pressurePlateRecipe(RecipeCategory.REDSTONE, item, ingredient), planks, "pressure_plate");
                    case SIGN_ITEM -> woodenRecipe(output, signRecipe(item, ingredient), planks, "sign");
                    case SLAB -> woodenRecipe(output, slabBuilder(RecipeCategory.BUILDING_BLOCKS, item, ingredient), planks, "slab");
                    case STAIRS -> woodenRecipe(output, stairBuilder(item, ingredient), planks, "stairs");
                    case TRAPDOOR -> woodenRecipe(output, trapdoorRecipe(item, ingredient), planks, "trapdoor");
                    default -> {}
                }
            });
        });

        family.getBlock(WoodFamily.Variant.LOG).ifPresent(log -> family.getBlock(WoodFamily.Variant.WOOD).ifPresent(wood -> woodFromLogs(wood, log)));
        family.getBlock(WoodFamily.Variant.STRIPPED_LOG).ifPresent(log -> {
            family.getBlock(WoodFamily.Variant.STRIPPED_WOOD).ifPresent(wood -> woodFromLogs(wood, log));
            family.getItem(WoodFamily.Variant.HANGING_SIGN_ITEM).ifPresent(sign -> hangingSign(sign, log));
        });

        family.getItem(WoodFamily.Variant.BOAT).ifPresent(boat -> family.getItem(WoodFamily.Variant.CHEST_BOAT).ifPresent(chest -> chestBoat(chest, boat)));
	}

	private void woodenRecipe(RecipeOutput output, RecipeBuilder builder, Block planks, String group) {
		builder.unlockedBy("has_planks", has(planks)).group("wooden_" + group).save(output);
	}

    public RecipeBuilder buttonRecipe(ItemLike button, Ingredient material) {
        return ShapelessRecipeBuilder.shapeless(items, RecipeCategory.REDSTONE, button).requires(material);
    }

    public RecipeBuilder fenceRecipe(ItemLike fence, Ingredient material) {
        return ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, fence, 3).define('W', material).define('#', Items.STICK).pattern("W#W").pattern("W#W");
    }

    public RecipeBuilder fenceGateRecipe(ItemLike fenceGate, Ingredient material) {
        return ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, fenceGate).define('#', Items.STICK).define('W', material).pattern("#W#").pattern("#W#");
    }

    public RecipeBuilder pressurePlateRecipe(RecipeCategory category, ItemLike pressurePlate, Ingredient material) {
        return ShapedRecipeBuilder.shaped(items, category, pressurePlate).define('#', material).pattern("##");
    }

    public RecipeBuilder signRecipe(ItemLike sign, Ingredient material) {
        return ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, sign, 3).group("sign").define('#', material).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    public RecipeBuilder trapdoorRecipe(ItemLike trapdoor, Ingredient material) {
        return ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, trapdoor, 2).define('#', material).pattern("###").pattern("###");
    }

	protected void fourItemPacker(RecipeOutput output, RecipeCategory category, ItemLike packed, ItemLike unpacked) {
		ShapedRecipeBuilder.shaped(items, category, packed, 1).define('#', unpacked).pattern("##").pattern("##").unlockedBy(getHasName(unpacked), has(unpacked)).save(output, createRecipeKey(getSimpleRecipeName(unpacked)));
	}

	protected void nineItemStorageRecipes(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
		nineItemStorageRecipes(output, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), getSimpleRecipeName(unpacked));
	}

	protected void nineItemStorageRecipes(RecipeOutput output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, String unpackedName) {
		ShapelessRecipeBuilder.shapeless(items, unpackedCategory, unpacked, 9).requires(packed).group(null).unlockedBy(getHasName(packed), has(packed)).save(output, createRecipeKey(unpackedName));
		ShapedRecipeBuilder.shaped(items, packedCategory, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").group(null).unlockedBy(getHasName(unpacked), has(unpacked)).save(output, createRecipeKey(packedName));
	}

	private ResourceKey<Recipe<?>> createRecipeKey(String path) {
		return ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(modId, path));
	}
}
