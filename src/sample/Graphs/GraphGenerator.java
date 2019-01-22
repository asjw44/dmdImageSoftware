package sample.Graphs;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class GraphGenerator {

    private String title = "";
    private String xLabel = "";
    private String yLabel = "";

    private ArrayList<SeriesData> axisData;
    private ArrayList<String> axisKeys;

    public interface Equation{
        Number createEquation(double x);
    }

    public GraphGenerator(){
        axisData = new ArrayList<>();
        axisKeys = new ArrayList<>();
    }


    public GraphGenerator setTitle(String title) {
        this.title = title;
        return this;
    }

    public GraphGenerator setxLabel(String xLabel) {
        this.xLabel = xLabel;
        return this;
    }

    public GraphGenerator setyLabel(String yLabel) {
        this.yLabel = yLabel;
        return this;
    }

    public GraphGenerator addData(SeriesData data){
        axisData.add(data);
        axisKeys.add("");
        return this;
    }

    public GraphGenerator addEquation(ArrayList<Number> xAxis, Equation equation, String equationLabel){
        ArrayList<Number> yAxis = new ArrayList<>();
        for(Number i : xAxis){
            yAxis.add(equation.createEquation(i.doubleValue()));
        }
        axisData.add(new SeriesData(xAxis,yAxis));
        axisKeys.add(equationLabel == null ? "" : equationLabel);
        return this;
    }

    public GraphGenerator addData(SeriesData data, String label){
        axisData.add(data);
        axisKeys.add(label);
        return this;
    }

    public ScatterChart<Number,Number> getScatterChart(){
        ScatterChart<Number, Number> chart = new ScatterChart<>(new NumberAxis(),new NumberAxis());
        chart.setBackground(new Background(new BackgroundFill(Paint.valueOf("#000000"),null,null)));
        chart.setTitle(title);
        chart.getXAxis().setLabel(xLabel);
        chart.getYAxis().setLabel(yLabel);
        for(int i=0;i<axisData.size();i++){
            XYChart.Series dataSeries = new XYChart.Series();
            for(int j=0;j<axisData.get(i).getxAxis().size();j++){
                XYChart.Data data = new XYChart.Data(axisData.get(i).getxAxis().get(j),axisData.get(i).getyAxis().get(j));
                dataSeries.getData().add(data);
                dataSeries.setName(axisKeys.get(i));
            }chart.getData().addAll(dataSeries);
        }return chart;
    }


}
