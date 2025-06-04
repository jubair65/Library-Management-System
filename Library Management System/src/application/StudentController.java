package application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import library.BookItem;
import library.CheckedOutItem;
import library.LibraryData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class StudentController {

    // Going back to home page
	@FXML
	public void handleBackButton(ActionEvent event) {
		try {

			Parent homePage = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
			Scene scene = new Scene(homePage);
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("Library - HomePage");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    // Exit button control
    @FXML
    private void exitApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    // Table & Search
    @FXML private TextField searchField;
    @FXML private TableView<BookItem> tableView;
    @FXML private TableColumn<BookItem, String> colId;
    @FXML private TableColumn<BookItem, String> colTitle;
    @FXML private TableColumn<BookItem, String> colAuthor;
    @FXML private TableColumn<BookItem, String> colType;
    @FXML private TableColumn<BookItem, String> colStatus;
    @FXML private TableColumn<BookItem, String> colAction;
    
    // Checkout & Status Tab
    @FXML private TextField checkoutItemId;
    @FXML private Label checkoutMessage;
    @FXML private TableView<CheckedOutItem> checkedOutItemsTable;
    @FXML private TableColumn<CheckedOutItem, String> checkedOutIdColumn;
    @FXML private TableColumn<CheckedOutItem, String> checkedOutTitleColumn;
    @FXML private TableColumn<CheckedOutItem, String> dueDateColumn;

    
    private final LibraryData libraryData = LibraryData.getInstance();

    // Initialize
    public void initialize() {
       
    	// TableView columns setup
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colTitle.setCellValueFactory(cell -> cell.getValue().titleProperty());
        colAuthor.setCellValueFactory(cell -> cell.getValue().authorProperty());
        colType.setCellValueFactory(cell -> cell.getValue().typeProperty());
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());

        // Action column button logic
        colAction.setCellFactory(col -> new TableCell<BookItem, String>() {
            private final Button button = new Button("Checkout");

            {
                button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;");
                button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-background-radius: 10;"));
                button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10;"));
                
                button.setOnAction(event -> {
                    BookItem item = getTableView().getItems().get(getIndex());
                    
                    // Show confirmation dialog
                    boolean confirmed = showCheckoutConfirmation(item);
                    if (confirmed) {
                        processCheckout(item);
                    }
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty) {
                    setGraphic(null);
                    return;
                }
                
                BookItem bookItem = getTableView().getItems().get(getIndex());
                if (bookItem.getStatus().equals("Available")) {
                    setGraphic(button);
                } else {
                    setGraphic(null); // No button for checked out items
                }
            }
        });
        
        
        
        
        
        // Filtered list for searching - using shared data
        FilteredList<BookItem> filteredData = new FilteredList<>(libraryData.getBooks(), p -> true);
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal.toLowerCase();
            filteredData.setPredicate(item -> 
                item.getId().toLowerCase().contains(lower) || 
                item.getTitle().toLowerCase().contains(lower) || 
                item.getAuthor().toLowerCase().contains(lower) || 
                item.getType().toLowerCase().contains(lower) || 
                item.getStatus().toLowerCase().contains(lower)
            );
        });
        
        tableView.setItems(filteredData);
        
        tableView.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
            col.setSortable(false);
            col.setEditable(false);
        });
        
        //checkedOut item table
        checkedOutIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        checkedOutTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
        
        // Load checked out items
        checkedOutItemsTable.setItems(libraryData.getCheckedOutItems());
        
    }
    
    
    // Handle checkout button from the Checkout & Status tab
    @FXML
    private void handleCheckout() {
        String itemId = checkoutItemId.getText().trim();
        
        if (itemId.isEmpty()) {
            showMessage("Please enter item ID", false);
            return;
        }
        
        // Find item by ID
        BookItem item = libraryData.findBookById(itemId);
        
        if (item == null) {
            showMessage("Item not found", false);
            return;
        }
        
        if (!item.getStatus().equals("Available")) {
            showMessage("Item is already checked out: " + item.getTitle(), false);
            return;
        }
        
        // Show confirmation dialog
        boolean confirmed = showCheckoutConfirmation(item);
        
        if (confirmed) {
            processCheckout(item);
        }
    }
    
    // Show confirmation dialog for checkout
    private boolean showCheckoutConfirmation(BookItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout Confirmation");
        alert.setHeaderText("Checkout Item");
        alert.setContentText("Are you sure you want to check out:\n" +
                            "ID: " + item.getId() + "\n" +
                            "Title: " + item.getTitle() + "\n" +
                            "Author: " + item.getAuthor() + "\n\n" +
                            "Due date will be: " + 
                            LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    // Process the checkout after confirmation
    private void processCheckout(BookItem item) {
        // Update status in shared data
        libraryData.updateBookStatus(item.getId(), "Checked Out");
        
        // Add to checked out items with due date (2 weeks from now)
        LocalDate dueDate = LocalDate.now().plusDays(14);
        libraryData.addCheckedOutItem(item.getId(), item.getTitle(), dueDate);
        
        // Show success message
        showMessage("Successfully checked out: " + item.getTitle(), true);
        
        // Clear the checkout field
        if (checkoutItemId != null) {
            checkoutItemId.clear();
        }
        
        // Refresh tables
        tableView.refresh();
        if (checkedOutItemsTable != null) {
            checkedOutItemsTable.refresh();
        }
    }
    
    // Show message in the checkout message label
    private void showMessage(String message, boolean isSuccess) {
        if (checkoutMessage != null) {
            checkoutMessage.setText(message);
            checkoutMessage.setStyle(isSuccess ? 
                "-fx-text-fill: green; -fx-font-weight: bold;" : 
                "-fx-text-fill: red; -fx-font-weight: bold;");
            checkoutMessage.setVisible(true);
            
            // Hide message after 5 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> {
                        checkoutMessage.setVisible(false);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            // Fallback to alert if label not available
            Alert alert = new Alert(isSuccess ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(isSuccess ? "Success" : "Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}