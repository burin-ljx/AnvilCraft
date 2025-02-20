package dev.dubhe.anvilcraft.util;

import dev.dubhe.anvilcraft.block.CementCauldronBlock;
import dev.dubhe.anvilcraft.block.FireCauldronBlock;
import dev.dubhe.anvilcraft.block.HoneyCauldronBlock;
import dev.dubhe.anvilcraft.block.OilCauldronBlock;
import dev.dubhe.anvilcraft.block.state.Color;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModInteractionMap {
    public static final CauldronInteraction.InteractionMap LAYERED_LAVA = CauldronInteraction.newInteractionMap("layered_lava");
    public static final CauldronInteraction.InteractionMap OIL = CauldronInteraction.newInteractionMap("oil");
    public static final CauldronInteraction.InteractionMap CEMENT = CauldronInteraction.newInteractionMap("cement");
    public static final CauldronInteraction.InteractionMap HONEY = CauldronInteraction.newInteractionMap("honey");
    public static final CauldronInteraction.InteractionMap MELT_GEM = CauldronInteraction.newInteractionMap("melt_gem");

    public static void initInteractionMap() {
        var oilInteractionMap = OIL.map();
        oilInteractionMap.put(
            Items.BUCKET,
            (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
                state,
                level,
                pos,
                player,
                hand,
                stack,
                ModItems.OIL_BUCKET.asStack(),
                (s) -> s.getValue(OilCauldronBlock.LEVEL) == 3,
                SoundEvents.BUCKET_FILL
            )
        );
        oilInteractionMap.put(
            Items.FLINT_AND_STEEL,
            (state, level, pos, player, hand, stack) -> {
                level.setBlockAndUpdate(
                    pos,
                    ModBlocks.FIRE_CAULDRON.getDefaultState()
                        .setValue(FireCauldronBlock.LEVEL, state.getValue(OilCauldronBlock.LEVEL))
                );
                stack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(hand));
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS);
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        );
        oilInteractionMap.put(
            Items.FIRE_CHARGE,
            (state, level, pos, player, hand, stack) -> {
                level.setBlockAndUpdate(
                    pos,
                    ModBlocks.FIRE_CAULDRON.getDefaultState()
                        .setValue(FireCauldronBlock.LEVEL, state.getValue(OilCauldronBlock.LEVEL))
                );
                stack.shrink(1);
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        );

        var layeredLavaInteractionMap = LAYERED_LAVA.map();
        layeredLavaInteractionMap.put(
            Items.BUCKET,
            (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteraction.fillBucket(
                blockState,
                level,
                blockPos,
                player,
                interactionHand,
                itemStack,
                Items.LAVA_BUCKET.getDefaultInstance(),
                (state) -> state.getValue(LayeredCauldronBlock.LEVEL) == 3,
                SoundEvents.BUCKET_FILL
            )
        );

        var cementInteractionMap = ModInteractionMap.CEMENT.map();
        cementInteractionMap.put(
            Items.BUCKET,
            (state, level, pos, player, hand, stack) -> {
                if (level.getBlockState(pos).getBlock() instanceof CementCauldronBlock cauldronBlock) {
                    Color color = cauldronBlock.getColor();
                    return CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, ModItems.CEMENT_BUCKETS.get(color).asStack(), (s) -> true, SoundEvents.BUCKET_FILL);
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        );

        var honeyInteractionMap = HONEY.map();
        honeyInteractionMap.put(
            Items.GLASS_BOTTLE,
            (state, level, pos, player, hand, stack) -> {
                if (!level.isClientSide()) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, Items.HONEY_BOTTLE.getDefaultInstance()));
                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);
                    level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        );
        honeyInteractionMap.put(
            Items.HONEY_BOTTLE,
            (state, level, pos, player, hand, stack) -> {
                int honeyLevel = state.getValue(HoneyCauldronBlock.LEVEL);
                if (honeyLevel < 3) {
                    level.setBlockAndUpdate(pos, state.cycle(HoneyCauldronBlock.LEVEL));
                    stack.shrink(1);
                    ItemStack backBottle = new ItemStack(Items.GLASS_BOTTLE);
                    if (!player.getInventory().add(backBottle)) {
                        player.drop(backBottle, false);
                    }
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        );

        var meltGemInteractionMap = MELT_GEM.map();
        meltGemInteractionMap.put(
            Items.BUCKET,
            (state, level, pos, player, hand, stack) -> CauldronInteraction.fillBucket(
                state,
                level,
                pos,
                player,
                hand,
                stack,
                ModItems.MELT_GEM_BUCKET.asStack(),
                s -> true,
                SoundEvents.BUCKET_FILL
            )
        );

        var emptyInteractionMap = CauldronInteraction.EMPTY.map();
        ModItems.CEMENT_BUCKETS.forEach((k, v) -> emptyInteractionMap.put(
                v.get(),
                (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
                    level,
                    pos,
                    player,
                    hand,
                    stack,
                    ModBlocks.CEMENT_CAULDRONS.get(k).getDefaultState(),
                    SoundEvents.BUCKET_EMPTY
                )
            )
        );
        emptyInteractionMap.put(
            ModItems.OIL_BUCKET.get(),
            (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
                level,
                pos,
                player,
                hand,
                stack,
                ModBlocks.OIL_CAULDRON.getDefaultState().setValue(OilCauldronBlock.LEVEL, 3),
                SoundEvents.BUCKET_EMPTY
            )
        );
        emptyInteractionMap.put(
            Items.HONEY_BOTTLE,
            (state, level, pos, player, hand, stack) -> {
                level.setBlockAndUpdate(pos, ModBlocks.HONEY_CAULDRON.getDefaultState());
                stack.shrink(1);
                ItemStack backBottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(backBottle)) {
                    player.drop(backBottle, false);
                }
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        );
        emptyInteractionMap.put(
            ModItems.MELT_GEM_BUCKET.get(),
            (state, level, pos, player, hand, stack) -> CauldronInteraction.emptyBucket(
                level,
                pos,
                player,
                hand,
                stack,
                ModBlocks.MELT_GEM_CAULDRON.getDefaultState(),
                SoundEvents.BUCKET_EMPTY
            )
        );
    }
}
