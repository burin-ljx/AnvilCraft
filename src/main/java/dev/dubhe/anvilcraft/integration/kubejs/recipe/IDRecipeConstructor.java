package dev.dubhe.anvilcraft.integration.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.recipe.component.ComponentValueMap;
import dev.latvian.mods.kubejs.recipe.schema.RecipeConstructor;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaType;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.Context;
import net.minecraft.resources.ResourceLocation;

public class IDRecipeConstructor extends RecipeConstructor {
    private static final RecipeKey<ResourceLocation> ID = AnvilCraftRecipeComponents.RESOURCE_LOCATION.otherKey("id");

    public IDRecipeConstructor() {
        super(ID);
    }

    @Override
    public KubeRecipe create(Context cx, SourceLine sourceLine, RecipeTypeFunction type, RecipeSchemaType schemaType, ComponentValueMap from) {
        var r = super.create(cx, sourceLine, type, schemaType, from);
        r.id(KubeResourceLocation.wrap(from.getValue(cx, r, ID)));
        return r;
    }

    @Override
    public void setValues(Context cx, KubeRecipe recipe, RecipeSchemaType schemaType, ComponentValueMap from) {
        for (var entry : overrides.entrySet()) {
            recipe.setValue(entry.getKey(), Cast.to(entry.getValue().getDefaultValue(schemaType)));
        }

        for (var entry : schemaType.schema.keyOverrides.entrySet()) {
            recipe.setValue(entry.getKey(), Cast.to(entry.getValue().getDefaultValue(schemaType)));
        }
    }
}
