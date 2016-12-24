package eu.labrush.traveller.data;

import java.math.BigInteger;

public class PointSet {

    int x[], y[] ;
    String name, desc ;
    BigInteger minDist ;

    public PointSet(String name, String desc, BigInteger minDist, int[] x, int[] y) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.desc = desc;
        this.minDist = minDist;
    }

    public Point[] getPoints() {

        Point[] list = new Point[x.length] ;

        for(int i = 0, c = x.length ; i < c ; i++){
            list[i] = new Point(x[i], y[i]);
        }

        return list;
    }

    public String introduceYourself() {
        return "Hello, I'm " + getName() + ", " + getDesc() + " my best known score is " + getMinDist() ;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public BigInteger getMinDist() {
        return minDist;
    }
}
