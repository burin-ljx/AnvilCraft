package dev.dubhe.anvilcraft.block;

import net.minecraft.world.level.block.BaseEntityBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseLaserBlock extends BaseEntityBlock {
    protected BaseLaserBlock(Properties properties) {
        super(properties);
    }
}
