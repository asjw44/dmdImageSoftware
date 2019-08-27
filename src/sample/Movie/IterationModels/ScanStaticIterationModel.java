package sample.Movie.IterationModels;

import org.jcodec.api.awt.AWTSequenceEncoder;
import sample.Model.Canvas;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;
import sample.Util.WriteBMP;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScanStaticIterationModel extends AbstractIterationModel {

    public double pauseTime = -1;

    @Override
    public void writeFrames(AWTSequenceEncoder encoder, int currentFrame, int totalFrames) {
        int w = width;
        int h = height;
        int totalWidth = iterationI * w;
        int totalHeight = iterationJ * h;

        int x0 = start_x;
        int y0 = start_y;

        int iterationK = 0;

        if(pauseTime > 0){
            iterationK = (int)(pauseTime * movieData.fps);
        }

        Canvas c = new Canvas();
        c.addShape(new Rectangle(RGB.white(), RGB.OverlapType.Add, x0, y0, totalWidth,totalHeight));
        c.drawShapes();
        try {
            BufferedImage fullImage = WriteBMP.getInstance().getBufferedImage(c, movieData.rescaleType);

            for (int j = 0; j < iterationJ; j++) {
                System.out.printf("\n============J: %d===========\n\n",j);
                for (int i = 0; i < iterationI; i++) {
                    currentFrame++;
                    printPercentage(currentFrame,totalFrames);
                    Canvas canvas = new Canvas();
                    canvas.addShape(new Rectangle(RGB.white(), RGB.OverlapType.Add,x0 + w*i,y0 + h*j, w,h));
                    canvas.drawShapes();
                    encoder.encodeImage(WriteBMP.getInstance().getBufferedImage(canvas,movieData.rescaleType));
                }

                for (int k = 0; k < iterationK; k++) {
                    currentFrame++;
                    printPercentage(currentFrame,totalFrames);
                    encoder.encodeImage(fullImage);
                }
            }

        }catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public int getTotalFrames() {
        totalFrames = iterationI * iterationJ + (pauseTime > 0 ? (int)(pauseTime * movieData.fps) * iterationJ : 0);
        return totalFrames;
    }
}
