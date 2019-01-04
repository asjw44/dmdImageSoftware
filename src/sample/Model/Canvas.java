package sample.Model;

import sample.Model.Shapes.AbstractShape;
import sample.Util.ArrayHelper;
import sample.Util.Constants;

import java.util.ArrayList;

public class Canvas extends BMPData{

    private boolean hasBeenDrawn = false;
    private boolean insidePerimeter = true;

    private ArrayList<AbstractShape> shapes = new ArrayList<>();

    public Canvas(String imageName, int width, int height, ArrayList<int[][]> colourArrayList){
        super(imageName, width, height);
        ArrayList<int[][]> arr = new ArrayList<>();

        arr.add(ArrayHelper.zeroes(width, height));
        arr.add(ArrayHelper.zeroes(width, height));
        arr.add(ArrayHelper.zeroes(width, height));

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++){
                for (int k = 0; k < 3; k++){
                    int l = colourArrayList.get(k)[i][j];
                    arr.get(k)[i][j] = l;
                }
            }
        }

        setAllColours(arr);
    }

    public Canvas(String imageName, int width, int height) {
        super(imageName, width, height);
    }

    public Canvas(String imageName) {
        super(imageName, Constants.DMD_WIDTH, Constants.DMD_HEIGHT);
    }

    public void addShape(AbstractShape abstractShape){
        shapes.add(abstractShape);
    }

    public boolean drawShapes(){
        for(AbstractShape shape : shapes){
            if(shape.isBoundsInsideArr(getColourArray(Colour.Green))){
                setAllColours(shape.draw(getAllColours()));
            }else{
                insidePerimeter = false;
                return false;
            }
        }
        hasBeenDrawn = true;
        return true;
    }

    private void flipSelectionHorizontally(int start_x, int start_y, int width2, int height2){
        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for(int i=start_x;i<start_x+width2;i++){
            for (int j=start_y;j<start_y+height2;j++){
                for(int k=0;k<data.size();k++){
                    data.get(k)[i][j] = getAllColours().get(k)[start_x+width2-i-1][j];
                }
            }
        }super.setAllColours(data);
    }

    private void flipSelectionVertically(int start_x, int start_y, int width2, int height2){
        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for(int i=start_x;i<start_x+width2;i++){
            for (int j=start_y;j<start_y+height2;j++){
                for(int k=0;k<data.size();k++){
                    data.get(k)[i][j] = getAllColours().get(k)[i][start_y+height2-j-1];
                }
            }
        }super.setAllColours(data);
    }

    private void copyPaste(int start_x, int start_y, int width, int height, int paste_x, int paste_y, boolean cut){

        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for (int i = start_x; i < start_x+width; i++) {
            for(int j=start_y; j < start_y+height; j++){
                for(int k=0;k<3;k++){
                    data.get(k)[paste_x+i-start_x][paste_y+j-start_y] = getAllColours().get(k)[i][j];
                    if (cut) {
                        data.get(k)[i][j] = 0;
                    }
                }
            }
        }setAllColours(data);
    }

    public void cutPaste(int start_x, int start_y, int width, int height, int paste_x, int paste_y){
        copyPaste(start_x, start_y, width, height, paste_x, paste_y,true);
    }

    public void mirrorQuarter(){
        int halfWidth = getWidth()/2;
        int halfHeight = getHeight()/2;

        copyPaste(0,0,halfWidth,halfHeight,halfWidth,0,false);
        copyPaste(0,0,halfWidth,halfHeight,0,halfHeight,false);
        copyPaste(0,0,halfWidth,halfHeight,halfWidth,halfHeight,false);

        flipSelectionHorizontally(halfWidth,0,halfWidth,halfHeight);
        flipSelectionHorizontally(halfWidth,halfHeight,halfWidth,halfHeight);
        flipSelectionVertically(0,halfHeight,halfWidth,halfHeight);
        flipSelectionHorizontally(halfWidth,halfHeight,halfWidth,halfHeight);

    }

    public boolean getInsidePerimeter(){
        return this.insidePerimeter;
    }

    public ArrayList<int[][]> getAllColours() {
        return super.getAllColours();
    }

    public void setColours(ArrayList<int[][]> arrayList) {

        if(arrayList.get(0).length != getAllColours().get(0).length || arrayList.get(0)[0].length != getAllColours().get(0)[0].length){
            ArrayList<int[][]> data = new ArrayList<>();
            data.addAll(getAllColours());
            for(int i = 0; i < arrayList.get(0).length; i++) {
                for(int j = 0; j < arrayList.get(0)[0].length; j++){
                    for(int k = 0; k < 3; k++){
                        data.get(k)[i][j] = arrayList.get(k)[i][j];
                    }
                }
            }
            super.setAllColours(data);
        }else{
            super.setAllColours(arrayList);
        }
    }

    public boolean hasBeenDrawn() {
        return hasBeenDrawn;
    }

    public void printMask(Mask mask, boolean sout){

        if(mask.getHeight() == getHeight() && mask.getWidth() == getWidth()){
            ArrayList<int[][]> data = new ArrayList<>();
            data.addAll(getAllColours());

            for(int i=0;i<mask.getWidth();i++){
                StringBuilder s = new StringBuilder();
                for(int j=0;j<mask.getHeight();j++){
                    s.append(data.get(0)[i][j]).append("|").append(mask.getMask()[i][j]).append("\t\t");
                    for(int k=0;k<3;k++){
                        data.get(k)[i][j] = data.get(k)[i][j] * mask.getMask()[i][j];
                    }
                }
                if(sout){System.out.println(s.toString());}
            }
            if(sout){System.out.println("\n==================\n");}

            setAllColours(data);

        }
    }

    public ArrayList<AbstractShape> getShapes() {
        return shapes;
    }
}
