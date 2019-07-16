package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.css.CssParser;
import javafx.css.Rule;
import javafx.css.Stylesheet;
import javafx.css.converter.ColorConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;
import sample.Graphs.GraphGenerator;
import sample.Graphs.SeriesData;
import sample.Model.PowerMeterData;
import sample.Util.ReadFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    private final String GRID_COLOUR = "-chart-grid-colour";
    private final String CHART_COLOUR = "-chart-background";
    private final String BACKGROUND_COLOUR = "-background-colour";
    private final String TITLE_COLOUR = "-title-colour";

    @FXML Button filePicker;
    @FXML Button plotButton;
    @FXML Button clearButton;
    @FXML Button snapButton;

    @FXML Label fileName;
    @FXML TextField serialName;
    @FXML Button graphSettings;

    @FXML Label pointCoordinates;

    @FXML ScatterChart<Number,Number> scatterChart;

    private PowerMeterData data;
    private GraphGenerator graphGenerator;

    private Popup settingsPopup;
    private ErrorPopup errorPopup;

    private Map<String,Color> customCSS = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filePicker.setOnAction(event -> setFile());
        plotButton.setOnAction(event -> plot());
        clearButton.setOnAction(event -> clear());
        snapButton.setOnAction(event -> snap());
        graphSettings.setOnAction(event -> showSettings());

        scatterChart.visibleProperty().addListener(((observable, oldValue, newValue) -> {
            snapButton.setDisable(oldValue);
            graphSettings.setDisable(oldValue);
            clearButton.setDisable(oldValue);
        }));

        scatterChart.setVisible(false);
        scatterChart.setAnimated(false);

        customCSS.put(GRID_COLOUR,parseColor(GRID_COLOUR));
        customCSS.put(CHART_COLOUR,parseColor(CHART_COLOUR));
        customCSS.put(BACKGROUND_COLOUR,parseColor(BACKGROUND_COLOUR));
        customCSS.put(TITLE_COLOUR, parseColor(TITLE_COLOUR));

        graphGenerator = new GraphGenerator();

        Platform.runLater(() -> errorPopup = new ErrorPopup(plotButton.getScene().getWindow()));
    }

    private void setFile(){
        data = ReadFile.getInstance().getMeterData();
        Window.getWindows().get(Window.getWindows().size()-1).requestFocus();
        if(!data.getError()){
            fileName.setText(data.getFileName());
            serialName.setText(data.getFileName());
        }
    }

    private void plot(){
        if (data != null) {
            if(!data.getError()){
                SeriesData seriesData = new SeriesData(data.getNumberData());
                if(seriesData.getyAxis().size() <= 0 ){
                    System.err.println("No data found in file");
                    errorPopup.showMessage("No data found in file.\nPlease make sure that it is a Thorlabs dataset.");
                }else{
                    graphGenerator.addData(seriesData,serialName.getText());
                    graphGenerator.setyLabel("Power (dBm)").setxLabel("Image number");
                    graphGenerator.setTitle("Test");

                    graphGenerator.writeChart(scatterChart, (dataId, data) -> {
                        int x = (int) data.getXValue();
                        double y = (double) data.getYValue();
                        pointCoordinates.setText(String.format(Locale.UK,"x: %d, y: %.2f",x,y));
                        serialName.setText(scatterChart.getData().get(dataId).getName());
                    });
                    if (!scatterChart.isVisible()) {
                        scatterChart.setVisible(true);
                    }
                }
            }else{
                System.err.println("No data found in file");
                errorPopup.showMessage("No data found in file.\nPlease make sure that it is a Thorlabs dataset.");
            }
        }else{
            System.err.println("No file found");
           errorPopup.showMessage("No file found.\nPlease choose a file first.");
        }
    }

    private void clear(){
        if (scatterChart.isVisible()) {
            scatterChart.setVisible(false);
        }
        graphGenerator.clearAll(scatterChart);
        fileName.setText("");
        serialName.setText("");
        pointCoordinates.setText("");
        data = null;
    }

    private void snap(){
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setTransform(new Scale(4,4));
        WritableImage image = scatterChart.snapshot(parameters,null);
        String path = saveFile("png");
        if(!path.isEmpty()) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(path));
            } catch (IOException e) {
                errorPopup.showMessage("Failure in saving the image. Check for a valid path");
                System.err.println("Failure in saving image");
                e.printStackTrace();

            }
        }else{
            errorPopup.showMessage("Alert","Not saved");
            System.err.println("Image not saved");
        }

    }

    private void showSettings(){

        final int labelWidth = 150;
        final int fieldWidth = 250;
        final int buttonSpacing = 10;
        final int buttonWidth = (labelWidth + fieldWidth - buttonSpacing)/2;

        settingsPopup = new Popup();

        VBox settingsContainer = new VBox();
        settingsContainer.setStyle("-fx-background-color: #C3C3C3; -fx-padding: 10;");
        settingsContainer.setSpacing(10);

        HBox headerBox = new HBox();
        Label header = new Label("Graph Settings");
        header.setStyle("-fx-font-size: 20px");
        headerBox.getChildren().add(header);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setStyle("-fx-padding: 10;");

        HBox titleHBox = new HBox();
        Label titleLabel = new Label("Title: ");
        titleLabel.setPrefWidth(labelWidth);
        final TextField titleField = new TextField();
        titleField.setText(scatterChart.getTitle() == null ? "" : scatterChart.getTitle());
        titleField.setPrefWidth(fieldWidth);

        titleHBox.getChildren().addAll(titleLabel,titleField);

        HBox xAxisTitleBox = new HBox();
        Label xLabel = new Label("X Label: ");
        xLabel.setPrefWidth(labelWidth);
        final TextField xAxisField = new TextField();
        xAxisField.setText(scatterChart.getXAxis().getLabel() == null ? "" : scatterChart.getXAxis().getLabel());
        xAxisField.setPrefWidth(fieldWidth);
        xAxisTitleBox.getChildren().addAll(xLabel,xAxisField);

        HBox yAxisTitleBox = new HBox();
        Label yLabel = new Label("Y Label: ");
        yLabel.setPrefWidth(labelWidth);
        final TextField yAxisField = new TextField();
        yAxisField.setText(scatterChart.getYAxis().getLabel() == null ? "" : scatterChart.getYAxis().getLabel());
        yAxisTitleBox.getChildren().addAll(yLabel,yAxisField);
        yAxisField.setPrefWidth(fieldWidth);

        HBox backgroundBox = new HBox();
        Label backgroundLabel = new Label("Background: ");
        backgroundLabel.setPrefWidth(labelWidth);
        ColorPicker backgroundCP = new ColorPicker(customCSS.get(BACKGROUND_COLOUR));
        backgroundCP.valueProperty().addListener(getListener(BACKGROUND_COLOUR));
        backgroundCP.setPrefWidth(fieldWidth);
        backgroundBox.getChildren().addAll(backgroundLabel,backgroundCP);

        HBox chartBackgroundBox = new HBox();
        Label chartBackgroundLabel = new Label("Chart background: ");
        chartBackgroundLabel.setPrefWidth(labelWidth);
        ColorPicker chartBackgroundCP = new ColorPicker(customCSS.get(CHART_COLOUR));
        chartBackgroundCP.valueProperty().addListener(getListener(CHART_COLOUR));
        chartBackgroundCP.setPrefWidth(fieldWidth);
        chartBackgroundBox.getChildren().addAll(chartBackgroundLabel,chartBackgroundCP);

        HBox gridColourBox = new HBox();
        Label gridColourLabel = new Label("Grid colour: ");
        gridColourLabel.setPrefWidth(labelWidth);
        ColorPicker gridCP = new ColorPicker(customCSS.get(GRID_COLOUR));
        gridCP.valueProperty().addListener(getListener(GRID_COLOUR));
        gridCP.setPrefWidth(fieldWidth);
        gridColourBox.getChildren().addAll(gridColourLabel,gridCP);

        HBox titleColourBox = new HBox();
        Label titleColourLabel = new Label("Title colour: ");
        titleColourLabel.setPrefWidth(labelWidth);
        ColorPicker titleCP = new ColorPicker(customCSS.get(TITLE_COLOUR));
        titleCP.valueProperty().addListener(getListener(TITLE_COLOUR));
        titleCP.setPrefWidth(fieldWidth);
        titleColourBox.getChildren().addAll(titleColourLabel,titleCP);

        HBox buttonBox = new HBox();
        buttonBox.setStyle("-fx-alignment: center;");
        buttonBox.setSpacing(buttonSpacing);

        Button close = new Button("Close");
        close.setPrefWidth(buttonWidth);
        close.setOnAction(event -> settingsPopup.hide());

        Button update = new Button("Update");
        update.setPrefWidth(buttonWidth);
        update.setOnAction(event -> {
            scatterChart.setTitle(titleField.getText());
            scatterChart.getXAxis().setLabel(xAxisField.getText());
            scatterChart.getYAxis().setLabel(yAxisField.getText());
            settingsPopup.hide();
        });

        buttonBox.getChildren().addAll(close,update);

        settingsContainer.getChildren().add(headerBox);
        settingsContainer.getChildren().add(titleHBox);
        settingsContainer.getChildren().add(xAxisTitleBox);
        settingsContainer.getChildren().add(yAxisTitleBox);
        settingsContainer.getChildren().add(backgroundBox);
        settingsContainer.getChildren().add(chartBackgroundBox);
        settingsContainer.getChildren().add(gridColourBox);
        settingsContainer.getChildren().add(titleColourBox);
        settingsContainer.getChildren().add(buttonBox);

        settingsPopup.getContent().add(settingsContainer);

        settingsPopup.show(graphSettings.getScene().getWindow());

    }

    //===========================Helper methods============================

    private void setStyle(){
        StringBuilder sb = new StringBuilder();
        for(String key : customCSS.keySet()){
            sb.append(String.format(Locale.UK, "%s: %s;",key,colourToHex(customCSS.get(key))));
        }scatterChart.setStyle(sb.toString());
    }

    private String colourToHex(Color c){
        return c.toString().replace("0x","#");
    }


    private Color parseColor(String property){
        CssParser parser = new CssParser();
        try{
            Stylesheet css = parser.parse(getClass().getResource("graph1.css").toURI().toURL());
            final Rule rootRule = css.getRules().get(0); // .root
            return  rootRule.getDeclarations().stream()
                    .filter(d -> d.getProperty().equals(property))
                    .findFirst()
                    .map(d -> ColorConverter.getInstance().convert(d.getParsedValue(), null))
                    .get();
        }catch (URISyntaxException | IOException ex){
            return Color.WHITE;
        }

    }

    private ChangeListener<Color> getListener(final String property){
        return ((observable, oldValue, newValue) -> {
            customCSS.replace(property, newValue);
            setStyle();
        });
    }

    private String saveFile(String format){
        FileChooser saveFile = new FileChooser();
        saveFile.setTitle("Save image");
        saveFile.setInitialFileName("Graph." + format);
        File file = saveFile.showSaveDialog(snapButton.getScene().getWindow());
        return file == null ? "" : file.getPath();
    }


}
