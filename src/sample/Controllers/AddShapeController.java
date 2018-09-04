package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Model.Shapes.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddShapeController implements Initializable {

    @FXML Button addAShapeButton;
    @FXML Button confirmButton;
    @FXML Button deleteAll;

    @FXML VBox shapeFrame;

    private ArrayList<HBox> shapeFrames = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addAShapeButton.setOnAction(event -> addShape());
        deleteAll.setOnAction(event -> setDeleteAll());

        shapeFrame.setSpacing(20);
        shapeFrame.setPadding(new Insets(10));

        if(Controller.staticShapes.size()>0){
            loadShapes(Controller.staticShapes);
        }

        final KeyCombination ctrlW = new KeyCodeCombination(KeyCode.W,KeyCombination.CONTROL_DOWN);
        final KeyCombination insert = new KeyCodeCombination(KeyCode.INSERT);
        final KeyCombination delete = new KeyCodeCombination(KeyCode.DELETE);
        final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);

        Platform.runLater(() ->{
            deleteAll.getScene().getAccelerators().put(ctrlW,()-> ((Stage) addAShapeButton.getScene().getWindow()).close());
            deleteAll.getScene().getAccelerators().put(insert, ()-> addAShapeButton.fire());
            deleteAll.getScene().getAccelerators().put(delete, this::deleteLast);
            deleteAll.getScene().getAccelerators().put(ctrlS, () -> confirmButton.fire());
        });

    }

    private void loadShapes(ArrayList<AbstractShape> shapes){
        for(AbstractShape shape : shapes){
            addShape();

            HBox shapeFrame = shapeFrames.get(shapeFrames.size()-1);

            if(shapeFrame.getChildren().get(1).getClass() == ChoiceBox.class){
                ChoiceBox choiceBox = (ChoiceBox) shapeFrame.getChildren().get(1);

                switch (shape.getShapeName()){
                    case "Rectangle":
                        choiceBox.getSelectionModel().selectFirst();
                        break;
                    case "Ellipse":
                        choiceBox.getSelectionModel().select(1);
                        break;
                    case "Doughnut":
                        choiceBox.getSelectionModel().select(2);
                }
            }

            if(shapeFrame.getChildren().get(2).getClass() == VBox.class) {
                VBox rows = ((VBox) shapeFrame.getChildren().get(2));

                if(rows.getChildren().get(0).getClass() == HBox.class){
                    HBox location = (HBox) rows.getChildren().get(0);

                    TextField x = (TextField) location.getChildren().get(1);
                    TextField y = (TextField) location.getChildren().get(2);
                    x.setText(Integer.toString(shape.getStartX()));
                    y.setText(Integer.toString(shape.getStartY()));
                }

                if(rows.getChildren().get(1).getClass() == HBox.class){
                    HBox size = (HBox) rows.getChildren().get(1);

                    TextField w = (TextField) size.getChildren().get(1);
                    TextField h = (TextField) size.getChildren().get(2);
                    w.setText(Integer.toString(shape.getWidth()));
                    h.setText(Integer.toString(shape.getHeight()));
                }

                if(shape.getShapeName().equals("Doughnut")){
                    HBox rad = (HBox) rows.getChildren().get(2);

                    TextField dR = (TextField) rad.getChildren().get(1);
                    dR.setText(Integer.toString(((Doughnut) shape).getOffset()));
                }

            }

        }
    }

    private void addShape(){

        int textFieldWidth = 88;
        int textFieldHeight = 25;

        int labelWidth = 75;

        HBox hBox = new HBox(15); //HBox(double spacing)

        VBox shapeProperties = new VBox(5);

        Label shapeLabel = new Label("Shape");
        shapeLabel.setFont(new Font(15));

        ChoiceBox<String> shapeSelector = new ChoiceBox<>();
        shapeSelector.getItems().addAll(AbstractShape.shapes);
        shapeSelector.getSelectionModel().selectFirst();
        shapeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("Doughnut")){
                HBox doughnutExtra = new HBox(5);

                Label doughnutDExtra = new Label("dLabel");
                doughnutDExtra.setPrefWidth(labelWidth);
                doughnutDExtra.setFont(new Font(15));

                TextField doughnutOffset = new TextField();
                doughnutOffset.setPromptText("Delta radius");
                doughnutOffset.setPrefWidth(textFieldWidth);

                doughnutExtra.getChildren().add(doughnutDExtra);
                doughnutExtra.getChildren().add(doughnutOffset);

                shapeProperties.getChildren().add(doughnutExtra);
            }else{
                if(shapeProperties.getChildren().size() == 3){
                    shapeProperties.getChildren().remove(2);
                }
            }
        });

        //-----------------------------------------

        HBox shapeLocFrame = new HBox(5);

        Label shapeLoc = new Label("Shape loc");
        shapeLoc.setPrefWidth(labelWidth);
        shapeLoc.setFont(new Font(15));

        TextField shapeLocX = new TextField();
        shapeLocX.setPromptText("X");
        shapeLocX.setPrefWidth(textFieldWidth);
        shapeLocX.setPrefHeight(textFieldHeight);

        TextField shapeLocY = new TextField();
        shapeLocY.setPromptText("Y");
        shapeLocY.setPrefWidth(textFieldWidth);
        shapeLocY.setPrefHeight(textFieldHeight);

        Button delete = new Button("Delete");
        delete.setOnAction(event -> {
            for (int i = 0; i < shapeFrames.size(); i++) {
                HBox frame = shapeFrames.get(i);
                Label label = (Label) frame.getChildren().get(0);
                if(label.hashCode() == shapeLabel.hashCode()){
                    shapeFrame.getChildren().remove(i);
                    shapeFrames.remove(i);
                }
            }
        });

        shapeLocFrame.getChildren().add(shapeLoc);
        shapeLocFrame.getChildren().add(shapeLocX);
        shapeLocFrame.getChildren().add(shapeLocY);
        shapeLocFrame.getChildren().add(delete);

        //-----------------------------------------

        HBox shapeSizeFrame = new HBox(5);

        Label shapeSize = new Label("Shape size");
        shapeSize.setPrefWidth(labelWidth);
        shapeSize.setFont(new Font(15));

        TextField shapeSizeX = new TextField();
        shapeSizeX.setPromptText("Width");
        shapeSizeX.setPrefWidth(textFieldWidth);
        shapeSizeX.setPrefHeight(textFieldHeight);

        TextField shapeSizeY = new TextField();
        shapeSizeY.setPromptText("Height");
        shapeSizeY.setPrefWidth(textFieldWidth);
        shapeSizeY.setPrefHeight(textFieldHeight);

        shapeSizeFrame.getChildren().add(shapeSize);
        shapeSizeFrame.getChildren().add(shapeSizeX);
        shapeSizeFrame.getChildren().add(shapeSizeY);

        //-----------------------------------------

        shapeProperties.getChildren().add(shapeLocFrame);
        shapeProperties.getChildren().add(shapeSizeFrame);

        hBox.getChildren().add(shapeLabel);
        hBox.getChildren().add(shapeSelector);
        hBox.getChildren().add(shapeProperties);

        shapeFrame.getChildren().add(hBox);
        shapeFrames.add(hBox);

    }

    @FXML
    public void confirm(){

        boolean ok = true;

        ArrayList<AbstractShape> shapes = new ArrayList<>();

        for(HBox hBox : shapeFrames){

            String shape = "";
            int x = 0;
            int y = 0;
            int w = 0;
            int h = 0;
            int dR = 0;

            if(hBox.getChildren().get(1).getClass() == ChoiceBox.class){
                shape = ((ChoiceBox) hBox.getChildren().get(1)).getValue().toString();
            }

            try {
                if(hBox.getChildren().get(2).getClass() == VBox.class){
                    VBox rows = ((VBox) hBox.getChildren().get(2));

                    if(rows.getChildren().get(0).getClass() == HBox.class){
                        HBox location = (HBox) rows.getChildren().get(0);
                        x = Integer.parseInt(((TextField) location.getChildren().get(1)).getText());
                        y = Integer.parseInt(((TextField) location.getChildren().get(2)).getText());
                    }

                    if(rows.getChildren().get(1).getClass() == HBox.class){
                        HBox size = (HBox) rows.getChildren().get(1);
                        w = Integer.parseInt(((TextField) size.getChildren().get(1)).getText());
                        h = Integer.parseInt(((TextField) size.getChildren().get(2)).getText());
                    }

                    if(shape.toLowerCase().equals("doughnut")){
                        if(rows.getChildren().get(2).getClass() == HBox.class){
                            HBox rad = (HBox) rows.getChildren().get(2);
                            dR = Integer.parseInt(((TextField) rad.getChildren().get(1)).getText());
                        }
                    }
                }

                AbstractShape addShape;

                switch (shape.toLowerCase()){
                    case "rectangle":
                        addShape = new Rectangle(new RGB(255),RGB.OverlapType.Add,x,y,w,h);
                        break;
                    case "ellipse":
                        addShape = new Ellipse(new RGB(255),RGB.OverlapType.Add,x,y,w,h);
                        break;
                    case "doughnut":
                        addShape = new Doughnut(new RGB(255),RGB.OverlapType.Add,x,y,w,h,dR);
                        break;
                    default:
                        addShape = new Rectangle(new RGB(255),RGB.OverlapType.Add,0,0,0,0);
                }
                shapes.add(addShape);

            } catch (NumberFormatException e) {
                Alert numberError = new Alert(Alert.AlertType.ERROR);
                numberError.setTitle("Fill in all the fields");
                numberError.setHeaderText(null);
                numberError.setContentText("Please either fill in all the fields or delete the offending shape");
                numberError.showAndWait();
                ok = false;
                break;
            }
        }

        if (ok) {
            for(AbstractShape shape: shapes){
                System.out.println(shape.toString());
            }

            Controller.staticShapes = shapes;
            Controller.setStaticShapesOpen(false);

            Stage stage = (Stage) addAShapeButton.getScene().getWindow();
            stage.close();
        }

    }

    private void deleteLast(){
        int i = shapeFrames.size() - 1;
        if (i>=0) {
            shapeFrames.remove(i);
            shapeFrame.getChildren().remove(i);
        }
    }

    private void setDeleteAll(){
        if(shapeFrame.getChildren().size() > 0){
            Alert areYouSure = new Alert(Alert.AlertType.CONFIRMATION);
            areYouSure.setTitle("Are you sure");
            areYouSure.setHeaderText(null);
            areYouSure.setContentText("Are you sure you want to delete all the shapes?");
            Optional<ButtonType> result = areYouSure.showAndWait();
            if(result.get() == ButtonType.OK){
                shapeFrame.getChildren().clear();
                shapeFrames.clear();
            }
        }

    }

}
