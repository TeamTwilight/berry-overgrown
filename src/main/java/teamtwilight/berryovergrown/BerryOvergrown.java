package teamtwilight.berryovergrown;

import net.minecraft.ChatFormatting;
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

	private final PackSource builtInNotAuto = new PackSource() {
		@Override
		public Component decorate(Component title) {
			return Component.translatable("pack.nameAndSource", title, Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY);
		}

		@Override
		public boolean shouldAddAutomatically() {
			return false;
		}
	};

	public BerryOvergrown(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerDataPacks);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

	private void registerDataPacks(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.SERVER_DATA) return;

		this.addPack(event, "ore", "Twilight Forest: Ore Berries", Config.ORE_BERRIES.getAsBoolean());
		this.addPack(event, "nether", "Twilight Forest: Nether Berries", Config.NETHER_BERRIES.getAsBoolean());
		this.addPack(event, "overworld", "Twilight Forest: Overworld Berries", Config.OVERWORLD_BERRIES.getAsBoolean());
	}

	private void addPack(AddPackFindersEvent event, String packFolder, String name, boolean autoAdd) {
		LOGGER.debug(autoAdd ? "Adding {} pack to activated column" : "Adding {} pack to deactivated column", name);

		event.addPackFinders(
				modid("datapacks/" + packFolder),
				PackType.SERVER_DATA,
				Component.literal(name),
				autoAdd ? PackSource.BUILT_IN : this.builtInNotAuto,
				autoAdd,
				Pack.Position.TOP
		);
	}

	public static ResourceLocation modid(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
