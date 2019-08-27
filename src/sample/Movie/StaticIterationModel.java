package sample.Movie;

import org.jcodec.api.awt.AWTSequenceEncoder;
import sample.Model.Canvas;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;
import sample.Util.WriteBMP;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class StaticIterationModel extends AbstractIterationModel{

    public double timeOnScreen = -1;

    @Override
    public void writeFrames(AWTSequenceEncoder encoder, int currentFrame, int totalFrames) {
        int x0 = start_x;
        int y0 = start_y;

        int w = width;
        int h = height;

        if(timeOnScreen > 0){
            iterationI = 1;
            iterationJ = (int) (timeOnScreen * movieData.fps);
        }

        Canvas canvas = new Canvas();
        canvas.addShape(new Rectangle(RGB.white(), RGB.OverlapType.Add, x0,y0,w,h));
        canvas.drawShapes();

        BufferedImage image = null;
        try {
            image = WriteBMP.getInstance().getBufferedImage(canvas,WriteBMP.RescaleType.START);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < iterationI; i++){
            for(int j = 0; j < iterationJ; j++){
                currentFrame++;
                printPercentage(currentFrame,totalFrames);
                try {
                    assert image != null;
                    encoder.encodeImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getTotalFrames() {
        if(timeOnScreen > 0){
            iterationI = 1;
            iterationJ = (int) (timeOnScreen * movieData.fps);
            totalFrames = iterationJ;
            return iterationJ;
        }else{
            totalFrames = iterationJ * iterationI;
            return totalFrames;
        }

    }
}
