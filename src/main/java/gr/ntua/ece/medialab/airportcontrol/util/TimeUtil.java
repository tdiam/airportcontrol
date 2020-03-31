package gr.ntua.ece.medialab.airportcontrol.util;

/**
 * Time utilities.
 */
public class TimeUtil {
    /**
     * Converts an integer number of minutes to the duration in hh:mm string format.
     * @param minutes Number of minutes.
     * @return String that represents the duration in hh:mm format.
     */
    public static String minutesToHM(int minutes) {
        int hours = minutes / 60;
        minutes %= 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
