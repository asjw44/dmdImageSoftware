package sample.Util;

import sample.Model.BMPData;
import sample.Model.Canvas;
import sample.Model.Shapes.AbstractShape;
import sample.Model.Shapes.Doughnut;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.SpreadFill;

import java.io.IOException;
import java.util.ArrayList;

public class Iterator {

    private String baseName;

    private int startX;
    private int startY;
    private int dx = 0;
    private int dy = 0;
    private int dWidth = 0;
    private int dHeight = 0;
    private int dRadius = 0;
    private int dSpreadFill = 0;
    private int width;
    private int height;

    private WriteBMP.RescaleType rescaleType = WriteBMP.RescaleType.NONE;

    private int images = 1;

    private boolean showShapeInfo = false;
    private boolean center = false;

    private ArrayList<AbstractShape> staticShapes = new ArrayList<>();

    private AbstractShape baseShape;

    public interface SetShapeIterator{
        AbstractShape setShapeIterator(RGB colour, int startX, int startY);
    }

    public Iterator(String baseName, int width, int height, int startX, int startY){
        this.baseName = baseName;
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    public Iterator setImages(int images){
        this.images = images;
        return this;
    }

    public Iterator setTranslation(int translationX, int translationY){
        this.dx = translationX;
        this.dy = translationY;
        return this;
    }

    public Iterator setHeightChange(int dHeight){
        this.dHeight = dHeight;
        return this;
    }

    public Iterator setDRadiusChange(int dRadius){
        this.dRadius = dRadius;
        return this;
    }

    public Iterator setRescaleType(WriteBMP.RescaleType rescaleType){
        this.rescaleType = rescaleType;
        return this;
    }

    public Iterator setWidthChange(int dWidth){
        this.dWidth = dWidth;
        return this;
    }

    public Iterator setShapeIterator(SetShapeIterator shapeIterator){
        baseShape = shapeIterator.setShapeIterator(new RGB(0),startX,startY);
        return this;
    }

    public Iterator showShapeInfo(){
        this.showShapeInfo = true;
        return this;
    }

    public Iterator setdSpreadFill(int dSpreadFill){
        this.dSpreadFill = dSpreadFill;
        return this;
    }

    public Iterator centerShape(boolean b){
        this.center = b;
        return this;
    }

    public Iterator setStaticShapes(ArrayList<AbstractShape> staticShapes){
        this.staticShapes = staticShapes;
        return this;
    }

    public Iterator.ExitCode write(){

        baseShape.setRescaleType(rescaleType);

        if(center){ //We do not want the shapes to be moved if the shape is scaling from the center: we need to control this value!
            this.dx = 0;
            this.dy = 0;
        }

        for (int i = 0; i < images; i++) {
            System.out.println("========================================== Image " + (i+1) + " ==========================================");
            Canvas img = new Canvas(baseName + "_" + i,width,height);
            for(int j = 0; j < Constants.BIT_DEPTH.length; j++){

                AbstractShape redShape = baseShape.copy();
                AbstractShape greenShape = baseShape.copy();
                AbstractShape blueShape = baseShape.copy();

                redShape.setColour(new RGB(BMPData.Colour.Red,Constants.BIT_DEPTH[j]));
                greenShape.setColour(new RGB(BMPData.Colour.Green,Constants.BIT_DEPTH[j]));
                blueShape.setColour(new RGB(BMPData.Colour.Blue,Constants.BIT_DEPTH[j]));

                if (Math.abs(dx+dy)>0) {
                    redShape.translate(dx*(24*i+j),dy*(24*i+j));
                    greenShape.translate(dx*(24*i+j+8),dy*(24*i+j+8));
                    blueShape.translate(dx*(24*i+j+16),dy*(24*i+j+16));
                }else if (center){
                    redShape.translate(-(dWidth/2)*(24*i+j),-(dHeight/2)*(24*i+j));
                    greenShape.translate(-(dWidth/2)*(24*i+j+8),-(dHeight/2)*(24*i+j+8));
                    blueShape.translate(-(dWidth/2)*(24*i+j+16),-(dHeight/2)*(24*i+j+16));
                }

                if (Math.abs(dWidth+dHeight)>0) {
                    redShape.changeWidthHeight(dWidth*(24*i+j),dHeight*(24*i+j));
                    greenShape.changeWidthHeight(dWidth*(24*i+j+8),dHeight*(24*i+j+8));
                    blueShape.changeWidthHeight(dWidth*(24*i+j+16),dHeight*(24*i+j+16));
                }

                if(Math.abs(dRadius) > 0){
                    if(baseShape instanceof Doughnut){
                        ((Doughnut) redShape).changeOffset(dRadius*(24*i+j));
                        ((Doughnut) greenShape).changeOffset(dRadius*(24*i+j+8));
                        ((Doughnut) blueShape).changeOffset(dRadius*(24*i+j+16));
                    }
                }

                if(Math.abs(dSpreadFill)  > 0){
                    if(baseShape instanceof SpreadFill){
                        ((SpreadFill) redShape).rollFillFactor(dSpreadFill*(24*i+j));
                        ((SpreadFill) greenShape).rollFillFactor(dSpreadFill*(24*i+j+8));
                        ((SpreadFill) blueShape).rollFillFactor(dSpreadFill*(24*i+j+16));
                    }
                }

                img.addShape(redShape);
                img.addShape(greenShape);
                img.addShape(blueShape);
            }

            if(staticShapes.size() > 0){
                for(AbstractShape shape : staticShapes){
                    AbstractShape staticShape = shape.copy();
                    staticShape.setColour(new RGB(255));
                    img.addShape(staticShape);
                }
            }

            if (showShapeInfo) {
                for(AbstractShape shape: img.getShapes()){
                    System.out.println(String.format("%s\t Colour: %s",shape.toString(),shape.getColour().toString()));
                }
            }
            if(img.drawShapes()) {
                try {
                    WriteBMP.getInstance().printBMP(img, rescaleType);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ExitCode.PrintError;
                }
            }else{
                System.out.println("A shape goes outside the perimeter");
                return ExitCode.PerimeterError;
            }
        }

        return ExitCode.Successful;
    }

    public enum ExitCode{
        Successful, PrintError, PerimeterError, NullEntryError
    }

}
