package dev.dubhe.anvilcraft.integration.jei.util;

import com.mojang.datafixers.util.Either;
import mezz.jei.common.util.RegistryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockTagDisplayHelper {

    public static Optional<Block> getDisplay(TagKey<Block> tag){
        return RegistryUtil.getRegistry(Registries.BLOCK).getTag(tag).map(it -> {
            if(it.size() == 0) return Optional.<Block>empty();
            return it.get((int) ((System.currentTimeMillis() / 1000) % it.size()))
                .unwrapKey()
                .map(key -> RegistryUtil.getRegistry(Registries.BLOCK).get(key));
        }).orElse(Optional.empty());
    }

    public static List<Component> getTooltipsForInput(Either<TagKey<Block>, Block> input){
        List<Component> tooltipList = new ArrayList<>();
        input.ifRight(block -> tooltipList.add(block.getName()))
            .ifLeft(tag -> {
                getDisplay(tag).ifPresent(block -> tooltipList.add(block.getName()));
                tooltipList.add(Component.translatable("jei.tooltip.recipe.tag", "")
                    .withStyle(ChatFormatting.GRAY));
                tooltipList.add(Component.translatableWithFallback(
                    Tags.getTagTranslationKey(tag),"#" + tag.location())
                    .withStyle(ChatFormatting.GRAY));
            });
        return tooltipList;
    }
}
