package sample.Movie;

import sample.Util.WriteBMP;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieData {

    String path;
    public WriteBMP.RescaleType rescaleType = WriteBMP.RescaleType.START;
    public int fps = 60;

    private ArrayList<AbstractIterationModel> abstractIterationModels = new ArrayList<>();

    public MovieData(String path, boolean fullPath){
        if(fullPath){
            this.path = path;
        }else{
            this.path = WriteBMP.imageDirectory + path;
        }
    }

    public void setAbstractIterationModels(ArrayList<AbstractIterationModel> abstractIterationModels) {
        this.abstractIterationModels = abstractIterationModels;
        giveThis();
    }

    public void setAbstractIterationModels(AbstractIterationModel... models){
        abstractIterationModels.addAll(Arrays.asList(models));
        giveThis();
    }

    ArrayList<AbstractIterationModel> getAbstractIterationModels() {
        return abstractIterationModels;
    }

    private void giveThis(){
        for(AbstractIterationModel iterationModel : abstractIterationModels){
            iterationModel.setMovieData(this);
        }
    }
}
