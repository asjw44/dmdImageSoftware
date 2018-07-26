package sample.Model;

import sample.Model.Shapes.AbstractShape;
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

    public ArrayList<AbstractShape> getShapes() {
        return shapes;
    }
}
