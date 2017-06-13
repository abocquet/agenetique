package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

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
                int tmp = f.getDNA(start);

                for (int i = 0; i < actOn - 1; i++) {
                    f.setDNA((start + i) % DNASIZE, f.getDNA((start + i + 1) % DNASIZE));
                }

                f.setDNA((start + actOn - 1) % DNASIZE, tmp);

            }
        }

    }
}
