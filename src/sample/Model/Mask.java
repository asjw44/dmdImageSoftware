package sample.Model;

import sample.Util.ArrayHelper;
import sample.Util.Constants;
import sample.Util.Geometry;

public class Mask extends AbstractImage {

    private int[][] mask;

    private boolean lock = true;

    public Mask(int width, int height) {
        super(width, height);
    }

    public Mask(){
        super(Constants.DMD_WIDTH,Constants.DMD_HEIGHT);
    }

    public Mask(BMPData bmpData){
        super(bmpData.getWidth(),bmpData.getHeight());
        for (int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++){
                if((bmpData.getColourArray(BMPData.Colour.Red)[i][j] + bmpData.getColourArray(BMPData.Colour.Blue)[i][j] + bmpData.getColourArray(BMPData.Colour.Green)[i][j])>0){
                    mask[i][j] = 1;
                }
            }
        }
    }

    void setLock(boolean b){
        this.lock = b;
    }

    @Override
    public void initialise() {
        mask = ArrayHelper.zeroes(width,height);
    }

    public int[][] getMask() {
        return mask;
    }

    public void setMask(int[][] mask) {
        this.mask = checkMask(mask);
    }

    private int[][] checkMask(int[][] mask){
        for(int i=0; i<mask.length;i++){
            for(int j=0;j<mask[0].length;j++){
                if (lock) {
                    mask[i][j] = mask[i][j] != 0 ? 1 : 0;
                }
            }
        }return mask;
    }

    public static Mask getMaskValueHeight(Mask... masks){
        Mask maskHeight = new Mask(masks[0].getWidth(),masks[0].getHeight());
        maskHeight.setLock(false);
        int[][] arr = maskHeight.getMask();
        for(Mask mask : masks){
            if (maskHeight.isBaseSizeEqual(mask)) {
                for(int i=0;i<maskHeight.getWidth();i++){
                    for(int j=0;j<maskHeight.getHeight();j++){
                        arr[i][j] += mask.getMask()[i][j];
                    }
                }
            }
        }maskHeight.setMask(arr);
        return maskHeight;
    }

    public Mask subtractMask(Mask mask2){
        if(mask2.isArraySizeEqual(mask)){
            Mask reMask = new Mask(width,height);
            int[][] arr = reMask.getMask();
            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){
                    arr[i][j] = clamp(mask[i][j] - mask2.getMask()[i][j]);
                }
            }reMask.setMask(arr);
            return reMask;
        }else{
            return new Mask(width,height);
        }
    }

    public void printMask(Mask subMask, int x, int y){
        //System.out.println(String.format("Print - x: %d\ty: %d",x,y));
        for (int i = 0; i < subMask.getWidth(); i++) {
            for(int j=0; j < subMask.getHeight(); j++){
                try {
                    mask[x+i][y+j] = subMask.getMask()[i][j];
                } catch (IndexOutOfBoundsException e) {
                    //Do Nothing (slightly cheating here)
                }
            }
        }
    }

    private int clamp(int a){
        return a < 0 || a == 0 ? 0 : 1;
    }


}
