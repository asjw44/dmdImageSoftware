package sample.Graphs;

import java.util.ArrayList;

public class SeriesData {

    private ArrayList<Number> xAxis = new ArrayList<>();
    private ArrayList<Number> yAxis = new ArrayList<>();

    public SeriesData(ArrayList<Number> xData, ArrayList<Number> yData){
        this.xAxis = xData;
        this.yAxis = yData;
    }

    public SeriesData(ArrayList<Number> yData){
        this.yAxis = yData;
        generateX();
    }

    public SeriesData(int[] yData){
        ArrayList<Number> y = new ArrayList<>();
        for(int i : yData){
            y.add(i);
        }this.yAxis = y;
        generateX();
    }

    public ArrayList<Number> getxAxis() {
        return xAxis;
    }

    public ArrayList<Number> getyAxis() {
        return yAxis;
    }

    private void generateX(){
        if(yAxis != null && yAxis.size() > 0) this.xAxis = GraphHelper.generateAxisInt(0,yAxis.size(),1);
    }
}
