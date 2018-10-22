package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import sample.Model.Shapes.AbstractShape;
import sample.Model.Shapes.Ellipse;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.SpreadFill;
import sample.Util.Constants;
import sample.Util.Iterator;
import sample.Util.WriteBMP;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("Controllers/sample.fxml"));
        primaryStage.setTitle("DMD Image Creation");
        primaryStage.setScene(new Scene(root, 700, 540));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();



        Iterator iteratorBlocks = new Iterator("doubles_20_block_1_f",Constants.DMD_WIDTH,Constants.DMD_HEIGHT,672,330);
        iteratorBlocks.setShapeIterator((colour, startX, startY) -> {
            SpreadFill fill = new SpreadFill(colour, RGB.OverlapType.Add,startX,startY,480,480,1.1);
            fill.setSize(20);
            fill.setSpreadType(SpreadFill.SpreadType.Random);
            int seed = (int) Math.sqrt((System.currentTimeMillis()))/2;
            fill.setSeed(seed);
            //1st test: 1550
            //2nd test: 2000
            System.out.println(seed);
            return fill;
        });
        iteratorBlocks.setdSpreadFill(0.1);
        iteratorBlocks.setImages(1);
        iteratorBlocks.showShapeInfo();
        iteratorBlocks.setRescaleType(WriteBMP.RescaleType.START);
        Iterator.ExitCode exitCode = iteratorBlocks.write();
        System.out.println(exitCode.toString());

        /*Iterator iterator = new Iterator("mirrorFour",Constants.DMD_WIDTH,Constants.DMD_HEIGHT,500,150);
        iterator.setShapeIterator((colour, startX, startY) -> new Ellipse(colour, RGB.OverlapType.Add,startX,startY,300,300));
        iterator.setImages(3);
        iterator.setWidthChange(-4);
        iterator.setHeightChange(-4);
        iterator.centerShape(true);
        iterator.setMirrorFour(true);
        iterator.setRescaleType(WriteBMP.RescaleType.NONE);
        Iterator.ExitCode exitCode = iterator.write();
        System.out.println(exitCode.toString());*/


        /*Iterator iterator = new Iterator("fill_size_12", Constants.DMD_WIDTH,Constants.DMD_HEIGHT,672,330);
        iterator.setShapeIterator((colour, startX, startY) -> {
            SpreadFill fill = new SpreadFill(colour, RGB.OverlapType.Add,startX,startY,480,480,2);
            fill.setSize(12);
            return fill;
        });
        iterator.setdSpreadFill(1);
        iterator.setImages(1);
        iterator.showShapeInfo();
        iterator.setRescaleType(WriteBMP.RescaleType.START);*/
        //Iterator.ExitCode exitCode = iterator.write();
        //System.out.println(exitCode.toString());

        /*Iterator randomIterator = new Iterator("test", Constants.DMD_WIDTH,Constants.DMD_HEIGHT,672,330);
        randomIterator.setShapeIterator((colour, startX, startY) -> {
            SpreadFill spreadFill = new SpreadFill(colour, RGB.OverlapType.Add,startX,startY,480,480,2);
            spreadFill.setSeed(100);
            spreadFill.setSpreadType(SpreadFill.SpreadType.Random);
            spreadFill.setSquareSize(100);
            return spreadFill;
        });
        randomIterator.setImages(1);
        randomIterator.setdSpreadFill(1);
        randomIterator.showShapeInfo();
        randomIterator.setRescaleType(WriteBMP.RescaleType.START);
        Iterator.ExitCode exitCode = randomIterator.write();
        System.out.println(exitCode.toString());*/

        /*SpreadFill spreadFill = new SpreadFill(new RGB(255), RGB.OverlapType.Add, 200,200,480,480,3);
        spreadFill.setSquareSize(480);
        spreadFill.setSeed(100);
        spreadFill.setSpreadType(SpreadFill.SpreadType.Random);
        Canvas canvas = new Canvas("Test",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
        canvas.addShape(spreadFill);
        canvas.drawShapes();
        WriteBMP.getInstance().printBMP(canvas, WriteBMP.RescaleType.NONE);*/

    }


    public static void main(String[] args) {
        launch(args);
    }
}
