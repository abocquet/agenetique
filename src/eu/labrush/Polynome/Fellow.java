package eu.labrush.Polynome;

import eu.labrush.AbstractFellow;

import java.util.Arrays;

public class Fellow extends AbstractFellow {

    public Fellow() {
        super();
    }

    public Fellow(int[] dna){
        super(dna);
    }

    /**
     * On convertit simplement l'ADN binaire en entier
     * puis on l'injecte dans le polynome -X**2 + 256X
     */
    public int getFitness() {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = 0 ; i < DNASIZE ; i++){
            if(this.dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        int res = x*(256 - x);

        if(res < 0){
            System.out.println("Error: negative fitness: " + res);
            System.out.println("Antécedent = " + x);
            System.out.println("DNA = " + Arrays.toString(this.dna));
            System.exit(-1);
        }

        return res ;
    }

    public int getX(){
        int x = 0 ;
        int tmp = 1 ;
        for(int i = 0 ; i < DNASIZE ; i++){
            if(this.dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x ;
    }

}
