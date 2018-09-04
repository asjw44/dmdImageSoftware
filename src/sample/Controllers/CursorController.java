package sample.Controllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class CursorController implements Initializable {

    @FXML private TextArea cursorPositions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(cursorPositions.getText().isEmpty()){
            if(Controller.mousePoints.size()>0){
                cursorPositions.setText(getMousePointString(Controller.mousePoints.get(0)));
            }
            if(Controller.mousePoints.size()>1){
                for (int i = 1; i < Controller.mousePoints.size(); i++) {
                    cursorPositions.setText(String.format(Locale.UK,"%s\n%s",cursorPositions.getText(),getMousePointString(Controller.mousePoints.get(i))));
                }
            }
        }

        final KeyCombination f5 = new KeyCodeCombination(KeyCode.F5);
        final KeyCombination f6 = new KeyCodeCombination(KeyCode.F6);
        final KeyCombination f7 = new KeyCodeCombination(KeyCode.F7);
        final KeyCombination f8 = new KeyCodeCombination(KeyCode.F8);

        Platform.runLater(() -> {
            cursorPositions.getScene().getAccelerators().put(f5,() -> {
                ((Stage) cursorPositions.getScene().getWindow()).close();
                Controller.setCursorWindowOpen(false);
            });
            cursorPositions.getScene().getAccelerators().put(f6,Controller::addMousePoint);
            cursorPositions.getScene().getAccelerators().put(f7,Controller::activateMousePoints);
            cursorPositions.getScene().getAccelerators().put(f8,Controller::removeMousePoints);
        });

        Controller.mousePoints.addListener((ListChangeListener<Point>) c -> {
            while(c.next()){
                if(c.wasAdded()){
                    for(Point point : c.getAddedSubList()){
                        if(cursorPositions.getText().isEmpty()){
                            cursorPositions.setText(getMousePointString(point));
                        }else{
                            cursorPositions.setText(String.format(Locale.UK,"%s\n%s",cursorPositions.getText(),getMousePointString(point)));
                        }
                    }
                }else if (c.wasRemoved()){
                    cursorPositions.setText("");
                }
            }

        });
    }

    private String getMousePointString(Point point){
        return String.format(Locale.UK,"X: %d\tY: %d",point.x,point.y);
    }

}
