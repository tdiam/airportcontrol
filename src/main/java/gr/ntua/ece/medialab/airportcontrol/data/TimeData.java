package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.application.Platform;
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
    private int clockIntervalMs = 500; // Every 5 seconds increase by 1 minute
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduleHandler;

    private SimpleIntegerProperty minutesSinceStart = new SimpleIntegerProperty(0);

    /**
     * Creates a new instance of the time data controller.
     * @param root Reference to root controller.
     */
    TimeData(Data root) {
        this.root = root;
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
        this.clockIntervalMs = clockIntervalMs;
    }

    /**
     * Gets the minutes since the scenario execution started.
     * @return Property that stores the minutes since start.
     */
    public SimpleIntegerProperty minutesSinceStartProperty() {
        return minutesSinceStart;
    }

    /**
     * Starts the clock.
     */
    public void start() {
        final Runnable task = () -> {
            Platform.runLater(this::step);
        };
        scheduleHandler = scheduler.scheduleAtFixedRate(task, clockIntervalMs, clockIntervalMs,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Proceeds the clock by one minute.
     */
    public void step() {
        minutesSinceStart.set(minutesSinceStart.get() + 1);
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
        // TODO: The delay may be longer than necessary, since the schedule is not synced with the tick schedule.
        long delay = (when - minutesSinceStart.get()) * clockIntervalMs;
        if (delay < 0) {
            if (runIfPast) {
                command.run();
            }
            return null;
        }
        return scheduler.schedule(() -> Platform.runLater(command), delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Converts an integer number of minutes to the duration in hh:mm string format.
     * @param minutes Number of minutes.
     * @return String that represents the duration in hh:mm format.
     */
    public String minutesToHM(int minutes) {
        int hours = minutes / 60;
        minutes %= 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
