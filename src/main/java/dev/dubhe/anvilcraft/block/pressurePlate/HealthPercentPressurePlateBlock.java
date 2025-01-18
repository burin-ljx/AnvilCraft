package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HealthPercentPressurePlateBlock extends PowerLevelPressurePlateBlock {
    private final boolean useMin;

    public HealthPercentPressurePlateBlock(Properties properties, boolean useMin) {
        super(BlockSetType.IRON, properties);
        this.useMin = useMin;
    }

    @Override
    protected Set<Class<? extends Entity>> getEntityClasses() {
        return ImmutableSet.of(LivingEntity.class);
    }

    @Override
    protected int getSignalStrength(
            Level level, AABB box, Set<Class<? extends Entity>> entityClasses
    ) {
        Pair<Float, Float> minAndMax = getEntitiesHealthPercentMinAndMax(level, box, entityClasses);
        float value = this.useMin ? minAndMax.getFirst() : minAndMax.getSecond();
        return (int) (value * 15);
    }

    protected static Pair<Float, Float> getEntitiesHealthPercentMinAndMax(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        Set<Entity> entities = Sets.newHashSet();
        for (Class<? extends Entity> entityClass : entityClasses) {
            entities.addAll(level.getEntitiesOfClass(
                    entityClass, box,
                    EntitySelector.NO_SPECTATORS.and(entity -> !entity.isIgnoringBlockTriggers())
            ));
        }

        float min = 0F;
        float max = 0F;
        for (Entity entity : entities) {
            float healthPercent = 0F;

            if (entity instanceof LivingEntity living) {
                healthPercent = living.getHealth() / living.getMaxHealth();
            } else if (entity instanceof EnderDragonPart part) {
                healthPercent = part.getParent().getHealth() / part.getParent().getHealth();
            }

            min = Math.min(min, healthPercent);
            max = Math.max(max, healthPercent);
        }

        return new Pair<>(Math.max(min, 0), Math.min(max, 1));
    }
}
