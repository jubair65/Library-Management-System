package library;

public class Book extends BookItem {
    public Book(String id, String title, String author, String type, String status, String publisherName) {
        super(id, title, author, type, status, publisherName); // publisherName is passed as source
    }

    public String getPublisherName() {
        return getSource(); // source from BookItem
    }

}
