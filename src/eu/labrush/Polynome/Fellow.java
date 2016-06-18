package eu.labrush.Polynome;

import eu.labrush.AbstractFellow;
import eu.labrush.AbstractNature;

public class Fellow extends AbstractFellow {


    /**
     * On convertit simplement l'ADN binaire en entier
     */
    public int getFitness() {
        int x = 0 ;
        int tmp = 1 ;

        for(int i = 0 ; i < this.dna.length ; i++){
            if(this.dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x*(256 - x) ;

    }

}
