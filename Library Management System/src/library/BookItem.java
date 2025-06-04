package library;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookItem {
    private final StringProperty id, title, author, type, status, source;

    public BookItem(String id, String title, String author, String type, String status, String source) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.type = new SimpleStringProperty(type);
        this.status = new SimpleStringProperty(status);
        this.source = new SimpleStringProperty(source);
    }
    

    public String getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getType() { return type.get(); }
    public String getStatus() { return status.get(); }
    public String getSource() { return source.get(); }

    public void setStatus(String status) { this.status.set(status); }

    public StringProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty typeProperty() { return type; }
    public StringProperty statusProperty() { return status; }
    public StringProperty sourceProperty() { return source; }
}
