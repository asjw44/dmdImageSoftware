package sample.Model.Shapes;

import sample.Model.Mask;
import sample.Util.Vector2;

import java.util.ArrayList;

public class Rectangle extends AbstractShape{

    public Rectangle(RGB colour, RGB.OverlapType type, int start_x, int start_y, int width, int height) {
        super(colour, type);

        this.startX = start_x;
        this.startY = start_y;

        this.width = width;
        this.height = height;
    }

    Rectangle(AbstractShape r){
        super(r.getColour(), r.getOverlapType());

        this.startX = r.getStartX();
        this.startY = r.getStartY();

        this.width = r.getWidth();
        this.height = r.getHeight();
    }

    public Rectangle(RGB colour, RGB.OverlapType type, Vector2 center, int radius){
        super(colour,type);

        this.startX = (int)center.x - radius;
        this.startY = (int)center.y - radius;

        this.width = radius * 2;
        this.height = radius * 2;
    }

    @Override
    public ArrayList<int[][]> draw(ArrayList<int[][]> arrayList) {
        if(arrayList.size() == getColour().getColourArray().length){
            for (int x = 0; x < width ; x++) {
                for(int y=0;y<height;y++) {
                    int[] arrayColours = new int[3];
                    for (int i = 0; i < 3; i++) {
                        arrayColours[i] = arrayList.get(i)[startX + x][startY + y];
                    }
                    int[] newColours = getColour().getResultantColour(getOverlapType(), arrayColours);
                    for (int i = 0; i < 3; i++) {
                        arrayList.get(i)[startX + x][startY + y] = newColours[i];
                    }
                }
            }
        }
        return arrayList;
    }

    @Override
    public Mask getShapeMask(int arrWidth, int arrHeight) {
        Mask mask = new Mask(arrWidth,arrHeight);
        mask.setMask(drawArray(mask.getMask(),1));
        return mask;
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        for (int x = 0; x < width ; x++) {
            for(int y=0;y<height;y++) {
                arr[startX+x][startY+y] = value;
            }
        }return arr;
    }

    @Override
    public String toString() {
        return String.format("Rectangle --> x: %d\ty: %d\twidth: %d\t height: %d",startX,startY,width,height);
    }

    @Override
    public String getShapeName() {
        return "Rectangle";
    }

}
