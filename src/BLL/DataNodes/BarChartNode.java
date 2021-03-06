package BLL.DataNodes;

import BLL.DataGenerator;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileNotFoundException;

public class BarChartNode implements IDataNode{
    /**
     * Takes the BarChart from the DataGenerator to make a Barchart
     *
     * @param pane the BorderPane of the PickerStageController
     * @param file the file you want to generate data from
     * @return A Node with the given BarChart
     */
    @Override
    public Node getData(BorderPane pane, File file) throws FileNotFoundException {
        BarChart<String, Number> bc = DataGenerator.getBarChart(file.getPath());

        //Makes it follow the panes width
        pane.widthProperty().addListener((observableValue, bounds, t1) -> {
            bc.setPrefWidth(t1.doubleValue());
        });
        //Makes it follow the panes height
        pane.heightProperty().addListener((observableValue, bounds, t1) -> {
            bc.setPrefHeight(t1.doubleValue());
        });
        //sets initial height and width
        bc.setPrefWidth(pane.getWidth());
        bc.setPrefHeight(pane.getHeight());
        bc.setAccessibleText(ViewType.BarChart.name() + String.format("=\"%s\"", file.getPath()));

        return bc;
    }

    @Override
    public Node getData(BorderPane pane, String uri) throws FileNotFoundException {
        return this.getData(pane, new File(uri));
    }
}
