package sample.Model;

import sample.Util.WriteBMP;

public class IterationModel {

    public String path;
    public int start_x = 12;
    public int start_y = 12;
    public int width = 2;
    public int height = 2;
    public int iterationI = 1; //900
    public int iterationJ = 1; //558
    public int fps;

    public IterationModel(String filename, boolean fullPath){
        if (fullPath){
            this.path = filename;
        }else{
            this.path = WriteBMP.imageDirectory + filename;
        }
    }

}
