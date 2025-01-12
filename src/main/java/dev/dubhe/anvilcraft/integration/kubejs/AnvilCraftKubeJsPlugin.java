package dev.dubhe.anvilcraft.integration.kubejs;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil.BlockCompressRecipeSchema;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;

public class AnvilCraftKubeJsPlugin implements KubeJSPlugin {
    @Override
    public void registerClasses(ClassFilter filter) {
        filter.allow("dev.dubhe.anvilcraft");
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ChanceItemStack", ChanceItemStack.class);
    }

    @Override
    public void registerRecipeComponents(RecipeComponentFactoryRegistry registry) {
        registry.register(AnvilCraftRecipeComponents.EITHER_BLOCK);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(AnvilCraft.of("block_compress"), BlockCompressRecipeSchema.SCHEMA);
    }
}
