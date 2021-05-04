package GUI.Controller.AdminControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PickerDashboardController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label lblTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GUI/View/AdminViews/PickerStage.fxml"));

        try {
            borderPane.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title){
        lblTitle.setText(title);
    }

    public void handleExit(MouseEvent mouseEvent) {
        handleCancel();
    }

    public void handleMaximize(MouseEvent mouseEvent) {
    }

    public void handleMinimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void handleCancel() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
    }

    public void handleSave(ActionEvent actionEvent) {
    }
}