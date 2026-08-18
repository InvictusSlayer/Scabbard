package net.invictusslayer.scabbard.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.invictusslayer.scabbard.client.model.IExtendedModelBuilder;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraftforge.client.model.generators.ModelBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModelBuilder.class, remap = false)
public class ModelBuilderMixin<T extends ModelBuilder<T>> implements IExtendedModelBuilder<T> {
	@Unique
	private Pair<Integer, Integer> scabbard$textureSize = null;

	@Inject(method = "toJson", at = @At("TAIL"))
	private void serializeTextureSize(CallbackInfoReturnable<JsonObject> cir, @Local(name = "root") JsonObject root) {
		if (scabbard$textureSize != null) {
			JsonArray size = new JsonArray();
			size.add(scabbard$textureSize.getFirst());
			size.add(scabbard$textureSize.getSecond());
			root.add("texture_size", size);
		}
	}

	@Inject(method = "lambda$toJson$0", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonObject;add(Ljava/lang/String;Lcom/google/gson/JsonElement;)V"))
	private void serializeLightEmission(JsonArray elements, BlockElement part, CallbackInfo ci, @Local(name = "partObj") JsonObject partObj) {
		int lightEmission = part.getFaceData().blockLight();
		if (lightEmission > 0 && lightEmission < 16) partObj.addProperty("light_emission", lightEmission);
	}

	@Override
	public T scabbard$textureSize(int width, int height) {
		scabbard$textureSize = Pair.of(width, height);
		return (T) (Object) this;
	}
}
