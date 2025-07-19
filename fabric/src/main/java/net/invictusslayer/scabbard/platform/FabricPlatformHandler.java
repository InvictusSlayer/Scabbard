package net.invictusslayer.scabbard.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.invictusslayer.scabbard.Scabbard;
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
	public Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(Scabbard.MOD_ID);
	}

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value) {
		T registered = Registry.register(registry, ResourceLocation.fromNamespaceAndPath(Scabbard.MOD_ID, name), value.get());
		return () -> registered;
	}

	@Override
	public <T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String name, Supplier<T> value) {
		Holder.Reference<T> registered = Registry.registerForHolder(registry, ResourceLocation.fromNamespaceAndPath(Scabbard.MOD_ID, name), value.get());
		return () -> registered;
	}
}
