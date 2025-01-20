package dev.dubhe.anvilcraft.recipe.anvil;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.anvil.builder.AbstractItemProcessBuilder;
import dev.dubhe.anvilcraft.util.CodecUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrushingRecipe extends AbstractItemProcessRecipe {
    public CrushingRecipe(NonNullList<Ingredient> ingredients, List<ChanceItemStack> results) {
        super(ingredients, results);
    }

    @Contract(" -> new")
    public static @NotNull StampingRecipe.Builder builder() {
        return new StampingRecipe.Builder();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CRUSHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CRUSHING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CrushingRecipe> {
        private static final MapCodec<CrushingRecipe> CODEC = RecordCodecBuilder.mapCodec((ins) -> ins.group(
                CodecUtil.createIngredientListCodec("ingredients", 9, "crushing")
                        .forGetter(CrushingRecipe::getIngredients),
                ChanceItemStack.CODEC.listOf().fieldOf("results")
                        .forGetter(CrushingRecipe::getResults)
        ).apply(ins, CrushingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::encode, Serializer::decode);

        @Override
        public MapCodec<CrushingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CrushingRecipe decode(RegistryFriendlyByteBuf buf) {
            List<ChanceItemStack> results = new ArrayList<>();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++) {
                results.add(ChanceItemStack.STREAM_CODEC.decode(buf));
            }
            size = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(i -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            return new CrushingRecipe(ingredients, results);
        }

        private static void encode(RegistryFriendlyByteBuf buf, CrushingRecipe recipe) {
            buf.writeVarInt(recipe.results.size());
            for (ChanceItemStack stack : recipe.results) {
                ChanceItemStack.STREAM_CODEC.encode(buf, stack);
            }
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
            }
        }
    }

    public static class Builder extends AbstractItemProcessBuilder<CrushingRecipe> {
        @Override
        public CrushingRecipe buildRecipe() {
            return new CrushingRecipe(ingredients, results);
        }

        @Override
        public String getType() {
            return "crushing";
        }
    }
}
