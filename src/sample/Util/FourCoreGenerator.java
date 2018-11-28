package sample.Util;

import sample.Model.Canvas;
import sample.Model.Mask;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class FourCoreGenerator {

    private int height;
    private int width;

    private boolean checkDimensions = true;
    private ArrayList<Canvas> canvas;

    public FourCoreGenerator(ArrayList<Canvas> canvas){
        if(canvas.size() != 0){
            height = canvas.get(0).getHeight()/2;
            width = canvas.get(0).getWidth()/2;
            for(Canvas c : canvas){
                if(c.getHeight() != 2 * height || c.getWidth() != 2 * width){
                    this.checkDimensions = false;
                }
            }if(checkDimensions){
                this.canvas = new ArrayList<>();
                for(Canvas c : canvas){
                    this.canvas.add(new Canvas(c.getImageName(), c.getWidth(), c.getHeight(), c.getAllColours()));
                }
            }this.canvas = canvas;
        }else{
           this.checkDimensions = false;
        }
    }

    public FourCoreGenerator(Canvas c){
        height = c.getHeight()/2;
        width = c.getWidth()/2;
        canvas = new ArrayList<>();
        canvas.add(c);
    }

    public void drawSimultaneously(WriteBMP.RescaleType rescaleType){
        if (checkDimensions) {
            for(Canvas c : canvas){
                if(!c.hasBeenDrawn()){c.drawShapes();}
                Canvas innerCanvas = new Canvas(c.getImageName() + "_sim",width * 2, height * 2);
                innerCanvas.setColours(c.getAllColours());
                innerCanvas.mirrorQuarter();
                try{
                    WriteBMP.getInstance().printBMP(innerCanvas,rescaleType);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void drawIndividually(WriteBMP.RescaleType rescaleType){
        if(checkDimensions){
            for(Canvas c : canvas){
                if(!c.hasBeenDrawn()){c.drawShapes();}
                Canvas innerCanvas = new Canvas(c.getImageName(),width * 2, height * 2);
                innerCanvas.setColours(c.getAllColours());
                innerCanvas.mirrorQuarter();

                Canvas c1 = new Canvas(c.getImageName() + "_ind_TL", width * 2, height * 2, innerCanvas.getAllColours());
                Canvas c2 = new Canvas(c.getImageName() + "_ind_TR", width * 2, height * 2, innerCanvas.getAllColours());
                Canvas c3 = new Canvas(c.getImageName() + "_ind_BL", width * 2, height * 2, innerCanvas.getAllColours());
                Canvas c4 = new Canvas(c.getImageName() + "_ind_BR", width * 2, height * 2, innerCanvas.getAllColours());

                Rectangle r = new Rectangle(new RGB(1), RGB.OverlapType.Add,0,0,width,height);
                Mask m = r.getShapeMask(width,height);

                Mask tlMask = new Mask(width * 2,height * 2);
                Mask trMask = new Mask(width * 2,height * 2);
                Mask blMask = new Mask(width * 2,height * 2);
                Mask brMask = new Mask(width * 2,height * 2);

                tlMask.printMask(m,0,0);
                trMask.printMask(m,width,0);
                blMask.printMask(m,0,height);
                brMask.printMask(m,width,height);

                //ArrayHelper.printMask(tlMask);
                //System.out.println();
                //ArrayHelper.printMask(brMask);

                c1.printMask(tlMask,false);
                c2.printMask(trMask,false);
                c3.printMask(blMask,false);
                c4.printMask(brMask,false);

                try{
                    WriteBMP.getInstance().printBMP(c1,rescaleType);
                    WriteBMP.getInstance().printBMP(c2,rescaleType);
                    WriteBMP.getInstance().printBMP(c3,rescaleType);
                    WriteBMP.getInstance().printBMP(c4,rescaleType);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
