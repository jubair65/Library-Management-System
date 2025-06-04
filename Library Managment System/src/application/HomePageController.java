package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class HomePageController {

    @FXML
    private void openLibrarianDashboard(ActionEvent event) {
        switchScene(event, "LibrarianDashboard.fxml");
    }

    @FXML
    private void openStudentDashboard(ActionEvent event) {
        switchScene(event, "StudentDashboard.fxml");
        
    }

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            if(fxmlFile.equals("StudentDashboard.fxml")) {
            	stage.setTitle("Library - Student Dashboard");
            }else
            {
            	stage.setTitle("Library - Librarian Dashboard");
            }
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
