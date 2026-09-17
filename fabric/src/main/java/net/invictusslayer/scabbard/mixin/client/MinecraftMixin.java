package net.invictusslayer.scabbard.mixin.client;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.invictusslayer.scabbard.client.ArmorRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerArmorRenderers(GameConfig gameConfig, CallbackInfo ci) {
        ArmorRendererRegistry.getRenderers().forEach((item, factory) -> {
            ArmorRenderer.register((poseStack, buffer, stack, entity, slot, light, original) -> {
                HumanoidModel<LivingEntity> model = factory.render(entity, stack, slot, original);
                original.copyPropertiesTo(model);
                ResourceLocation loc = BuiltInRegistries.ITEM.getKey(stack.getItem());
                ResourceLocation texture = new ResourceLocation(loc.getNamespace(), "textures/models/armor/" + loc.getPath() + ".png");
                ArmorRenderer.renderPart(poseStack, buffer, light, stack, model, texture);
            }, item);
        });
    }
}
