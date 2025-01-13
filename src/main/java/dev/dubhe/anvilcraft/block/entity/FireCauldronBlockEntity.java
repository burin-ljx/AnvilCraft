package dev.dubhe.anvilcraft.block.entity;

import dev.dubhe.anvilcraft.block.HeaterBlock;
import dev.dubhe.anvilcraft.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class FireCauldronBlockEntity extends BlockEntity {
    public FireCauldronBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity) {
        clearPlasmaJetsAbove(level, blockPos, 6);
        if (checkHeaterIfOverload(level, blockPos) && checkIsObstruct(level, blockPos)) {
            for (int i = 1; i < 5; ++i) {
                if (!level.getBlockState(blockPos.above(i).west()).is(Blocks.AIR) &&
                        !level.getBlockState(blockPos.above(i).east()).is(Blocks.AIR) &&
                        !level.getBlockState(blockPos.above(i).north()).is(Blocks.AIR) &&
                        !level.getBlockState(blockPos.above(i).south()).is(Blocks.AIR)
                ) {
                    if (i != 1) level.setBlock(blockPos.above(i), Blocks.AIR.defaultBlockState(), 3);
                    level.setBlock(blockPos.above(i + 1), ModBlocks.PLASMA_JETS_BLOCK.getDefaultState(), 3);

                    if (level.getBlockState(blockPos.above(i).west()).is(Blocks.NETHERITE_BLOCK)) {
                        level.setBlock(blockPos.above(i).west(), ModBlocks.GLOWING_NETHERITE.getDefaultState(), 3);
                    } else if (level.getBlockState(blockPos.above(i).west()).is(ModBlocks.TUNGSTEN_BLOCK)) {
                        level.setBlock(blockPos.above(i).west(), ModBlocks.GLOWING_TUNGSTEN.getDefaultState(), 3);
                    }

                    if (level.getBlockState(blockPos.above(i).east()).is(Blocks.NETHERITE_BLOCK)) {
                        level.setBlock(blockPos.above(i).east(), ModBlocks.GLOWING_NETHERITE.getDefaultState(), 3);
                    } else if (level.getBlockState(blockPos.above(i).east()).is(ModBlocks.TUNGSTEN_BLOCK)) {
                        level.setBlock(blockPos.above(i).east(), ModBlocks.GLOWING_TUNGSTEN.getDefaultState(), 3);
                    }

                    if (level.getBlockState(blockPos.above(i).south()).is(Blocks.NETHERITE_BLOCK)) {
                        level.setBlock(blockPos.above(i).south(), ModBlocks.GLOWING_NETHERITE.getDefaultState(), 3);
                    } else if (level.getBlockState(blockPos.above(i).south()).is(ModBlocks.TUNGSTEN_BLOCK)) {
                        level.setBlock(blockPos.above(i).south(), ModBlocks.GLOWING_TUNGSTEN.getDefaultState(), 3);
                    }

                    if (level.getBlockState(blockPos.above(i).north()).is(Blocks.NETHERITE_BLOCK)) {
                        level.setBlock(blockPos.above(i).north(), ModBlocks.GLOWING_NETHERITE.getDefaultState(), 3);
                    } else if (level.getBlockState(blockPos.above(i).north()).is(ModBlocks.TUNGSTEN_BLOCK)) {
                        level.setBlock(blockPos.above(i).north(), ModBlocks.GLOWING_TUNGSTEN.getDefaultState(), 3);
                    }
                } else if (i == 1) {
                    level.setBlock(blockPos.above(i), ModBlocks.PLASMA_JETS_BLOCK.getDefaultState(), 3);
                    break;
                } else break;
                applyEffectToEntity(level, blockPos, i);
            }
        }
    }

    private static void clearPlasmaJetsAbove(Level level, BlockPos blockPos, int height) {
        for (int i = 0; i < height; ++i) {
            if (level.getBlockState(blockPos.above(i + 1)).is(ModBlocks.PLASMA_JETS_BLOCK)) {
                level.setBlock(blockPos.above(i + 1), Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    private static boolean checkHeaterIfOverload(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos.below()).is(ModBlocks.HEATER) && !level.getBlockState(blockPos.below()).getValue(HeaterBlock.OVERLOAD);
    }

    private static boolean checkIsObstruct(Level level, BlockPos blockPos) {
        boolean flag = true;
        for (int i = 0; i < 4; i++) {
            if (!level.getBlockState(blockPos.above(i + 1)).is(Blocks.AIR) && !level.getBlockState(blockPos.above(i + 1)).is(ModBlocks.PLASMA_JETS_BLOCK)) flag = false;
        }
        for (int i = 4; i < 8; i++) {
            if (!level.getBlockState(blockPos.above(i + 1)).is(Blocks.AIR)) flag = false;
        }
        return flag;
    }

    private static void applyEffectToEntity(Level level, BlockPos blockPos, int distance) {
        BlockPos pos = blockPos.above(distance);
        for (Entity entity : level.getEntities(null, ModBlocks.PLASMA_JETS_BLOCK.get().getVisualShape(level.getBlockState(pos), level, pos, CollisionContext.empty()).bounds().move(pos))) {
            if (!entity.fireImmune()) {
                entity.igniteForSeconds(15.0f);
                if (entity.hurt(entity.damageSources().inFire(), 32.0f)) {
                    entity.playSound(SoundEvents.GENERIC_BURN, 0.4f, 2.0f + RandomSource.create().nextFloat() * 0.4f);
                }
            }
        }
    }
}
