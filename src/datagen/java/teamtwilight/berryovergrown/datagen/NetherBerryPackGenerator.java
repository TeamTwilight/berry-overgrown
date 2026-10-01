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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFFeatures;
import twilightforest.world.components.feature.config.BerryBushConfig;

import java.util.List;

public class NetherBerryPackGenerator extends BerryPackGenerator {

	private record NetherBerryBush(String name, DeferredBlock<Block> bushBlock, List<String> bopBiomes) { }

	private static final List<NetherBerryBush> NETHER_BERRY_BUSHES = List.of(
			new NetherBerryBush("blightberry", TFBlocks.BLIGHTBERRY_BUSH, List.of("crystalline_chasm", "undergrowth")),
			new NetherBerryBush("duskberry", TFBlocks.DUSKBERRY_BUSH, List.of("crystalline_chasm", "withered_abyss")),
			new NetherBerryBush("skyberry", TFBlocks.SKYBERRY_BUSH, List.of("crystalline_chasm")),
			new NetherBerryBush("stingberry", TFBlocks.STINGBERRY_BUSH, List.of("crystalline_chasm", "undergrowth"))
	);

	public NetherBerryPackGenerator() {
		super("nether", "Adds TF's Berry Bushes to the Nether!");
	}

	@Override
	protected void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		for (NetherBerryBush netherBerryBush : NETHER_BERRY_BUSHES) {
			context.register(this.configuredFeature(netherBerryBush), new ConfiguredFeature<>(TFFeatures.BERRY_BUSH.get(), new BerryBushConfig(
					netherBerryBush.bushBlock().get().defaultBlockState(),
					BlockTagGenerator.DARK_TOWER_BERRY_BUSHES_SURVIVE,
					false
			)));
		}
	}

	@Override
	protected void placedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

		for (NetherBerryBush netherBerryBush : NETHER_BERRY_BUSHES) {
			//noinspection deprecation
			context.register(this.placedFeature(netherBerryBush), new PlacedFeature(features.getOrThrow(this.configuredFeature(netherBerryBush)), List.of(
					CountOnEveryLayerPlacement.of(1),
					RarityFilter.onAverageOnceEvery(50),
					BiomeFilter.biome()
			)));
		}
	}

	@Override
	protected void biomeModifiers(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

		for (NetherBerryBush netherBerryBush : NETHER_BERRY_BUSHES) {
			context.register(this.key(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "add_" + netherBerryBush.name() + "_bushes"), new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(this.biomeTag(netherBerryBush)),
					HolderSet.direct(placedFeatures.getOrThrow(this.placedFeature(netherBerryBush))),
					GenerationStep.Decoration.VEGETAL_DECORATION
			));
		}
	}

	@Override
	protected void biomeTags(BiomeTagAppender tags) {
		for (NetherBerryBush netherBerryBush : NETHER_BERRY_BUSHES) {
			var tag = tags.tag(this.biomeTag(netherBerryBush)).add(Biomes.NETHER_WASTES);
			netherBerryBush.bopBiomes().forEach(biome -> tag.addOptional(ResourceLocation.fromNamespaceAndPath("biomesoplenty", biome)));
		}
	}

	private ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(NetherBerryBush netherBerryBush) {
		return this.key(Registries.CONFIGURED_FEATURE, netherBerryBush.name() + "_bushes");
	}

	private ResourceKey<PlacedFeature> placedFeature(NetherBerryBush netherBerryBush) {
		return this.key(Registries.PLACED_FEATURE, netherBerryBush.name() + "_bush");
	}

	private TagKey<Biome> biomeTag(NetherBerryBush netherBerryBush) {
		return this.biomeTag("generates_" + netherBerryBush.name() + "_bushes");
	}

}
