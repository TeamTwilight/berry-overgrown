package teamtwilight.berryovergrown;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

@Mod(BerryOvergrown.MODID)
public class BerryOvergrown {

    public static final String MODID = "berry_overgrown";
    public static final Logger LOGGER = LogUtils.getLogger();

	public BerryOvergrown(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerDataPacks);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

	private void registerDataPacks(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.SERVER_DATA) return;

		LOGGER.debug("Adding Berry Overgrown packs.");

		event.addPackFinders(modid("datapacks/ore"), PackType.SERVER_DATA, Component.literal("Twilight Forest: Ore Berries"), PackSource.BUILT_IN, Config.ORE_BERRIES.getAsBoolean(), Pack.Position.TOP);
		event.addPackFinders(modid("datapacks/nether"), PackType.SERVER_DATA, Component.literal("Twilight Forest: Nether Berries"), PackSource.BUILT_IN, Config.NETHER_BERRIES.getAsBoolean(), Pack.Position.TOP);
		event.addPackFinders(modid("datapacks/overworld"), PackType.SERVER_DATA, Component.literal("Twilight Forest: Overworld Berries"), PackSource.BUILT_IN, Config.OVERWORLD_BERRIES.getAsBoolean(), Pack.Position.TOP);
	}

	public static ResourceLocation modid(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
