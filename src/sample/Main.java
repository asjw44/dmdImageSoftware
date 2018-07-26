package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Controllers/sample.fxml"));
        primaryStage.setTitle("DMD Image Creation");
        primaryStage.setScene(new Scene(root, 700, 490));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
