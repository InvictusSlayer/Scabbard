package net.invictusslayer.scabbard.data;

import net.invictusslayer.scabbard.world.level.WoodFamily;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public abstract class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    protected final String modId;

    public RecipeProvider(PackOutput output, String modId) {
        super(output);
        this.modId = modId;
    }

	protected void generateBlockFamily(Consumer<FinishedRecipe> output, BlockFamily family) {
        if (family.shouldGenerateRecipe(FeatureFlagSet.of(FeatureFlags.VANILLA))) generateRecipes(output, family);
	}

	protected void generateWoodFamily(Consumer<FinishedRecipe> output, WoodFamily family) {
        family.getBlock(WoodFamily.Variant.PLANKS).ifPresent(planks -> {
            planksFromLog(output, planks, family.getLogItems(), 4);
            Ingredient ingredient = Ingredient.of(planks);

            family.getVariants().forEach((variant, supplier) -> {
                if (!(supplier.get() instanceof ItemLike item)) return;
                switch (variant) {
                    case BOAT -> woodenBoat(output, item, planks);
                    case BUTTON -> woodenRecipe(output, buttonBuilder(item, ingredient), planks, "button");
                    case DOOR -> woodenRecipe(output, doorBuilder(item, ingredient), planks, "door");
                    case FENCE -> woodenRecipe(output, fenceBuilder(item, ingredient), planks, "fence");
                    case FENCE_GATE -> woodenRecipe(output, fenceGateBuilder(item, ingredient), planks, "fence_gate");
                    case PRESSURE_PLATE -> woodenRecipe(output, pressurePlateBuilder(RecipeCategory.REDSTONE, item, ingredient), planks, "pressure_plate");
                    case SIGN_ITEM -> woodenRecipe(output, signBuilder(item, ingredient), planks, "sign");
                    case SLAB -> woodenRecipe(output, slabBuilder(RecipeCategory.BUILDING_BLOCKS, item, ingredient), planks, "slab");
                    case STAIRS -> woodenRecipe(output, stairBuilder(item, ingredient), planks, "stairs");
                    case TRAPDOOR -> woodenRecipe(output, trapdoorBuilder(item, ingredient), planks, "trapdoor");
                    default -> {}
                }
            });
        });

        family.getBlock(WoodFamily.Variant.LOG).ifPresent(log -> family.getBlock(WoodFamily.Variant.WOOD).ifPresent(wood -> woodFromLogs(output, wood, log)));
        family.getBlock(WoodFamily.Variant.STRIPPED_LOG).ifPresent(log -> {
            family.getBlock(WoodFamily.Variant.STRIPPED_WOOD).ifPresent(wood -> woodFromLogs(output, wood, log));
            family.getItem(WoodFamily.Variant.HANGING_SIGN_ITEM).ifPresent(sign -> hangingSign(output, sign, log));
        });

        family.getItem(WoodFamily.Variant.BOAT).ifPresent(boat -> family.getItem(WoodFamily.Variant.CHEST_BOAT).ifPresent(chest -> chestBoat(output, chest, boat)));
	}

	private static void woodenRecipe(Consumer<FinishedRecipe> output, RecipeBuilder builder, Block planks, String group) {
		builder.unlockedBy("has_planks", has(planks)).group("wooden_" + group).save(output);
	}

    public static RecipeBuilder buttonBuilder(ItemLike button, Ingredient material) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button).requires(material);
    }

    public static RecipeBuilder fenceBuilder(ItemLike fence, Ingredient material) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3).define('W', material).define('#', Items.STICK).pattern("W#W").pattern("W#W");
    }

    public static RecipeBuilder fenceGateBuilder(ItemLike fenceGate, Ingredient material) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate).define('#', Items.STICK).define('W', material).pattern("#W#").pattern("#W#");
    }

    public static RecipeBuilder pressurePlateBuilder(RecipeCategory category, ItemLike pressurePlate, Ingredient material) {
        return ShapedRecipeBuilder.shaped(category, pressurePlate).define('#', material).pattern("##");
    }

    public static RecipeBuilder signBuilder(ItemLike sign, Ingredient material) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3).group("sign").define('#', material).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    public static RecipeBuilder trapdoorBuilder(ItemLike trapdoor, Ingredient material) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2).define('#', material).pattern("###").pattern("###");
    }

	protected void fourItemPacker(Consumer<FinishedRecipe> output, RecipeCategory category, ItemLike packed, ItemLike unpacked) {
		ShapedRecipeBuilder.shaped(category, packed, 1).define('#', unpacked).pattern("##").pattern("##").unlockedBy(getHasName(unpacked), has(unpacked)).save(output, new ResourceLocation(modId, getSimpleRecipeName(unpacked)));
	}

	protected void nineItemStorageRecipes(Consumer<FinishedRecipe> output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
		nineItemStorageRecipes(output, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), getSimpleRecipeName(unpacked));
	}

	protected void nineItemStorageRecipes(Consumer<FinishedRecipe> output, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, String unpackedName) {
		ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9).requires(packed).group(null).unlockedBy(getHasName(packed), has(packed)).save(output, new ResourceLocation(modId, unpackedName));
		ShapedRecipeBuilder.shaped(packedCategory, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").group(null).unlockedBy(getHasName(unpacked), has(unpacked)).save(output, new ResourceLocation(modId, packedName));
	}
}
