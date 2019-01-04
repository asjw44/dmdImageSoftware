package sample.Util;

import java.util.ArrayList;

public class GeometryUtil {

    public static Vector2 getCenter(ArrayList<Vector2> vList){

        float x = 0;
        float y = 0;

        for(Vector2 v : vList){
            x += v.x;
            y += v.y;
        }

        return new Vector2(x/vList.size(),y/vList.size());
    }

    /**
     * Gives the angle (in radians) to the vector (1,0), or the unit x vector, of the vector v1-v2.
     * @param v1 First vector.
     * @param v2 Vector being subtracted.
     * @return The angle to the x axis in radians.
     */

    public static float getAngleToX(Vector2 v1, Vector2 v2){
        Vector2 diffV = v1.sub(v2);
        return (float) Math.acos(diffV.x /(diffV.length()));
    }

    public static float getAngleToY(Vector2 v1, Vector2 v2){
        Vector2 diffV = v1.sub(v2);
        return (float) Math.acos(diffV.y/diffV.length());
    }

    public static float toDegrees(float radians){
        return radians * 180 / (float) Math.PI;
    }

    public static float toRadians(float degrees){
        return degrees * (float) Math.PI / 180;
    }

}
