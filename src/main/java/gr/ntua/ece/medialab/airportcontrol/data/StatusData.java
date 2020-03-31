package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StatusData {
    public static final long CLEAR_TIMEOUT = 5000; // 5 seconds
    private Data root;
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleBooleanProperty isError = new SimpleBooleanProperty(false);
    private ScheduledFuture<?> scheduleHandler;

    public StatusData(Data root) {
        this.root = root;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public SimpleBooleanProperty isErrorProperty() {
        return isError;
    }

    public void setStatus(String status) {
        setStatus(status, CLEAR_TIMEOUT);
    }

    public void setStatus(String status, long clearTimeout) {
        this.status.set(status);
        this.isError.set(false);
        cancelClear();
        scheduleClear(clearTimeout);
    }

    public void setError(String errorMessage) {
        setError(errorMessage, CLEAR_TIMEOUT);
    }

    public void setError(String errorMessage, long clearTimeout) {
        this.status.set("Error! " + errorMessage);
        this.isError.set(true);
        cancelClear();
        scheduleClear(clearTimeout);
    }

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
