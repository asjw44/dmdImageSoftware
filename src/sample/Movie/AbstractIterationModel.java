package sample.Movie;

import org.jcodec.api.awt.AWTSequenceEncoder;

public abstract class AbstractIterationModel {

    public int start_x = 12;
    public int start_y = 12;
    public int width = 2;
    public int height = 2;
    public int iterationI = 1; //900
    public int iterationJ = 1; //558

    public int totalFrames;

    MovieData movieData;

    public abstract void writeFrames(AWTSequenceEncoder encoder, int currentFrame, int totalFrames);

    public abstract int getTotalFrames();

    void printPercentage(int current, int total){
        System.out.printf("%.4f%%\n", 100 * (double)current/(double) total);
    }

    void setMovieData(MovieData movieData) {
        this.movieData = movieData;
    }
}
