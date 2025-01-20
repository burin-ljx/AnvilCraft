package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

public class RecipeHandler {
    public static void init(RegistrateRecipeProvider provider) {
        BlockCrushRecipeLoader.init(provider);
        UnpackRecipeLoader.init(provider);
        BlockCompressRecipeLoader.init(provider);
        ItemCompressRecipeLoader.init(provider);
        MeshRecipeLoader.init(provider);
        StampingRecipeLoader.init(provider);
        SuperHeatingRecipeLoader.init(provider);
        TimeWarpRecipeLoader.init(provider);
        CookingRecipeLoader.init(provider);
        BulgingRecipeLoader.init(provider);
        ItemInjectRecipeLoader.init(provider);
        MassInjectRecipeLoader.init(provider);
        SqueezingRecipeLoader.init(provider);
        MultiBlockRecipeLoader.init(provider);
        MultiBlockConversionRecipeLoader.init(provider);
        MobTransformRecipeLoader.init(provider);
        ConcreteRecipeLoader.init(provider);
        MineralFountainRecipeLoader.init(provider);
        VanillaRecipesLoader.init(provider);
        JewelCraftingRecipeLoader.init(provider);
        SpecialCraftingRecipeLoader.init(provider);
        CrushingRecipeLoader.init(provider);
    }
}
