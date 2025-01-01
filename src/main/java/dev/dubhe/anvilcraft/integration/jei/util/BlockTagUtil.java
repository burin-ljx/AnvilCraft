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

public class BlockTagUtil {

    /**
     * 根据方块标签，获取当前的用于循环展示的方块。
     *
     * @param tag 需要显示的方块标签
     * @return 用于展示的方块（当<code>tag</code>为空标签或无效标签时，返回值也为空）
     */
    public static Optional<Block> getDisplay(TagKey<Block> tag){
        return RegistryUtil.getRegistry(Registries.BLOCK)
            .getTag(tag)
            .filter(it -> it.size() > 0)
            .map(it -> it.get((int) ((System.currentTimeMillis() / 1000) % it.size())).value());
    }

    /**
     * 根据方块配方输入，获取需要展示的工具提示
     *
     * @param input 方块标签或方块的配方输入
     * @return 展示方块对应的工具提示。若为方块标签，还会展示具体的标签名
     */
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
