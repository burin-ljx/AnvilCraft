package dev.dubhe.anvilcraft.integration.kubejs.recipe;

import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.util.CodecUtil;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AnvilCraftRecipeComponents {
    public static final RecipeComponent<Either<TagKey<Block>, Block>> EITHER_BLOCK = new RecipeComponent<>() {
        @Override
        public Codec<Either<TagKey<Block>, Block>> codec() {
            return Codec.xor(TagKey.hashedCodec(Registries.BLOCK), CodecUtil.BLOCK_CODEC);
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(Block.class);
        }

        @Override
        public Either<TagKey<Block>, Block> wrap(Context cx, KubeRecipe recipe, Object from) {
            if (from instanceof Block b) {
                return Either.right(b);
            }
            if (from instanceof TagKey<?> k) {
                return Either.left(TagKey.create(Registries.BLOCK, k.location()));
            }

            String s = from instanceof JsonPrimitive json ? json.getAsString() : String.valueOf(from);

            if (s.startsWith("#")) {
                // is tag
                return Either.left(TagKey.create(Registries.BLOCK, ResourceLocation.parse(s.substring(1))));
            }

            return Either.right(BuiltInRegistries.BLOCK.get(ResourceLocation.parse(s)));
        }

        @Override
        public String toString() {
            return "either_block";
        }
    };

    public static final RecipeComponent<ChanceItemStack> CHANCE_ITEM_STACK = new RecipeComponent<>() {

        @Override
        public Codec<ChanceItemStack> codec() {
            return ChanceItemStack.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(ChanceItemStack.class);
        }

        @Override
        public String toString() {
            return "chance_item_stack";
        }
    };

    public static final RecipeComponent<ResourceLocation> RESOURCE_LOCATION = new RecipeComponent<>() {

        @Override
        public Codec<ResourceLocation> codec() {
            return ResourceLocation.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(ResourceLocation.class);
        }

        @Override
        public String toString() {
            return "resource_location";
        }
    };
}
