package sample.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.Util.Vector2;
import sample.Util.VectorIterator;
import sample.Util.WriteBMP;

import static sample.Util.Constants.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GenerateAttenuatorController implements Initializable{

    @FXML TextField c1x;
    @FXML TextField c1y;
    @FXML TextField c2x;
    @FXML TextField c2y;
    @FXML TextField c3x;
    @FXML TextField c3y;
    @FXML TextField c4x;
    @FXML TextField c4y;

    @FXML Button writeAttenuators;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        c1x.textProperty().addListener(checkNumber(c1x));
        c1y.textProperty().addListener(checkNumber(c1y));
        c2x.textProperty().addListener(checkNumber(c2x));
        c2y.textProperty().addListener(checkNumber(c2y));
        c3x.textProperty().addListener(checkNumber(c3x));
        c3y.textProperty().addListener(checkNumber(c3y));
        c4x.textProperty().addListener(checkNumber(c4x));
        c4y.textProperty().addListener(checkNumber(c4y));

        writeAttenuators.setOnAction(event -> write());

    }

    private ChangeListener<String> checkNumber(final TextField textField){
        return (observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                textField.setText(newValue.replaceAll("[^\\d]",""));
            }
        };
    }

    private void write(){
        int x1 = Integer.parseInt(c1x.getText());
        int y1 = Integer.parseInt(c1y.getText());
        int x2 = Integer.parseInt(c2x.getText());
        int y2 = Integer.parseInt(c2y.getText());
        int x3 = Integer.parseInt(c3x.getText());
        int y3 = Integer.parseInt(c3y.getText());
        int x4 = Integer.parseInt(c4x.getText());
        int y4 = Integer.parseInt(c4y.getText());

        Vector2 core1 = new Vector2(x1,y1);
        Vector2 core2 = new Vector2(x2,y2);
        Vector2 core3 = new Vector2(x3,y3);
        Vector2 core4 = new Vector2(x4,y4);

        ArrayList<Vector2> cores = new ArrayList<>();
        cores.add(core1);
        cores.add(core2);
        cores.add(core3);
        cores.add(core4);

        VectorIterator iterator = new VectorIterator("4c_ attenuator_f2f_3",DMD_WIDTH,DMD_HEIGHT,cores);
        iterator.setImages(3).setdRadius(-2).setCircleRadii(74).setRescaleType(WriteBMP.RescaleType.START).write();
        iterator.writeIndividual();

        VectorIterator iterator2 = new VectorIterator("4c_attenuator_f2f_3r",DMD_WIDTH,DMD_HEIGHT,cores);
        iterator2.setImages(3).setdRadius(-2).setCircleRadii(72).setRescaleType(WriteBMP.RescaleType.START).makeRectangle().write();
        iterator2.writeIndividual();

    }


}
