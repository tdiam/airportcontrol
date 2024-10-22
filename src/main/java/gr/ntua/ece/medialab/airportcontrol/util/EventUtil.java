package gr.ntua.ece.medialab.airportcontrol.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * JavaFX event utilities.
 */
public class EventUtil {
    /**
     * Adapted from:
     * https://stackoverflow.com/a/53908008
     *
     * @param evt JavaFX event.
     * @return The stage where the event occurred.
     */
    public static Stage getWindowFromEvent(javafx.event.Event evt) {
        return (Stage) ((Node) evt.getSource()).getScene().getWindow();
    }
}
