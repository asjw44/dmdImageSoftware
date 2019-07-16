package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import sample.Graphs.GraphGenerator;
import sample.Graphs.SeriesData;
import sample.Model.Helper;
import sample.Model.PowerMeterData;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;
import static sample.Util.Constants.*;
import sample.Util.Iterator.ExitCode;
import static sample.Util.Iterator.ExitCode.*;
import sample.Util.Iterator;
import sample.Util.ReadFile;
import sample.Util.WriteBMP;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class FourCoreScanController implements Initializable{

    private static final int x_start = 672;
    private static final int y_start = 330;

    @FXML VBox containerVBox;

    @FXML TabPane tabPane;
    @FXML Tab coreCentres;

    @FXML ScatterChart<Number, Number> xGraphDisplay;
    @FXML ScatterChart<Number, Number> yGraphDisplay;

    @FXML Button fileButton;
    @FXML Label coordinateLabel;

    @FXML HBox squareHBox;
    @FXML TextField x1Field;
    @FXML TextField x2Field;
    @FXML TextField y1Field;
    @FXML TextField y2Field;

    @FXML HBox diamondHBox;
    @FXML TextField diamondX1;
    @FXML TextField diamondX2;
    @FXML TextField diamondX3;
    @FXML TextField diamondY1;
    @FXML TextField diamondY2;
    @FXML TextField diamondY3;

    @FXML ChoiceBox<String> shapeSelector;
    @FXML Button setPathButton;
    @FXML Label pathLabel;
    @FXML TextField scanTitle;
    @FXML Button writeButton;

    private PowerMeterData data;
    private ErrorPopup errorPopup;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileButton.setOnAction(event -> getFile());
        writeButton.setOnAction(event -> write());
        setPathButton.setOnAction(event -> setPath());

        pathLabel.setText(WriteBMP.imageDirectory);

        x1Field.textProperty().addListener(checkNumber(x1Field));
        x2Field.textProperty().addListener(checkNumber(x2Field));
        y1Field.textProperty().addListener(checkNumber(y1Field));
        y2Field.textProperty().addListener(checkNumber(y2Field));

        diamondX1.textProperty().addListener(checkNumber(diamondX1));
        diamondX2.textProperty().addListener(checkNumber(diamondX2));
        diamondX3.textProperty().addListener(checkNumber(diamondX3));
        diamondY1.textProperty().addListener(checkNumber(diamondY1));
        diamondY2.textProperty().addListener(checkNumber(diamondY2));
        diamondY3.textProperty().addListener(checkNumber(diamondY3));

        shapeSelector.getItems().addAll("Square","Diamond");
        shapeSelector.getSelectionModel().selectFirst();
        shapeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("Square")){
                squareHBox.setVisible(true);
                diamondHBox.setVisible(false);
            }else if(newValue.equals("Diamond")){
                squareHBox.setVisible(false);
                diamondHBox.setVisible(true);
            }
        });
        Platform.runLater(() -> errorPopup = new ErrorPopup(fileButton.getScene().getWindow()));
    }


    private void getFile(){
        data = ReadFile.getInstance().getMeterData();
        Window.getWindows().get(Window.getWindows().size()-1).requestFocus();
        if(!data.getError()){
            plot();
        }else{
            errorPopup.showMessage("Error in the data - check that it is a ThorLabs dataSet");
        }
    }

    private void plot() {
        ArrayList<Number> rawData = data.getNumberData();
        if(data == null){
            errorPopup.showMessage("No file found");
        }else if(data.getError()){
            errorPopup.showMessage("Error in data - check that it is a ThorLabs data set");
        }else if(rawData.size() <= 0){
            errorPopup.showMessage("No data found in file");
        }else{
            ArrayList<Integer> gradPeaks = Helper.getGradPeaks(rawData,10);
            ArrayList<Number> number = new ArrayList<>();
            for (int i = 0; i < gradPeaks.get(0); i++) {
                number.add(rawData.get(i));
            }

            GraphGenerator graphGenerator = new GraphGenerator();
            SeriesData xData = new SeriesData(number);
            graphGenerator.addData(xData);
            graphGenerator.setTitle("X");
            graphGenerator.writeChart(xGraphDisplay, onClick("X"));

            ArrayList<Number> yNumber = new ArrayList<>();
            for(int i = gradPeaks.get(1) + 1; i < rawData.size(); i++){
                yNumber.add(rawData.get(i));
            }

            graphGenerator = new GraphGenerator();
            graphGenerator.addData(new SeriesData(yNumber));
            graphGenerator.setTitle("Y");
            graphGenerator.writeChart(yGraphDisplay, onClick("Y"));
        }

    }

    private void write(){
        String title = scanTitle.getText();
        if(shapeSelector.getSelectionModel().getSelectedItem().equals("Square")){
            writeSquare(title);
        }else if(shapeSelector.getSelectionModel().getSelectedItem().equals("Diamond")){
            writeDiamond(title);
        }else{
            errorPopup.showMessage("Critical error");
        }
    }

    private void writeSquare(String title){
        int x1_u = Integer.parseInt(x1Field.getText().isEmpty() ? "0" : x1Field.getText());
        int x2_u = Integer.parseInt(x2Field.getText().isEmpty() ? "0" : x2Field.getText());
        int y1_u = Integer.parseInt(y1Field.getText().isEmpty() ? "0" : y1Field.getText());
        int y2_u = Integer.parseInt(y2Field.getText().isEmpty() ? "0" : y2Field.getText());

        if(!isValid(x1_u) || !isValid(x2_u) || !isValid(y1_u) || !isValid(y2_u)){
            errorPopup.showMessage("Field values need to be between 0 and 344");
            return;
        }

        ExitCode e1 = writeImage(title + "_tl_x",x1_u,y1_u,true);
        ExitCode e2 = writeImage(title + "_tl_y",x1_u,y1_u,false);
        ExitCode e3 = writeImage(title + "_tr_x",x2_u,y1_u,true);
        ExitCode e4 = writeImage(title + "_tr_y",x2_u,y1_u,false);
        ExitCode e5 = writeImage(title + "_bl_x",x1_u,y2_u,true);
        ExitCode e6 = writeImage(title + "_bl_y",x1_u,y2_u,false);
        ExitCode e7 = writeImage(title + "_br_x",x2_u,y2_u,true);
        ExitCode e8 = writeImage(title + "_br_y",x2_u,y2_u,false);

        showCompleteDialog(e1,e2,e3,e4,e5,e6,e7,e8);
    }

    private void writeDiamond(String title){
        int x1 = Integer.parseInt(diamondX1.getText().isEmpty() ? "0" : diamondX1.getText());
        int x2 = Integer.parseInt(diamondX2.getText().isEmpty() ? "0" : diamondX2.getText());
        int x3 = Integer.parseInt(diamondX3.getText().isEmpty() ? "0" : diamondX3.getText());
        int y1 = Integer.parseInt(diamondY1.getText().isEmpty() ? "0" : diamondY1.getText());
        int y2 = Integer.parseInt(diamondY2.getText().isEmpty() ? "0" : diamondY2.getText());
        int y3 = Integer.parseInt(diamondY3.getText().isEmpty() ? "0" : diamondY3.getText());

        if(!isValid(x1) || !isValid(x2) || !isValid(x3) || !isValid(y1) || !isValid(y2) || !isValid(y3)){
            errorPopup.showMessage("Field values need to be between 0 and 344");
            return;
        }

        ExitCode e1 = writeImage(title + "_l_x", x1,y2,true);
        ExitCode e2 = writeImage(title + "_l_y", x1,y2,false);
        ExitCode e3 = writeImage(title + "_t_x",x2,y1,true);
        ExitCode e4 = writeImage(title + "_t_y",x2,y1,false);
        ExitCode e5 = writeImage(title + "_b_x",x2,y3,true);
        ExitCode e6 = writeImage(title + "_b_y",x2,y3,false);
        ExitCode e7 = writeImage(title + "_r_x",x3,y2,true);
        ExitCode e8 = writeImage(title + "_r_y",x3,y2,false);

        showCompleteDialog(e1,e2,e3,e4,e5,e6,e7,e8);
    }

    private void showCompleteDialog(ExitCode e1, ExitCode e2, ExitCode e3, ExitCode e4, ExitCode e5, ExitCode e6, ExitCode e7, ExitCode e8){
        if(e1 == Successful && e2 == Successful && e3 == Successful && e4 == Successful &&
                e5 == Successful && e6 == Successful && e7 == Successful && e8 == Successful){
            errorPopup.showMessage("Success!","All images have been created successfully!");
        }else{
            errorPopup.showMessage(String.format(Locale.UK,"Not all the images have been created successfully. " +
                            "Error statuses:\ne1: %s\ne2: %s\ne3: %s\ne4: %s\ne5: %s\ne6: %s\ne7: %s\ne8: %s",
                    e1.toString(),e2.toString(),e3.toString(),e4.toString(),e5.toString(),e6.toString(),e7.toString(),e8.toString()));
        }
    }

    private ExitCode writeImage(String title, int x, int y, boolean forX){
        Iterator iterator = new Iterator(title, DMD_WIDTH, DMD_HEIGHT, x_start + x, y_start + y)
                .setRescaleType(WriteBMP.RescaleType.START).setImages(2);
        if(forX){
            return iterator.setTranslation(2,0)
                    .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add, startX,startY, 2,96))
                    .write();
        }else{
            return iterator.setTranslation(0,2)
                    .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add, startX,startY, 96,2))
                    .write();
        }
    }

    private void setPath(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select directory for the images");
        chooser.setInitialDirectory(new File(WriteBMP.imageDirectory));
        File file = chooser.showDialog(setPathButton.getScene().getWindow());
        if(file !=  null){
            String osPath = "\\";
            String os = System.getProperty("os.name");
            if(os.contains("Mac")){
                osPath = "/";
            }
            WriteBMP.imageDirectory = file.getPath() + osPath;
            pathLabel.setText(WriteBMP.imageDirectory);
        }
        tabPane.getSelectionModel().selectNext();
    }

    private boolean isValid(int a){
        return a >= 0 && a < 344;
    }

    private ChangeListener<String> checkNumber(final TextField textField){
        return (observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                textField.setText(newValue.replaceAll("[^\\d]",""));
            }
        };
    }

    private GraphGenerator.SeriesClicked onClick(final String chartId){
        return (dataId, data) -> {
            String x = data.getXValue().toString();
            int xInt = (int)data.getXValue();
            String y = data.getYValue().toString();
            coordinateLabel.setText(String.format(Locale.UK, "x: %s; y: %s for %s graph. Use x = %d.",x,y,chartId,xInt*2));
        };
    }

}
