package sample.Util;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import sample.Model.Canvas;
import sample.Model.IterationModel;
import sample.Model.Shapes.RGB;
import sample.Model.Shapes.Rectangle;

import java.io.File;
import java.io.IOException;

public class MovieGenerator {

    private static MovieGenerator movieGenerator;

    public static MovieGenerator getInstance(){
        if(movieGenerator == null){
            movieGenerator = new MovieGenerator();
        }return movieGenerator;
    }

    public void generateMovie(IterationModel iterationModel) throws IOException{
        int start_x = iterationModel.start_x;
        int start_y = iterationModel.start_y;
        int width = iterationModel.width;
        int height = iterationModel.height;
        SeekableByteChannel out = null;
        File file = new File(iterationModel.path);
        long start = System.currentTimeMillis();
        try{
            out = NIOUtils.writableFileChannel(file.getPath());
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(iterationModel.fps, 1));
            for (int j = 0; j < iterationModel.iterationJ; j++) { //558
                for(int i = 0; i < iterationModel.iterationI; i++){ //900
                    System.out.printf("%d\t%d\n",i,j);
                    Canvas canvas = new Canvas("");
                    Rectangle r = new Rectangle(RGB.white(), RGB.OverlapType.Add,start_x + width*i,start_y + height*j, width,height);
                    canvas.addShape(r);
                    if(canvas.drawShapes()){
                        encoder.encodeImage(WriteBMP.getInstance().getBufferedImage(canvas, WriteBMP.RescaleType.START));
                    }else{
                        System.out.println("Issue with: i = " + i + " and j = " + j);
                    }
                }
            }
            encoder.finish();
        }finally {
            NIOUtils.closeQuietly(out);
        }
        long end = System.currentTimeMillis();
        System.out.println(TimeHelper.getTimeString(end-start));
    }
}
