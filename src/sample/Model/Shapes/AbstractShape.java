package sample.Model.Shapes;

import sample.Model.Mask;
import sample.Util.Iterator;
import sample.Util.WriteBMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShape {

    public static String[] shapes = {"Rectangle","Ellipse","Doughnut","Spread Fill"};

    int startX;
    int startY;
    int width;
    int height;

    private Map<String, Integer> extraInfo = new HashMap<>();

    private RGB colour;

    private RGB.OverlapType overlapType;

    WriteBMP.RescaleType rescaleType;

    AbstractShape(RGB colour, RGB.OverlapType overlapType){
        this.colour = colour;
        this.overlapType = overlapType;
    }

    /**
     * Extra info is for storing the properties of shapes that are not included here for them to be recreated from just this abstract class.
     * @return The extra info map for recreating properties.
     */
    Map<String, Integer> getExtraInfo() {
        return extraInfo;
    }

    void setExtraInfo(Map<String, Integer> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    RGB.OverlapType getOverlapType() {
        return overlapType;
    }

    /*public void setOverlapType(RGB.OverlapType overlapType) {
        this.overlapType = overlapType;
    }*/

    public RGB getColour() {
        return colour;
    }

    public void setColour(RGB colour) {
        this.colour = colour;
    }

    /**
     * Draws the shape onto the given colour arrays
     * @param arrayList Array with all colour matrices. 0 - Red, 1 - Green, 2 - Blue
     * @return the arrays with the shape drawn onto it.
     */
    public abstract ArrayList<int[][]> draw(ArrayList<int[][]> arrayList);

    public abstract Mask getShapeMask(int arrWidth, int arrHeight);

    public abstract int[][] drawArray(int[][] arr, int value);

    public boolean isBoundsInsideArr(int[][] arr){
        return !(startX < 0 || startY < 0 || startX + width > arr.length || startY + height > arr[0].length);
    }

    public void translate(int x, int y){
        startX += x;
        startY += y;
    }

    public void changeWidthHeight(int dW, int dH){
        width += dW;
        height += dH;
    }

    public void setRescaleType(WriteBMP.RescaleType rescaleType) {
        this.rescaleType = rescaleType;
    }

    @Override
    public String toString() {
        return "Abstract shape located at (" + startX + "," + startY + ")";
    }

    /**
     * Use it for AbstractShape shape = [originalShape].copy(). Allows for polymorphism that keeps the properties of the shape, but can be altered without affecting the original shape.
     * @return Returns an identical shape.
     */
    public AbstractShape copy(){
        if(this.getClass() == Rectangle.class){
            return new Rectangle(this);
        }else if(this.getClass() == Ellipse.class){
            return new Ellipse(this);
        }else if(this.getClass() == Doughnut.class){
            return new Doughnut(this, extraInfo);
        }else if(this.getClass() == SpreadFill.class){
            return new SpreadFill(this, extraInfo);
        }else{
            return null;
        }
    }

    public abstract String getShapeName();

}
