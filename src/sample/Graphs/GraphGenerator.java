package sample.Graphs;

import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Series;

import java.util.ArrayList;

import static javafx.scene.chart.XYChart.*;

public class GraphGenerator {

    private String title = "";
    private String xLabel = "";
    private String yLabel = "";

    private ArrayList<SeriesData> axisData;
    private ArrayList<String> axisKeys;

    public interface Equation{
        Number createEquation(double x);
    }

    public interface SeriesClicked{
        void onSeriesClicked(int dataId, Data data);
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
        return scatterInternal(new ScatterChart<>(new NumberAxis(),new NumberAxis()), null);

    }

    public void writeChart(ScatterChart<Number,Number> chart, SeriesClicked seriesClicked){
        scatterInternal(chart, seriesClicked);
    }

    private ScatterChart<Number,Number> scatterInternal(ScatterChart<Number, Number> chart,final SeriesClicked seriesClicked){
        chart.setTitle(title);
        chart.getXAxis().setLabel(xLabel);
        chart.getYAxis().setLabel(yLabel);

        chart.setAnimated(false);

        for(int i=0;i<axisData.size();i++){
            Series dataSeries = new Series();

            dataSeries.setName(axisKeys.get(i));

            chart.getData().add(dataSeries);

            final int iInner = chart.getData().size()-1;

            for(int j=0;j<axisData.get(i).getxAxis().size();j++){
                final Data data = new Data(axisData.get(i).getxAxis().get(j),axisData.get(i).getyAxis().get(j));
                dataSeries.getData().add(data);
                data.getNode().setOnMouseClicked(event -> {
                    if(seriesClicked != null){
                        seriesClicked.onSeriesClicked(iInner, data);
                    }
                });
            }
        }

        chart.getXAxis().setAutoRanging(true);

        for(Axis.TickMark<Number> t : chart.getXAxis().getTickMarks()){
            t.setValue(t.getValue().intValue() * 3);

            t.setLabel(t.getValue().toString() + " L");

            System.out.println(t.getValue().toString() + "\t" + t.getLabel() + "\t" + t.getPosition());
        }
        return chart;
    }

    public void clearAll(ScatterChart<Number, Number> chart){
        chart.getData().clear();
        axisData = new ArrayList<>();
        axisKeys = new ArrayList<>();
    }


}
