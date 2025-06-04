package application;
import library.*;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableRow;
import library.CirculationRecord;
public class LibrarianController {
    
    //going back to home page
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
    
    //exit button control
    public void exitApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            Platform.exit();
        }
    }


    
    
    /////////------All Variable Initialization -----//////////////
    
    
    // ComboBoxes
    @FXML private ComboBox<String> bookcategoryComboBox;
    @FXML private ComboBox<String> publicationCategoryComboBox;

    // Book input fields
    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorsField;
    @FXML private TextField bookYearField;
    @FXML private TextField publisherNameField;

    // Publication input fields
    @FXML private TextField publicationTitleField;
    @FXML private TextField publicationAuthorsField;
    @FXML private TextField publicationYearField;
    @FXML private TextField journalNameField;

    // Table & Search
    @FXML private TextField searchField;
    @FXML private TableView<BookItem> tableView;
    
    @FXML private TableColumn<BookItem, String> idColumn;
    @FXML private TableColumn<BookItem, String> svTitleColumn;
    @FXML private TableColumn<BookItem, String> authorColumn;
    @FXML private TableColumn<BookItem, String> typeColumn;
    @FXML private TableColumn<BookItem, String> statusColumn;
  //  @FXML private TableColumn<BookItem, Void> actionsColumn;

    // Circulation
    @FXML private TextField checkoutItemIdField;
    @FXML private TextField checkoutMemberIdField;
    @FXML private TextField checkInItemField;
    @FXML private TextField checkInMemberField;

    @FXML private TableView<CirculationRecord> circulationTable;
    @FXML private TableColumn<CirculationRecord, String> dateColumn;
    @FXML private TableColumn<CirculationRecord, String> itemIdColumn;
    @FXML private TableColumn<CirculationRecord, String> titleColumn;
    @FXML private TableColumn<CirculationRecord, String> memberIdColumn2;
    @FXML private TableColumn<CirculationRecord, String> actionColumn;

    //Members
    @FXML private TextField memberIdField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> colId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, Integer> colItems;
    @FXML private TableColumn<Member, Void> colActions;
    
    
    
    //LibraryData singleton
    private final LibraryData libraryData = LibraryData.getInstance();
    private final ObservableList<CirculationRecord> circulationData = FXCollections.observableArrayList();

    
    ////////////-------Initialization---------/////////////
    
    
    public void initialize() {
        
    	// ComboBox setup
        bookcategoryComboBox.getItems().addAll("Fiction", "Non-Fiction", "Science", "History", "Biography", "Fantasy", "Romance", "Mystery", "Technology", "Children's Books");
        publicationCategoryComboBox.getItems().addAll("Book", "Magazine", "Journal", "Newspaper", "E-book", "Research Paper", "Thesis / Dissertation", "Newsletter", "Conference Proceedings", "Manual / Guide");

        // TableView columns
        idColumn.setCellValueFactory(cell -> cell.getValue().idProperty());
        svTitleColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        authorColumn.setCellValueFactory(cell -> cell.getValue().authorProperty());
        typeColumn.setCellValueFactory(cell -> cell.getValue().typeProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().statusProperty());
        
        //members
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colItems.setCellValueFactory(new PropertyValueFactory<>("itemsCheckedOut"));
        memberTable.setItems(libraryData.getMembers());
        addActionButtons();
        
        
        // filtered list for searching
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

        tableView.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
            col.setSortable(false);
            col.setEditable(false);
        });

        tableView.setItems(filteredData);

        

        // Circulation table
        dateColumn.setCellValueFactory(cell -> cell.getValue().dateProperty());
        itemIdColumn.setCellValueFactory(cell -> cell.getValue().itemIdProperty());
        titleColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        memberIdColumn2.setCellValueFactory(cell -> cell.getValue().memberIdProperty());
        actionColumn.setCellValueFactory(cell -> cell.getValue().actionProperty());

        circulationTable.setItems(circulationData);

        
        actionColumn.setCellFactory(column -> new TableCell<CirculationRecord, String>() {
            @Override
            public void updateItem(String action, boolean empty) {
                super.updateItem(action, empty);
                if (empty || action == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(action);
                    if ("Checkin".equalsIgnoreCase(action)) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: black;"); // green
                    } else if ("Checkout".equalsIgnoreCase(action)) {
                        setStyle("-fx-background-color: #f8d7da; -fx-text-fill: black;"); // red
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

   
 
   ////////////////-----ALL METHODS----- /////////////////
  
    private void showConfirmation(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearBookFields() {
        bookTitleField.clear();
        bookAuthorsField.clear();
        bookYearField.clear();
        publisherNameField.clear();
        bookcategoryComboBox.setValue(null);
    }

    private void clearPublicationFields() {
        publicationTitleField.clear();
        publicationAuthorsField.clear();
        publicationYearField.clear();
        journalNameField.clear();
        publicationCategoryComboBox.setValue(null);
    }

    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    @FXML
    private void onAddBook() {
        String title = bookTitleField.getText();
        String authors = bookAuthorsField.getText();
        String year = bookYearField.getText();
        String publisher = publisherNameField.getText();
        String category = bookcategoryComboBox.getValue();

        if (title.isEmpty() || authors.isEmpty() || year.isEmpty() || publisher.isEmpty() || category == null) {
            showAlert("Please fill all book fields.");
            return;
        }

        // Add book to shared data
        libraryData.addBook(title, authors, publisher, category);
        
        clearBookFields();
        showConfirmation("Book \"" + title + "\" added successfully!");
    }

    @FXML
    private void onAddPublication() {
        String title = publicationTitleField.getText();
        String authors = publicationAuthorsField.getText();
        String year = publicationYearField.getText();
        String journal = journalNameField.getText();
        String category = publicationCategoryComboBox.getValue();

        if (title.isEmpty() || authors.isEmpty() || year.isEmpty() || journal.isEmpty() || category == null) {
            showAlert("Please fill all publication fields.");
            return;
        }

        // Add publication to shared data
        libraryData.addPublication(title, authors, journal, category);
        
        clearPublicationFields();
        showConfirmation("Publication \"" + title + "\" added successfully!");
    }

    @FXML
    private void handleCheckout() {
        String itemId = checkoutItemIdField.getText();
        String memberId = checkoutMemberIdField.getText();

        if (itemId == null || itemId.isEmpty()) {
            showAlert("Please enter an Item ID.");
            return;
        }

        if (memberId == null || memberId.isEmpty()) {
            showAlert("Please enter a Member ID.");
            return;
        }

        BookItem book = libraryData.findBookById(itemId);
       
        if (book != null &&  libraryData.findMemberById(memberId)!=null) {
            if (book.getStatus().equals("Checked Out")) {
                showAlert("Item is already checked out.");
                return;
            }

            // Update status
            libraryData.updateBookStatus(itemId, "Checked Out");
          
            //clearing field
            checkoutItemIdField.clear();
            checkoutMemberIdField.clear();
         
            //member's checkout count
            Member member = libraryData.findMemberById(memberId);
            if (member == null) {
                showAlert("Member ID not found.");
                return;
            }
            
            libraryData.updateMemberCheckoutCount(memberId, true);
            memberTable.refresh(); 
            
            tableView.refresh();
            circulationData.add(new CirculationRecord(
                getCurrentDate(), itemId, book.getTitle(), memberId, "Checkout"
            ));
            showConfirmation("Item \"" + book.getTitle() + "\" checked out.");
            return;
            
            
        }else if(book==null){
        	showAlert("Item ID not found.");
        }else {
        	showAlert("Member ID not found.");
        }
    }

    @FXML
    private void handleCheckin() {
        String itemId = checkInItemField.getText();
        String memberId = checkInMemberField.getText();

        if (itemId == null || itemId.isEmpty()) {
            showAlert("Please enter an Item ID.");
            return;
        }

        if (memberId == null || memberId.isEmpty()) {
            showAlert("Please enter a Member ID.");
            return;
        }

        BookItem book = libraryData.findBookById(itemId);
        
        if (book != null && libraryData.findMemberById(memberId)!=null) {
            if (book.getStatus().equals("Available")) {
                showAlert("Item is already available.");
                return;
            }

            //status updte
            libraryData.updateBookStatus(itemId, "Available");
            
            //clear
            checkInItemField.clear();
            checkInMemberField.clear();
            
         // member's checkout count
            Member member = libraryData.findMemberById(memberId);
            if (member == null) {
                showAlert("Member ID not found.");
                return;
            }
            libraryData.updateMemberCheckoutCount(memberId, false);
            memberTable.refresh();
            
            tableView.refresh();
            circulationData.add(new CirculationRecord(
                getCurrentDate(), itemId, book.getTitle(), memberId, "Checkin"
            ));
            
            showConfirmation("Item \"" + book.getTitle() + "\" checked in.");
            return;
        }

        showAlert("Item ID not found.");
    }
    
    //members methods
       
    @FXML
    private void handleAddMember() {
        String id = memberIdField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        Member newMember = new Member(id, name, email, phone, 0);
        libraryData.getMembers().add(newMember);
        showConfirmation("Member Added Successfully!");
        clearForm();
        
    }

    private void clearForm() {
        memberIdField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }



    private void addActionButtons() {
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View Details");

            {
                btn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    Member member = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Member Details");
                    alert.setHeaderText(member.getName());
                    alert.setContentText("ID: " + member.getId() + "\nEmail: " + member.getEmail() + "\nPhone: " + member.getPhone() + "\nItems Checked Out: " + member.getItemsCheckedOut());
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }
}