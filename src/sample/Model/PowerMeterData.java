package sample.Model;

import java.util.ArrayList;

public class PowerMeterData {

    private String fileName = "";
    private String filePath = "";

    private ArrayList<String[]> rawData = new ArrayList<>();
    private ArrayList<Number> numberData = new ArrayList<>();

    private boolean dataError = false;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<String[]> getRawData() {
        return rawData;
    }

    public void setRawData(ArrayList<String[]> rawData) {
        this.rawData = rawData;
    }

    public void notifyError(){
        dataError = true;
    }

    public boolean getError(){
        return dataError;
    }

    public ArrayList<Number> getNumberData() {
        if(numberData.size() == 0 && rawData.size() > 0){
            for(String[] aData : rawData){
                try {
                    numberData.add(Double.parseDouble(aData[1].toLowerCase()));
                }catch (ArrayIndexOutOfBoundsException e){
                    notifyError();
                    break;
                }
            }
        }return numberData;
    }

    public void printData(){
        for(String[] s : rawData){
            StringBuilder sIn = new StringBuilder();
            for(String s1 : s){
                sIn.append(s1).append("\t");
            }System.out.println(sIn);
        }
    }

    public void sort(){
        numberData.sort((o1, o2) -> o1.doubleValue() < o1.doubleValue() ? -1 : o1.doubleValue() == o2.doubleValue() ? 0 : 1);
    }

}
