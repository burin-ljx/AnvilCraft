package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@ParametersAreNonnullByDefault
public class EntityTypePressurePlateBlock extends PowerLevelPressurePlateBlock {
    public EntityTypePressurePlateBlock(
            BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        return Math.clamp(getEntitiesType(
                level, TOUCH_AABB.move(pos),
                ImmutableSet.of(LivingEntity.class)
        ), 0, 15);
    }

    protected static int getEntitiesType(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        Set<Entity> entities = Sets.newHashSet();
        for (Class<? extends Entity> entityClass : entityClasses) {
            entities.addAll(level.getEntitiesOfClass(
                    entityClass, box,
                    EntitySelector.NO_SPECTATORS.and(entity -> !entity.isIgnoringBlockTriggers())
            ));
        }

        Set<Class<? extends Entity>> entityClassez = Sets.newHashSet();
        for (Entity entity : entities) {
            entityClassez.add(entity.getClass());
        }

        return entityClassez.size();
    }
}
