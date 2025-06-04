package library;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CirculationRecord {
    private final StringProperty date, itemId, title, memberId, action;

    public CirculationRecord(String date, String itemId, String title, String memberId, String action) {
        this.date = new SimpleStringProperty(date);
        this.itemId = new SimpleStringProperty(itemId);
        this.title = new SimpleStringProperty(title);
        this.memberId = new SimpleStringProperty(memberId);
        this.action = new SimpleStringProperty(action);
    }

    public StringProperty dateProperty() { return date; }
    public StringProperty itemIdProperty() { return itemId; }
    public StringProperty titleProperty() { return title; }
    public StringProperty memberIdProperty() { return memberId; }
    public StringProperty actionProperty() { return action; }
}

