package dev.dubhe.anvilcraft.integration.kubejs.recipe;

import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPattern;
import dev.dubhe.anvilcraft.recipe.transform.NumericTagValuePredicate;
import dev.dubhe.anvilcraft.recipe.transform.TagModification;
import dev.dubhe.anvilcraft.recipe.transform.TransformOptions;
import dev.dubhe.anvilcraft.recipe.transform.TransformResult;
import dev.dubhe.anvilcraft.util.CodecUtil;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

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

    public static final RecipeComponent<NumberProvider> NUMBER_PROVIDER = new RecipeComponent<>() {

        @Override
        public Codec<NumberProvider> codec() {
            return NumberProviders.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(NumberProvider.class);
        }

        @Override
        public String toString() {
            return "number_provider";
        }
    };

    public static final RecipeComponent<BlockPattern> BLOCK_PATTERN = new RecipeComponent<>() {

        @Override
        public Codec<BlockPattern> codec() {
            return BlockPattern.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(BlockPattern.class);
        }

        @Override
        public String toString() {
            return "block_pattern";
        }
    };

    public static final RecipeComponent<EntityType<?>> ENTITY_TYPE = new RecipeComponent<>() {

        @Override
        public Codec<EntityType<?>> codec() {
            return CodecUtil.ENTITY_CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(EntityType.class);
        }

        @Override
        public String toString() {
            return "entity_type";
        }
    };

    public static final RecipeComponent<TransformResult> TRANSFORM_RESULT = new RecipeComponent<>() {

        @Override
        public Codec<TransformResult> codec() {
            return TransformResult.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(TransformResult.class);
        }

        @Override
        public String toString() {
            return "transform_result";
        }
    };

    public static final RecipeComponent<NumericTagValuePredicate> NUMERIC_TAG_VALUE_PREDICATE = new RecipeComponent<>() {

        @Override
        public Codec<NumericTagValuePredicate> codec() {
            return NumericTagValuePredicate.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(NumericTagValuePredicate.class);
        }

        @Override
        public String toString() {
            return "numeric_tag_value_predicate";
        }
    };

    public static final RecipeComponent<TagModification> TAG_MODIFICATION = new RecipeComponent<>() {

        @Override
        public Codec<TagModification> codec() {
            return TagModification.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(TagModification.class);
        }

        @Override
        public String toString() {
            return "tag_modification";
        }
    };

    public static final RecipeComponent<TransformOptions> TRANSFORM_OPTIONS = new RecipeComponent<>() {
        @Override
        public Codec<TransformOptions> codec() {
            return TransformOptions.CODEC;
        }

        @Override
        public TypeInfo typeInfo() {
            return TypeInfo.of(TransformOptions.class);
        }

        @Override
        public String toString() {
            return "transform_options";
        }
    };
}
