package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

//TODO: TESTS

/**
 * On tire une table de référence binaire et on rempli l'enfant suivant la règle
 * ref = 0 -> parent 1
 * ref = 1 -> parent 2
 */
public class Uniform implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int[] reference = new int[DNASIZE] ;
        for (int i = 0; i < DNASIZE; i++) {
            reference[i] = (int) (Math.random() * 100) % 2 ;
        }

        for(int i = 0 ; i < 2 ; i++) {
            int[] dna = new int[DNASIZE] ;
            int[] c = new int[]{0, 0} ;

            Arrays.fill(dna, -1);

            for (int j = 0; j < DNASIZE; j++) {
                int r = reference[j];

                while(arrayContains(dna, parents[r].getDNA(c[r]))){
                    c[r] = (c[r] + 1) % DNASIZE ;
                }

                dna[j] = parents[r].getDNA(c[r]);
                c[r] = (c[r] + 1) % DNASIZE ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, true);
        }

        return new Tuple<>(children[0], children[1]);
    }

    private static boolean arrayContains(int[] u, int e){
        for(int i = 0 ; i < u.length ; i++){
            if(u[i] == e) return true ;
        }

        return false ;
    }
}
