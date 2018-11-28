package sample.Util;

import sample.Model.Canvas;
import sample.Model.Mask;

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

    static void printCanvas(Canvas canvas){
        int[][] innerCanvas = canvas.getAllColours().get(0);
        for (int[] c : innerCanvas) {
            StringBuilder s = new StringBuilder();
            for (int aC : c) {
                s.append(aC).append("\t");
            }System.out.println(s.toString());
        }
    }

    static void printMask(Mask mask){
        for(int[] m : mask.getMask()){
            StringBuilder s = new StringBuilder();
            for(int n : m){
                s.append(n).append("\t");
            }
            System.out.println(s.toString());
        }
    }

}
