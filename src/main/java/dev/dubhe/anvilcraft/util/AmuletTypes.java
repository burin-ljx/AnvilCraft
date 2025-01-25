package dev.dubhe.anvilcraft.util;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModDamageTypes;
import dev.dubhe.anvilcraft.init.ModDataAttachments;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum AmuletTypes {
    EMERALD(
        "emerald", (sources, source) ->
        source.getEntity().getType().equals(EntityType.IRON_GOLEM)
        || source.getEntity().getType().equals(EntityType.PILLAGER)
    ),
    TOPAZ(
        "topaz",
        (sources, source) ->
            source.type().equals(sources.damageTypes.get(DamageTypes.LIGHTNING_BOLT))
    ),
    RUBY(
        "ruby", (sources, source) ->
        source.type().equals(sources.damageTypes.get(DamageTypes.ON_FIRE))
        || source.type().equals(sources.damageTypes.get(DamageTypes.CAMPFIRE))
        || source.type().equals(sources.damageTypes.get(DamageTypes.LAVA))
        || source.type().equals(sources.damageTypes.get(DamageTypes.HOT_FLOOR))
        || source.type().equals(sources.damageTypes.get(ModDamageTypes.LASER))
    ),
    SAPPHIRE(
        "sapphire", (sources, source) ->
        source.type().equals(sources.damageTypes.get(DamageTypes.DROWN))
        || source.type().equals(sources.damageTypes.get(DamageTypes.DRY_OUT))
        || source.getEntity().getType().equals(EntityType.GUARDIAN)
        || source.getEntity().getType().equals(EntityType.ELDER_GUARDIAN)
    ),
    ;

    public static final ResourceLocation RAFFLE_WINNING_PROBABILITY_ID = AnvilCraft.of("raffle_winning_probability");

    @Getter
    private final String typeId;
    private final BiPredicate<DamageSources, DamageSource> predicate;
    @Getter
    private Supplier<AttachmentType<Integer>> dataAttachment;

    AmuletTypes(String typeId, BiPredicate<DamageSources, DamageSource> predicate) {
        this.typeId = typeId;
        this.predicate = predicate;
    }

    public String getDataAttachmentId() {
        return this.typeId + "_amulet_raffle_probability";
    }

    public boolean isValid(DamageSources sources, DamageSource source) {
        return this.predicate.test(sources, source);
    }

    public static void initDataAttachments(DeferredRegister<AttachmentType<?>> register) {
        for (AmuletTypes type : AmuletTypes.values()) {
            type.dataAttachment = register.register(
                type.getDataAttachmentId(), () -> AttachmentType.builder(() -> 0).build());
        }
    }

    public static float getData(Player player, DamageSource source) {
        DamageSources sources = player.level().damageSources();
        for (AmuletTypes type : AmuletTypes.values()) {
            if (type.isValid(sources, source)) {
                return player.getData(type.getDataAttachment()) * 0.01F;
            }
        }

        return -1;
    }

    public static void startRaffle(ServerPlayer player, DamageSource source, boolean isConsumeAmuletBox) {
        RandomSource random = player.level().getRandom();
        int winningProbability = player.getData();
    }
}
