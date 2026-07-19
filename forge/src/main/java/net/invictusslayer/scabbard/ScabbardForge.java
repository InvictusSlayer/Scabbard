package net.invictusslayer.scabbard;

import net.invictusslayer.scabbard.resource.BuiltInPackHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.nio.file.Path;
import java.util.Optional;

@Mod(Scabbard.MOD_ID)
public class ScabbardForge {
	public ScabbardForge(final FMLJavaModLoadingContext context) {
		IEventBus bus = context.getModEventBus();
		Scabbard.init();

		bus.addListener((AddPackFindersEvent event) -> {
			if (event.getPackType() == PackType.CLIENT_RESOURCES) {
				BuiltInPackHandler.PACKS.forEach(data -> {
					Path path = ModList.get().getModFileById(data.modId()).getFile().findResource("resourcepacks/" + data.packId());
                    PackLocationInfo info = new PackLocationInfo(data.packId(), Component.literal(data.name()), data.enabled() ? PackSource.BUILT_IN : PackSource.FEATURE, Optional.empty());
					Pack pack = Pack.readMetaAndCreate(info, new PathPackResources.PathResourcesSupplier(path), event.getPackType(),
                            new PackSelectionConfig(true, Pack.Position.TOP, false));
					event.addRepositorySource(consumer -> consumer.accept(pack));
				});
			}
		});

//		ForgePlatformHandler.register(bus);
	}
}
