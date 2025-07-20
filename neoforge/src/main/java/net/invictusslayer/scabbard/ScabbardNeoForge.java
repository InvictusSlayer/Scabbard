package net.invictusslayer.scabbard;

import net.invictusslayer.scabbard.platform.NeoForgePlatformHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Scabbard.MOD_ID)
public class ScabbardNeoForge {
	public ScabbardNeoForge(final IEventBus bus) {
		Scabbard.init();
		NeoForgePlatformHandler.register(bus);
	}
}
