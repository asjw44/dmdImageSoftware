package sample.Model.Shapes;

import sample.Model.BMPData;

public class RGB {

    private int red;
    private int green;
    private int blue;

    public RGB(int red, int green, int blue){
        this.red = correct(red);
        this.green = correct(green);
        this.blue = correct(blue);
    }

    public RGB(int uniformValue){
        int a = correct(uniformValue);
        red = a;
        green = a;
        blue = a;
    }

    public RGB(BMPData.Colour colour, int value){
        int a = correct(value);
        switch (colour){
            case Green:
                green = a;
                red = 0;
                blue = 0;
                break;
            case Red:
                red = a;
                green = 0;
                blue = 0;
                break;
            case Blue:
                blue = a;
                green = 0;
                red = 0;
        }
    }

    int[] getColourArray(){
        return new int[] {red, green, blue};
    }

    private int correct(int a){
        return a > 255 ? 255 : a < 0 ? 0 : a;
    }

    public enum OverlapType{
        Overlap,Add,Subtract,Average,Ignore
    }

    int[] getResultantColour(OverlapType type, int[] arrayRGB){
        switch (type){
            case Add:
                return new int[]{correct(arrayRGB[0] + red),correct(arrayRGB[1] + green), correct(arrayRGB[2] + blue) };
            case Subtract:
                return new int[]{correct(arrayRGB[0] - red),correct(arrayRGB[1] - green), correct(arrayRGB[2] - blue) };
            case Overlap:
                return getColourArray();
            case Ignore:
                return arrayRGB;
            case Average:
                int r = correct((int)Math.floor((arrayRGB[0] + red)/2));
                int g = correct((int)Math.floor((arrayRGB[1] + green)/2));
                int b = correct((int)Math.floor((arrayRGB[2] + blue)/2));
                return new int[]{r,g,b};
            default:
                return getColourArray();
        }

    }

    public static RGB white(){
        return new RGB(255);
    }

    public static RGB black(){
        return new RGB(0);
    }

    @Override
    public String toString() {
        return String.format("R: %d, G: %d, B: %d",red,green,blue);
    }
}
