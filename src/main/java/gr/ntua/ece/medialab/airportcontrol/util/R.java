package gr.ntua.ece.medialab.airportcontrol.util;

import java.util.ResourceBundle;

public class R {
    private static ResourceBundle bundle = new ResourceBundleWrapper(
            ResourceBundle.getBundle("gr.ntua.ece.medialab.airportcontrol.bundle"));

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static String get(String key, Object... args) {
        if (args.length == 0) {
            return bundle.getString(key);
        }
        return String.format(bundle.getString(key), args);
    }
}
