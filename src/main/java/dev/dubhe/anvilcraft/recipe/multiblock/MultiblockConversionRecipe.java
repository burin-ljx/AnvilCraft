package dev.dubhe.anvilcraft.recipe.multiblock;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractRecipeBuilder;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@Getter
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MultiblockConversionRecipe implements Recipe<MultiblockInput> {
    private final BlockPattern inputPattern;
    private final BlockPattern outputPattern;
    private final Optional<ModifySpawnerAction> modifySpawnerAction;
    private Rotation matchedRotation = Rotation.NONE;

    public MultiblockConversionRecipe(BlockPattern inputPattern, BlockPattern outputPattern) {
        this(inputPattern, outputPattern, Optional.empty());
    }

    public MultiblockConversionRecipe(
        BlockPattern inputPattern,
        BlockPattern outputPattern,
        @Nullable ModifySpawnerAction modifySpawnerAction
    ) {
        this(inputPattern, outputPattern, Optional.ofNullable(modifySpawnerAction));
    }

    public MultiblockConversionRecipe(
        BlockPattern inputPattern,
        BlockPattern outputPattern,
        Optional<ModifySpawnerAction> modifySpawnerAction
    ) {
        this.inputPattern = inputPattern;
        this.outputPattern = outputPattern;
        this.modifySpawnerAction = modifySpawnerAction;
    }

    @Contract(" -> new")
    public static Builder builder() {
        return new Builder();
    }
    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.MULTIBLOCK_CONVERSION_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.MULTIBLOCK_CONVERSION_SERIALIZER.get();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @SuppressWarnings("deprecated")
    @Override
    public boolean matches(MultiblockInput input, Level level) {
        int size = input.size();
        if (inputPattern.getLayers().size() != size) {
            return false;
        }
        // 无旋转
        boolean flag = true;
        for (int x = 0; x < size && flag; x++) {
            for (int y = 0; y < size && flag; y++) {
                for (int z = 0; z < size && flag; z++) {
                    if (!inputPattern.getPredicate(x, y, z).test(input.getBlockState(x, y, z))) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            matchedRotation = Rotation.NONE;
            return true;
        }
        // 旋转90
        flag = true;
        for (int x = 0; x < size && flag; x++) {
            for (int y = 0; y < size && flag; y++) {
                for (int z = 0; z < size && flag; z++) {
                    if (!inputPattern.getPredicate(x, y, z).test(
                        input.getBlockState(z, y, size - 1 - x).rotate(Rotation.CLOCKWISE_90))) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            matchedRotation = Rotation.CLOCKWISE_90;
            return true;
        }
        // 旋转180
        flag = true;
        for (int x = 0; x < size && flag; x++) {
            for (int y = 0; y < size && flag; y++) {
                for (int z = 0; z < size && flag; z++) {
                    if (!inputPattern.getPredicate(x, y, z).test(
                        input.getBlockState(size - 1 - x, y, size - 1 - z).rotate(Rotation.CLOCKWISE_180))) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            matchedRotation = Rotation.CLOCKWISE_180;
            return true;
        }
        // 旋转270
        flag = true;
        for (int x = 0; x < size && flag; x++) {
            for (int y = 0; y < size && flag; y++) {
                for (int z = 0; z < size && flag; z++) {
                    if (!inputPattern.getPredicate(x, y, z).test(
                        input.getBlockState(size - 1 - z, y, x).rotate(Rotation.COUNTERCLOCKWISE_90))) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            matchedRotation = Rotation.COUNTERCLOCKWISE_90;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack assemble(MultiblockInput input, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public static class Builder extends AbstractRecipeBuilder<MultiblockConversionRecipe> {
        private final BlockPattern inputPattern = BlockPattern.create();
        private final BlockPattern outputPattern = BlockPattern.create();
        private ModifySpawnerAction postAction = null;

        public Builder() {}

        public Builder inputLayer(String... layers) {
            inputPattern.layer(layers);
            return this;
        }

        public Builder outputLayer(String... layers) {
            outputPattern.layer(layers);
            return this;
        }

        public Builder symbol(char symbol, BlockPredicateWithState predicate) {
            inputPattern.symbol(symbol, predicate);
            outputPattern.symbol(symbol, predicate);
            return this;
        }

        public Builder symbol(char symbol, Block block) {
            return this.symbol(symbol, BlockPredicateWithState.of(block));
        }

        public Builder symbol(char symbol, String blockName) {
            return this.symbol(symbol, BlockPredicateWithState.of(blockName));
        }

        public Builder symbolForInput(char symbol, BlockPredicateWithState predicate) {
            inputPattern.symbol(symbol, predicate);
            return this;
        }

        public Builder symbolForInput(char symbol, Block block) {
            return this.symbolForInput(symbol, BlockPredicateWithState.of(block));
        }

        public Builder symbolForInput(char symbol, String blockName) {
            return this.symbolForInput(symbol, BlockPredicateWithState.of(blockName));
        }

        public Builder symbolForOutput(char symbol, BlockPredicateWithState predicate) {
            outputPattern.symbol(symbol, predicate);
            return this;
        }

        public Builder symbolForOutput(char symbol, Block block) {
            return this.symbolForOutput(symbol, BlockPredicateWithState.of(block));
        }

        public Builder symbolForOutput(char symbol, String blockName) {
            return this.symbolForOutput(symbol, BlockPredicateWithState.of(blockName));
        }

        public Builder modifySpawnerAction(ModifySpawnerAction postAction) {
            this.postAction = postAction;
            return this;
        }

        @Override
        public MultiblockConversionRecipe buildRecipe() {
            return new MultiblockConversionRecipe(inputPattern, outputPattern, postAction);
        }

        @Override
        public void validate(ResourceLocation pId) {
            if (!inputPattern.checkSymbols()) {
                throw new IllegalArgumentException("Input pattern must contain all valid symbols: " + pId);
            }
            if (!outputPattern.checkSymbols()) {
                throw new IllegalArgumentException("Output pattern must contain all valid symbols: " + pId);
            }
        }

        @Override
        public String getType() {
            return "multiblock_conversion";
        }

        @Override
        public Item getResult() {
            return Items.AIR;
        }
    }

    public static class Serializer implements RecipeSerializer<MultiblockConversionRecipe> {

        private static final MapCodec<MultiblockConversionRecipe> CODEC =
            RecordCodecBuilder.mapCodec(ins -> ins.group(
                BlockPattern.CODEC.fieldOf("inputPattern").forGetter(MultiblockConversionRecipe::getInputPattern),
                BlockPattern.CODEC.fieldOf("outputPattern").forGetter(MultiblockConversionRecipe::getOutputPattern),
                ModifySpawnerAction.CODEC.codec().optionalFieldOf("modifySpawnerAction")
                    .forGetter(MultiblockConversionRecipe::getModifySpawnerAction)
            )
            .apply(ins, MultiblockConversionRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, MultiblockConversionRecipe> STREAM_CODEC =
            StreamCodec.composite(
                BlockPattern.STREAM_CODEC,
                MultiblockConversionRecipe::getInputPattern,
                BlockPattern.STREAM_CODEC,
                MultiblockConversionRecipe::getOutputPattern,
                ByteBufCodecs.optional(ModifySpawnerAction.STREAM_CODEC),
                MultiblockConversionRecipe::getModifySpawnerAction,
                MultiblockConversionRecipe::new
            );

        @Override
        public MapCodec<MultiblockConversionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MultiblockConversionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
