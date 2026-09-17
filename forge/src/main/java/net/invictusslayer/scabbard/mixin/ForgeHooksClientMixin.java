package net.invictusslayer.scabbard.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.invictusslayer.scabbard.client.ArmorRendererRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class ForgeHooksClientMixin {
    @ModifyReturnValue(method = "getArmorTexture", at = @At("RETURN"))
    private static String injectArmorTextures(String original, @Local(argsOnly = true) ItemStack armor, @Local(argsOnly = true, ordinal = 0) String _default) {
        if (!original.equals(_default)) return original;
        if (armor.getItem() instanceof ArmorItem item && ArmorRendererRegistry.getRenderers().containsKey(item)) {
            ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
            if (loc == null) return original;
            return loc.withPrefix("textures/models/armor/").withSuffix(".png").toString();
        }
        return _default;
    }

    @ModifyReturnValue(method = "getArmorModel", at = @At("RETURN"))
    private static Model injectArmorRenderers(Model original, LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<LivingEntity> _default) {
        if (original != _default) return original;
        if (itemStack.getItem() instanceof ArmorItem item && ArmorRendererRegistry.getRenderers().containsKey(item)) {
            HumanoidModel<LivingEntity> model = ArmorRendererRegistry.getRenderers().get(item).render(entityLiving, itemStack, slot, _default);
            _default.copyPropertiesTo(model);
            return model;
        }
        return _default;
    }
}
