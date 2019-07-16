package sample.Controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

class ErrorPopup extends Popup {

    private String message;
    private Window window;

    ErrorPopup(Window window){
        this.window = window;
    }

    void showMessage(String errorMessage){
        showMessage("Error!",errorMessage);
    }

    void showMessage(String title, String message){
        this.message = message;
        if(getContent().size() > 0) {
            for(int i = 0; i < getContent().size(); i++){
                getContent().remove(0);
            }
        }iShowMessage(title);
    }

    private void iShowMessage(String titleString){
        VBox container = new VBox();
        container.setStyle("-fx-background-color: #EEE; -fx-padding: 20;");
        container.setSpacing(10);

        Label title = new Label(titleString);
        title.setStyle("-fx-font-size: 3em");

        Label errorLabel = new Label(message);
        title.setStyle("-fx-font-size: 2em");

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10,10,0,10));

        Button errorButton = new Button("OK");
        errorButton.setPrefWidth(100);
        errorButton.setOnMouseClicked((event -> hide()));
        buttonBox.getChildren().add(errorButton);

        container.getChildren().addAll(title,errorLabel,buttonBox);

        getContent().addAll(container);

        show(window);
    }

}
