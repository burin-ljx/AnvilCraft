package dev.dubhe.anvilcraft.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.anvilcraft.block.AbstractMultiplePartBlock;
import dev.dubhe.anvilcraft.mixin.accessor.CropBlockAccessor;
import dev.dubhe.anvilcraft.mixin.accessor.GrowingPlantAccessor;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.BED_PART;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

/**
 * 方块状态注入
 */
public class BlockStateUtil {
    /**
     * 从 Json 读取
     *
     * @param stateJson json
     * @return 方块状态
     */
    public static @NotNull BlockState fromJson(@NotNull JsonElement stateJson) {
        if (!stateJson.isJsonObject()) throw new JsonSyntaxException("Expected item to be object");
        JsonObject object = stateJson.getAsJsonObject();
        if (!object.has("block")) throw new JsonSyntaxException("The field block is missing");
        JsonElement blockElement = object.get("block");
        if (!blockElement.isJsonPrimitive()) throw new JsonSyntaxException("Expected item to be string");
        StringBuilder block = new StringBuilder(blockElement.getAsString());
        if (object.has("state")) {
            block.append(GsonHelper.getAsString(object, "state"));
        }
        HolderLookup<Block> blocks = new BlockHolderLookup();
        BlockStateParser.BlockResult blockResult;
        try {
            blockResult = BlockStateParser.parseForBlock(blocks, block.toString(), true);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return blockResult.blockState();
    }

    /**
     * 序列化方块状态
     *
     * @param state 方块状态
     * @return 序列化JSON
     */
    public static @NotNull JsonElement toJson(@NotNull BlockState state) {
        JsonObject object = new JsonObject();
        object.addProperty(
                "block", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
        if (!state.getValues().isEmpty()) {
            String stringBuilder = '['
                    + state.getValues().entrySet().stream()
                            .map(PROPERTY_ENTRY_TO_STRING_FUNCTION)
                            .collect(Collectors.joining(","))
                    + ']';
            object.addProperty("state", stringBuilder);
        }
        return object;
    }

    /**
        硬编码一些通过Block#asItem方法获取不到的物品。。。。
        这些物品通常可以通过Block#getCloneItemStack方法获取到，但是需要LevelReader实例
        为了让获取物品在没有level上下文的情况下也能运作，此处硬编码部分特殊方块
     */
    public static final Map<Block, ItemStack> HARDCODED_SPECIAL_AS_ITEM = ImmutableMap.<Block, ItemStack>builder()
        .put(Blocks.ATTACHED_MELON_STEM, Items.MELON_SEEDS.getDefaultInstance())
        .put(Blocks.ATTACHED_PUMPKIN_STEM, Items.PUMPKIN_SEEDS.getDefaultInstance())
        .put(Blocks.BAMBOO_SAPLING, Items.BAMBOO.getDefaultInstance())
        .put(Blocks.BIG_DRIPLEAF_STEM, Items.BIG_DRIPLEAF.getDefaultInstance())
        .put(Blocks.TALL_GRASS, new ItemStack(Items.SHORT_GRASS, 2))
        .put(Blocks.LARGE_FERN, new ItemStack(Items.FERN, 2))
        .put(Blocks.PISTON_HEAD, ItemStack.EMPTY)
        .build();

    public static final Set<IntegerProperty> COUNT_PROPERTIES = ImmutableSet.of(
        BlockStateProperties.LAYERS,
        BlockStateProperties.PICKLES,
        BlockStateProperties.EGGS,
        BlockStateProperties.CANDLES,
        BlockStateProperties.FLOWER_AMOUNT
    );

    public static boolean isMultifaceLike(Block block) {
        return block instanceof MultifaceBlock || block instanceof VineBlock;
    }

    /**
     * 对某个方块状态，获取用于摆放它的物品列表。供多方快合成的JEI显示使用。<br/>
     * <b>不考虑</b>方块实体。<s>要是考虑的话那我真得累死</s><br/>
     * 硬编码了原版和本模组的各种各样的特殊情形。
     *
     * @param state 要摆放的方块状态
     * @return 用于摆放的物品列表
     */
    public static List<ItemStack> ingredientsForPlacement(BlockState state) {
        Block block = state.getBlock();
        ItemStack baseItem = switch (block) {
            case CropBlock crop -> ((CropBlockAccessor) crop).invoker$getBaseSeedId().asItem().getDefaultInstance();
            case FlowerPotBlock $ -> Items.FLOWER_POT.getDefaultInstance();
            case GrowingPlantBodyBlock plantHead -> ((GrowingPlantAccessor) plantHead).invoker$getHeadBlock()
                .asItem().getDefaultInstance();
            case CandleCakeBlock $ -> Items.CAKE.getDefaultInstance();
            default -> HARDCODED_SPECIAL_AS_ITEM.getOrDefault(block, block.asItem().getDefaultInstance());
        };
        if (state.hasProperty(DOUBLE_BLOCK_HALF) && state.getValue(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            baseItem = ItemStack.EMPTY;
        }
        if (state.hasProperty(BED_PART) && state.getValue(BED_PART) != BedPart.HEAD) {
            baseItem = ItemStack.EMPTY;
        }
        if (block instanceof AbstractMultiplePartBlock<?> multiplePartBlock &&
            !state.getValue(multiplePartBlock.getPart()).getOffset()
                .equals(multiplePartBlock.getMainPartOffset())) {
            baseItem = ItemStack.EMPTY;
        }
        if (!baseItem.isEmpty()) {
            ItemStack baseItemRef = baseItem;
            state.getProperties().stream()
                .filter(p -> p instanceof IntegerProperty)
                .map(p -> (IntegerProperty) p)
                .filter(COUNT_PROPERTIES::contains)
                .findFirst()
                .ifPresent(p -> baseItemRef.setCount(state.getValue(p)));

            if (isMultifaceLike(block)) {
                long faceCount = PipeBlock.PROPERTY_BY_DIRECTION.values().stream()
                    .filter(state::hasProperty)
                    .filter(state::getValue)
                    .count();
                baseItem.setCount((int) faceCount);
            }
        }
        ItemStack additionalItem = switch (block) {
            case CandleCakeBlock cake -> cake.candleBlock.asItem().getDefaultInstance();
            case FlowerPotBlock pot -> pot.getPotted().asItem().getDefaultInstance();
            default -> {
                FluidState fluidState = state.getFluidState();
                if (fluidState.isSource()) {
                    yield fluidState.getType().getBucket().getDefaultInstance();
                } else yield ItemStack.EMPTY;
            }
        };
        if (baseItem.isEmpty() && additionalItem.isEmpty()) return List.of();
        if (additionalItem.isEmpty()) return List.of(baseItem);
        if (baseItem.isEmpty()) return List.of(additionalItem);
        return List.of(baseItem, additionalItem);
    }

    public static class BlockHolderLookup implements HolderLookup<Block>, HolderOwner<Block> {
        @Override
        public @NotNull Stream<Holder.Reference<Block>> listElements() {
            return BuiltInRegistries.BLOCK.stream()
                    .map(BuiltInRegistries.BLOCK::getResourceKey)
                    .filter(Optional::isPresent)
                    .map(key -> BuiltInRegistries.BLOCK.getHolderOrThrow(key.get()));
        }

        @Override
        public @NotNull Stream<HolderSet.Named<Block>> listTags() {
            return BuiltInRegistries.BLOCK.getTags().map(Pair::getSecond);
        }

        @Override
        public @NotNull Optional<Holder.Reference<Block>> get(@NotNull ResourceKey<Block> resourceKey) {
            return Optional.of(BuiltInRegistries.BLOCK.getHolderOrThrow(resourceKey));
        }

        @Override
        public @NotNull Optional<HolderSet.Named<Block>> get(@NotNull TagKey<Block> tagKey) {
            return BuiltInRegistries.BLOCK.getTag(tagKey);
        }
    }
}
