package sample.Util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.Model.PowerMeterData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {

    private static ReadFile ourInstance = new ReadFile();

    public static ReadFile getInstance(){return ourInstance;}

    private ReadFile(){}

    public ArrayList<String[]> getData(){
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("Load file");
        ArrayList<String[]> data = new ArrayList<>();
        if (Window.getWindows().size() > 0) {
            File file = fileDialog.showOpenDialog(Window.getWindows().get(0));
            try{
                BufferedReader in = new BufferedReader(new FileReader(file.getPath()));
                String str;
                while((str = in.readLine()) != null){
                    data.add(str.split("\\t"));
                }in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            data.remove(0);
            data.remove(0);
        }
        return data;
    }

    public PowerMeterData getMeterData(){
        PowerMeterData powerMeterData = new PowerMeterData();

        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("Load file");
        ArrayList<String[]> data = new ArrayList<>();
        if(Window.getWindows().size()>0){
            File file = fileDialog.showOpenDialog(Window.getWindows().get(0));
            try{
                BufferedReader in = new BufferedReader(new FileReader(file.getPath()));
                String str;
                while((str = in.readLine()) != null){
                    data.add(str.split("\\t"));
                }in.close();

                if (data.size() > 2) {
                    data.remove(0);
                    data.remove(0); //Removing the first two lines.
                }else{
                    powerMeterData.notifyError();
                    System.err.println("Data too small");
                }

                powerMeterData.setFileName(file.getName());
                powerMeterData.setFilePath(file.getPath());

                powerMeterData.setRawData(data);


            }catch (IOException e){
                e.printStackTrace();
                System.err.println("File error");
                powerMeterData.notifyError();
            }catch (NullPointerException e){
                e.printStackTrace();
                System.err.println("Null pointer exception on file: " + e.getMessage()) ;
                powerMeterData.notifyError();
            }

        }else{
            System.err.println("No windows found");
            powerMeterData.notifyError();
        }

        return powerMeterData;
    }

}
