package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Random {

    static java.util.Random r = new java.util.Random();

    /**
     * @param max
     * @return a number in [0, max - 1]
     */
    static int random(int max){
        if(max == 0){
            return 0 ;
        }

        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

    static Object random(List u){
        return u.get(random(u.size()));
    }

    static Object random(Collection u){
        return random(new ArrayList(u));
    }

    static double gauss(double stdev, double max, double min){
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
