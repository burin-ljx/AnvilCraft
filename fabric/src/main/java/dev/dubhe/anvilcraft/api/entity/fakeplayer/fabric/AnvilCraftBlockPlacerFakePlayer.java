package dev.dubhe.anvilcraft.api.entity.fakeplayer.fabric;

import com.mojang.authlib.GameProfile;
import dev.dubhe.anvilcraft.api.entity.player.IAnvilCraftBlockPlacer;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class AnvilCraftBlockPlacerFakePlayer extends FakePlayer implements IAnvilCraftBlockPlacer {
    static final UUID placerUUID = UUID.randomUUID();
    static final String placerName = "AnvilCraftBlockPlacer";
    static final GameProfile fakeProfile = new GameProfile(placerUUID, "[Block Placer of " + placerName + "]");

    public AnvilCraftBlockPlacerFakePlayer(ServerLevel world) {
        super(world, AnvilCraftBlockPlacerFakePlayer.fakeProfile);
    }

    @Override
    public ServerPlayer getPlayer() {
        return this;
    }
}
