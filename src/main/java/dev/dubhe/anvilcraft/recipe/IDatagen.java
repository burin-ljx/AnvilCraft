package dev.dubhe.anvilcraft.recipe;

public interface IDatagen {
    String toDatagen();

    default String getSuggestedName() {
        return Integer.toHexString(this.hashCode());
    }
}
