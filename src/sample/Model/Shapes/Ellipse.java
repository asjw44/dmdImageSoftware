package sample.Model.Shapes;

import sample.Model.Mask;

import java.util.ArrayList;

public class Ellipse extends AbstractShape {

    private int centerX;
    private int centerY;

    public Ellipse(RGB colour, RGB.OverlapType type, int startX, int startY, int width, int height) {
        super(colour, type);

        this.width = width;
        this.height = height;

        this.startX = startX;
        this.startY = startY;

        this.centerX = startX + (int)Math.floor(width/2);
        this.centerY = startY + (int)Math.floor(height/2);

    }

    Ellipse(AbstractShape r){
        super(r.getColour(), r.getOverlapType());

        this.startX = r.getStartX();
        this.startY = r.getStartY();

        this.width = r.getWidth();
        this.height = r.getHeight();

        this.centerX = r.getStartX() + (int)Math.floor(width/2);
        this.centerY = r.getStartY() + (int)Math.floor(height/2);
    }

    @Override
    public ArrayList<int[][]> draw(ArrayList<int[][]> arrayList) {

        this.centerX = getStartX() + (int)Math.floor(width/2);
        this.centerY = getStartY() + (int)Math.floor(height/2);

        Mask mask = getShapeMask(arrayList.get(0).length,arrayList.get(0)[0].length);
        if(arrayList.size() == getColour().getColourArray().length){
            for(int x = startX; x <= startX + width; x++){
                for(int y = startY ; y <= startY + height; y++){
                    if (mask.getMask()[x][y] == 1) {
                        int[] arrayColours = new int[3];
                        for (int i = 0; i < 3; i++) {
                            arrayColours[i] = arrayList.get(i)[x][y];
                        }
                        int[] newColours = getColour().getResultantColour(getOverlapType(), arrayColours);
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

        int width2 = (int)Math.floor(width/2);
        int height2 = (int)Math.floor(height/2);

        int a2 = width2*width2;
        int b2 = height2*height2;
        int fa2 = 4*a2;
        int fb2 = 4*b2;
        int y = height2;
        int sigma = 2*b2+a2*(1-2*height2);

        for(int x=0;b2*x<=a2*y;x++){
            arr[centerX+x][centerY+y] = 1;
            arr[centerX-x][centerY+y] = 1;
            arr[centerX+x][centerY-y] = 1;
            arr[centerX-x][centerY-y] = 1;

            if(sigma>=0){
                sigma+=fa2*(1-y);
                y--;
            }sigma += b2*((4*x)+6);
        }

        int x = width2;
        sigma = 2*a2+b2*(1-2*x);
        for(y=0;a2*y<=b2*x;y++){
            arr[centerX+x][centerY+y] = 1;
            arr[centerX-x][centerY+y] = 1;
            arr[centerX+x][centerY-y] = 1;
            arr[centerX-x][centerY-y] = 1;

            if(sigma>=0){
                sigma+=fb2*(1-x);
                x--;
            }sigma += a2*((4*y)+6);
        }

        for(x=0; x<=width2;x++){
            boolean endY = false;

            y=0;
            while (!endY){
                arr[centerX+x][centerY+y] = 1;
                arr[centerX-x][centerY+y] = 1;
                arr[centerX+x][centerY-y] = 1;
                arr[centerX-x][centerY-y] = 1;

                y++;
                try {
                    endY = arr[centerX + x][centerY + y] == 1;
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println(String.format("Arr out of bounds (skipping): x: %d\ty: %d",centerX + x,centerY + y));
                    endY = true;
                }
            }
        }
        mask.setMask(arr);
        return mask;
    }

    @Override
    public int[][] drawArray(int[][] arr, int value) {
        if(isBoundsInsideArr(arr)){
            Mask mask = getShapeMask(arr.length,arr[0].length);
            for(int x=startX;x<=startX+width;x++){
                for(int y=startY;y<=startY+height;y++){
                    if(mask.getMask()[x][y]==1){
                        arr[x][y] = value;
                    }
                }
            }return arr;
        }
        return new int[0][];
    }

    @Override
    public String toString() {
        return String.format("Ellipse --> x: %d\ty: %d\twidth: %d\t height: %d",startX,startY,width,height);
    }

    @Override
    public String getShapeName() {
        return "Ellipse";
    }

}
