package dev.dubhe.anvilcraft.integration;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

public enum CompatMods {
    CURIOS("curios"),
    EMBEDDIUM("embeddium"),
    IRIS("iris"),
    JADE("jade"),
    JEI("jei"),
    KUBEJS("kubejs"),
    MEKANISM("mekanism"),
    PATCHOULI("patchouli"),
    SODIUM("sodium"),
    THE_ONE_PROBE("theoneprobe"),
    WTHIT("wthit");
    
    public final String modId;

    CompatMods(String modId) {
        this.modId = modId;
    }
    
    public boolean isLoaded(){
        return ModList.get().isLoaded(this.modId);
    }
    
    public ResourceLocation of(String path){
        return ResourceLocation.fromNamespaceAndPath(this.modId, path);
    }
}
