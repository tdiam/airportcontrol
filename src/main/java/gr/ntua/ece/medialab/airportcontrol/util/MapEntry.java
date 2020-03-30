package gr.ntua.ece.medialab.airportcontrol.util;

import java.util.Comparator;
import java.util.Map;

public class MapEntry {
    public static <K, V> Comparator<Map.Entry<K, V>> ValueComparator(Comparator<V> cmp) {
        return (e1, e2) -> cmp.compare(e1.getValue(), e2.getValue());
    }
}
