package sample.Model.Shapes;

import sample.Model.Mask;
import sample.Util.WriteBMP;

import java.util.ArrayList;
import java.util.Locale;
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

    private double fillFactor;
    private int size = 2;
    private int seed = 0;

    private WriteBMP.RescaleType rescaleType;

    private SpreadType spreadType = SpreadType.Lines;

    public SpreadFill(RGB colour, RGB.OverlapType type, int start_x, int start_y, int width, int height, double fillFactor) {
        super(colour, type);

        this.startX = start_x;
        this.startY = start_y;

        this.width = width;
        this.height = height;

        setFillFactor(fillFactor);
        setSquareSize(2);
    }

    SpreadFill(AbstractShape r, Map<String, Number> map, WriteBMP.RescaleType rescaleType){
        super(r.getColour(),r.getOverlapType());

        this.startX = r.getStartX();
        this.startY = r.getStartY();

        this.width = r.getWidth();
        this.height = r.getHeight();

        this.rescaleType = rescaleType;

        setExtraInfo(map);
        this.fillFactor = (Double) map.get(FILL_FACTOR);
        this.size = (Integer) map.get(SIZE);
        int check = 3;
        if(map.get(SPREAD_TYPE) != null){
            check = (Integer) map.get(SPREAD_TYPE);
        }
        switch (check){
            case 1:
                this.spreadType = SpreadType.Lines;
                break;
            case 2:
                this.spreadType = SpreadType.Checkerboard;
                break;
            case 3:
                this.spreadType = SpreadType.Random;
                break;
            case 4:
                this.spreadType = SpreadType.RandomBlock;
                break;
            default:
                this.spreadType = SpreadType.Lines;
                System.out.println("Error finding spread type. Setting to \"Lines\"");
        }

        this.seed = map.get(RANDOM_SEED) == null ? 0 : (Integer) map.get(RANDOM_SEED);
    }

    public void setFillFactor(double fillFactor) {
        Map<String,Number> map = getExtraInfo();
        map.put(FILL_FACTOR,fillFactor);
        setExtraInfo(map);
        this.fillFactor = fillFactor;
    }

    public void setSquareSize(int size){
        Map<String, Number> map = getExtraInfo();
        map.put(SIZE,size);
        setExtraInfo(map);
        this.size = size;
    }

    public void setSpreadType(SpreadType spreadType) {
        Map<String, Number> map = getExtraInfo();
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
            case RandomBlock:
                map.put(SPREAD_TYPE,3);
            default:
                map.put(SPREAD_TYPE,-1);
        }
        setExtraInfo(map);
        this.spreadType = spreadType;
    }

    public void setSeed(int seed){
        Map<String,Number> map = getExtraInfo();
        map.put(RANDOM_SEED,seed);
        setExtraInfo(map);
        this.seed = seed;
    }

    public double getFillFactor(){
        return fillFactor;
    }

    public void setSize(int size) {
        setSquareSize(size);
    }

    @Override
    public ArrayList<int[][]> draw(ArrayList<int[][]> arrayList) {
        System.out.println(width);
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
            case RandomBlock:
                arr = setRandomBlockMask(arr,arrWidth,arrHeight);
                break;
        }

        mask.setMask(arr);
        return mask;
    }

    @Override
    public void setRescaleType(WriteBMP.RescaleType rescaleType) {
        this.rescaleType = rescaleType;
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

        int maskWidth = size;

        /*if(this.rescaleType != WriteBMP.RescaleType.NONE){
            maskWidth = size/2;
        }*/

        Mask blockMask = new Mask(maskWidth,size);
        int[][] randomMask = blockMask.getMask();

        int seedInternal = seed;

        if(seed <= 0 ){
            seedInternal = (int) System.currentTimeMillis();
        }

        Random random = new Random(seedInternal);

        System.out.println(this.rescaleType);
        System.out.println(String.format(Locale.UK,"%d\t%d\t%.2f\t%d",maskWidth,size,fillFactor,(int)(maskWidth*size/fillFactor)));
        int[] idx = new int[(int)(maskWidth*size/fillFactor)];

        for (int i = 0; i < idx.length; i++) {
            boolean contains = true;
            int rand;
            do{
                rand = random.nextInt(maskWidth*size);
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
            //System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d",idx.length,i,i/size,i%size,randomMask.length,randomMask[0].length));
            randomMask[(int)Math.floor(i/size)][i%size] = 1;
        }

        blockMask.setMask(randomMask);

        //System.out.println(randomMask.length * randomMask[0].length);

        double total = 0;
        double total2 = 0;
        for (int[] aRandomMask : randomMask) {
            for (int j = 0; j < randomMask[0].length; j++) {
                total+=aRandomMask[j];
                if(j%2 == 0){
                    total2 += aRandomMask[j];
                }
            }
        }
        System.out.println("\nActual fill %: " + (100 * (total)/(maskWidth*size)));
        System.out.println("Fill factor: " + Math.pow((total)/(maskWidth*size),-1));
        System.out.println("Fill converted: " + (100*(2*total2)/(maskWidth*size)));
        System.out.println("Fill factor converted: " + Math.pow((2*total2)/(maskWidth*size),-1));

        Mask blockMask2 = new Mask(size, size);
        int[][] arr = blockMask2.getMask();
        for (int i = 0; i < size; i+=2) {
            for(int j = 0; j < size; j++){
                arr[i][j] = randomMask[i/2][j];
                arr[i+1][j] = randomMask[i/2][j];
            }
        }blockMask2.setMask(arr);

        for(int i = 0; i < arrWidth/size; i++){
            for(int j=0; j < arrHeight/size; j++){
                mask.printMask(blockMask2, i*size, j*size);
            }
        }

        return mask;
    }

    private int[][] setRandomBlockMask(int[][] arr, int arrWidth, int arrHeight){
        int cols = (int)Math.ceil(arrWidth/size);
        int rows = (int)Math.ceil(arrHeight/size);
        int area = cols*rows;

        int[] idx = new int[(int)Math.floor(area/fillFactor)];

        Mask randomMask = new Mask(cols,rows);
        int[][] randomArr = randomMask.getMask();

        int seedInternal = seed;
        if(seed <= 0 ){
            seedInternal = (int) System.currentTimeMillis();
        }Random random = new Random(seedInternal);

        for (int i = 0; i < idx.length; i++) {
            boolean contains = true;
            int rand;
            do{
                rand = random.nextInt(area);
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


        for(int i:idx){
            //System.out.println(String.format("%d\t%d\t%d\t%d\t%d\t%d",idx.length,i,i/size,i%size,randomArr.length,randomArr[0].length));
            randomArr[(int)Math.floor(i%cols)][(int)Math.floor(i/cols)] = 1;
        }

        int total = 0;
        int add = 0;

        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                if(randomArr[i][j] == 1){
                    total++;
                    for(int k = 0; k < size; k++){
                        for(int l = 0; l < size; l++){
                            int x = i*size + k;
                            int y = j*size + l;
                            if(x < arrWidth && y < arrHeight){
                                arr[x][y]=1;
                            }else{
                                System.out.println("break");
                            }
                        }
                    }
                }else{
                    add++;
                }
            }
        }

        int total2 = 0;
        int overall = 0;
        for(int[] aW : arr){
            for(int a : aW){
                overall++;
                if(a == 1){
                    total2++;
                }
            }
        }

        System.out.println(cols*rows/total);
        System.out.println(add);
        System.out.println((double)overall/(double)total2 + "\t" + total2 + "\t" + overall);

        return arr;
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        return new int[0][];
    }

    public void rollFillFactor(double dFill){
        this.fillFactor += dFill;
    }

    @Override
    public String toString() {
        return String.format("SpreadFill --> x: %d\ty: %d\twidth: %d\theight: %d\tsize: %d\tfill: %.1f  ",startX,startY,width,height,size,fillFactor);
    }

    @Override
    public String getShapeName() {
        return "Spread Fill";
    }


    public enum SpreadType{
        Lines, Checkerboard, Random, RandomBlock
    }

}
