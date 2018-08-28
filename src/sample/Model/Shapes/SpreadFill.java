package sample.Model.Shapes;

import sample.Model.Mask;

import java.util.ArrayList;
import java.util.Map;

public class SpreadFill extends AbstractShape {

    private static final String FILL_FACTOR = "fillFactor";
    private int fillFactor;
    private int size = 2;

    public SpreadFill(RGB colour, RGB.OverlapType type, int start_x, int start_y, int width, int height, int fillFactor) {
        super(colour, type);

        this.startX = start_x;
        this.startY = start_y;

        this.width = width;
        this.height = height;

        setFillFactor(fillFactor);
    }

    SpreadFill(AbstractShape r, Map<String,Integer> map){
        super(r.getColour(),r.getOverlapType());

        this.startX = r.getStartX();
        this.startY = r.getStartY();

        this.width = r.getWidth();
        this.height = r.getHeight();

        setExtraInfo(map);
        this.fillFactor = map.get(FILL_FACTOR);
    }

    public void setFillFactor(int fillFactor) {
        Map<String,Integer> map = getExtraInfo();
        map.put(FILL_FACTOR,fillFactor);
        setExtraInfo(map);
        this.fillFactor = fillFactor;
    }

    public int getFillFactor(int fillFactor){
        return fillFactor;
    }

    public void setSize(int size) {
        this.size = size;
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

    @Override
    public Mask getShapeMask(int arrWidth, int arrHeight) {
        Mask mask = new Mask(arrWidth,arrHeight);
        int[][] arr = mask.getMask();
        System.out.println(fillFactor);
        for (int i = 0; i < arrWidth/size; i++) {
            for(int j=0;j<arrHeight/size; j++){
                if(i%fillFactor==0){
                    if(j%2==0){
                        for(int k=0;k<size;k++) {
                            for (int l = 0; l < size; l++) {
                                arr[size*i + k][size*j + l] = 1;
                            }
                        }
                    }else{
                        for(int k = 0; k < size; k++){
                            for(int l = 0; l< size; l++){
                                try{
                                    arr[size*(i+1) + k][size*j + l] = 1;
                                }catch (ArrayIndexOutOfBoundsException e){

                                }
                            }
                        }
                    }

                }
            }
        }

        mask.setMask(arr);
        return mask;
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        return new int[0][];
    }

    @Override
    public String getShapeName() {
        return "Spread Fill";
    }
}
