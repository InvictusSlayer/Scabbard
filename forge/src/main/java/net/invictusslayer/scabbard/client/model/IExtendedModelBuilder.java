package net.invictusslayer.scabbard.client.model;

import net.minecraftforge.client.model.generators.ModelBuilder;

public interface IExtendedModelBuilder<T extends ModelBuilder<T>> {
	T textureSize(int width, int height);
}
