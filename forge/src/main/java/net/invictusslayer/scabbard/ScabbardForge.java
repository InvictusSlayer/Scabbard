package net.invictusslayer.scabbard;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Scabbard.MOD_ID)
public class ScabbardForge {
	public ScabbardForge(final FMLJavaModLoadingContext context) {
		IEventBus bus = context.getModEventBus();
		Scabbard.init();
//		ForgePlatformHandler.register(bus);
	}
}
