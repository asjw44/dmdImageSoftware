package sample.Util;

public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v){
        this.x = v.x;
        this.y = v.y;
    }

    public void set(Vector2 v){
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2 sub(Vector2 sub){
        return new Vector2(x-sub.x,y-sub.y);
    }

    public Vector2 add(Vector2 add){
        return new Vector2(x+add.x,y+add.y);
    }

    /**
     * Gives the dot product between this vector and v.
     * @param v Vector to calculate the new vector.
     * @return the dot product.
     */
    public float dot(Vector2 v){
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Gives the dot product of itself (ie v^2)
     * @return dot product of itself.
     */
    public float dot(){
        return this.x * this.x + this.y * this.y;
    }

    public float length(){
        return (float) Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
    }

    /**
     * Rotates this vector around a reference vector by the given radians.
     * Gives the result as a new vector (ie does not change this vector's properties)
     *
     * @param ref The reference vector (the axis for the rotation)
     * @param radians The angle of rotation in radians.
     * @return A new vector that is the location of this vector pivoted around the reference vector.
     */
    public Vector2 rotate(Vector2 ref, float radians){
        Vector2 r = sub(ref);

        float cos = (float)Math.cos(radians);
        float sin = (float)Math.sin(radians);

        float newX = r.x*cos - r.y*sin;
        float newY = r.x*sin + r.y*cos;

        Vector2 newVector = new Vector2(newX,newY);

        return newVector.add(ref);
    }

    @Override
    public String toString() {
        return String.format("(%.1f,%.1f)",x,y);
    }
}
