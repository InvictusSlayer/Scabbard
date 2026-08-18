package net.invictusslayer.scabbard.mixin.client;

import net.invictusslayer.scabbard.client.model.IExtendedBlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockElementFace.class)
public class BlockElementFaceMixin implements IExtendedBlockElementFace {
    @Unique
    private int scabbard$lightEmission;

    @Override
    public void scabbard$setLightEmission(int value) {
        scabbard$lightEmission = value;
    }

    @Override
    public int scabbard$getLightEmission() {
        return scabbard$lightEmission;
    }
}
