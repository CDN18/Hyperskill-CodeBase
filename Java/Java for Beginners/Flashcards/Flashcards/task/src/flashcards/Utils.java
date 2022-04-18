package flashcards;

import java.util.Map;
import java.util.Objects;

/**
 * Returns a relevant key in map whose value is equal to the provided one.
 * Applicable to the case where both key and value are unique.
 */
public class Utils {
    public static <K,V> K getKeyByValue (Map<K, V> map, String value) {
        for (var entry: map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
