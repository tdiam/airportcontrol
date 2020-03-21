package gr.ntua.ece.medialab.airportcontrol.util;

public class Errors {
    public static class FlightFormValidationError extends Exception {
        public FlightFormValidationError(String message) {
            super(message);
        }
    }
}
