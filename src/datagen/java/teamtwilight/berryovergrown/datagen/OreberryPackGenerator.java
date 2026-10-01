package teamtwilight.berryovergrown.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import teamtwilight.berryovergrown.OreberryBushPlacementFilter;
import twilightforest.block.TFBushBlock;
import twilightforest.init.TFBlocks;

import java.util.List;

public class OreberryPackGenerator extends BerryPackGenerator {

	private record OreberryBush(String name, DeferredBlock<Block> bushBlock, int minDepth, int maxDepth, int rarity) {}

	private static final List<OreberryBush> OREBERRY_BUSHES = List.of(
			new OreberryBush("copper", TFBlocks.COPPER_OREBERRY, -48, -2, 9),
			new OreberryBush("iron", TFBlocks.IRON_OREBERRY, -80, -16, 16),
			new OreberryBush("gold", TFBlocks.GOLD_OREBERRY, -128, -48, 25),
			new OreberryBush("essence", TFBlocks.ESSENCE_OREBERRY, -256, -80, 36)
	);

	public OreberryPackGenerator() {
		super("ore", "Adds TF's Oreberry Bushes to caves!");
	}

	@Override
	protected void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		for (OreberryBush oreberryBush : OREBERRY_BUSHES) {
			context.register(this.configuredFeature(oreberryBush), new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
					BlockStateProvider.simple(oreberryBush.bushBlock().get().defaultBlockState().setValue(TFBushBlock.AGE, 3))
			)));
		}
	}

	@Override
	protected void placedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

		for (OreberryBush oreberryBush : OREBERRY_BUSHES) {
			//noinspection deprecation
			context.register(this.placedFeature(oreberryBush), new PlacedFeature(features.getOrThrow(this.configuredFeature(oreberryBush)), List.of(
					CountOnEveryLayerPlacement.of(1),
					SurfaceRelativeThresholdFilter.of(Heightmap.Types.WORLD_SURFACE_WG, oreberryBush.minDepth(), oreberryBush.maxDepth()),
					RarityFilter.onAverageOnceEvery(oreberryBush.rarity()),
					new OreberryBushPlacementFilter(),
					BiomeFilter.biome()
			)));
		}
	}

	@Override
	protected void biomeModifiers(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

		for (OreberryBush oreberryBush : OREBERRY_BUSHES) {
			context.register(this.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "add_" + oreberryBush.name() + "_oreberry_bushes"), new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(this.biomeTag(oreberryBush)),
					HolderSet.direct(placedFeatures.getOrThrow(this.placedFeature(oreberryBush))),
					GenerationStep.Decoration.VEGETAL_DECORATION
			));
		}
	}

	@Override
	protected void biomeTags(BiomeTagAppender tags) {
		for (OreberryBush oreberryBush : OREBERRY_BUSHES) {
			tags.tag(this.biomeTag(oreberryBush)).addTag(BiomeTags.IS_OVERWORLD);
		}
	}

	private ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(OreberryBush oreberryBush) {
		return this.key(Registries.CONFIGURED_FEATURE, oreberryBush.name() + "_oreberry_bush");
	}

	private ResourceKey<PlacedFeature> placedFeature(OreberryBush oreberryBush) {
		return this.key(Registries.PLACED_FEATURE, oreberryBush.name() + "_oreberry_bush");
	}

	private TagKey<Biome> biomeTag(OreberryBush oreberryBush) {
		return this.biomeTag("generates_" + oreberryBush.name() + "_oreberry_bushes");
	}

}
