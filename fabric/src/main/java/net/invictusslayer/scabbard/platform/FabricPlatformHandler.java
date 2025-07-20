package net.invictusslayer.scabbard.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.function.Supplier;

public class FabricPlatformHandler implements IPlatformHandler {
	@Override
	public Platform getPlatform() {
		return Platform.FABRIC;
	}

	@Override
	public Path configPath(String modId) {
		return FabricLoader.getInstance().getConfigDir().resolve(modId);
	}

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String modId, String name, Supplier<T> value) {
		T registered = Registry.register(registry, ResourceLocation.fromNamespaceAndPath(modId, name), value.get());
		return () -> registered;
	}

	@Override
	public <T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String modId, String name, Supplier<T> value) {
		Holder.Reference<T> registered = Registry.registerForHolder(registry, ResourceLocation.fromNamespaceAndPath(modId, name), value.get());
		return () -> registered;
	}
}
