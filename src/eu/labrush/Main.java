package eu.labrush;

import eu.labrush.traveller.Nature;
import eu.labrush.traveller.data.Berlin52;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Nature nature = new Nature(21, 0.5, 0.05, new Berlin52(), new Order1(), new im());
        System.out.println(nature);

        for(int i = 0 ; i < 1_000_000 ; i++){
            if(i % 100_000 == 0) System.out.println(nature.getShortest());
            if(i % 10_000 == 0) System.out.print(".");
            nature.evolve();
        }
        System.out.println("");
        System.out.println(nature);
        System.out.println(nature.introduceYourself());

    }
}
