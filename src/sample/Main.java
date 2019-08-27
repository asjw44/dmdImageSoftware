package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Canvas;
import sample.Movie.*;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.SpreadFill;
import sample.Util.*;

import java.io.IOException;
import java.util.Locale;


public class Main extends Application {

    public static int width = Constants.DMD_WIDTH;
    public static int height = Constants.DMD_HEIGHT;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Controllers/sample.fxml"));
        primaryStage.setTitle("DMD Image Creation");
        primaryStage.setScene(new Scene(root, 700, 540));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();

        int width = Constants.DMD_WIDTH;
        int height = Constants.DMD_HEIGHT;
        final int constant = 220;

        int squareWidth = 250;
        int squareHeight = 250;
        int fps = 10;

        WriteBMP.RescaleType rescaleType = WriteBMP.RescaleType.START;

        int startX = width/2 - squareWidth/2;
        int startY = height/2 - squareHeight/2;

        /*Canvas canvas = new Canvas("Block_scan_8080_rectangle_short");
        Rectangle rectangle = new Rectangle(RGB.white(), RGB.OverlapType.Add, startX, startY, squareWidth, 126);
        canvas.addShape(rectangle);
        if(canvas.drawShapes()){
            WriteBMP.getInstance().printBMP(canvas, rescaleType);
        }*/



        MovieData movieData = new MovieData(squareWidth + "x" + squareHeight + "_" + fps + "fps_w_allOn.mp4", false);
        movieData.rescaleType = WriteBMP.RescaleType.START;
        movieData.fps = fps;

        StaticIterationModel staticIterationModel = new StaticIterationModel();
        staticIterationModel.start_x = startX;
        staticIterationModel.start_y = startY;
        staticIterationModel.width = squareWidth;
        staticIterationModel.height = squareHeight;
        staticIterationModel.timeOnScreen = 2;

        ScanStaticIterationModel model = new ScanStaticIterationModel();
        model.start_x = startX;
        model.start_y = startY;
        model.iterationI = squareWidth/2;
        model.iterationJ = squareHeight/2;
        model.pauseTime = 0.5;

        movieData.setAbstractIterationModels(staticIterationModel,model,staticIterationModel);

        MovieGenerator.getInstance().generateMovie(movieData);


        System.out.println("\nMain class code complete.");

    }

    private static void generate(double fillFactor, int seed, int square_size, WriteBMP.RescaleType rescaleType){
        SpreadFill spreadFill = new SpreadFill(RGB.white(), RGB.OverlapType.Add, 300,200,1350,850,fillFactor);
        spreadFill.setSeed(seed);
        spreadFill.setSpreadType(SpreadFill.SpreadType.RandomBlock);
        spreadFill.setSquareSize(square_size);
        spreadFill.setRescaleType(rescaleType);

        Canvas spreadCanvas = new Canvas(String.format(Locale.UK,"spreadBlock_%.1f_seed_%d_square_%s",fillFactor,seed,square_size),width,height);
        spreadCanvas.addShape(spreadFill);
        printCanvas(spreadCanvas,rescaleType);
    }

    private static void printCanvas(Canvas c, WriteBMP.RescaleType rescaleType){
        if(c.drawShapes()){
            try{
                WriteBMP.getInstance().printBMP(c,rescaleType);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Drawing image error");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
