package dev.dubhe.anvilcraft.util;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StateUtil {
    public static <O, T extends StateHolder<O, T>, E extends Comparable<E>>
    List<T> findPossibleStatesForProperty(T initialState, Property<E> property) {
        List<T> result = new ArrayList<>();
        T currentIterating = initialState;
        while (!result.contains(currentIterating)) {
            result.add(currentIterating);
            currentIterating = currentIterating.cycle(property);
        }
        result.sort(Comparator.comparing(it -> it.getValue(property)));
        return result;
    }
}
