package gr.ntua.ece.medialab.airportcontrol.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Simple wrapper of ResourceBundle in order to workaround a bug of fx:include.
 * https://community.oracle.com/thread/2595439
 */
public class ResourceBundleWrapper extends ResourceBundle {
    private final ResourceBundle bundle;

    public ResourceBundleWrapper(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected Object handleGetObject(String key) {
        return bundle.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return bundle.getKeys();
    }

    @Override
    public boolean containsKey(String key) {
        return bundle.containsKey(key);
    }

    @Override
    public Locale getLocale() {
        return bundle.getLocale();
    }

    @Override
    public Set<String> keySet() {
        return bundle.keySet();
    }
}
