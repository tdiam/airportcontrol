package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Time data controller.
 */
public class TimeData {
    private Data root;
    private int clockIntervalMs = 5000; // Every 5 seconds increase by 1 minute
    private int clockResolution = 10; // This must be a divisor of clockIntervalMs so that time is measured correctly
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduleHandler;

    private SimpleDoubleProperty fractionalTime = new SimpleDoubleProperty(0.0);
    private SimpleIntegerProperty time = new SimpleIntegerProperty();

    /**
     * Creates a new instance of the time data controller.
     * @param root Reference to root controller.
     */
    TimeData(Data root) {
        this.root = root;
        this.time.bind(Bindings.createIntegerBinding(() -> (int)fractionalTime.get(), fractionalTime));
    }

    /**
     * Gets the clock interval in milliseconds.
     * @return Clock interval in milliseconds as integer.
     */
    public int getClockIntervalMs() {
        return clockIntervalMs;
    }

    /**
     * Sets the clock interval.
     * @param clockIntervalMs New clock interval in milliseconds.
     */
    public void setClockIntervalMs(int clockIntervalMs) {
        // TODO: This won't fix the timing of scheduled tasks
        this.clockIntervalMs = clockIntervalMs;
        stop();
        start();
    }

    /**
     * Gets the minutes since the scenario execution started, in full resolution.
     * @return Double property.
     */
    public SimpleDoubleProperty fractionalTimeProperty() {
        return fractionalTime;
    }

    /**
     * Gets the minutes since the scenario execution started.
     * @return Integer property.
     */
    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    /**
     * Starts the clock.
     */
    public void start() {
        final Runnable task = () -> {
            Platform.runLater(this::step);
        };
        int stepMs = clockIntervalMs / clockResolution;
        scheduleHandler = scheduler.scheduleAtFixedRate(task, stepMs, stepMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Proceeds the clock by one step (1 / resolution of a minute).
     */
    public void step() {
        fractionalTime.set(fractionalTime.get() + (double)1 / clockResolution);
    }

    /**
     * Stops the clock.
     */
    public void stop() {
        scheduleHandler.cancel(true);
    }

    /**
     * {@code runIfPast} defaults to {@code false}.
     *
     * @see TimeData#schedule(Runnable, long, boolean)
     */
    public ScheduledFuture<?> schedule(Runnable command, long when) {
        return schedule(command, when, false);
    }

    /**
     * Schedule a task for some time in the future.
     * @param command A runnable.
     * @param when Time given as simulation minutes since start.
     * @param runIfPast Run task if {@code when} was in the past.
     * @return A ScheduledFuture instance.
     */
    public ScheduledFuture<?> schedule(Runnable command, long when, boolean runIfPast) {
        // NOTE: The delay is not exactly right due to rounding errors and depending on the resolution
        long delay = (int)((when - fractionalTime.get()) * clockIntervalMs);
        if (delay < 0) {
            if (runIfPast) {
                command.run();
            }
            return null;
        }
        return scheduler.schedule(() -> Platform.runLater(command), delay, TimeUnit.MILLISECONDS);
    }
}
