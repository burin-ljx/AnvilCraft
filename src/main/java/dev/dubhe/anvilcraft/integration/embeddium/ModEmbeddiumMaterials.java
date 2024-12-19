package dev.dubhe.anvilcraft.integration.embeddium;


import org.embeddedt.embeddium.impl.render.chunk.terrain.material.Material;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.parameters.AlphaCutoffParameter;

public class ModEmbeddiumMaterials {
    public static final Material LASER = new Material(ModEmbeddiumTerrainRenderPasses.LASER, AlphaCutoffParameter.ZERO, true);
}
