package sample.Model.Shapes;

import sample.Model.Mask;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


/**
 *
 */
public class SpreadFill extends AbstractShape {

    private static final String FILL_FACTOR = "fillFactor";
    private static final String SIZE = "size";
    private static final String SPREAD_TYPE = "spreadType";
    private static final String RANDOM_SEED = "seed";

    private int fillFactor;
    private int size = 2;
    private int seed = 0;

    private SpreadType spreadType = SpreadType.Lines;

    public SpreadFill(RGB colour, RGB.OverlapType type, int start_x, int start_y, int width, int height, int fillFactor) {
        super(colour, type);

        this.startX = start_x;
        this.startY = start_y;

        this.width = width;
        this.height = height;

        setFillFactor(fillFactor);
        setSquareSize(2);
    }

    SpreadFill(AbstractShape r, Map<String,Integer> map){
        super(r.getColour(),r.getOverlapType());

        this.startX = r.getStartX();
        this.startY = r.getStartY();

        this.width = r.getWidth();
        this.height = r.getHeight();

        setExtraInfo(map);
        this.fillFactor = map.get(FILL_FACTOR);
        this.size = map.get(SIZE);
        switch (map.get(SPREAD_TYPE)){
            case 1:
                this.spreadType = SpreadType.Lines;
                break;
            case 2:
                this.spreadType = SpreadType.Checkerboard;
                break;
            case 3:
                this.spreadType = SpreadType.Random;
                break;
            default:
                this.spreadType = SpreadType.Lines;
                System.out.println("Error finding spread type. Setting to \"Lines\"");
        }
    }

    public void setFillFactor(int fillFactor) {
        Map<String,Integer> map = getExtraInfo();
        map.put(FILL_FACTOR,fillFactor);
        setExtraInfo(map);
        this.fillFactor = fillFactor;
    }

    public void setSquareSize(int size){
        Map<String, Integer> map = getExtraInfo();
        map.put(SIZE,size);
        setExtraInfo(map);
        this.size = size;
    }

    public void setSpreadType(SpreadType spreadType) {
        Map<String, Integer> map = getExtraInfo();
        switch (spreadType){
            case Lines:
                map.put(SPREAD_TYPE,1);
                break;
            case Checkerboard:
                map.put(SPREAD_TYPE,2);
                break;
            case Random:
                map.put(SPREAD_TYPE,3);
                break;
            default:
                map.put(SPREAD_TYPE,-1);
        }
        setExtraInfo(map);
        this.spreadType = spreadType;
    }

    public void setSeed(int seed){
        Map<String,Integer> map = getExtraInfo();
        map.put(RANDOM_SEED,seed);
        setExtraInfo(map);
        this.seed = seed;
    }

    public int getFillFactor(){
        return fillFactor;
    }

    public void setSize(int size) {
        setSquareSize(size);
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

        switch (spreadType){
            case Checkerboard:
                arr = setCheckerboardMask(arr,arrWidth,arrHeight);
                break;
            case Lines:

                break;

            case Random:
              return setRandomMask(mask,arrWidth,arrHeight);
        }

        mask.setMask(arr);
        return mask;
    }

    private int[][] setCheckerboardMask(int[][] arr, int arrWidth, int arrHeight){
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
                                    //Do nothing
                                }
                            }
                        }
                    }

                }
            }
        }
        return arr;
    }

    private Mask setRandomMask(Mask mask, int arrWidth, int arrHeight){

        Mask blockMask = new Mask(size,size);
        int[][] randomMask = blockMask.getMask();

        int seedInternal = seed;

        if(seed <= 0 ){
            seedInternal = (int) System.currentTimeMillis();
        }

        Random random = new Random(seedInternal);

        int[] idx = new int[size*size/fillFactor];

        for (int i = 0; i < idx.length; i++) {
            boolean contains = true;
            int rand;
            do{
                rand = random.nextInt(size*size);
                boolean check = true;
                for(int internal : idx){
                    if(rand == internal){
                        check = false;
                        break;
                    }
                }if(check){
                    contains = false;
                }
            }while(contains);
            idx[i] = rand;
        }

        for(int i : idx){
            randomMask[(int)Math.floor(i/size)][i%size] = 1;
        }

        blockMask.setMask(randomMask);

        double total = 0;
        for (int[] aRandomMask : randomMask) {
            for (int j = 0; j < randomMask[0].length; j++) {
                total+=aRandomMask[j];
            }
        }
        System.out.println("\nActual fill %: " + (100 * (total)/(size*size)));
        System.out.println("Fill factor: " + Math.pow((total)/(size*size),-1) + "\n");

        for(int i = 0; i < arrWidth/size; i++){
            for(int j=0; j < arrHeight/size; j++){
                mask.printMask(blockMask, i*size, j*size);
            }
        }

        return mask;
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        return new int[0][];
    }

    public void rollFillFactor(int dFill){
        this.fillFactor += dFill;
    }

    @Override
    public String toString() {
        return String.format("SpreadFill --> x: %d\ty: %d\twidth: %d\theight: %d\tsize: %d\tfill: %d  ",startX,startY,width,height,size,fillFactor);
    }

    @Override
    public String getShapeName() {
        return "Spread Fill";
    }


    public enum SpreadType{
        Lines, Checkerboard, Random
    }

}
