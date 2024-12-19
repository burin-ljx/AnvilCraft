package dev.dubhe.anvilcraft.integration.embeddium;


import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;

public class ModEmbeddiumTerrainRenderPasses {
    public static final TerrainRenderPass LASER = new TerrainRenderPass(ModRenderTypes.LASER, true, true);
}
