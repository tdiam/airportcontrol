package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeData {
    private Data root;
    private int clockIntervalMs = 100; // Every 5 seconds increase by 1 minute
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduleHandler;

    private SimpleIntegerProperty minutesSinceStart = new SimpleIntegerProperty(0);

    TimeData(Data root) {
        this.root = root;
    }

    public int getClockIntervalMs() {
        return clockIntervalMs;
    }

    public void setClockIntervalMs(int clockIntervalMs) {
        this.clockIntervalMs = clockIntervalMs;
    }

    public SimpleIntegerProperty minutesSinceStartProperty() {
        return minutesSinceStart;
    }

    public void start() {
        final Runnable task = () -> {
            Platform.runLater(this::step);
        };
        scheduleHandler = scheduler.scheduleAtFixedRate(task, clockIntervalMs, clockIntervalMs,
                TimeUnit.MILLISECONDS);
    }

    public void step() {
        minutesSinceStart.set(minutesSinceStart.get() + 1);
    }

    public void stop() {
        scheduleHandler.cancel(true);
    }
}
