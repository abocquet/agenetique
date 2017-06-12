package eu.labrush.traveller.compare;

import eu.labrush.NEAT.utils.Random;
import eu.labrush.traveller.data.Point;
import eu.labrush.traveller.data.PointSet;

public class AnnealingSolver implements TSPSolver {

    protected PointSet set ;

    private int i1, i2 ;
    private boolean swapped = false ;
    protected int[] path ;

    @Override
    public long solveSet(PointSet set, double error, long maxtime) {

        long t1 = System.currentTimeMillis() ;

        this.set = set ;

        double Ti= 1000, T0 = 1 ;
        double coolingRate = 0.0001;
        double dist = Double.POSITIVE_INFINITY;

        Point[] points = set.getPoints() ;
        path = new int[points.length];

        for (int i = 0; i < path.length; i++) {
            path[i] = i ;
        }

        for (int i = 0; i < path.length; i++) {
            randomSwap();
        }

        while(dist > set.getMinDist() * (1 + error) && System.currentTimeMillis() - t1 <= maxtime) {
            double T = Ti ;
            int k = 0 ;
            while (T > T0) {
                randomSwap();

                double newDist = calcDist();
                double delta = newDist - dist ;
                if (delta > 0) { // Si on gagne en distance, on ne fait rien
                    double p = Math.random();
                    if (p >=  Math.exp(-delta / T)) { // on détermine aléatoirement si on revient à l'ancienne solution
                        unswap();
                    } else {
                        dist = newDist ;
                    }
                } else {
                    dist = newDist ;
                }

                k++ ;
                T *= 1 - coolingRate;
            }
            //System.out.println(calcDist());
        }


        //System.out.println((dist > set.getMinDist() * (1 + error)) + " " + (System.currentTimeMillis() - t1 <= maxtime));
        //System.out.print(dist);

        long t2 = System.currentTimeMillis();

        return t2 - t1;
    }

    protected double calcDist(){
        long distance  = 0 ;

        for(int i = 0 ; i < path.length - 1 ; i ++){
            distance += set.distBetween(path[i], path[i+1]);
        }

        distance += set.distBetween(path[0], path[path.length - 1]);

        return distance ;
    }

    private void randomSwap() {
        i1 = Random.randInt(path.length);
        i2 = Random.randInt(path.length);

        swap(i1, i2);
        swapped = true ;
    }

    private void unswap(){

        if(swapped){
            swapped = false ;
            swap(i1, i2);
            i1 = i2 ;
        }
    }

    private void swap(int i1, int i2){
        int tmp = this.path[i1] ;
        this.path[i1] = this.path[i2] ;
        this.path[i2] = tmp ;
    }


}
