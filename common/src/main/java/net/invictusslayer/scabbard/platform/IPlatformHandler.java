package net.invictusslayer.scabbard.platform;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.util.function.Supplier;

/*
Based on Joseph T. McQuigg's BWG PlatformHandler
https://github.com/Potion-Studios/Oh-The-Biomes-Weve-Gone/blob/c9c97df5bf24e1b8aa9eaa32bf477d569aa7c156/Common/src/main/java/net/potionstudios/biomeswevegone/PlatformHandler.java
 */
public interface IPlatformHandler {
	Platform getPlatform();

	Path configPath();

	<T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value);

	<T> Supplier<Holder.Reference<T>> registerHolder(Registry<T> registry, String name, Supplier<T> value);

	enum Platform {
		FORGE,
		FABRIC,
		NEOFORGE
	}
}
