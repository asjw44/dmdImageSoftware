package sample.Model;

import sample.Util.ArrayHelper;
import sample.Util.Geometry;

import java.util.ArrayList;

public abstract class BMPData extends AbstractImage{

    private String imageName;

    private int[][] r;
    private int[][] g;
    private int[][] b;

    BMPData(String imageName, int width, int height){
        super(width,height);
        this.imageName = imageName;
    }

    @Override
    public void initialise() {
        r = ArrayHelper.zeroes(width,height);
        b = ArrayHelper.zeroes(width,height);
        g = ArrayHelper.zeroes(width,height);
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private void setR(int[][] r) {
        this.r = r;
    }

    private void setG(int[][] g) {
        this.g = g;
    }

    private void setB(int[][] b) {
        this.b = b;
    }

    @Override
    public void addDefaultEllipse(int center_x, int center_y, int width, int height) {
        setR(Geometry.addEllipseFill(r,center_x,center_y,width,height,255));
    }

    @Override
    public void addDefaultRectangle(int start_x, int start_y, int width, int height) {
        setR(Geometry.addRectangle(r,start_x,start_y,width,height,255));
    }

    public ArrayList<int[][]> getAllColours(){
        ArrayList<int[][]> arrayList = new ArrayList<>();
        arrayList.add(r);
        arrayList.add(g);
        arrayList.add(b);
        return arrayList;
    }

    public void setAllColours(ArrayList<int[][]> arrayList){
        if (arrayList.size() == 3) {
            setR(arrayList.get(0));
            setG(arrayList.get(1));
            setB(arrayList.get(2));
        }
    }

    public int[][] getColourArray(Colour colour){
        switch (colour){
            case Red:
                return r;
            case Green:
                return g;
            default:
                return b;
        }
    }

    void setColourArray(Colour colour, int[][] arr){
        switch (colour){
            case Blue:
                setB(arr);
                break;
            case Red:
                setR(arr);
                break;
            case Green:
                setG(arr);
                break;
        }
    }

    public boolean isSizeEqual(){
        return r.length == g.length && r.length == b.length && r[0].length == g[0].length && r[0].length == b[0].length;
    }

    public enum Colour{
        Red, Blue, Green
    }

}
