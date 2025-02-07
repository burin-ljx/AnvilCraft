package dev.dubhe.anvilcraft.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;

import java.util.function.Supplier;

public class AnvilCraftKubeRecipe extends KubeRecipe {
    public <T> T computeIfAbsent(RecipeKey<T> key, Supplier<T> supplier) {
        if (getValue(key) == null) {
            setValue(key, supplier.get());
        }
        return getValue(key);
    }
}
