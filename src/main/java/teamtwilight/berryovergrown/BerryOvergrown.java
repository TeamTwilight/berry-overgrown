package teamtwilight.berryovergrown;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;

import java.util.function.Supplier;

@SuppressWarnings("Convert2MethodRef")
@Mod(BerryOvergrown.MODID)
public class BerryOvergrown {

    public static final String MODID = "berry_overgrown";
    public static final Logger LOGGER = LogUtils.getLogger();

	private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, MODID);

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<OreberryBushPlacementFilter>> OREBERRY_PLACE_FILTER = PLACEMENT_MODIFIER_TYPES.register("oreberry_place_filter", () -> () -> OreberryBushPlacementFilter.CODEC);

	private final PackSource overworld = new ConfigToggledPackSource(() -> Config.OVERWORLD_BERRIES.getAsBoolean());
	private final PackSource nether = new ConfigToggledPackSource(() -> Config.NETHER_BERRIES.getAsBoolean());
	private final PackSource ore = new ConfigToggledPackSource(() -> Config.OREBERRIES.getAsBoolean());

	public BerryOvergrown(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerDataPacks);

		PLACEMENT_MODIFIER_TYPES.register(modEventBus);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

	private void registerDataPacks(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.SERVER_DATA) return;

		this.addPack(event, "ore", "Twilight Forest: Oreberries", Config.OREBERRIES.getAsBoolean(), this.ore);
		this.addPack(event, "nether", "Twilight Forest: Nether Berries", Config.NETHER_BERRIES.getAsBoolean(), this.nether);
		this.addPack(event, "overworld", "Twilight Forest: Overworld Berries", Config.OVERWORLD_BERRIES.getAsBoolean(), this.overworld);
	}

	private void addPack(AddPackFindersEvent event, String packFolder, String name, boolean autoAdd, PackSource packSource) {
		LOGGER.debug(autoAdd ? "Adding {} pack to activated column" : "Adding {} pack to deactivated column", name);

		event.addPackFinders(
				modid("datapacks/" + packFolder),
				PackType.SERVER_DATA,
				Component.literal(name),
				packSource,
				false,
				Pack.Position.TOP
		);
	}

	public static ResourceLocation modid(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	private record ConfigToggledPackSource(Supplier<Boolean> checkAddAutomatically) implements PackSource {
		@Override
		public Component decorate(Component title) {
			return Component.translatable("pack.nameAndSource", title, Component.translatable("pack.source.builtin")).withStyle(ChatFormatting.GRAY);
		}

		@Override
		public boolean shouldAddAutomatically() {
			return this.checkAddAutomatically.get();
		}
	}
}
