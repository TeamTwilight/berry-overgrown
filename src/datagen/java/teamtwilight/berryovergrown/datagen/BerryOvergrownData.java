package teamtwilight.berryovergrown.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import teamtwilight.berryovergrown.BerryOvergrown;

import java.util.List;

@EventBusSubscriber(modid = BerryOvergrown.MODID)
public class BerryOvergrownData {

	private static final List<BerryPackGenerator> PACKS = List.of(
			new OreberryPackGenerator(),
			new NetherBerryPackGenerator(),
			new OverworldBerryPackGenerator()
	);

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		for (BerryPackGenerator packGenerator : PACKS) {
			packGenerator.gather(event);
		}
	}

}
