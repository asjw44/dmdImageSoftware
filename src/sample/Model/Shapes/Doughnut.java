package sample.Model.Shapes;

import sample.Model.Mask;

import java.util.ArrayList;
import java.util.Map;

public class Doughnut extends Ellipse {

    private static final String OFFSET = "offset";

    private int offset;

    public Doughnut(RGB colour, RGB.OverlapType type, int startX, int startY, int width, int height, int offset){
        super(colour,type,startX,startY,width,height);
        this.offset = offset;
        setOffset(offset);
    }

    Doughnut(AbstractShape r, Map<String, Number> map){
        super(r.getColour(),r.getOverlapType(),r.getStartX(),r.getStartY(),r.getWidth(),r.getHeight());
        setExtraInfo(map);
        this.offset = (int) getExtraInfo().get(OFFSET);
    }

    private void setOffset(int offset){
        Map<String, Number> map = getExtraInfo();
        map.put(OFFSET,offset);
        setExtraInfo(map);
    }

    @Override
    public ArrayList<int[][]> draw(ArrayList<int[][]> arrayList) {
        int[][] shapeMask = getShapeMask(arrayList.get(0).length,arrayList.get(0)[0].length).getMask();
        if(arrayList.size() == getColour().getColourArray().length){
            for(int x=startX;x<=startX+width;x++){
                for(int y=startY;y<=startY+height;y++){
                    if (shapeMask[x][y]==1){
                        int[] arrayColours = new int[3];
                        for (int i = 0; i < 3; i++) {
                            arrayColours[i] = arrayList.get(i)[x][y];
                        }int[] newColours = getColour().getResultantColour(getOverlapType(),arrayColours);
                        for (int i = 0; i < 3; i++) {
                            arrayList.get(i)[x][y] = newColours[i];
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public void changeOffset(int dOffset){
        this.offset += dOffset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public Mask getShapeMask(int arrWidth, int arrHeight) {
        Mask outer = super.getShapeMask(arrWidth,arrHeight);
        Ellipse innerCircle = new Ellipse(new RGB(0),RGB.OverlapType.Overlap,startX+offset/2,startY+offset/2,width-offset,height-offset);
        Mask inner = innerCircle.getShapeMask(arrWidth,arrHeight);
        return outer.subtractMask(inner);
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        //ToDo: update function for doughnut
        return super.drawArray(arr, value);
    }

    @Override
    public String toString() {
        return String.format("Doughnut --> x: %d\ty: %d\twidth: %d\theight: %d\tdRadius: %d",startX,startY,width,height,offset);
    }

    @Override
    public String getShapeName() {
        return "Doughnut";
    }

}
