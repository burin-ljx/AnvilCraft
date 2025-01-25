package dev.dubhe.anvilcraft.util;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.dubhe.anvilcraft.init.ModDamageTypes;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.amulet.AbstractAmuletItem;
import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public enum AmuletTypes {
    EMERALD(
        "emerald", (sources, source) ->
        source.getEntity().getType().equals(EntityType.IRON_GOLEM)
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
        || source.getEntity().getType().equals(EntityType.GUARDIAN)
        || source.getEntity().getType().equals(EntityType.ELDER_GUARDIAN),
        ModItems.SAPPHIRE_AMULET
    ),
    //ANVIL(
    //    "anvil", (sources, source) ->
    //    source.type().equals(sources.damageTypes.get(DamageTypes.FALLING_ANVIL)),
    //    ModItems.EMERALD_AMULET
    //),
    ;

    @Getter
    private final String typeId;
    private final BiPredicate<DamageSources, DamageSource> predicate;
    @Getter
    private final ItemEntry<? extends AbstractAmuletItem> entry;
    @Getter
    private Supplier<AttachmentType<Integer>> dataAttachment;

    AmuletTypes(String typeId, BiPredicate<DamageSources, DamageSource> predicate, ItemEntry<? extends AbstractAmuletItem> entry) {
        this.typeId = typeId;
        this.predicate = predicate;
        this.entry = entry;
    }

    public String getDataAttachmentId() {
        return this.typeId + "_amulet_raffle_probability";
    }

    public boolean isValid(DamageSources sources, DamageSource source) {
        try {
            return this.predicate.test(sources, source);
        } catch (Throwable ignored) {}

        return false;
    }

    public static void initDataAttachments(DeferredRegister<AttachmentType<?>> register) {
        for (AmuletTypes type : AmuletTypes.values()) {
            type.dataAttachment = register.register(
                type.getDataAttachmentId(), () -> AttachmentType.builder(() -> 0).build());
        }
    }

    public static AmuletTypes getType(Player player, DamageSource source) {
        DamageSources sources = player.level().damageSources();
        for (AmuletTypes type : AmuletTypes.values()) {
            if (type.isValid(sources, source)) {
                return type;
            }
        }

        return null;
    }

    public static int getData(Player player, DamageSource source) {
        DamageSources sources = player.level().damageSources();
        for (AmuletTypes type : AmuletTypes.values()) {
            if (type.isValid(sources, source)) {
                return getData(player, type);
            }
        }

        return 0;
    }
    public static int getData(Player player, AmuletTypes type) {
        return player.getData(type.getDataAttachment());
    }
    public static void setData(Player player, DamageSource source, NonNullUnaryOperator<Integer> modifier) {
        DamageSources sources = player.level().damageSources();
        for (AmuletTypes type : AmuletTypes.values()) {
            if (type.isValid(sources, source)) {
                Supplier<AttachmentType<Integer>> dataAttachment = type.getDataAttachment();
                player.setData(dataAttachment, modifier.apply(player.getData(dataAttachment)));
            }
        }
    }
    public static void setData(Player player, AmuletTypes type, NonNullUnaryOperator<Integer> modifier) {
        player.setData(type.dataAttachment, modifier.apply(player.getData(type.dataAttachment)));
    }

    public static void startRaffle(ServerPlayer player, DamageSource source, boolean isConsumeAmuletBox) {
        RandomSource random = player.level().getRandom();
        int raffleProbability = Math.min(getData(player, source) + (isConsumeAmuletBox ? 20 : 5), 50);

        if (raffleProbability > random.nextIntBetweenInclusive(0, 100)) {
            setData(player, source, value -> 0);

            AmuletTypes type = getType(player, source);
            if (type != null) {
                player.getInventory().add(type.getEntry().asStack());
            }
        } else {
            setData(player, source, value -> Math.min(value + 5, 50));
        }
    }
}
