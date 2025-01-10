package dev.dubhe.anvilcraft.block.entity;

import dev.dubhe.anvilcraft.api.tooltip.providers.IBlockEntityTooltipProvider;
import dev.dubhe.anvilcraft.init.ModBlockEntities;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.util.AnvilUtil;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceOvercompressorBlockEntity extends BlockEntity implements IBlockEntityTooltipProvider {

    public static long NEUTRONIUM_INGOT_MASS = 30_000_000;
    public static int MAX_OUTPUT_PER_TIME = 640;
    private long storedMass = 0;

    public SpaceOvercompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public SpaceOvercompressorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SPACE_OVERCOMPRESSOR.get(), pos, blockState);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(9);

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putLong("storedMass", storedMass);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.storedMass = tag.getLong("storedMass");
    }

    public void injectMass(long mass) {
        this.storedMass += mass;
        this.produceNeutronium();
        this.setChanged();
    }

    public void produceNeutronium() {
        Level level = this.level;
        if (level == null) return;
        BlockPos pos = this.getBlockPos();
        int produceCount = (int) Math.min(MAX_OUTPUT_PER_TIME, this.storedMass / NEUTRONIUM_INGOT_MASS);
        if (produceCount <= 0) return;
        this.storedMass -= produceCount * NEUTRONIUM_INGOT_MASS;
        AnvilUtil.dropItems(List.of(ModItems.NEUTRONIUM_INGOT.asStack(produceCount)),
            level,
            pos.below().getCenter());
    }

    public Component displayStoredMass() {
        return displayStoredMass(this.storedMass);
    }

    public static Component displayStoredMass(long mass) {
        if (mass <= 0) return Component.literal("0");
        if (mass % 100 == 0) return Component.literal(String.valueOf(mass / 100));
        if (mass % 10 == 0) return Component.literal(String.valueOf(mass / 100) + '.' + (mass % 100) / 10);
        return Component.literal(String.valueOf(mass / 100) + '.' + (mass % 100));
    }

    @Override
    public boolean accepts(BlockEntity entity) {
        return entity instanceof SpaceOvercompressorBlockEntity;
    }

    @Override
    public List<Component> tooltip(BlockEntity e) {
        if (!(e instanceof SpaceOvercompressorBlockEntity thiz)) return List.of();
        return List.of(Component.translatable("tooltip.anvilcraft.space_overcompressor.stored_mass",
            thiz.displayStoredMass()));
    }

    @Override
    public ItemStack icon(BlockEntity entity) {
        return ModBlocks.SPACE_OVERCOMPRESSOR.asStack();
    }

    @Override
    public int priority() {
        return 0;
    }
}
