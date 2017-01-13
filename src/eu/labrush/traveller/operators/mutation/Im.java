package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;


/**
 * Inverse un segment du chromosome
 */
public class Im implements MutationInterface {

    @Override
    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= pmutation){
                int s1 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE ;
                int s2 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE ;

                Cim.mirror(f, s1, s2);
            }
        }
    }
}
