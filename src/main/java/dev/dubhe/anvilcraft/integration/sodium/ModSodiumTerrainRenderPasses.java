package dev.dubhe.anvilcraft.integration.sodium;


import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;

public class ModSodiumTerrainRenderPasses {
    public static final TerrainRenderPass LASER = new TerrainRenderPass(ModRenderTypes.LASER, true, true);
}
