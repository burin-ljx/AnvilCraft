package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MonsterCountPressurePlateBlock extends PowerLevelPressurePlateBlock {
    public MonsterCountPressurePlateBlock(
            BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Override
    protected Set<Class<? extends Entity>> getEntityClasses() {
        return ImmutableSet.of(LivingEntity.class);
    }

    @Override
    protected int getSignalStrength(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        int result = 0;

        for (Class<? extends Entity> entityClass : entityClasses) {
            result += getEntityCount(level, box, entityClass);
        }

        return Math.clamp(result, 0, 15);
    }
}
