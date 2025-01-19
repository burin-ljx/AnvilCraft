package dev.dubhe.anvilcraft.recipe.anvil.builder;

import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.anvil.AbstractItemProcessRecipe;

import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@Setter
@Accessors(fluent = true, chain = true)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class AbstractItemProcessBuilder<T extends AbstractItemProcessRecipe> extends AbstractRecipeBuilder<T> {
    protected NonNullList<Ingredient> ingredients = NonNullList.create();
    protected List<ChanceItemStack> results = new ArrayList<>();
    @Getter
    protected boolean generated = false;

    public AbstractItemProcessBuilder<T> requires(Ingredient ingredient, int count) {
        for (int i = 0; i < count; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public AbstractItemProcessBuilder<T> requires(Ingredient ingredient) {
        return requires(ingredient, 1);
    }

    public AbstractItemProcessBuilder<T> requires(ItemLike pItem, int count) {
        return requires(Ingredient.of(pItem), count);
    }

    public AbstractItemProcessBuilder<T> requires(ItemLike pItem) {
        return requires(pItem, 1);
    }

    public AbstractItemProcessBuilder<T> requires(TagKey<Item> pTag, int count) {
        return requires(Ingredient.of(pTag), count);
    }

    public AbstractItemProcessBuilder<T> requires(TagKey<Item> pTag) {
        return requires(pTag, 1);
    }

    public AbstractItemProcessBuilder<T> result(ChanceItemStack stack) {
        results.add(stack);
        return this;
    }

    public AbstractItemProcessBuilder<T> result(ItemStack stack) {
        results.add(ChanceItemStack.of(stack));
        return this;
    }

    public AbstractItemProcessBuilder<T> result(ItemLike item) {
        return this.result(item.asItem().getDefaultInstance());
    }

    public AbstractItemProcessBuilder<T> result(ItemLike item, int count) {
        ItemStack stack = item.asItem().getDefaultInstance();
        stack.setCount(count);
        return this.result(stack);
    }

    public AbstractItemProcessBuilder<T> result(ItemLike item, int count, float chance) {
        ItemStack stack = item.asItem().getDefaultInstance();
        stack.setCount(count);
        return this.result(ChanceItemStack.of(stack).withChance(chance));
    }

    @Override
    public void validate(ResourceLocation pId) {
        if (ingredients.isEmpty() || ingredients.size() > 9) {
            throw new IllegalArgumentException("Recipe ingredients size must in 0-9, RecipeId: " + pId);
        }
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Recipe results must not be null, RecipeId: " + pId);
        }
    }

    @Override
    public Item getResult() {
        return results.getFirst().getStack().getItem();
    }
}
