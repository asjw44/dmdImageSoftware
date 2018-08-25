package sample.Controllers;

import com.thoughtworks.xstream.XStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Model.SaveData;
import sample.Model.Shapes.*;
import sample.Model.Shapes.Rectangle;
import sample.Util.Constants;
import sample.Util.Iterator;
import sample.Util.Hint;
import sample.Util.WriteBMP;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    static ArrayList<AbstractShape> staticShapes = new ArrayList<>();

    private final FileChooser fileDialog = new FileChooser();

    private Path currentFilePath;

    //MenuBar

    @FXML private MenuItem menuFileNew;
    @FXML private MenuItem menuFileOpen;
    @FXML private MenuItem menuFileSave;
    @FXML private MenuItem menuFileSaveAs;
    @FXML private MenuItem menuFileExit;

    @FXML private MenuItem menuEditCut;
    @FXML private MenuItem menuEditCopy;
    @FXML private MenuItem menuEditPaste;
    @FXML private MenuItem menuEditDelete;

    @FXML private MenuItem menuHelpAbout;

    //Interface

    @FXML private TextField baseNameTextField;
    @FXML private TextField canvasSizeWidth;
    @FXML private TextField canvasSizeHeight;
    @FXML private CheckBox useDMDCheckbox;
    @FXML private Button addStaticShapes;

    @FXML private ChoiceBox<String> selectShape;
    @FXML private TextField shapeSizeWidth;
    @FXML private TextField shapeSizeHeight;
    @FXML private TextField shapeLocX;
    @FXML private TextField shapeLocY;
    @FXML private TextField doughnutOffset;
    @FXML private Label doughnutOffsetLabel;

    @FXML private TextField numOfImages;
    @FXML private TextField translateX;
    @FXML private TextField translateY;
    @FXML private TextField dWidth;
    @FXML private TextField dHeight;

    @FXML private CheckBox translateXCheckbox;
    @FXML private CheckBox translateYCheckbox;
    @FXML private CheckBox dWidthCheckbox;
    @FXML private CheckBox dHeightCheckbox;
    @FXML private CheckBox centerShapes;
    @FXML private CheckBox deployDMDCheckbox;
    @FXML private Label rescaleTypeLabel;
    @FXML private ChoiceBox<String> rescaleTypePicker;

    @FXML private Label imagePath;
    @FXML private Button imagePathButton;

    @FXML private Button writeButton;

    private ArrayList<Point> mouserPoints = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML","*.xml"),
                new FileChooser.ExtensionFilter("All files","*.*")
        );

        String os = System.getProperty("os.name");
        if(os.contains("Mac")){
            WriteBMP.imageDirectory = System.getProperty("user.dir") + "/";
        }

        fileDialog.setInitialDirectory(new File(WriteBMP.imageDirectory));

        //***************************************************************************
        //*                                                                         *
        //* Buttons                                                                 *
        //*                                                                         *
        //***************************************************************************

        //File
        menuFileNew.setOnAction(event -> resetGUI());
        menuFileOpen.setOnAction(event -> open());
        menuFileSave.setOnAction(event -> save());
        menuFileSaveAs.setOnAction(event -> saveAs());
        menuFileExit.setOnAction(event -> exit());

        //Edit
        menuEditCut.setOnAction(event -> cut());
        menuEditCopy.setOnAction(event -> copy());
        menuEditPaste.setOnAction(event -> paste());
        menuEditDelete.setOnAction(event -> delete());

        //Help
        menuHelpAbout.setOnAction(event -> help());

        //Interface
        addStaticShapes.setOnAction(event -> displayAddWindows());
        writeButton.setOnAction((event -> write()));

        //***************************************************************************
        //*                                                                         *
        //* Other UI components                                                     *
        //*                                                                         *
        //***************************************************************************

        //ChoiceBox
        selectShape.getItems().addAll(AbstractShape.shapes);
        selectShape.getSelectionModel().selectFirst();
        selectShape.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean show = newValue.equals("Doughnut");
            doughnutOffset.setVisible(show);
            doughnutOffsetLabel.setVisible(show);
        });

        rescaleTypePicker.getItems().addAll("Start", "End", "Average");
        rescaleTypePicker.getSelectionModel().selectFirst();

        //CheckBoxes
        useDMDCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            canvasSizeWidth.setEditable(!newValue);
            canvasSizeHeight.setEditable(!newValue);
            if(newValue){
                canvasSizeWidth.setText(Integer.toString(Constants.DMD_WIDTH));
                canvasSizeHeight.setText(Integer.toString(Constants.DMD_HEIGHT));
            }
        });

        centerShapes.selectedProperty().addListener((observable, oldValue, newValue) -> {
            translateX.setEditable(!newValue);
            translateY.setEditable(!newValue);
            if(newValue){
                translateX.setText("0");
                translateY.setText("0");
            }
        });

        deployDMDCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            rescaleTypeLabel.setVisible(newValue);
            rescaleTypePicker.setVisible(newValue);
        });

        imagePath.setText("Image path: " + WriteBMP.imageDirectory);

        imagePathButton.setOnAction(event -> setImagePath());

        //***************************************************************************
        //*                                                                         *
        //* Tooltips                                                                *
        //*                                                                         *
        //***************************************************************************

        baseNameTextField.setTooltip(Hint.getTooltip(Hint.ITERATION_BASE_NAME));
        canvasSizeWidth.setTooltip(Hint.getTooltip(Hint.ITERATION_CANVAS_SIZE));
        canvasSizeHeight.setTooltip(Hint.getTooltip(Hint.ITERATION_CANVAS_SIZE));
        useDMDCheckbox.setTooltip(Hint.getTooltip(Hint.ITERATION_USE_DMD));
        addStaticShapes.setTooltip(Hint.getTooltip(Hint.ITERATION_STATIC_SHAPES));

        selectShape.setTooltip(Hint.getTooltip(Hint.ITERATION_SHAPE));
        shapeLocX.setTooltip(Hint.getTooltip(Hint.ITERATION_SHAPE_LOC));
        shapeLocY.setTooltip(Hint.getTooltip(Hint.ITERATION_SHAPE_LOC));
        shapeSizeWidth.setTooltip(Hint.getTooltip(Hint.ITERATION_SHAPE_SIZE));
        shapeSizeHeight.setTooltip(Hint.getTooltip(Hint.ITERATION_SHAPE_SIZE));
        doughnutOffset.setTooltip(Hint.getTooltip(Hint.ITERATION_DOUGHNUT));

        numOfImages.setTooltip(Hint.getTooltip(Hint.ITERATION_NUM_IMAGES));
        translateX.setTooltip(Hint.getTooltip(Hint.ITERATION_TRANSLATE_X));
        translateY.setTooltip(Hint.getTooltip(Hint.ITERATION_TRANSLATE_Y));
        dWidth.setTooltip(Hint.getTooltip(Hint.ITERATION_dWIDTH));
        dHeight.setTooltip(Hint.getTooltip(Hint.ITERATION_dHEIGHT));

        centerShapes.setTooltip(Hint.getTooltip(Hint.ITERATION_CENTER_SHAPES));
        deployDMDCheckbox.setTooltip(Hint.getTooltip(Hint.ITERATION_DEPLOY));
        rescaleTypePicker.setTooltip(Hint.getTooltip(Hint.ITERATION_DEPLOY_TYPE));

        //***************************************************************************
        //*                                                                         *
        //* Other                                                                   *
        //*                                                                         *
        //***************************************************************************

        //Prevent entering any non number values - can only take integers
        canvasSizeWidth.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(canvasSizeWidth,newValue)));
        canvasSizeHeight.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(canvasSizeHeight,newValue)));
        shapeSizeWidth.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(shapeSizeWidth,newValue)));
        shapeSizeHeight.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(shapeSizeHeight,newValue)));
        shapeLocX.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(shapeLocX,newValue)));
        shapeLocY.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(shapeLocY,newValue)));
        doughnutOffset.textProperty().addListener((observable, oldValue, newValue) -> checkNumber(doughnutOffset,newValue));
        numOfImages.textProperty().addListener(((observable, oldValue, newValue) -> checkNumber(numOfImages,newValue)));
        translateX.textProperty().addListener((observable, oldValue, newValue) -> checkNumber(translateX,newValue));
        translateY.textProperty().addListener((observable, oldValue, newValue) -> checkNumber(translateY,newValue));
        dWidth.textProperty().addListener((observable, oldValue, newValue) -> checkNumber(dWidth,newValue));
        dHeight.textProperty().addListener((observable, oldValue, newValue) -> checkNumber(dHeight,newValue));


        final KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrlShiftS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN);
        final KeyCombination ctrlO = new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrlR = new KeyCodeCombination(KeyCode.R,KeyCombination.CONTROL_DOWN);
        final KeyCombination ctrlD  = new KeyCodeCombination(KeyCode.D,KeyCombination.CONTROL_DOWN);
        final KeyCombination f6 = new KeyCodeCombination(KeyCode.F6);
        final KeyCombination shiftF6 = new KeyCodeCombination(KeyCode.F6,KeyCombination.SHIFT_DOWN);

        menuFileNew.setAccelerator(ctrlN);
        menuFileSave.setAccelerator(ctrlS);
        menuFileSaveAs.setAccelerator(ctrlShiftS);
        menuFileOpen.setAccelerator(ctrlO);

        Platform.runLater(() -> {
            writeButton.getScene().getAccelerators().put(ctrlR,()-> writeButton.fire());
            writeButton.getScene().getAccelerators().put(ctrlD, () -> addStaticShapes.fire());
            writeButton.getScene().getAccelerators().put(f6, this::addMousePoint);
            writeButton.getScene().getAccelerators().put(shiftF6, this::activateMousePoints);
        });

    }

    private void checkNumber(TextField textField, String newValue){
        if(!newValue.matches("\\d*")){
            textField.setText(newValue.replaceAll("[^\\d]",""));
        }
    }

    /**
     * Write creates the images. It first sets the scale type, then it used the <code>Iterator</code> builder to create the images.
     * This method returns an enum whose value determines whether the image creation was successful or not.
     * If it wasn't successful, it will display an alert saying what went wrong.
     */

    private void write(){
        String name = baseNameTextField.getText();

        WriteBMP.RescaleType rescaleType = WriteBMP.RescaleType.NONE;
        if(deployDMDCheckbox.isSelected()){
            switch (rescaleTypePicker.getValue()){
                case "Start":
                    rescaleType = WriteBMP.RescaleType.START;
                    break;
                case "End":
                    rescaleType = WriteBMP.RescaleType.END;
                    break;
                case "Average":
                    rescaleType = WriteBMP.RescaleType.AVERAGE;
                    break;
                default:
                    rescaleType = WriteBMP.RescaleType.NONE;
            }
        }

        if(!name.isEmpty()){
            Iterator.ExitCode code;
            try {
                code = new Iterator(name, Integer.parseInt(canvasSizeWidth.getText()),Integer.parseInt(canvasSizeHeight.getText()),Integer.parseInt(shapeLocX.getText()),Integer.parseInt(shapeLocY.getText()))
                .setImages(Integer.parseInt(numOfImages.getText()))
                .setTranslation(
                        translateXCheckbox.isSelected() ? -1 * Integer.parseInt(translateX.getText()) : Integer.parseInt(translateX.getText()),
                        translateYCheckbox.isSelected() ? -1 * Integer.parseInt(translateY.getText()) : Integer.parseInt(translateY.getText())
                ).setHeightChange(
                        dHeightCheckbox.isSelected() ? -1 * Integer.parseInt(dHeight.getText()) : Integer.parseInt(dHeight.getText())
                ).setWidthChange(
                        dWidthCheckbox.isSelected() ? -1 * Integer.parseInt(dWidth.getText()) : Integer.parseInt(dWidth.getText())
                ).centerShape(centerShapes.isSelected())
                .showShapeInfo()
                .setRescaleType(rescaleType)
                .setStaticShapes(staticShapes)
                .setShapeIterator((colour, startX, startY) -> {
                    switch (selectShape.getValue()) {
                        case "Rectangle":
                            return new Rectangle(colour, RGB.OverlapType.Add, startX, startY, Integer.parseInt(shapeSizeWidth.getText()), Integer.parseInt(shapeSizeHeight.getText()));
                        case "Ellipse":
                            return new Ellipse(colour, RGB.OverlapType.Add, startX, startY, Integer.parseInt(shapeSizeWidth.getText()), Integer.parseInt(shapeSizeHeight.getText()));
                        case "Doughnut":
                            return new Doughnut(colour, RGB.OverlapType.Add, startX, startY, Integer.parseInt(shapeSizeWidth.getText()), Integer.parseInt(shapeSizeHeight.getText()), Integer.parseInt(doughnutOffset.getText()));
                        default:
                            return null;
                    }
                }).write();
            } catch (NumberFormatException e) {
                code = Iterator.ExitCode.NullEntryError;
            }

            switch (code){
                case Successful:
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success!");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The images have been created successfully");
                    successAlert.showAndWait();
                    break;

                case PerimeterError:
                    Alert perimeterAlert = new Alert(Alert.AlertType.ERROR);
                    perimeterAlert.setTitle("Error!");
                    perimeterAlert.setHeaderText("Out of perimeter error");
                    perimeterAlert.setContentText("A shape exits out of the perimeter of the image. Please revise the parameters to prevent this from happening");
                    perimeterAlert.showAndWait();
                    break;

                case PrintError:
                    Alert printError = new Alert(Alert.AlertType.ERROR);
                    printError.setTitle("Error!");
                    printError.setHeaderText("Error printing the images to a bitmap");
                    printError.setContentText("There was an error when converting the image to a bitmap.");
                    printError.showAndWait();
                    break;

                case NullEntryError:
                    Alert nullError = new Alert(Alert.AlertType.ERROR);
                    nullError.setTitle("Error!");
                    nullError.setHeaderText(null);
                    nullError.setContentText("Please fill in all the fields");
                    nullError.showAndWait();
            }

        }

    }

    private void displayAddWindows(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("addAShape.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add a shape");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error showing the window");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void setImagePath(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the image save location");
        directoryChooser.setInitialDirectory(new File(WriteBMP.imageDirectory));
        File dir = directoryChooser.showDialog(null);
        if(dir != null){
            String osPath = "\\";
            String os = System.getProperty("os.name");
            if(os.contains("Mac")){
                osPath = "/";
            }
            WriteBMP.imageDirectory = dir.getPath() + osPath;
            imagePath.setText("Image path: " + WriteBMP.imageDirectory);
        }
    }

    //=============================File Menu===========================

    private void resetGUI(){
        baseNameTextField.setText("Untitled");
        canvasSizeWidth.setText("");
        canvasSizeHeight.setText("");
        useDMDCheckbox.setSelected(false);
        staticShapes.clear();

        selectShape.getSelectionModel().selectFirst();
        shapeLocX.setText("");
        shapeLocY.setText("");
        shapeSizeWidth.setText("");
        shapeSizeHeight.setText("");

        numOfImages.setText("1");
        translateX.setText("0");
        translateY.setText("0");
        dWidth.setText("0");
        dHeight.setText("0");

        translateXCheckbox.setSelected(false);
        translateYCheckbox.setSelected(false);
        dWidthCheckbox.setSelected(false);
        dHeightCheckbox.setSelected(false);
        centerShapes.setSelected(false);
        deployDMDCheckbox.setSelected(false);
    }

    private void open(){
        fileDialog.setTitle("Open iteration set up");
        File file = fileDialog.showOpenDialog(writeButton.getScene().getWindow());
        if(file == null){
            return;
        }

        String data;

        try{
            byte[] dataStream = Files.readAllBytes(file.toPath());
            data = new String(dataStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        XStream xStream = new XStream();
        xStream.alias("rectangle",Rectangle.class);
        xStream.alias("ellipse",Ellipse.class);
        xStream.alias("doughnut",Doughnut.class);
        xStream.alias("rgb",RGB.class);
        xStream.alias("data",SaveData.class);
        SaveData load = (SaveData) xStream.fromXML(data);

        if (load.getId().equals("0.1.1")) {
            currentFilePath = file.toPath();

            //Initial setup
            baseNameTextField.setText(load.getBaseName());
            setTextField(canvasSizeWidth,load.getCanvasSizeWidth());
            setTextField(canvasSizeHeight,load.getCanvasSizeHeight());

            useDMDCheckbox.setSelected(load.isUseDMDCheckbox());
            staticShapes = load.getStaticShapes();

            //Iteration shape
            selectShape.getSelectionModel().select(load.getShape());
            setTextField(shapeLocX,load.getShapeLocX());
            setTextField(shapeLocY,load.getShapeLocY());
            setTextField(shapeSizeWidth,load.getShapeSizeWidth());
            setTextField(shapeSizeHeight,load.getShapeSizeHeight());
            setTextField(doughnutOffset,load.getDoughnutOffset());

            //Iteration
            setTextField(numOfImages, load.getNumOfImages());
            setTextField(translateX, load.getTranslateX());
            setTextField(translateY, load.getTranslateY());
            setTextField(dWidth, load.getdWidth());
            setTextField(dHeight, load.getdHeight());

            translateXCheckbox.setSelected(load.isTranslateXCheckbox());
            translateYCheckbox.setSelected(load.isTranslateYCheckbox());
            dWidthCheckbox.setSelected(load.isdWidthCheckbox());
            dHeightCheckbox.setSelected(load.isdHeightCheckbox());

            centerShapes.setSelected(load.isCenterShapes());
            deployDMDCheckbox.setSelected(load.isDeployDMDCheckbox());
            rescaleTypePicker.getSelectionModel().select(load.getRescaleTypePicker());
            if(load.getImagePath() != null){
                File testPath = new File(load.getImagePath());
                if (Files.exists(testPath.toPath())) {
                    WriteBMP.imageDirectory = load.getImagePath();
                    imagePath.setText("Image path: " + WriteBMP.imageDirectory);
                }
            }
        }



    }

    private void save(){
        if(currentFilePath == null){
            saveAs();
        }else{
            try{
                String xml = getSaveStateXML();
                Files.write(currentFilePath,xml.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cannot save file");
                alert.setHeaderText(null);
                alert.setContentText("There was an error while saving the file.");
            }
        }
    }

    private void saveAs(){
        currentFilePath = null;
        String xml = getSaveStateXML();

        fileDialog.setTitle("Save as");
        fileDialog.setInitialFileName(String.format("%s.xml",baseNameTextField.getText()));
        File file = fileDialog.showSaveDialog(writeButton.getScene().getWindow());
        if( file == null){
            return;
        }
        try {
            Files.write(file.toPath(), xml.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot save file");
            alert.setHeaderText(null);
            alert.setContentText("There was an error while saving the file.");
        }
    }

    /**
     * This gets all the fields on the UI and stores them in a class (<code>SaveData</code>).
     * Integers boxes that have no input (ie "") are set as "-1" using <code>setTextField</code>.
     * The code loading the data then knows that this has no input
     * It then uses XStream to parse this data into an xml string, along with the alias' for the classes set below.
     *
     * @return The xml string of the <code>SaveData</code> class.
     */

    private String getSaveStateXML(){
        SaveData save = new SaveData();

        //Initial set up
        save.setBaseName(baseNameTextField.getText());
        save.setCanvasSizeWidth(getInt(canvasSizeWidth));
        save.setCanvasSizeHeight(getInt(canvasSizeHeight));
        save.setUseDMDCheckbox(useDMDCheckbox.isSelected());
        save.setStaticShapes(staticShapes);

        //Iteration shape
        save.setShape(selectShape.getValue());
        save.setShapeLocX(getInt(shapeLocX));
        save.setShapeLocY(getInt(shapeLocY));
        save.setShapeSizeWidth(getInt(shapeSizeWidth));
        save.setShapeSizeHeight(getInt(shapeSizeHeight));
        save.setDoughnutOffset(getInt(doughnutOffset));

        //Iteration
        save.setNumOfImages(getInt(numOfImages));
        save.setTranslateX(getInt2(translateX));
        save.setTranslateY(getInt2(translateY));
        save.setdWidth(getInt2(dWidth));
        save.setdHeight(getInt2(dHeight));

        save.setTranslateXCheckbox(translateXCheckbox.isSelected());
        save.setTranslateYCheckbox(translateYCheckbox.isSelected());
        save.setdWidthCheckbox(dWidthCheckbox.isSelected());
        save.setdHeightCheckbox(dHeightCheckbox.isSelected());

        save.setCenterShapes(centerShapes.isSelected());
        save.setDeployDMDCheckbox(deployDMDCheckbox.isSelected());
        save.setRescaleTypePicker(rescaleTypePicker.getValue());
        save.setImagePath(WriteBMP.imageDirectory);

        XStream xStream = new XStream();
        xStream.alias("rectangle",Rectangle.class);
        xStream.alias("ellipse",Ellipse.class);
        xStream.alias("doughnut",Doughnut.class);
        xStream.alias("rgb",RGB.class);
        xStream.alias("data",SaveData.class);
        return xStream.toXML(save);

    }

    private int getInt(TextField textField){
        return Integer.parseInt(textField.getText().equals("") ? "-1" : textField.getText());
    }

    private int getInt2(TextField textField){
        return Integer.parseInt(textField.getText().equals("") ? "0" : textField.getText());
    }

    private void setTextField(TextField textField, int number){
        textField.setText(number > -1 ? Integer.toString(number) : "");
    }

    private void exit(){
        Platform.exit();
    }

    //=============================Edit Menu===========================

    private void cut(){
        if(writeButton.getScene().focusOwnerProperty().get() instanceof TextField){
            ((TextField) writeButton.getScene().focusOwnerProperty().get()).cut();
        }
    }

    private void copy(){
        if(writeButton.getScene().focusOwnerProperty().get() instanceof TextField){
            ((TextField) writeButton.getScene().focusOwnerProperty().get()).copy();
        }
    }

    private void paste(){
        if(writeButton.getScene().focusOwnerProperty().get() instanceof TextField){
            ((TextField) writeButton.getScene().focusOwnerProperty().get()).paste();
        }
    }

    private void delete(){
        if(writeButton.getScene().focusOwnerProperty().get() instanceof TextField){
            TextField textField = (TextField) writeButton.getScene().focusOwnerProperty().get();
            IndexRange range = textField.getSelection();
            textField.deleteText(range.getStart(),range.getEnd());
        }
    }

    //=============================Help Menu===========================

    private void help(){
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About");
        about.setHeaderText("DMD Image Software\nCreated by Andrew Wood. Copyright 2018.\n\n" +
                "Created for use at the Optoelectronics Research Centre,\nSouthampton University");
        about.setContentText("Version:\t0.1.2\n\nLibraries used:\nxStream 1.4.10");
        about.showAndWait();
    }

    //=============================Mouse================================

    private void addMousePoint(){
        mouserPoints.add(MouseInfo.getPointerInfo().getLocation());
    }

    private void activateMousePoints(){
        for(Point point : mouserPoints){
            try{
                Robot robot = new Robot();
                robot.mouseMove(point.x,point.y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }catch (AWTException e){
                e.printStackTrace();
            }

        }
    }

}
