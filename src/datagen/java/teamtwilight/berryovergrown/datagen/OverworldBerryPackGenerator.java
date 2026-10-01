package teamtwilight.berryovergrown.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.init.TFConfiguredFeatures;

import java.util.List;

public class OverworldBerryPackGenerator extends BerryPackGenerator {

	private record BerryBush(String name, ResourceKey<ConfiguredFeature<?, ?>> featureKey, List<ResourceKey<Biome>> biomes, List<String> bopBiomes) { }

	private static final BerryBush BLACKBERRY_BUSH = new BerryBush("blackberry", TFConfiguredFeatures.BLACKBERRY_BUSHES, List.of(
			Biomes.BIRCH_FOREST,
			Biomes.DARK_FOREST,
			Biomes.FOREST,
			Biomes.JUNGLE,
			Biomes.MANGROVE_SWAMP,
			Biomes.MEADOW,
			Biomes.MUSHROOM_FIELDS,
			Biomes.OLD_GROWTH_BIRCH_FOREST,
			Biomes.SPARSE_JUNGLE,
			Biomes.SWAMP
	), List.of(
			"bayou",
			"bog",
			"fungal_jungle",
			"highland",
			"jacaranda_glade",
			"jade_cliffs",
			"marsh",
			"old_growth_woodland",
			"pumpkin_patch",
			"rainforest",
			"tropics",
			"wetland",
			"woodland"
	));

	private static final BerryBush BLUEBERRY_BUSH = new BerryBush("blueberry", TFConfiguredFeatures.BLUEBERRY_BUSHES, List.of(
			Biomes.BIRCH_FOREST,
			Biomes.DARK_FOREST,
			Biomes.DEEP_OCEAN,
			Biomes.FOREST,
			Biomes.OCEAN,
			Biomes.OLD_GROWTH_BIRCH_FOREST,
			Biomes.OLD_GROWTH_PINE_TAIGA,
			Biomes.OLD_GROWTH_SPRUCE_TAIGA,
			Biomes.PLAINS,
			Biomes.RIVER,
			Biomes.TAIGA
	), List.of(
			"coniferous_forest",
			"field",
			"fir_clearing",
			"floodplain",
			"forested_field",
			"grassland",
			"maple_woods",
			"mediterranean_forest",
			"old_growth_woodland",
			"orchard",
			"prairie",
			"shrubland",
			"woodland"
	));

	private static final BerryBush MALOBERRY_BUSH = new BerryBush("maloberry", TFConfiguredFeatures.MALOBERRY_BUSHES, List.of(
			Biomes.COLD_OCEAN,
			Biomes.DEEP_COLD_OCEAN,
			Biomes.DEEP_FROZEN_OCEAN,
			Biomes.FROZEN_OCEAN,
			Biomes.FROZEN_PEAKS,
			Biomes.FROZEN_RIVER,
			Biomes.GROVE,
			Biomes.ICE_SPIKES,
			Biomes.JAGGED_PEAKS,
			Biomes.SNOWY_PLAINS,
			Biomes.SNOWY_SLOPES,
			Biomes.SNOWY_TAIGA
	), List.of(
			"auroral_garden",
			"muskeg",
			"snowblossom_grove",
			"snowy_coniferous_forest",
			"snowy_fir_clearing",
			"snowy_maple_woods",
			"tundra"
	));

	private static final BerryBush RASPBERRY_BUSH = new BerryBush("raspberry", TFConfiguredFeatures.RASPBERRY_BUSHES, List.of(
			Biomes.BIRCH_FOREST,
			Biomes.CHERRY_GROVE,
			Biomes.DARK_FOREST,
			Biomes.FOREST,
			Biomes.JUNGLE,
			Biomes.MEADOW,
			Biomes.OLD_GROWTH_BIRCH_FOREST,
			Biomes.PLAINS,
			Biomes.SPARSE_JUNGLE,
			Biomes.SWAMP
	), List.of(
			"aspen_glade",
			"bog",
			"field",
			"forested_field",
			"fungal_jungle",
			"grassland",
			"jacaranda_glade",
			"marsh",
			"old_growth_woodland",
			"orchard",
			"prairie",
			"rainforest",
			"redwood_forest",
			"shrubland",
			"tropics",
			"wetland",
			"woodland"
	));

	private static final List<BerryBush> BERRY_BUSHES = List.of(
			BLACKBERRY_BUSH,
			BLUEBERRY_BUSH,
			MALOBERRY_BUSH,
			RASPBERRY_BUSH
	);

	public OverworldBerryPackGenerator() {
		super("overworld", "Adds TF's Berry Bushes to the Overworld!");
	}

	@Override
	protected void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		for (BerryBush berryBush : BERRY_BUSHES) {
			context.register(berryBush.featureKey(), new ConfiguredFeature<>(Feature.NO_OP, NoneFeatureConfiguration.INSTANCE));
		}
	}

	@Override
	protected void placedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

		for (BerryBush berryBush : BERRY_BUSHES) {
			context.register(this.placedFeature(berryBush), new PlacedFeature(features.getOrThrow(berryBush.featureKey()), List.of(
					HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
					RarityFilter.onAverageOnceEvery(50),
					InSquarePlacement.spread(),
					HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
					BiomeFilter.biome()
			)));
		}
	}

	@Override
	protected void biomeModifiers(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

		for (BerryBush berryBush : BERRY_BUSHES) {
			context.register(this.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "add_" + berryBush.name() + "_bushes"), new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(this.biomeTag(berryBush)),
					HolderSet.direct(placedFeatures.getOrThrow(this.placedFeature(berryBush))),
					GenerationStep.Decoration.VEGETAL_DECORATION
			));
		}
	}

	@Override
	protected void biomeTags(BiomeTagAppender tags) {
		for (BerryBush berryBush : BERRY_BUSHES) {
			var tag = tags.tag(this.biomeTag(berryBush));
			berryBush.biomes().forEach(tag::add);
			berryBush.bopBiomes().forEach(biome -> tag.addOptional(ResourceLocation.fromNamespaceAndPath("biomesoplenty", biome)));
		}
	}

	private ResourceKey<PlacedFeature> placedFeature(BerryBush berryBush) {
		return this.key(Registries.PLACED_FEATURE, berryBush.name() + "_bush");
	}

	private TagKey<Biome> biomeTag(BerryBush berryBush) {
		return this.biomeTag("generates_" + berryBush.name() + "_bushes");
	}

}
