package dev.dubhe.anvilcraft.block.pressurePlate;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.anvilcraft.util.MathUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemDurabilityPressurePlateBlock extends PowerLevelPressurePlateBlock {
    private final boolean useMin;

    public ItemDurabilityPressurePlateBlock(Properties properties, boolean useMin) {
        super(BlockSetType.IRON, properties);
        this.useMin = useMin;
    }

    @Override
    protected Set<Class<? extends Entity>> getEntityClasses() {
        return ImmutableSet.of(ItemEntity.class);
    }

    @Override
    protected int getSignalStrength(Level level, AABB box, Set<Class<? extends Entity>> entityClasses) {
        Pair<Float, Float> minAndMax = getItemDurabilityPercentMinAndMax(level, box);
        float value = this.useMin ? minAndMax.getFirst() : minAndMax.getSecond();
        return (int) (value * 15);
    }

    protected static Pair<Float, Float> getItemDurabilityPercentMinAndMax(Level level, AABB box) {
        float min = 0F;
        float max = 0F;
        for (ItemEntity item : level.getEntitiesOfClass(
                ItemEntity.class, box,
                EntitySelector.NO_SPECTATORS.and(entity -> !entity.isIgnoringBlockTriggers())
        )) {
            ItemStack stack = item.getItem();
            float durabilityPercent = MathUtil.safeDivide(stack.getDamageValue(), stack.getMaxDamage());

            min = Math.min(min, durabilityPercent);
            max = Math.max(max, durabilityPercent);
        }

        return new Pair<>(Math.max(min, 0), Math.min(max, 1));
    }
}
