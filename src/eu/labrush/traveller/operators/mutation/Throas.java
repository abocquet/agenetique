package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

//TODO: TESTS
/**
 * Translate n points consécutifs dans le gène
 */
public class Throas implements MutationInterface {
    public int actOn = 3 ;

    @Override
    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {
        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= pmutation){

                int start = (int) (Math.random() * 2 * DNASIZE) % DNASIZE ;
                int tmp = f.getDNA(DNASIZE - 1);

                for (int i = 0; i < actOn - 1; i++) {
                    f.setDNA(start + i, f.getDNA((start + i + 1) % DNASIZE));
                }

                f.setDNA(0, tmp);

            }
        }

    }
}
