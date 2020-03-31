package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Status data controller.
 */
public class StatusData {
    /**
     * Default timeout to clear status bar after printing a message.
     */
    public static final long CLEAR_TIMEOUT = 5000; // 5 seconds

    private Data root;
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleBooleanProperty isError = new SimpleBooleanProperty(false);
    private ScheduledFuture<?> scheduleHandler;

    /**
     * Creates a new instance of the status data controller.
     * @param root Reference to root controller.
     */
    public StatusData(Data root) {
        this.root = root;
    }

    /**
     * Gets the current status.
     * @return String property.
     */
    public SimpleStringProperty statusProperty() {
        return status;
    }

    /**
     * Gets a boolean value that represents whether the current status is an error or not.
     * @return Boolean property.
     */
    public SimpleBooleanProperty isErrorProperty() {
        return isError;
    }

    /**
     * {@code clearTimeout} defaults to {@code CLEAR_TIMEOUT}
     * @see StatusData#setStatus(String, long)
     */
    public void setStatus(String status) {
        setStatus(status, CLEAR_TIMEOUT);
    }

    /**
     * Sets status that will be cleared after given timeout.
     * @param status Status message.
     * @param clearTimeout Number of milliseconds after which the status will be cleared.
     */
    public void setStatus(String status, long clearTimeout) {
        this.status.set(status);
        this.isError.set(false);
        cancelClear();
        scheduleClear(clearTimeout);
    }

    /**
     * {@code clearTimeout} defaults to {@code CLEAR_TIMEOUT}
     * @see StatusData#setError(String, long)
     */
    public void setError(String errorMessage) {
        setError(errorMessage, CLEAR_TIMEOUT);
    }

    /**
     * Sets error status that will be cleared after given timeout.
     * @param errorMessage Error status message.
     * @param clearTimeout Number of milliseconds after which the status will be cleared.
     */
    public void setError(String errorMessage, long clearTimeout) {
        this.status.set("Error! " + errorMessage);
        this.isError.set(true);
        cancelClear();
        scheduleClear(clearTimeout);
    }

    /**
     * Clears status.
     */
    public void clear() {
        this.status.set("");
        this.isError.set(false);
    }

    private void cancelClear() {
        if (scheduleHandler != null) {
            scheduleHandler.cancel(true);
        }
    }

    private void scheduleClear(long clearTimeout) {
        final Runnable task = () -> Platform.runLater(this::clear);
        scheduleHandler = root.timeData().getScheduler().schedule(task, clearTimeout, TimeUnit.MILLISECONDS);
    }
}
