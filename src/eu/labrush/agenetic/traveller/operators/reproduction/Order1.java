package eu.labrush.agenetic.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.traveller.TravelFactory;
import eu.labrush.agenetic.traveller.operators.ReproductionOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Order1 implements ReproductionOperator {

    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female, TravelFactory factory) {

        AbstractFellow[] parents = new AbstractFellow[]{male, female};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = male.getDNASIZE();
        int offset = 0 ;

        for(int i = 0 ; i < 2 ; i++) {
            int s1 = (int) (Math.random() * 1000) % DNASIZE;
            int s2 = (int) (Math.random() * 1000) % DNASIZE;

            if(s2 < s1){
                int tmp = s2 ;
                s2 = s1 ;
                s1 = tmp ;
            }

            int[] dna = new int[DNASIZE];
            Arrays.fill(dna, -1);

            for(int j = s1 ; j < s2 ; j++){
                dna[j] = parents[offset].getDna()[j] ;
            }

            int c = s2 ; // The cursor that points on the second parent dna
            int[] completion_dna = parents[(offset + 1) %  2].getDna() ;

            for(int j = s2 ; j < DNASIZE ; j++){
                while(arrayContains(dna,  completion_dna[c], DNASIZE)){
                    c = (c+1) % DNASIZE ;
                }

                dna[j] =  completion_dna[c];
                c = (c+1) % DNASIZE ;
            }

            for(int j = 0 ; j < s1 ; j++){
                while(arrayContains(dna,  completion_dna[c], DNASIZE)){
                    c = (c+1) % DNASIZE ;
                }

                dna[j] =  completion_dna[c];
                c = (c+1) % DNASIZE ;
            }

            children[i] = factory.newInstance(dna, true);

            /* We swap both parents so the second child has repaired DNA from the original father */
            offset++ ;
        }

        return new Tuple<>(children[0], children[1]);

    }

    private boolean arrayContains(int[] u, int e, int size){
        for(int i = 0 ; i < size ; i++){
            if(u[i] == e) return true ;
        }

        return false ;
    }
}
