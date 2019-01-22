package sample.Util;

import sample.Model.BMPData;
import sample.Model.Canvas;
import sample.Model.Shapes.AbstractShape;
import sample.Model.Shapes.Ellipse;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class VectorIterator {

    private String baseName;

    private int width = 0;
    private int height = 0;
    private int dRadius = 0;
    private int images = 1;

    private boolean rectangle;

    private WriteBMP.RescaleType rescaleType;

    private int[] radius;

    private ArrayList<Vector2> vectors;

    private ArrayList<AbstractShape> baseShapes;

    public VectorIterator(String baseName, int width, int height, ArrayList<Vector2> vectors){
        this.baseName = baseName;
        this.width = width;
        this.height = height;
        if (vectors != null) {
            this.vectors = vectors;
        }else{
            this.vectors = new ArrayList<>();
        }
    }

    public VectorIterator makeRectangle(){
        this.rectangle = true;
        return this;
    }

    public VectorIterator setCircleRadii(int radius){
        this.radius = new int[vectors.size()];
        for(int i = 0; i < this.radius.length; i++){
            this.radius[i] = radius;
        }return this;
    }


    public VectorIterator setdRadius(int dRadius){
        this.dRadius = dRadius;
        return this;
    }

    public VectorIterator setRescaleType(WriteBMP.RescaleType rescaleType) {
        this.rescaleType = rescaleType;
        return this;
    }

    public VectorIterator setImages(int images){
        this.images = images;
        return this;
    }

    public void writeIndividual(){
        if(radius != null && radius.length >0 && radius.length == vectors.size()){
            for (int i = 0; i < radius.length; i++) {
                baseShapes = new ArrayList<>();
                if (!rectangle) {
                    baseShapes.add(new Ellipse(RGB.black(), RGB.OverlapType.Add,vectors.get(i),radius[i]));
                }else{
                    baseShapes.add(new Rectangle(RGB.black(), RGB.OverlapType.Add,vectors.get(i),radius[i]));
                }
                for(int j = 0 ; j < images; j++){
                    Canvas img = writeShapes(new Canvas(baseName + "_(core " + (i + 1) + ")_" + j,width,height),j);
                    if(img.drawShapes()){
                        try{
                            WriteBMP.getInstance().printBMP(img, rescaleType);
                        }catch (IOException e){
                            System.out.println("Printing error");
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("Draw shape error");
                    }
                }
            }
        }

    }

    public void write(){
        baseShapes = new ArrayList<>();
        if(radius != null){
            for(int i = 0 ; i < radius.length; i++){
                if(!rectangle){
                    baseShapes.add(new Ellipse(RGB.black(),RGB.OverlapType.Add,vectors.get(i),radius[i]));
                }else{
                    baseShapes.add(new Rectangle(RGB.black(),RGB.OverlapType.Add,vectors.get(i),radius[i]));
                }

            }
        }

        for(int i = 0; i < images; i++){

            Canvas img = writeShapes(new Canvas(baseName + "_" + i, width, height),i);

            for (AbstractShape shape : img.getShapes()) {
                System.out.println(String.format("%s\t Colour: %s", shape.toString(), shape.getColour().toString()));
            }

            if(img.drawShapes()){
                try{
                    WriteBMP.getInstance().printBMP(img,rescaleType);
                }catch (IOException e){
                    System.out.println("Printing error");
                    e.printStackTrace();
                }
            }else{
                System.out.println("Draw shapes error");
            }

        }
    }

    private Canvas writeShapes(Canvas img, int i){
        for(int j = 0; j < Constants.BIT_DEPTH.length; j++){

            for(AbstractShape baseShape : baseShapes){

                AbstractShape redShape = baseShape.copy();
                AbstractShape greenShape = baseShape.copy();
                AbstractShape blueShape = baseShape.copy();

                redShape.setColour(new RGB(BMPData.Colour.Red, Constants.BIT_DEPTH[j]));
                greenShape.setColour(new RGB(BMPData.Colour.Green, Constants.BIT_DEPTH[j]));
                blueShape.setColour(new RGB(BMPData.Colour.Blue, Constants.BIT_DEPTH[j]));

                redShape.translate(-(dRadius / 2) * (24 * i + j), -(dRadius / 2) * (24 * i + j));
                greenShape.translate(-(dRadius / 2) * (24 * i + j + 8), -(dRadius / 2) * (24 * i + j + 8));
                blueShape.translate(-(dRadius / 2) * (24 * i + j + 16), -(dRadius / 2) * (24 * i + j + 16));

                redShape.changeWidthHeight(dRadius * (24 * i + j), dRadius * (24 * i + j));
                greenShape.changeWidthHeight(dRadius * (24 * i + j + 8), dRadius * (24 * i + j + 8));
                blueShape.changeWidthHeight(dRadius * (24 * i + j + 16), dRadius * (24 * i + j + 16));

                img.addShape(redShape);
                img.addShape(greenShape);
                img.addShape(blueShape);

            }
        }

        return img;
    }

}
