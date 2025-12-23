package teamtwilight.berryovergrown;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	public static final ModConfigSpec.BooleanValue OVERWORLD_BERRIES = BUILDER
			.comment("Automatically add Overworld Berries datapack during World Creation.")
			.define("overworld-datapack", true);

	public static final ModConfigSpec.BooleanValue NETHER_BERRIES = BUILDER
			.comment("Automatically add Nether Berries datapack during World Creation.")
			.define("nether-datapack", true);

	public static final ModConfigSpec.BooleanValue OREBERRIES = BUILDER
			.comment("Automatically add Oreberries datapack during World Creation.")
			.define("ore-datapack", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
