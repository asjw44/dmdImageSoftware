package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Util.Vector2;
import sample.Util.*;

import java.util.ArrayList;


public class Main extends Application {

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

        Vector2 x = new Vector2(802,508);
        Vector2 y = new Vector2(996,530);
        Vector2 z = new Vector2(828,382);
        Vector2 a = new Vector2(1014,402);

        ArrayList<Vector2> vs = new ArrayList<>();
        vs.add(x);
        vs.add(y);
        vs.add(z);
        vs.add(a);

        VectorIterator v = new VectorIterator("4c_ attenuator_5r",Constants.DMD_WIDTH,Constants.DMD_HEIGHT,vs);
        v.setImages(3)
                .setdRadius(-2)
                .setCircleRadii(72) //74 - circle, 72 - rectangle
                .setRescaleType(WriteBMP.RescaleType.START)
                .makeRectangle()
                .write();
        v.writeIndividual();


        ArrayList<String[]> data = ReadFile.getInstance().getData();
        for(String[] s : data){
            StringBuilder sIn = new StringBuilder();
            for(String s1 : s){
                sIn.append(s1).append("\t");
            }System.out.println(sIn);
        }


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
        }

        int x_start = 672;
        int y_start = 330;

        int x1_u = 108;

        int x2_u = 282;

        int y1_u = 20;

        int y2_u = 140;


        Iterator scan1 = new Iterator("small_scan_tl_x",width,height,x_start + x1_u,y_start + y1_u);
        scan1.setImages(2)
                .setTranslation(2,0)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,2,96))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();
        Iterator scan1a = new Iterator("small_scan_tl_y",width,height,x_start + x1_u,y_start + y1_u);
        scan1a.setImages(2)
                .setTranslation(0,2)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,96,2))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();


        Iterator scan2 = new Iterator("small_scan_tr_x",width,height,x_start + x2_u,y_start + y1_u);
        scan2.setImages(2)
                .setTranslation(2,0)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,2,96))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();
        Iterator scan2a = new Iterator("small_scan_tr_y",width,height,x_start + x2_u,y_start + y1_u);
        scan2a.setImages(2)
                .setTranslation(0,2)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,96,2))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();

        Iterator scan3 = new Iterator("small_scan_bl_x",width,height,x_start + x1_u,y_start + y2_u);
        scan3.setImages(2)
                .setTranslation(2,0)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,2,96))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();
        Iterator scan3a = new Iterator("small_scan_bl_y",width,height,x_start + x1_u,y_start + y2_u);
        scan3a.setImages(2)
                .setTranslation(0,2)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,96,2))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();

        Iterator scan4 = new Iterator("small_scan_br_x",width,height,x_start + x2_u,y_start + y2_u);
        scan4.setImages(2)
                .setTranslation(2,0)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,2,96))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();
        Iterator scan4a = new Iterator("small_scan_br_y",width,height,x_start + x2_u,y_start + y2_u);
        scan4a.setImages(2)
                .setTranslation(0,2)
                .setShapeIterator((colour, startX, startY) -> new Rectangle(colour, RGB.OverlapType.Add,startX,startY,96,2))
                .setRescaleType(WriteBMP.RescaleType.START)
                .write();

        */


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


    public static void main(String[] args) {
        launch(args);
    }
}
