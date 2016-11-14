package eu.labrush.traveller;

import eu.labrush.traveller.data.Berlin52;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

public class Main {

    public static void main(String[] args) {

        Nature nature = new Nature(20, 0.5, 0.05, new Berlin52(), new Order1(), new im());
        //System.out.println(nature);

        Logger logger = new Logger("traveller.csv", nature);

        for(int i = 0, N = 10_000, p = 10 ; i < N+1 ; i++){
            if(i % p == 0) {
                logger.log();
                System.out.println(i/p + " / " + N/p);
            }
            nature.evolve();
        }
        //System.out.println("");
        //System.out.println(nature);
        //System.out.println(nature.introduceYourself());


    }
}
