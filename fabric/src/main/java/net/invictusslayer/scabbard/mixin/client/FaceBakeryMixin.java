package net.invictusslayer.scabbard.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.invictusslayer.scabbard.client.model.IExtendedBlockElementFace;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FaceBakery.class)
public class FaceBakeryMixin {
    @Unique
    private static final int STRIDE = DefaultVertexFormat.BLOCK.getIntegerSize();
    @Unique
    private static final int OFFSET = getOffset(DefaultVertexFormat.ELEMENT_UV2);

    @ModifyArg(method = "bakeQuad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/BakedQuad;<init>([IILnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Z)V"))
    private int[] setLightEmission(int[] vertices, @Local(argsOnly = true) BlockElementFace face) {
        int value = ((IExtendedBlockElementFace) face).scabbard$getLightEmission();
        for (int i = 0; i < 4; ++i) {
            vertices[i * STRIDE + OFFSET] = LightTexture.pack(value, value);
        }
        return vertices;
    }

    @Unique
    private static int getOffset(VertexFormatElement element) {
        int i = DefaultVertexFormat.BLOCK.getElements().indexOf(element);
        return ((VertexFormatAccessor) DefaultVertexFormat.BLOCK).getOffsets().getInt(i) / 4;
    }
}
