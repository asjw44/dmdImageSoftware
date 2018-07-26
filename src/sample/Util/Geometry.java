package sample.Util;

import sample.Model.Mask;

public class Geometry {

    public static int[][] addRectangle(int[][] arr, int start_x, int start_y, int width, int height, int value){
        for (int x = 0; x < width ; x++) {
            for(int y=0;y<height;y++){
                arr[start_x+x][start_y+y] = value;
            }
        }return arr;
    }

    public static int[][] addEllipseBorder(int[][] arr, int center_x, int center_y, int width, int height, int value){

        int a2 = width*width;
        int b2 = height*height;
        int fa2 = 4*a2;
        int fb2 = 4*b2;
        int y = height;
        int sigma = 2*b2+a2*(1-2*y);

        for(int x=0;b2*x<=a2*y;x++){
             arr[center_x+x][center_y+y] = value;
             arr[center_x-x][center_y+y] = value;
             arr[center_x+x][center_y-y] = value;
             arr[center_x-x][center_y-y] = value;

             if(sigma>=0){
                 sigma+=fa2*(1-y);
                 y--;
             }sigma += b2*((4*x)+6);
        }

        int x = width;
        sigma = 2*a2+b2*(1-2*x);
        for(y=0;a2*y<=b2*x;y++){
            arr[center_x+x][center_y+y] = value;
            arr[center_x-x][center_y+y] = value;
            arr[center_x+x][center_y-y] = value;
            arr[center_x-x][center_y-y] = value;

            if(sigma>=0){
                sigma+=fb2*(1-x);
                x--;
            }sigma += a2*((4*y)+6);
        }

        return arr;
    }

    public static int[][] addEllipseFill(int[][] arr, int center_x, int center_y, int width, int height, int value){
        Mask mask = new Mask(arr.length,arr[0].length);
        mask.setMask(addEllipseBorder(mask.getMask(),center_x,center_y,width,height,1));

        int[][] maskInternal = mask.getMask();

        for (int x = 0; x <= width; x++) {
            boolean endY = false;

            int y=0;

            while (!endY){
                maskInternal[center_x+x][center_y+y] = 1;
                maskInternal[center_x+x][center_y-y] = 1;
                maskInternal[center_x-x][center_y+y] = 1;
                maskInternal[center_x-x][center_y-y] = 1;

                y++;
                endY = maskInternal[center_x+x][center_y+y] == 1;
            }
        }

        for (int i = center_x-width; i < center_x+width; i++) {
            for(int j = center_y - height; j < center_y+height; j++){
                if(maskInternal[i][j]==1){

                    //ToDo: add value rather than set it
                    arr[i][j] = value;
                }
            }
        }return arr;

    }



}
