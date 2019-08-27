package sample.Movie;

import org.jcodec.api.awt.AWTSequenceEncoder;
import sample.Model.Canvas;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;
import sample.Util.WriteBMP;

import java.io.IOException;

public class ScanIterationModel extends AbstractIterationModel {

    @Override
    public void writeFrames(AWTSequenceEncoder encoder, int currentFrame, int totalFrames) {
        int x0 = start_x;
        int y0 = start_y;

        int w = width;
        int h = height;

        for(int j = 0; j < iterationJ; j++){
            for(int i = 0; i < iterationI; i++){
                currentFrame++;
                printPercentage(currentFrame,totalFrames);
                Canvas canvas = new Canvas();
                canvas.addShape(new Rectangle(RGB.white(), RGB.OverlapType.Add,x0 + w*i,y0 + h*j, w,h));
                canvas.drawShapes();
                try {
                    encoder.encodeImage(WriteBMP.getInstance().getBufferedImage(canvas,WriteBMP.RescaleType.START));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getTotalFrames() {
        totalFrames = iterationI * iterationJ;
        return totalFrames;
    }


}
