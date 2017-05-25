package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

//TODO: TESTS

/**
 * Echange deux genes de position
 */
public class Twor implements MutationInterface {
    @Override
    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= pmutation ){
                int s1 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE ;
                int s2 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE ;

                int tmp = f.getDNA(s1);
                f.setDNA(s1, f.getDNA(s2));
                f.setDNA(s2, tmp);
            }
        }

    }
}
