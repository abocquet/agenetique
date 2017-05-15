package eu.labrush.numfun.functions;

import eu.labrush.numfun.NumFunction;

public class Rosenbrock extends NumFunction {
    @Override
    public int f(int arg) {
        //On coupe l'argument en (x,y)

        int xi = arg % (2 >> getDNASIZE() / 2); // Todo: vérifier que 10010111 devient bien 1001 et 0111
        int yi = arg >> getDNASIZE() / 2;

        double x = 100 / ((double) xi);
        double y = 100 / ((double) yi);

        double[] X = {x, y};

        return (int) (100 * Math.pow(1 - x, 2) + 100 * Math.pow(x - y, 2));
    }

    @Override
    public int getDNASIZE() {
        return 16;
    }
}