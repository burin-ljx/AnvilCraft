package dev.dubhe.anvilcraft.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public class DamageSourceUtil {
    @SafeVarargs
    public static boolean isMatchTypes(DamageSource source, DamageSources sources, ResourceKey<DamageType>... types) {
        Optional<DamageType> type = Optional.of(source.type());

        for (ResourceKey<DamageType> typeKey : types) {
            type = type.filter(type1 -> type1.equals(sources.damageTypes.get(typeKey)));
        }

        return type.isPresent();
    }

    @SafeVarargs
    public static boolean isEntityMatchTypes(DamageSource source, EntityType<? extends Entity>... types) {
        Optional<Entity> entity = Optional.ofNullable(source.getEntity());

        for (EntityType<? extends Entity> type : types) {
            entity = entity.filter(entity1 -> entity1.getType().equals(type));
        }

        return entity.isPresent();
    }
}
