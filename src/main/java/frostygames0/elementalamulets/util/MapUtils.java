package frostygames0.elementalamulets.util;

import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

public class MapUtils {
    public static <T> void mergeAndRemoveNegativeValues(Map<T, Integer> map, T key, int amount) {
        map.merge(key, amount, (oldAmount, toAdd) -> {
            var result = oldAmount + toAdd;
            return result > 0 ? result : null;
        });
    }

    public static <M extends Map<K, V>, K, V> M createFromListOfPairs(Supplier<M> factory, List<Pair<K, V>> list, BinaryOperator<V> mergeFunction) {
        return list.stream().collect(Collectors.toMap(Pair<K, V>::getFirst, Pair<K, V>::getSecond, mergeFunction, factory));
    }

    public static <K, V> List<Pair<K, V>> convertToListOfPairs(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static <T> void transformMapAndRemoveNegative(Map<T, Integer> map, BiFunction<T, Integer, Integer> transformer) {
        applyFunctionAndRemove(map, transformer, (key, value) -> value <= 0);
    }

    public static <K, V> void applyFunctionAndRemove(Map<K, V> map, BiFunction<K, V, V> transformer, BiPredicate<K, V> removalCondition) {
        var iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();

            var element = entry.getKey();
            var amount = entry.getValue();

            var transformed = transformer.apply(element, amount);

            if (removalCondition.test(element, amount)) {
                iterator.remove();
            } else {
                entry.setValue(transformed);
            }
        }
    }
}
