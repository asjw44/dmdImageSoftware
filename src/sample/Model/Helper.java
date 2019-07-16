package sample.Model;

import java.util.ArrayList;

public class Helper {

    public static ArrayList<Integer> getGradPeaks(ArrayList<Number> arr, Number threshold){
        double[] gradArr = getGradArray(arr);
        ArrayList<Integer> returnArr = new ArrayList<>();

        for (int i = 0; i < gradArr.length; i++) {
            if(Math.abs(gradArr[i]) > threshold.doubleValue()){
                returnArr.add(i);
            }
        }return returnArr;
    }

    private static double[] getGradArray(ArrayList<Number> arr){
        double[] returnList = new double[arr.size()-1];
        for(int i=0;i<returnList.length;i++){
            returnList[i] = arr.get(i+1).doubleValue() - arr.get(i).doubleValue();
        }return returnList;
    }

}
