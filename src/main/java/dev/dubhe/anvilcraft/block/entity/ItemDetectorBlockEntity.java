package dev.dubhe.anvilcraft.block.entity;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.itemhandler.FilteredItemStackHandler;
import dev.dubhe.anvilcraft.init.ModBlockEntities;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModMenuTypes;
import dev.dubhe.anvilcraft.inventory.ItemDetectorMenu;
import dev.dubhe.anvilcraft.inventory.container.FilterOnlyContainer;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Getter
public class ItemDetectorBlockEntity extends BlockEntity implements MenuProvider, IFilterBlockEntity {

    private static final FilteredItemStackHandler DUMMY_HANDLER = new FilteredItemStackHandler(0);
    public static final int DATASLOT_ID_RANGE = 0;
    public static final int DATASLOT_ID_FILTER_MODE = 1;

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 8;

    private Mode filterMode;
    private int range;
    private final FilterOnlyContainer filter;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATASLOT_ID_RANGE -> ItemDetectorBlockEntity.this.range;
                case DATASLOT_ID_FILTER_MODE -> ItemDetectorBlockEntity.this.filterMode.ordinal();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            AnvilCraft.LOGGER.debug("index: {}, value: {}", index, value);
            switch (index) {
            case DATASLOT_ID_RANGE:
                ItemDetectorBlockEntity.this.setRange(value);
                break;
            case DATASLOT_ID_FILTER_MODE:
                if (value < 0 || value >= Mode.values().length) return;
                ItemDetectorBlockEntity.this.setFilterMode(Mode.values()[value]);
                break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ItemDetectorBlockEntity(BlockEntityType<? extends BlockEntity> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.filterMode = Mode.ANY;
        this.range = 1;
        this.filter = new FilterOnlyContainer(this, 9);
    }

    public ItemDetectorBlockEntity(BlockPos pos, BlockState blockState) {
        this(ModBlockEntities.ITEM_DETECTOR.get(), pos, blockState);
    }

    public static ItemDetectorBlockEntity createBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState) {
        return new ItemDetectorBlockEntity(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.setRange(tag.getInt("Range"));
        this.filterMode = Mode.valueOf(tag.getString("FilterMode"));
        this.filter.deserializeNBT(registries, tag.getCompound("Filter"));
        AnvilCraft.LOGGER.debug("[1058297]filter: {}", this.filter.getFilterList());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Range", this.range);
        tag.putString("FilterMode", this.filterMode.toString());
        tag.put("Filter", this.filter.serializeNBT(registries));
    }

    public void cycleFilterMode(){
        this.filterMode = this.filterMode.cycle();
    }

    public void setFilterMode(Mode filterMode){
        if (this.filterMode == filterMode) return;
        this.filterMode = filterMode;
        this.setChanged();
    }

    public void increaseRange(){
        this.range = Mth.clamp(range + 1, MIN_RANGE, MAX_RANGE);
    }

    public void decreaseRange(){
        this.range = Mth.clamp(range - 1, MIN_RANGE, MAX_RANGE);
    }

    public void setRange(int range){
        range = Mth.clamp(range, MIN_RANGE, MAX_RANGE);
        if (this.range == range) return;
        this.range = range;
        this.setChanged();
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.ITEM_DETECTOR.get().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ItemDetectorMenu(ModMenuTypes.ITEM_DETECTOR.get(), id, inventory, this);
    }

    @Override
    public FilteredItemStackHandler getFilteredItemDepository() {
        return DUMMY_HANDLER;
    }

    @Override
    public boolean isFilterEnabled() {
        return true;
    }

    @Override
    public void setFilterEnabled(boolean enable) {
    }

    @Override
    public boolean isSlotDisabled(int slot) {
        return false;
    }

    @Override
    public void setSlotDisabled(int slot, boolean disable) {
    }

    @Override
    public NonNullList<ItemStack> getFilteredItems() {
        return this.filter.getFilterList();
    }

    @Override
    public ItemStack getFilter(int slot) {
        if (slot < 0 || slot >= this.filter.getContainerSize()) return ItemStack.EMPTY;
        return this.filter.getItem(slot);
    }

    @Override
    public boolean setFilter(int slot, ItemStack filter) {
        if (slot < 0 || slot >= this.filter.getContainerSize()) return false;
        this.filter.setItem(slot, filter);
        return true;
    }

    public boolean clearFilter(int slot){
        return this.setFilter(slot, ItemStack.EMPTY);
    }

    public enum Mode {
        ANY("any"),
        ALL("all");

        public final String buttonPath;

        Mode(String buttonPath) {
            this.buttonPath = buttonPath;
        }

        public Mode cycle() {
            return this == ANY ? ALL : ANY;
        }
    }
}
