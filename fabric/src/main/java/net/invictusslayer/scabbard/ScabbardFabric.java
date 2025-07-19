package net.invictusslayer.scabbard;

import net.fabricmc.api.ModInitializer;

public class ScabbardFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		Scabbard.init();
	}
}
