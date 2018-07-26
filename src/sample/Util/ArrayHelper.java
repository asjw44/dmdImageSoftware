package sample.Util;

public class ArrayHelper {

    public static int[][] zeroes(int width, int height){
        return generateUniformArray(width,height,0);
    }

    public static int[][] ones(int width, int height){
        return generateUniformArray(width,height,1);
    }

    private static int[][] generateUniformArray(int width, int height, int value){
        int[][] arr = new int[width][height];
        for (int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++){
                arr[i][j] = value;
            }
        }return arr;
    }

}
