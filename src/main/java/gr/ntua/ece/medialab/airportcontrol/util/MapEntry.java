package gr.ntua.ece.medialab.airportcontrol.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Source:
 * https://stackoverflow.com/a/38490212
 */
public class MapEntry<K, V> {
    private final K key;
    private final V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapEntry) {
            MapEntry<?, ?> other = (MapEntry<?, ?>) obj;
            return Objects.equals(key, other.key);
        }
        return false;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K, V> ArrayList<MapEntry<K, V>> mapToMapEntryArrayList(Map<K, V> map) {
        ArrayList<MapEntry<K, V>> arr = new ArrayList<>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            arr.add(new MapEntry<>(entry.getKey(), entry.getValue()));
        }

        return arr;
    }
}
