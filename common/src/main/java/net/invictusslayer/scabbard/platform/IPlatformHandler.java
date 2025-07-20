package net.invictusslayer.scabbard.platform;

import net.invictusslayer.scabbard.Scabbard;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/*
Based on Joseph T. McQuigg's BWG PlatformHandler
https://github.com/Potion-Studios/Oh-The-Biomes-Weve-Gone/blob/c9c97df5bf24e1b8aa9eaa32bf477d569aa7c156/Common/src/main/java/net/potionstudios/biomeswevegone/PlatformHandler.java
 */
public interface IPlatformHandler {
	IPlatformHandler PLATFORM = load(IPlatformHandler.class);

	Platform getPlatform();

	Path configPath(String modId);

	<T> Supplier<T> register(Registry<? super T> registry, String modId, String name, Supplier<T> value);

	<T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String modId, String name, Supplier<T> value);

	private static <T> T load(Class<T> clazz) {
		final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
		Scabbard.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
		return loadedService;
	}

	enum Platform {
		FABRIC,
		FORGE,
		NEOFORGE
	}
}
