package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Model.Canvas;
import sample.Model.Shapes.AbstractShape;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;
import sample.Model.Shapes.SpreadFill;
import sample.Util.Vector2;
import sample.Util.*;

import java.io.IOException;
import java.util.ArrayList;
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

        WriteBMP.RescaleType rescaleType = WriteBMP.RescaleType.START;
        //double fillFactor = 2;
        //int seed = 100;
        int square_size = 500;

        //generate(fillFactor,1000,1,rescaleType);

        /*for(double fillFactor = 6; fillFactor <= 10; fillFactor+=0.5){
            System.out.println("\n" + fillFactor + "\n");
            generate(fillFactor,1000,2,rescaleType);
        }*/

        /*Rectangle rectangle = new Rectangle(RGB.white(), RGB.OverlapType.Add, 0,0,width/2,height);
        Canvas half = new Canvas("half",width,height);
        half.addShape(rectangle);
        printCanvas(half, WriteBMP.RescaleType.START);*/


        /*SpreadFill spreadFill1 = new SpreadFill(RGB.white(), RGB.OverlapType.Add, 300,200,1350,850,2);
        spreadFill1.setSeed(1000);
        spreadFill1.setSpreadType(SpreadFill.SpreadType.RandomBlock);
        spreadFill1.setSquareSize(40);
        spreadFill1.setRescaleType(WriteBMP.RescaleType.NONE);

        Canvas canvas = new Canvas("Spread trial",width,height);
        canvas.addShape(spreadFill1);
        printCanvas(canvas,WriteBMP.RescaleType.NONE);

        /*Iterator iterator = new Iterator("spread_fill_try",20,20,0,0);
        iterator.setShapeIterator((colour, startX, startY) -> new SpreadFill(colour, RGB.OverlapType.Add,0,0,20,20,0.5))
                .setImages(1).setRescaleType(WriteBMP.RescaleType.NONE).showShapeInfo();
        iterator.writeCanvases();
        System.out.println(iterator.getCanvases().get(0).getImageName());
        try {
            System.out.println("HERE");
            WriteBMP.getInstance().printBMP(iterator.getCanvases().get(0), WriteBMP.RescaleType.NONE);
        }catch (IOException e){
            e.printStackTrace();
        }*/
        //WriteBMP.getInstance().printBMP();

        /*

        System.out.println(width/2-constant);
        System.out.println(height/2-constant);

        /*Iterator it = new Iterator("core_small_c", width,height,width/2-constant,height/2-constant);
        it.setShapeIterator(((colour, startX, startY) -> new Ellipse(colour, RGB.OverlapType.Add, startX, startY, constant,constant)));
        it.centerShape(true);
        it.setWidthChange(-2);
        it.setHeightChange(-2);
        it.setImages(4);
        it.writeCanvases();

        FourCoreGenerator fcg = new FourCoreGenerator(it.getCanvases());
        fcg.drawSimultaneously(WriteBMP.RescaleType.START);
        fcg.drawIndividually(WriteBMP.RescaleType.START);*/

        /*Vector2 x = new Vector2(860,666);
        Vector2 y = new Vector2(724,560);
        Vector2 z = new Vector2(994,562);
        Vector2 a = new Vector2(842,452);

        ArrayList<Vector2> vs = new ArrayList<>();
        vs.add(x);
        vs.add(y);
        vs.add(z);
        vs.add(a);

        VectorIterator v = new VectorIterator("4c_ attenuator_f2f_2r",Constants.DMD_WIDTH,Constants.DMD_HEIGHT,vs);
        v.setImages(3)
                .setdRadius(-2)
                .setCircleRadii(74) //74 - circle, 72 - rectangle
                .setRescaleType(WriteBMP.RescaleType.START)
                .makeRectangle()
                .write();
        v.writeIndividual();


        /*Rectangle r = new Rectangle(RGB.white(), RGB.OverlapType.Add,y,3);
        Canvas c = new Canvas("rectangle_x",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
        c.addShape(r);
        c.drawShapes();
        WriteBMP.getInstance().printBMP(c, WriteBMP.RescaleType.START);


        //=================================================================

        Ellipse ex = new Ellipse(RGB.white(),RGB.OverlapType.Add,x,60);
        Ellipse ey = new Ellipse(RGB.white(),RGB.OverlapType.Add,y,60);
        Ellipse ez = new Ellipse(RGB.white(),RGB.OverlapType.Add,z,60);
        Ellipse ea = new Ellipse(RGB.white(),RGB.OverlapType.Add,a,60);

        Canvas cx = new Canvas("vector_x",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
        Canvas cy = new Canvas("vector_y",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
        Canvas cz = new Canvas("vector_z",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
        Canvas ca = new Canvas("vector_a",Constants.DMD_WIDTH,Constants.DMD_HEIGHT);

        cx.addShape(ex);
        cx.drawShapes();
        cy.addShape(ey);
        cy.drawShapes();
        cz.addShape(ez);
        cz.drawShapes();
        ca.addShape(ea);
        ca.drawShapes();

        try{
            WriteBMP.getInstance().printBMP(cx, WriteBMP.RescaleType.START);
            WriteBMP.getInstance().printBMP(cy, WriteBMP.RescaleType.START);
            WriteBMP.getInstance().printBMP(cz, WriteBMP.RescaleType.START);
            WriteBMP.getInstance().printBMP(ca, WriteBMP.RescaleType.START);

        }catch (IOException e){
            e.printStackTrace();
        }*/





        /*Iterator scan = new Iterator("core_small_scan_rows_2",width,height, width/2-constant,height/2-constant);
        scan.setShapeIterator(((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,constant,2)));
        scan.setTranslation(0,2);
        scan.setImages(4);
        scan.writeCanvases();

        FourCoreGenerator fc = new FourCoreGenerator(scan.getCanvases());
        fc.drawIndividually(WriteBMP.RescaleType.START);

        Iterator scanColumns = new Iterator("core_small_scan_columns_2",width,height, width/2-constant,height/2-constant);
        scanColumns.setShapeIterator(((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,2, constant)));
        scanColumns.setTranslation(2,0);
        scanColumns.setImages(4);
        scanColumns.writeCanvases();

        FourCoreGenerator fc2 = new FourCoreGenerator(scanColumns.getCanvases());
        fc2.drawIndividually(WriteBMP.RescaleType.START);*/

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
