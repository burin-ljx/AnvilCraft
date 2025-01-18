package dev.dubhe.anvilcraft.block.pressurePlate;

import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ContinuousPressingPressurePlateBlock extends PowerLevelPressurePlateBlock {
    public static final IntegerProperty COOLDOWN_TIME_DEFAULT = IntegerProperty.create("cooldown_time", 0, 10);

    @Getter
    protected IntegerProperty cooldownTimeProperty = COOLDOWN_TIME_DEFAULT;
    @Getter
    protected int cooldownTime = getMaxCooldownTime();

    public ContinuousPressingPressurePlateBlock(Properties properties) {
        super(BlockSetType.IRON, properties);
        this.registerDefaultState(this.stateDefinition.any()
                                          .setValue(POWER, 0)
                                          .setValue(this.cooldownTimeProperty, 0)
        );
    }

    protected abstract int getMaxCooldownTime();

    public boolean isInCooldown() {
        return this.cooldownTime != getMaxCooldownTime() && this.cooldownTime > 0;
    }

    @Override
    protected void checkPressed(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, int currentSignal) {
        int expectedSignal = this.getSignalStrength(level, pos);
        if (!isInCooldown()) {
            updateSignal(level, pos, state, currentSignal, expectedSignal);

            boolean isActivating = currentSignal > 0;
            boolean needActivate = expectedSignal > 0;
            sendEvent(entity, level, pos, needActivate, isActivating);

            if (needActivate) {
                level.scheduleTick(new BlockPos(pos), this, 1);
            }
            this.cooldownTime = this.getMaxCooldownTime();
        } else {
            this.cooldownTime--;
            level.scheduleTick(new BlockPos(pos), this, 1);
        }
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() != this) return 0;

        int power = state.getValue(POWER);
        if (isInCooldown()) return power;

        return power + 1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        this.cooldownTimeProperty = IntegerProperty.create("cooldown_time", 0, getMaxCooldownTime());
        builder.add(this.cooldownTimeProperty);
    }
}
