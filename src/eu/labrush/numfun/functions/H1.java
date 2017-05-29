package eu.labrush.numfun.functions;

import eu.labrush.numfun.NumFunction;

import java.util.Arrays;

import static java.lang.Math.*;

public class H1 extends NumFunction {
    @Override
    public long f(int[] dna) {
        //On coupe l'argument en (x,y)
        int n = (int) ((double)dna.length / 2);
        int xi = getArguments(Arrays.copyOfRange(dna, 0, n));
        int yi = getArguments(Arrays.copyOfRange(dna, n+1, dna.length - 1));

        double x = 200 * xi / pow(2, n) - 100 ;
        double y = 200 * yi / pow(2, n) - 100 ;

        assert x <= 100 ;
        assert x >= -100;
        assert y <= 100;
        assert y >= -100;

        double res = pow(sin(x - y/8), 2) + pow(sin(y + x/8), 2);
        res /= sqrt(pow(x-8.6, 2) + pow(y-6.7, 2)) + 1 ;

        return (long) (100_000_000 * res) ;
    }

    @Override
    public int getDNASIZE() {
        return 32;
    }
}