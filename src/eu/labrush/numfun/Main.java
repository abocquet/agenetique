package eu.labrush.numfun;

import eu.labrush.numfun.functions.Shekel;

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

        Nature nature = new Nature(150, 0.7, 0.05, 0.4, new Shekel());
        System.out.println(nature);

        for(int i = 0 ; i < 100 ; i++){
            nature.evolve();
            System.out.println(nature.getBest());
        }
        System.out.println(nature);

    }
}
