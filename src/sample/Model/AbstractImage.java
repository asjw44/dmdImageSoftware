package sample.Model;

public abstract class AbstractImage {

    int width;
    int height;

    AbstractImage(int width, int height){
        this.width = width;
        this.height = height;
        initialise();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract void initialise();

    public void refresh(){
        initialise();
    }

    public boolean isBaseSizeEqual(AbstractImage abstractImage){
        return this.width == abstractImage.getWidth() && this.height == abstractImage.getHeight();
    }

    public boolean isArraySizeEqual(int[][] arr){
        return this.width == arr.length && this.height == arr[0].length;
    }
}
