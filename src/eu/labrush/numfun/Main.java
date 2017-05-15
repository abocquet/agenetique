package eu.labrush.numfun;

import eu.labrush.numfun.functions.Rosenbrock;

public class Main {

    public static void main(String[] args) {

        /*eu.labrush.numfun.Nature nature = new eu.labrush.numfun.Nature(21, 0.5, 0.05, new NumFunction() {
            @Override
            public int f(int x) {
                return x*(256-x);
            }

            @Override
            public int getDNASIZE() {
                return 8;
            }
        });*/

        Nature nature = new Nature(10, 0.5, 0.05, new Rosenbrock());

        System.out.println(nature);

        for(int i = 0 ; i < 10 ; i++){
            nature.evolve();
            System.out.println(nature.getBest());
        }
        System.out.println(nature);

    }
}
