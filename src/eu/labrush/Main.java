package eu.labrush;

import eu.labrush.Polynome.Fellow;
import eu.labrush.Polynome.Nature;

public class Main {
    
    public static void main(String[] args) {

        Nature nature = new Nature(21, 0.5, 0.05);
        System.out.println(nature);

        for(int i = 0 ; i < 1000 ; i++){
            nature.evolve();
        }

        System.out.println(nature);
        for (AbstractFellow f : nature.getPopulation()){
            System.out.print(((Fellow) f).getX() + " ");
        }
    }
}
