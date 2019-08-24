package sample.Util;

public class TimeHelper {

    public static String getTimeString(long t){
        long seconds = (t/1000);
        int minutes = (int) Math.floor(seconds / 60);
        if(minutes > 0) {
            int remainingSeconds = (int) seconds % minutes;
            return minutes + " minutes " + remainingSeconds + " seconds.";
        }return seconds + " seconds.";
    }

}
