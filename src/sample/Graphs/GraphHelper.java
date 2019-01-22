package sample.Graphs;

import java.util.ArrayList;

public class GraphHelper {

    public static int[] generateXInt(int size, int start, int interval){
        int[] xAxis = new int[size];

        for(int i=0;i<xAxis.length;i++){
            xAxis[i] = start+i*interval;
        }return xAxis;
    }

    public static ArrayList<Double> generateAxisDouble(double start, double end, double interval){
        ArrayList<Double> doubleArrayList = new ArrayList<>();

        for(double i=start;i<=end;i+=interval){
            doubleArrayList.add(i);
        }
        return doubleArrayList;
    }

    public static ArrayList<Number> generateAxisInt(int start, int end, int interval){
        ArrayList<Number> integerArrayList = new ArrayList<>();

        for(int i=start;i<end;i+=interval){
            integerArrayList.add(i);
        }return integerArrayList;
    }

}
