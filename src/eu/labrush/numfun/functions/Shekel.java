package eu.labrush.numfun.functions;

import eu.labrush.numfun.NumFunction;

public class Shekel extends NumFunction {
    @Override
    public int f(int arg) {
        //On coupe l'argument en (x,y)

        int xi = arg % (2 >> getDNASIZE()/2); // Todo: vérifier que 10010111 devient bien 1001 et 0111
        int yi = arg >> getDNASIZE()/2 ;


        double x = 100 / ((double) xi) ;
        double y = 100 / ((double) yi) ;

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

        return (int) S * 100;
    }

    @Override
    public int getDNASIZE() {
        return 16;
    }
}
