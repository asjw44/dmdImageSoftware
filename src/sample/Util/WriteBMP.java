package sample.Util;

import sample.Model.BMPData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WriteBMP {
    private static WriteBMP ourInstance = new WriteBMP();

    public static String imageDirectory = System.getProperty("user.dir") + "\\";

    public static WriteBMP getInstance() {
        return ourInstance;
    }

    private WriteBMP() {
    }

    public void printBMP(BMPData bmpData, RescaleType rescaleType) throws IOException{
        if(bmpData.isSizeEqual()){
            int[][] r;
            int[][] g;
            int[][] b;

            int width;
            int height;

            //Dealing with any rescaling and the type required for the DMD

            if(rescaleType == RescaleType.NONE){
                width = bmpData.getWidth();
                height = bmpData.getHeight();

                r = bmpData.getColourArray(BMPData.Colour.Red);
                g = bmpData.getColourArray(BMPData.Colour.Green);
                b = bmpData.getColourArray(BMPData.Colour.Blue);

            }else{
                width = bmpData.getWidth()/2;
                height = bmpData.getHeight();

                r = new int[width][height];
                g = new int[width][height];
                b = new int[width][height];

                int[][] rBMP = bmpData.getColourArray(BMPData.Colour.Red);
                int[][] gBMP = bmpData.getColourArray(BMPData.Colour.Green);
                int[][] bBMP = bmpData.getColourArray(BMPData.Colour.Blue);

                for(int i=0;i<2*width;i+=2){
                    for(int j=0;j<height;j++){
                         switch (rescaleType){
                             case START:
                                 r[i/2][j] = rBMP[i][j];
                                 g[i/2][j] = gBMP[i][j];
                                 b[i/2][j] = bBMP[i][j];
                                 break;
                             case END:
                                 r[i/2][j] = rBMP[i+1][j];
                                 g[i/2][j] = gBMP[i+1][j];
                                 b[i/2][j] = bBMP[i+1][j];
                                 break;
                             case AVERAGE:
                                 r[i/2][j] = (int)Math.floor((rBMP[i][j] + rBMP[i+1][j])/2);
                                 g[i/2][j] = (int)Math.floor((gBMP[i][j] + gBMP[i+1][j])/2);
                                 b[i/2][j] = (int)Math.floor((bBMP[i][j] + bBMP[i+1][j])/2);
                                 break;
                         }
                    }
                }
            }

            //Creating the image and outputting it

            BufferedImage img = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++){
                    int red = correctColours(r[i][j]);
                    int green = correctColours(g[i][j]);
                    int blue = correctColours(b[i][j]);

                    int rgb = (red<<16)|(green<<8)|blue;

                    img.setRGB(i,j,rgb);
                }
            }ImageIO.write(img,"BMP",new java.io.File(imageDirectory + bmpData.getImageName() + ".bmp"));
        }else{
            System.out.println("Array sizes are not the same. Cannot print them out");
        }

        System.out.println("Finished");
    }

    private int correctColours(int a){
        return a > 255 ? 255 : a < 0 ? 0 : a;
    }


    public enum RescaleType{
        START,END,AVERAGE, NONE
    }
}
