package GUI.Controller.AdminControllers;

import BE.*;
import BLL.EmailManager;
import BLL.LoginManager;
import BLL.StageShower;
import GUI.Controller.PopupControllers.ConfirmationDialog;
import GUI.Controller.PopupControllers.SettingsController;
import GUI.Controller.PopupControllers.SnackBarPopup;
import GUI.Controller.PopupControllers.WarningController;
import GUI.Model.*;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    @FXML
    private Label lblWelcome;
    @FXML
    private Label lblBar;
    @FXML
    private AnchorPane root;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnSelectAll;
    @FXML
    private Label dateTimeLabel;
    @FXML
    private ImageView image;

    private boolean isMaximized = false;
    private EmailManager emailManager = EmailManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }

    private void initialize() {
        User currentUser = LoginManager.getCurrentUser();
        ClockCalender.initClock(dateTimeLabel);

        image.setImage(currentUser.getPhotoPath() == null ? new Image("/GUI/Resources/defaultPerson.png") : new Image(currentUser.getPhotoPath()));
        lblWelcome.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
        lblBar.setText("Admin Dashboard - " + currentUser.getFirstName() + " " + currentUser.getLastName());
        handleBugReportUpdate();
        checkEmailSettings();

        try {
            handleUserManagement();

            // Update the bug model to get the snackbar to pop up.
            DataModel.getInstance().updateAllBugs();
        } catch (IOException e) {
            e.printStackTrace();
            WarningController.createWarning("Oh no! Something went wrong trying to load the admins user management view." +
                    " It might be corrupted or lost.");
        }
    }

    private void checkEmailSettings() {

        if (!emailManager.canSendEmail())
            WarningController.createWarning(String.format("The email: \"%s\" for sending email notification for administrators is incorrect! Managers and users are not able to send emails until it's set correctly.", emailManager.getUsername()));
    }

    /**
     * Handle any new incoming bug reports.
     */
    private void handleBugReportUpdate() {
        DataModel.getInstance().getAllUnresolvedBugs().addListener((ListChangeListener<Bug>) c -> {

            if (c.getList().size() > 0) {
                String properContext = c.getList().size() > 1 ? "reports" : "report";
                String title = String.format("New bug %s", properContext);
                String text = String.format("%d new bug %s", c.getList().size(), properContext);
                while (c.next()) {

                    // If a new bug report is added show a SnackBar at the bottom center.
                    if (c.wasAdded()) {
                        SnackBarPopup.createSnackBarPopup(borderPane, title, text, 3.25).showSnackBar();
                        break;
                    }
                }
            }
        });
    }

    public void handleUserManagement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GUI/View/AdminViews/AdminManagement.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    public void handleScreenManagement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GUI/View/AdminViews/AdminScreenManagement.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    public void handleBugManagement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GUI/View/AdminViews/AdminBugReport.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    public void handleCreateMessage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/GUI/View/ManagerViews/ManagerMessage.fxml"));
        borderPane.setCenter(fxmlLoader.load());
    }

    public void handleSettings() {
        SettingsController.openSettings();
    }

    public void minimize() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }

    public void maximize() {
        isMaximized = !isMaximized;
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setMaximized(isMaximized);
    }

    public void exit() {
        System.exit(0);
    }

    public void Logout(ActionEvent actionEvent) throws IOException {
        ConfirmationDialog confirmation = new ConfirmationDialog("Are you sure you want to logout of the application?");

        Optional<Boolean> result = confirmation.showAndWait();

        if (result.isPresent()) {
            if (result.get()) {
                Stage root1 = (Stage) root.getScene().getWindow();
                SceneMover sceneMover = new SceneMover();

                // Reset the singleton instance so we don't leave any cache behind.
//                UserModel.getInstance().resetSingleton();
//                ScreenModel.getInstance().resetSingleton();
//                BugModel.getInstance().resetSingleton();
//                MessageModel.getInstance().resetSingleton();
//                SettingsModel.getInstance().resetSingleton();

                StageShower stageShower = new StageShower();

                stageShower.handleLogout(root1, sceneMover);
            }
        }
    }
}
