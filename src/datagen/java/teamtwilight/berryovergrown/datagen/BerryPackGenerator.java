package teamtwilight.berryovergrown.datagen;

import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.TagKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.Set;

public abstract class BerryPackGenerator {

	private final String folder;
	private final String namespace;
	private final String description;

	protected BerryPackGenerator(String name, String description) {
		this.folder = name;
		this.namespace = "%s_tf_bushes".formatted(name);
		this.description = description;
	}

	protected abstract void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context);

	protected abstract void placedFeatures(BootstrapContext<PlacedFeature> context);

	protected abstract void biomeModifiers(BootstrapContext<BiomeModifier> context);

	protected abstract void biomeTags(BiomeTagAppender tags);

	public void gather(GatherDataEvent event) {
		DataGenerator.PackGenerator pack = event.getGenerator().getPackGenerator(event.includeServer(), this.folder, "datapacks/" + this.folder);

		pack.addProvider(output -> new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
				Component.literal(this.description),
				SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA),
				Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE))
		)));

		RegistrySetBuilder registries = new RegistrySetBuilder()
				.add(Registries.CONFIGURED_FEATURE, this::configuredFeatures)
				.add(Registries.PLACED_FEATURE, this::placedFeatures)
				.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, this::biomeModifiers);

		DatapackBuiltinEntriesProvider entries = pack.addProvider(output -> new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registries, Set.of(this.namespace)));

		pack.addProvider(output -> new BiomeTagsProvider(output, entries.getRegistryProvider(), this.namespace, event.getExistingFileHelper()) {
			@Override
			protected void addTags(HolderLookup.Provider provider) {
				BerryPackGenerator.this.biomeTags(this::tag);
			}
		});
	}

	protected ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(this.namespace, path);
	}

	protected <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String path) {
		return ResourceKey.create(registry, this.id(path));
	}

	protected TagKey<Biome> biomeTag(String path) {
		return TagKey.create(Registries.BIOME, this.id(path));
	}

	@FunctionalInterface
	protected interface BiomeTagAppender {
		TagsProvider.TagAppender<Biome> tag(TagKey<Biome> tag);
	}

}
