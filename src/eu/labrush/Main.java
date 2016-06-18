package eu.labrush;

import eu.labrush.Polynome.Nature;

public class Main {

    public static void main(String[] args) {
        Nature nature = new Nature();
        System.out.println(nature);

        for(int i = 0 ; i < 1000 ; i++){
            nature.evolve();
        }

        System.out.println(nature);
    }
}
