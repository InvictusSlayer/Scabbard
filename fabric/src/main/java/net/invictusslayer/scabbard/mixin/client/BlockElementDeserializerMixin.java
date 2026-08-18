package net.invictusslayer.scabbard.mixin.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.invictusslayer.scabbard.client.model.IExtendedBlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockElement.Deserializer.class)
public class BlockElementDeserializerMixin {
    @ModifyReturnValue(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockElement;", at = @At("RETURN"))
    private BlockElement onDeserialize(BlockElement original, @Local JsonObject json) {
        int i;
        if (json.has("light_emission")) {
            boolean flag = GsonHelper.isNumberValue(json, "light_emission");
            i = flag ? GsonHelper.getAsInt(json, "light_emission") : 0;

            if (!flag || i < 0 || i > 15) {
                throw new JsonParseException("Expected light_emission to be an Integer between (inclusive) 0 and 15");
            }
        } else {
            i = 0;
        }
        original.faces.forEach((dir, face) -> ((IExtendedBlockElementFace) face).scabbard$setLightEmission(i));
        return original;
    }
}
