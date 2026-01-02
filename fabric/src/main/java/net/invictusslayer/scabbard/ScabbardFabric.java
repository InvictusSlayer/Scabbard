package net.invictusslayer.scabbard;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.invictusslayer.scabbard.api.ScabbardApi;

public class ScabbardFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Scabbard.init();
		FabricLoader.getInstance().getEntrypointContainers("scabbard", ScabbardApi.class).forEach(entrypoint -> {
			ScabbardApi api = entrypoint.getEntrypoint();
			api.onScabbardInitialised();
		});
	}
}
