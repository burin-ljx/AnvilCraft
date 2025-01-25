package dev.dubhe.anvilcraft.init;

import com.mojang.serialization.Codec;
import dev.dubhe.anvilcraft.util.AmuletTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static dev.dubhe.anvilcraft.AnvilCraft.MOD_ID;

public class ModDataAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<Integer>> AMULET_COUNT = ATTACHMENT_TYPES.register(
            "amulet_count", () -> AttachmentType.builder(() -> 0).build());

    public static final Supplier<AttachmentType<Integer>> AMULET_MAX = ATTACHMENT_TYPES.register(
            "amulet_max", () -> AttachmentType.builder(() -> 1).build());

    public static final Supplier<AttachmentType<Float>> DISCOUNT_RATE = ATTACHMENT_TYPES.register(
            "discount_rate", () -> AttachmentType.builder(() -> 0f).build());

    public static final Supplier<AttachmentType<Boolean>> IMMUNE_TO_LIGHTNING = ATTACHMENT_TYPES.register(
        "immune_to_lightning", () -> AttachmentType.builder(() -> false).build());

    public static final Supplier<AttachmentType<Boolean>> ZOMBIFICATED_BY_CURSE = ATTACHMENT_TYPES.register(
        "zombificated_by_curse", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static void register(IEventBus eventBus) {
        AmuletTypes.initDataAttachments(ATTACHMENT_TYPES);
        ATTACHMENT_TYPES.register(eventBus);
    }
}
