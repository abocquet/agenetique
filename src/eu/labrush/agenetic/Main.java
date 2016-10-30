package eu.labrush.agenetic;

import eu.labrush.traveller.Nature;
import eu.labrush.traveller.data.Berlin52;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

public class Main {

    public static void main(String[] args) {

        Nature nature = new Nature(20, 0.5, 0.05, new Berlin52(), new Order1(), new im());
        System.out.println(nature);

        for(int i = 0 ; i < 10_000 ; i++){
            if(i % 100 == 0) System.out.println(nature.getShortest());
            nature.evolve();
        }
        System.out.println("");
        System.out.println(nature);
        System.out.println(nature.introduceYourself());

        /*eu.labrush.agenetic.numfun.Nature nature = new eu.labrush.agenetic.numfun.Nature(21, 0.5, 0.05, new NumFunction() {
            @Override
            public int f(int x) {
                return x*(256-x);
            }

            @Override
            public int getDNASIZE() {
                return 8;
            }
        });

        System.out.println(nature);

        for(int i = 0 ; i < 10 ; i++){
            nature.evolve();
            System.out.println(nature.getBest());
        }
        System.out.println(nature);
        System.out.println(nature.introduceYourself());*/

    }
}
