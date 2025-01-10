package dev.dubhe.anvilcraft.integration.jade.provider;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.entity.SpaceOvercompressorBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum SpaceOvercompressorProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag serverData = blockAccessor.getServerData();
        if (serverData.contains("storedMass")) {
            long mass = serverData.getLong("storedMass");
            iTooltip.add(Component.translatable("tooltip.anvilcraft.space_overcompressor.stored_mass", mass));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof SpaceOvercompressorBlockEntity blockEntity) {
            compoundTag.putLong("storedMass", blockEntity.getStoredMass());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return AnvilCraft.of("space_overcompressor");
    }
}
