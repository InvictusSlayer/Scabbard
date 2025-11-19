package net.invictusslayer.scabbard.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.invictusslayer.scabbard.client.model.IExtendedModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ModelBuilder.class)
public class ModelBuilderMixin<T extends ModelBuilder<T>> implements IExtendedModelBuilder<T> {
	@Unique
	private Pair<Integer, Integer> textureSize = null;

	@Inject(method = "toJson", at = @At("TAIL"))
	private void onToJson(CallbackInfoReturnable<JsonObject> cir, @Local(name = "root") JsonObject root) {
		if (textureSize != null) root.addProperty("texture_size", String.format("[%s,%s]", textureSize.getFirst(), textureSize.getSecond()));
	}

	@Override
	public T textureSize(int width, int height) {
		textureSize = Pair.of(width, height);
		return (T) (Object) this;
	}
}
