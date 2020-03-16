package gr.ntua.ece.medialab.airportcontrol.data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TimeData {
    private Data root;

    TimeData(Data root) {
        this.root = root;
    }

    private SimpleIntegerProperty minutesSinceStart = new SimpleIntegerProperty(0);

    public SimpleIntegerProperty minutesSinceStartProperty() {
        return minutesSinceStart;
    }

    public void step() {
        minutesSinceStart.set(minutesSinceStart.get() + 1);
    }
}
