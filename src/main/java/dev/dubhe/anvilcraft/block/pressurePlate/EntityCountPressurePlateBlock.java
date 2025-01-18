package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityCountPressurePlateBlock extends PowerLevelPressurePlateBlock {
    private final Set<Class<? extends Entity>> entityClasses = Sets.newHashSet();

    @SafeVarargs
    public EntityCountPressurePlateBlock(Properties properties, Class<? extends Entity>... entityClasses) {
        super(BlockSetType.IRON, properties);
        Collections.addAll(this.entityClasses, entityClasses);
    }

    @Override
    protected Set<Class<? extends Entity>> getEntityClasses() {
        return this.entityClasses;
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
