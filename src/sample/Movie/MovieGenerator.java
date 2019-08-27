package sample.Movie;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import sample.Util.TimeHelper;

import java.io.File;
import java.io.IOException;

public class MovieGenerator {

    private static MovieGenerator movieGenerator;

    public static MovieGenerator getInstance(){
        if(movieGenerator == null){
            movieGenerator = new MovieGenerator();
        }return movieGenerator;
    }

    public void generateMovie(MovieData movieData){
        SeekableByteChannel out = null;
        File file = new File(movieData.path);
        long start = System.currentTimeMillis();
        int totalFrames = 0;
        int currentFrame = 0;
        for(AbstractIterationModel iterationModel : movieData.getAbstractIterationModels()){
            totalFrames += iterationModel.getTotalFrames();
        }
        try{
            out = NIOUtils.writableFileChannel(file.getPath());
            AWTSequenceEncoder encoder = new AWTSequenceEncoder(out, Rational.R(movieData.fps, 1));
            for(AbstractIterationModel iterationModel : movieData.getAbstractIterationModels()){
                iterationModel.writeFrames(encoder, currentFrame,totalFrames);
                currentFrame += iterationModel.getTotalFrames();
            }
            encoder.finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            NIOUtils.closeQuietly(out);
        }
        long end = System.currentTimeMillis();
        System.out.println(TimeHelper.getTimeString(end-start));
    }
}
