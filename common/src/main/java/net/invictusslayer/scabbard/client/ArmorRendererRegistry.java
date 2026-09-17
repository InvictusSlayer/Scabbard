package net.invictusslayer.scabbard.client;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class ArmorRendererRegistry {
    private static final Map<ArmorItem, RenderFactory> ARMOR_RENDERERS = new Reference2ObjectOpenHashMap<>();

    public static Map<ArmorItem, RenderFactory> getRenderers() {
        return Map.copyOf(ARMOR_RENDERERS);
    }

    public static void registerRenderer(ArmorItem item, RenderFactory factory) {
        ARMOR_RENDERERS.putIfAbsent(item, factory);
    }

    @FunctionalInterface
    public interface RenderFactory {
        HumanoidModel<LivingEntity> render(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<LivingEntity> original);
    }
}
