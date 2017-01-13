package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

//TODO: TESTS

/**
 * Coupe le chromosome en deux et fait une mutation miroir de
 * chaque élément de la partion
 */
public class Cim implements MutationInterface {
    @Override
    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= pmutation){
                int cut = (int) (Math.random() * 2 * DNASIZE) % (DNASIZE - 1) ;

                Cim.mirror(f, 0, cut);
                Cim.mirror(f, cut + 1, DNASIZE-1);
            }
        }

    }

    static public void mirror(AbstractFellow f, int start, int end){
        if(end < start){
            int tmp = start ;
            start = end ;
            end = tmp ;
        }

        for(int i = 0 ; i <= (end - start)/2 ; i++){
            int tmp = f.getDNA(start + i) ;
            f.setDNA(start+i, f.getDNA(end - i));
            f.setDNA(end-i, tmp) ;
        }
    }
}
