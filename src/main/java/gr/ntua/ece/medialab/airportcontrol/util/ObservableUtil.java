package gr.ntua.ece.medialab.airportcontrol.util;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.AbstractMap;
import java.util.Map;

public class ObservableUtil {
    /**
     * Extract an ObservableList of key-value pairs from an ObservableMap.
     *
     * Adapted from:
     * https://stackoverflow.com/a/21339428
     *
     * @param map An ObservableMap instance.
     * @return An ObservableList with Map.Entry elements (key-value pairs).
     */
    public static <K, V> ObservableList<Map.Entry<K, V>> observableMapToList(ObservableMap<K, V> map) {
        ObservableList<Map.Entry<K, V>> list = FXCollections.observableArrayList(map.entrySet());

        map.addListener((MapChangeListener.Change<? extends K, ? extends V> change) -> {
            if (change.wasAdded()) {
                list.add(new AbstractMap.SimpleEntry<>(change.getKey(), change.getValueAdded()));
            }
            if (change.wasRemoved()) {
                for (Map.Entry<K, V> entry : list) {
                    if (entry.getKey().equals(change.getKey())) {
                        list.remove(entry);
                        break;
                    }
                }
            }
        });

        return list;
    }
}
