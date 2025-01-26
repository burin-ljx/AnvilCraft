package dev.dubhe.anvilcraft.util;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.dubhe.anvilcraft.init.ModDamageTypes;
import dev.dubhe.anvilcraft.init.ModDataAttachments;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.amulet.AbstractAmuletItem;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;

import java.util.Collections;
import java.util.Objects;
import java.util.function.BiPredicate;

public class AmuletUtil {
    public enum Types {
        EMERALD(
            "emerald", (sources, source) ->
            Objects.requireNonNull(source.getEntity()).getType().equals(EntityType.IRON_GOLEM)
            || source.getEntity().getType().equals(EntityType.PILLAGER),
            ModItems.EMERALD_AMULET
        ),
        TOPAZ(
            "topaz", (sources, source) ->
            source.type().equals(sources.damageTypes.get(DamageTypes.LIGHTNING_BOLT)),
            ModItems.TOPAZ_AMULET
        ),
        RUBY(
            "ruby", (sources, source) ->
            source.type().equals(sources.damageTypes.get(DamageTypes.ON_FIRE))
            || source.type().equals(sources.damageTypes.get(DamageTypes.CAMPFIRE))
            || source.type().equals(sources.damageTypes.get(DamageTypes.LAVA))
            || source.type().equals(sources.damageTypes.get(DamageTypes.HOT_FLOOR))
            || source.type().equals(sources.damageTypes.get(ModDamageTypes.LASER)),
            ModItems.RUBY_AMULET
        ),
        SAPPHIRE(
            "sapphire", (sources, source) ->
            source.type().equals(sources.damageTypes.get(DamageTypes.DROWN))
            || source.type().equals(sources.damageTypes.get(DamageTypes.DRY_OUT))
            || Objects.requireNonNull(source.getEntity()).getType().equals(EntityType.GUARDIAN)
            || source.getEntity().getType().equals(EntityType.ELDER_GUARDIAN),
            ModItems.SAPPHIRE_AMULET
        ),
        //ANVIL(
        //    "anvil", (sources, source) ->
        //    source.type().equals(sources.damageTypes.get(DamageTypes.FALLING_ANVIL)),
        //    ModItems.EMERALD_AMULET
        //),
        COMRADE(
            "comrade", (sources, source) -> {
                if (source.getEntity() instanceof Player murder && source.getDirectEntity() instanceof Player victim) {
                    PlayerTeam team = victim.getTeam();
                    if (team != null) {
                        return team.getPlayers().contains(murder.getScoreboardName());
                    } else {
                        return true;
                    }
                }

                return false;
            },
            ModItems.COMRADE_AMULET
        );

        @Getter
        private final String typeId;
        private final BiPredicate<DamageSources, DamageSource> predicate;
        @Getter
        private final ItemEntry<? extends AbstractAmuletItem> entry;

        Types(String typeId, BiPredicate<DamageSources, DamageSource> predicate, ItemEntry<? extends AbstractAmuletItem> entry) {
            this.typeId = typeId;
            this.predicate = predicate;
            this.entry = entry;
        }

        public boolean isValid(DamageSources sources, DamageSource source) {
            try {
                return this.predicate.test(sources, source);
            } catch (Throwable ignored) {
            }

            return false;
        }
    }

    public static Types getType(Player player, DamageSource source) {
        DamageSources sources = player.damageSources();
        for (Types type : Types.values()) {
            if (type.isValid(sources, source)) {
                return type;
            }
        }

        return null;
    }

    public static int getRaffleProbability(Player player, DamageSource source, boolean isConsumeAmuletBox) {
        DamageSources sources = player.damageSources();
        for (Types type : Types.values()) {
            if (type.isValid(sources, source)) {
                return getRaffleProbability(player, type, isConsumeAmuletBox);
            }
        }

        return 0;
    }

    public static int getStoredRaffleProbability(Player player, Types type) {
        return player.getData(ModDataAttachments.AMULET_RAFFLE_PROBABILITY).getInt(type.getTypeId());
    }
    public static int getRaffleProbability(Player player, Types type, boolean isConsumeAmuletBox) {
        if (!hasAmuletInInventory(player, type)) {
            return getStoredRaffleProbability(player, type) + (isConsumeAmuletBox ? 20 : 5);
        } else {
            return 0;
        }
    }

    public static void setRaffleProbability(Player player, DamageSource source, NonNullUnaryOperator<Integer> modifier) {
        DamageSources sources = player.damageSources();
        for (Types type : Types.values()) {
            if (type.isValid(sources, source)) {
                setRaffleProbability(player, type, modifier);
            }
        }
    }
    public static void setRaffleProbability(Player player, Types type, NonNullUnaryOperator<Integer> modifier) {
        CompoundTag root = player.getData(ModDataAttachments.AMULET_RAFFLE_PROBABILITY);
        if (!hasAmuletInInventory(player, type)) {
            root.putInt(type.getTypeId(), modifier.apply(root.getInt(type.getTypeId())));
        } else {
            root.putInt(type.getTypeId(), 0);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasAmuletInInventory(Player player, Types type) {
        return player.getInventory().hasAnyOf(Collections.singleton(type.getEntry().asItem()));
    }

    public static void startRaffle(ServerPlayer player, DamageSource source, boolean isConsumeAmuletBox) {
        RandomSource random = player.getRandom();
        int raffleProbability = Math.min(getRaffleProbability(player, source, isConsumeAmuletBox), 50);

        if (raffleProbability > random.nextIntBetweenInclusive(0, 100)) {
            AmuletUtil.setRaffleProbability(player, source, value -> 0);

            Types type = getType(player, source);
            if (type != null) {
                player.getInventory().add(type.getEntry().asStack());
            }
        } else {
            AmuletUtil.setRaffleProbability(player, source, value -> Math.min(value + 5, 50));
        }
    }
}
