package GUI.Controller.AdminControllers;

import BLL.DataGenerator;
import BLL.ViewMaker;
import BLL.DataNodes.ViewType;
import GUI.Controller.PopupControllers.WarningController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class DataManagementController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private FontAwesomeIconView leftBtn;
    @FXML
    private FontAwesomeIconView rightBtn;
    @FXML
    private BorderPane bar;
    @FXML
    private TextField textField;
    @FXML
    private ComboBox<ViewType> comboBox;
    @FXML
    private Button pickFile;
    @FXML
    private BorderPane pane;
    private final AtomicReference<File> file = new AtomicReference<>();
    private Stage stage;
    private PickerStageController pickerStageController;
    private Node previousNode;
    private final DataGenerator dataGenerator = new DataGenerator();
    private ViewType selectedItem;

    public BorderPane getBar() {
        return bar;
    }

    public FontAwesomeIconView getLeftBtn() {
        return leftBtn;
    }

    public FontAwesomeIconView getRightBtn() {
        return rightBtn;
    }

    /**
     * Sets the current pickerStageController
     *
     * @param pickerStageController
     */
    public void setPickerStageController(PickerStageController pickerStageController) {
        this.pickerStageController = pickerStageController;
    }

    /**
     * Sets the window in the PickerStageController to the given Stage
     *
     * @param stage the owner of this Window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        previousNode = pickerStageController.getContent();
        FileChooser fileChooser = new FileChooser();
        //fileChooser.setInitialDirectory(new File("C:\\Users\\Carlo De Leon\\Desktop\\test"));

        // We need a selection from the combo box first.
        comboBox.setOnAction((v) -> {
            fileChooser.getExtensionFilters().clear();
            var htmlExtension = new FileChooser.ExtensionFilter("html", "*.html");
            var pdfExtension = new FileChooser.ExtensionFilter("pdf", "*.pdf");
            var imgExtension = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg");
            var excelCsvExtension = new FileChooser.ExtensionFilter("Csv/Excel file", "*.csv", "*.xlsx");

            // Now let's add some extension based on the selected item.
            var viewTypeSelected = ViewType.valueOf(comboBox.getSelectionModel().getSelectedItem().toString());
            fileChooser.getExtensionFilters().clear();
            switch (viewTypeSelected) {
                case HTTP -> {
                    fileChooser.getExtensionFilters().add(htmlExtension);
                }
                case PDF -> {
                    fileChooser.getExtensionFilters().add(pdfExtension);
                }
                case Image -> {
                    fileChooser.getExtensionFilters().add(imgExtension);
                }
                case BarChart, PieChart, ScatterChart, BubbleChart, AreaChart, StackedAreaChart, StackedBarChart -> {
                    fileChooser.getExtensionFilters().add(excelCsvExtension);
                }
            }
            //tryToMakeContent();
            selectedItem = viewTypeSelected;
        });

        pickFile.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (selectedItem != null) {
                file.set(fileChooser.showOpenDialog(stage));
                if (file.get() != null) {
                    textField.setText(file.get().getAbsolutePath());
                    tryToMakeContent();
                }
            } else {
                Stage stage1 = new Stage();
                stage1.initOwner(root.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/View/PopUpViews/Warning.fxml"));
                try {
                    stage1.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WarningController warningController = fxmlLoader.getController();
                warningController.setText("Please select a screen type.");
                stage1.initStyle(StageStyle.TRANSPARENT);
                stage1.show();
                stage1.setX(mouseEvent.getScreenX() - stage1.getWidth() / 2);
                stage1.setY(mouseEvent.getScreenY() - 180);
                stage1.setAlwaysOnTop(true);
            }
        });

        textField.textProperty().addListener((observable -> tryToMakeContent(textField.getText())));

    }

    /**
     * Tries to use the selected Item to make the given file
     */
    private void tryToMakeContent() {
        if (comboBox.getSelectionModel().getSelectedItem() != null && file.get() != null) {
            try {
                ViewMaker.callProperMethod(pickerStageController, selectedItem, file.get());
            } catch (Exception e) {
                e.printStackTrace();
                WarningController.createWarning(String.format("Failed to create the view for: %s! Please check if the file exists or chart format is correct.", comboBox.getSelectionModel().getSelectedItem()));
            }
        }
    }

    private void tryToMakeContent(String uri) {
        if (comboBox.getSelectionModel().getSelectedItem() != null && !uri.isEmpty()) {
            try {
                ViewMaker.callProperMethod(pickerStageController, selectedItem, uri);
            } catch (Exception e) {
                e.printStackTrace();
                WarningController.createWarning(String.format("Failed to create the view for: %s! Please check the url.", comboBox.getSelectionModel().getSelectedItem()));
            }
        }
    }

    /**
     * Adds all the ViewTypes to the comboBox
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (ViewType viewType : ViewType.values())
            comboBox.getItems().add(viewType);
    }

    /**
     * Closes the stage
     *
     * @param event
     */
    public void confirm(ActionEvent event) {
        stage.close();
    }

    /**
     * Changes the pickerStageController Back to its original state and closes the stage.
     *
     * @param event
     */
    public void cancel(ActionEvent event) {
        pickerStageController.setContent(previousNode, null);
        stage.close();
    }
}
