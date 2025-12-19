package teamtwilight.berryovergrown;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class OreberryBushPlacementFilter extends PlacementFilter {
	public static final MapCodec<OreberryBushPlacementFilter> CODEC = MapCodec.unit(OreberryBushPlacementFilter::new);

	@Override
	protected boolean shouldPlace(PlacementContext placementContext, RandomSource randomSource, BlockPos blockPos) {
		WorldGenLevel level = placementContext.getLevel();

		if (this.isReplaceableNotLiquid(blockPos, level)) {
			return true;
		}

		// If replacing liquid, check if not water
		return !level.getFluidState(blockPos).is(FluidTags.WATER) && this.isReplaceableNotLiquid(blockPos.above(), level);
	}

	private boolean isReplaceableNotLiquid(BlockPos blockPos, WorldGenLevel level) {
		BlockState blockState = level.getBlockState(blockPos);
		return (blockState.isAir() || blockState.canBeReplaced()) && blockState.getFluidState().isEmpty();
	}

	@Override
	public PlacementModifierType<?> type() {
		return BerryOvergrown.OREBERRY_PLACE_FILTER.value();
	}
}
