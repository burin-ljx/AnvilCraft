package dev.dubhe.anvilcraft.block;

import dev.dubhe.anvilcraft.block.state.IStateAddableMultiplePartBlockState;
import dev.dubhe.anvilcraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class AbstractStateAddableMultiplePartBlock<
        P extends Enum<P> & IStateAddableMultiplePartBlockState<P, E>,
        T extends Property<E>,
        E extends Comparable<E>
    > extends Block {
    final P mainPart;

    public AbstractStateAddableMultiplePartBlock(Properties properties) {
        super(properties);
        this.mainPart = Arrays.stream(getParts()).filter(IStateAddableMultiplePartBlockState::isMain).findFirst().orElse(null);
    }

    public abstract Property<P> getPart();

    public abstract P[] getParts();

    public abstract T getAdditionalProperty();

    public <J extends Property<H>, H extends Comparable<H>> void updateState(Level level, BlockPos pos, J property, H value, int flag) {
        BlockState state = level.getBlockState(pos);
        E additionalPropertyValue = state.getValue(getAdditionalProperty());
        Vec3i origin = pos.subtract(state.getValue(getPart()).getOffset(additionalPropertyValue));
        for (P part : getParts()) {
            Vec3i offset = origin.offset(part.getOffset(additionalPropertyValue));
            level.setBlock(new BlockPos(offset), state.setValue(getPart(), part).setValue(property, value), flag);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getPart(), getAdditionalProperty());
    }

    @Override
    public void setPlacedBy(
            @NotNull Level level,
            @NotNull BlockPos pos,
            BlockState state,
            @Nullable LivingEntity placer,
            @NotNull ItemStack stack
    ) {
        if (!state.hasProperty(this.getPart())) return;
        for (P part : this.getParts()) {
            BlockPos blockPos = pos.offset(part.getOffset(state.getValue(getAdditionalProperty())));
            BlockState newState = placedState(part, state);
            level.setBlockAndUpdate(blockPos, newState);
        }
    }

    protected BlockState placedState(P part, BlockState state) {
        return state.setValue(this.getPart(), part);
    }

    @Override
    public @NotNull BlockState updateShape(
            BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos) {
        if (!state.hasProperty(this.getPart())) {
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }
        IStateAddableMultiplePartBlockState<P, E> state1 = state.getValue(this.getPart());
        for (P part : getParts()) {
            Vec3i offset = neighborPos.subtract(pos).offset(state1.getOffset(state.getValue(getAdditionalProperty()))); // 更新来源偏移值
            if (offset.distSqr(part.getOffset(state.getValue(getAdditionalProperty()))) != 0) continue;
            if (!neighborState.is(this)
                    || !neighborState.hasProperty(this.getPart())
                    || neighborState.getValue(this.getPart()) != part) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(
            Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull Player player
    ) {
        if (!level.isClientSide && player.isCreative()) {
            this.preventCreativeDropFromMainPart(level, pos, state, player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private void preventCreativeDropFromMainPart(
            Level level,
            BlockPos pos,
            BlockState state,
            Player player
    ) {
        if (!state.is(this)) return;
        if (!state.hasProperty(this.getPart())) return;
        P value = state.getValue(this.getPart());
        if (value.isMain()) return;
        BlockPos mainPartPos = pos.subtract(value.getOffset(state.getValue(getAdditionalProperty()))).offset(mainPart.getOffset(state.getValue(getAdditionalProperty())));
        BlockState mainPartState = level.getBlockState(mainPartPos);
        if (!mainPartState.is(this)) return;
        if (!mainPartState.hasProperty(this.getPart())) return;
        BlockState blockState2 = mainPartState.getFluidState().createLegacyBlock();
        level.setBlock(mainPartPos, blockState2, 35);
        level.levelEvent(player, 2001, mainPartPos, Block.getId(mainPartState));
    }

    /**
     * 获取多方块战利品表
     *
     * @param provider 提供器
     * @param block    方块
     */
    public static <P extends Enum<P> & IStateAddableMultiplePartBlockState<P, E>, T extends Property<E>, E extends Comparable<E>> void loot(
            BlockLootSubProvider provider, AbstractStateAddableMultiplePartBlock<P, T, E> block
    ) {
        for (P part : block.getParts()) {
            if (part.isMain()) {
                provider.add(block, provider.createSinglePropConditionTable(block, block.getPart(), part));
                break;
            }
        }
    }

    @Nullable
    public BlockState getPlacementState(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }

    /**
     * 是否有足够的空间放下方块
     */
    public boolean hasEnoughSpace(BlockState originState, BlockPos pos, LevelReader level) {
        for (P part : getParts()) {
            BlockPos pos1 = pos.offset(part.getOffset(originState.getValue(getAdditionalProperty())));
            if (level.isOutsideBuildHeight(pos1)) return false;
            BlockState state = level.getBlockState(pos1);
            if (!state.isAir() && !state.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack pStack,
            @NotNull BlockState pState,
            @NotNull Level pLevel,
            @NotNull BlockPos pPos,
            @NotNull Player pPlayer,
            @NotNull InteractionHand pHand,
            @NotNull BlockHitResult pHitResult) {
        return Util.interactionResultConverter().apply(this.use(pState, pLevel, pPos, pPlayer, pHand, pHitResult));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState pState,
            @NotNull Level pLevel,
            @NotNull BlockPos pPos,
            @NotNull Player pPlayer,
            @NotNull BlockHitResult pHitResult
    ) {
        return this.use(pState, pLevel, pPos, pPlayer, InteractionHand.MAIN_HAND, pHitResult);
    }

    @SuppressWarnings("unused")
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        return InteractionResult.PASS;
    }
}
