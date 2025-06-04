package library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckedOutItem {
    private final StringProperty id;
    private final StringProperty title;
    private final StringProperty dueDate;
    private final LocalDate dueDateValue;
    
    public CheckedOutItem(String id, String title, LocalDate dueDate) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.dueDateValue = dueDate;
        this.dueDate = new SimpleStringProperty(
            dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        );
    }
    
    public String getId() { return id.get();}
    public String getTitle() {return title.get();}
    public String getDueDate() {return dueDate.get();}
    
    public StringProperty idProperty() {return id;}
    public StringProperty titleProperty() { return title;}
    public StringProperty dueDateProperty() {return dueDate;}
    public LocalDate getDueDateValue() {return dueDateValue;}
}