package eu.labrush.numfun.functions;

import eu.labrush.numfun.NumFunction;

import java.util.Arrays;

public class Shekel extends NumFunction {
    @Override
    public long f(int[] dna) {
        //On coupe l'argument en (x,y)
        int n = (int) ((double)dna.length / 2);
        int xi = getArguments(Arrays.copyOfRange(dna, 0, n));
        int yi = getArguments(Arrays.copyOfRange(dna, n+1, dna.length - 1));

        double x = ((double) xi) / Math.pow(2, n) ;
        double y = ((double) yi) / Math.pow(2, n) ;

        assert 0 <= x;
        assert x <= 1;
        assert 0 <= y ;
        assert y <= 1 ;

        double[] X = {x, y} ;

        double[][] A = {{0.5, 0.5}, {0.25, 0.25}, {0.25, 0.75}, {0.75, 0.25}, {0.75, 0.75}};
        double[] C = {0.002, 0.005, 0.005, 0.005, 0.005} ;

        double S = .0 ;

        for (int i = 0; i < A.length; i++) {
            S += C[i] ;

            for (int j = 0; j < A[i].length; j++) {
                S += Math.pow(X[j] - A[i][j], 2) ;
            }
        }

        return (long) (S * 100_000);
    }

    @Override
    public int getDNASIZE() {
        return 16;
    }
}
