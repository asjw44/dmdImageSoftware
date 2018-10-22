package sample.Model;

import sample.Model.Shapes.AbstractShape;
import sample.Util.ArrayHelper;
import sample.Util.Constants;

import java.util.ArrayList;

public class Canvas extends BMPData{

    private ArrayList<AbstractShape> shapes = new ArrayList<>();

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
                return false;
            }
        }return true;
    }

    private void flipSelectionHorizontally(int start_x, int start_y, int width, int height){
        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for(int i=start_x;i<start_x+width;i++){
            for (int j=start_y;j<start_y+height;j++){
                for(int k=0;k<data.size();k++){
                    data.get(k)[i][j] = getAllColours().get(k)[start_x+width-i][j];
                }
            }
        }setAllColours(data);
    }

    private void flipSelectionVertically(int start_x, int start_y, int width, int height){
        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for(int i=start_x;i<start_x+width;i++){
            for (int j=start_y;j<start_y+height;j++){
                for(int k=0;k<data.size();k++){
                    data.get(k)[i][j] = getAllColours().get(k)[i][start_y+height-j];
                }
            }
        }setAllColours(data);
    }

    private void copyPaste(int start_x, int start_y, int width, int height, int paste_x, int paste_y){

        ArrayList<int[][]> data = new ArrayList<>();
        data.addAll(getAllColours());

        for (int i = start_x; i < start_x+width; i++) {
            for(int j=start_y; j < start_y+height; j++){
                for(int k=0;k<3;k++){
                    data.get(k)[paste_x+i-start_x][paste_y+j-start_y] = getAllColours().get(k)[i][j];
                }
            }
        }setAllColours(data);
    }

    public void mirrorQuarter(){
        int halfWidth = getWidth()/2;
        int halfHeight = getHeight()/2;

        copyPaste(0,0,halfWidth,halfHeight,halfWidth,0);
        copyPaste(0,0,halfWidth,halfHeight,0,halfHeight);
        copyPaste(0,0,halfWidth,halfHeight,halfWidth,halfHeight);

        flipSelectionHorizontally(halfWidth,0,halfWidth,halfHeight);
        flipSelectionHorizontally(halfWidth,halfHeight,halfWidth,halfHeight);
        flipSelectionVertically(0,halfHeight,halfWidth,halfHeight);
        flipSelectionHorizontally(halfWidth,halfHeight,halfWidth,halfHeight);

    }

    public ArrayList<AbstractShape> getShapes() {
        return shapes;
    }
}
