package dev.dubhe.anvilcraft.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DamageSourceUtil {
    @SafeVarargs
    public static boolean isMatchTypes(DamageSource source, DamageSources sources, ResourceKey<DamageType>... types) {
        List<DamageType> types1 = ImmutableList.copyOf(new Iterator<>() {
            final Iterator<ResourceKey<DamageType>> typeKeys = Lists.newArrayList(types).iterator();

            @Override
            public boolean hasNext() {
                return this.typeKeys.hasNext();
            }

            @Override
            public DamageType next() {
                return sources.damageTypes.get(this.typeKeys.next());
            }
        });

        return Optional.of(source.type())
            .filter(types1::contains)
            .isPresent();
    }

    @SafeVarargs
    public static boolean isEntityMatchTypes(DamageSource source, EntityType<? extends Entity>... types) {
        return Optional.ofNullable(source.getEntity())
            .filter(entity -> Lists.newArrayList(types).contains(entity.getType()))
            .isPresent();
    }
}
