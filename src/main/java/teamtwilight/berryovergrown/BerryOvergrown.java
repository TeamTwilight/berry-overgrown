package teamtwilight.berryovergrown;

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
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
