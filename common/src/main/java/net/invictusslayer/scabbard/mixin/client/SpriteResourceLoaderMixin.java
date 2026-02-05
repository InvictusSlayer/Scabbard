package net.invictusslayer.scabbard.mixin.client;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.Dynamic;
import net.invictusslayer.scabbard.Scabbard;
import net.invictusslayer.scabbard.world.level.item.ArmorTrimHandler;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(SpriteResourceLoader.class)
public class SpriteResourceLoaderMixin {
	@Inject(method = "load", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/atlas/SpriteSources;FILE_CODEC:Lcom/mojang/serialization/Codec;", opcode = Opcodes.GETSTATIC))
	private static void updateDynamic(ResourceManager resourceManager, ResourceLocation location, CallbackInfoReturnable<SpriteResourceLoader> cir, @Local Resource resource, @Local LocalRef<Dynamic<JsonElement>> dynamic) {
		boolean isPatterns = location.equals(new ResourceLocation("armor_trims"));
		if (!(isPatterns || location.equals(new ResourceLocation("blocks")))) return;
		if (!resource.sourcePackId().equals("vanilla")) return;

		Dynamic<JsonElement> sources = dynamic.get().get("sources").orElseEmptyList();
		sources = sources.createList(sources.asStream().map(source -> scabbard$checkPalettedPermutation(source, isPatterns)));

		Scabbard.LOGGER.info("Updated armor trim atlas JSON: {}", sources);
		dynamic.set(dynamic.get().set("sources", sources));
	}

	@Unique
	private static Dynamic<?> scabbard$checkPalettedPermutation(Dynamic<?> source, boolean isPatterns) {
		if (!source.get("type").asString("none").equals("paletted_permutations")) return source;
		return source.update("textures", textures -> scabbard$updateTextures(textures, isPatterns))
				.update("permutations", SpriteResourceLoaderMixin::scabbard$updatePermutations);
	}

	@Unique
	private static Dynamic<?> scabbard$updateTextures(Dynamic<?> textures, boolean isPatterns) {
		List<ResourceLocation> list = isPatterns ? ArmorTrimHandler.getTrimPatterns() : ArmorTrimHandler.getItemOverlays();
		if (list.isEmpty()) return textures;
		return textures.createList(Stream.concat(textures.asStream(),
				list.stream().map(loc -> textures.createString(loc.toString()))));
	}

	@Unique
	private static Dynamic<?> scabbard$updatePermutations(Dynamic<?> permutations) {
		Map<String, ResourceLocation> map = ArmorTrimHandler.getTrimMaterials();
		if (map.isEmpty()) return permutations;
		return permutations.createMap(Stream.concat(permutations.asMap(Function.identity(), Function.identity()).entrySet().stream(),
				map.entrySet().stream().collect(Collectors.toMap(
						entry -> permutations.createString(entry.getKey()),
						entry -> permutations.createString(entry.getValue().toString()))
				).entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}
}
