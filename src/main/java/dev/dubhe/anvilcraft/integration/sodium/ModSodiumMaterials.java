package dev.dubhe.anvilcraft.integration.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.parameters.AlphaCutoffParameter;

public class ModSodiumMaterials {
    public static final Material LASER = new Material(ModSodiumTerrainRenderPasses.LASER, AlphaCutoffParameter.ZERO, true);
}
