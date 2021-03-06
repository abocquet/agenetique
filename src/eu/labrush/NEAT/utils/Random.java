package eu.labrush.NEAT.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Random {

    static java.util.Random r = new java.util.Random();

    /**
     * @param max
     * @return a number in [0, max - 1]
     */
    public static int randInt(int max){
        if(max == 0){
            return 0 ;
        }

        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

    public static Object random(List u){
        return u.get(randInt(u.size()));
    }

    public static Object random(Collection u){
        return random(new ArrayList(u));
    }

    public static double gauss(double stdev, double max, double min){
        double v = r.nextGaussian() * stdev ;
        if(v < min){
            return min ;
        } else if (v > max){
            return max ;
        } else {
            return v ;
        }
    }

}
