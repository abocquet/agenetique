package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

import java.util.Arrays;


/**
 * Translate n points non consécutifs dans le gène
 */
public class Thrors implements MutationInterface {

    public int actOn = 3 ; // Le nombre de genes à modifier

    @Override
    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= pmutation){

                int[] points = new int[actOn] ;
                for (int i = 0; i < actOn; i++) {
                    points[i] = (int) (Math.random() * 2 * DNASIZE) % DNASIZE;
                }

                Arrays.sort(points);

                int tmp = f.getDNA(points[0]);

                for (int i = 0; i < actOn - 1; i++) {
                    f.setDNA(points[i], f.getDNA(points[i+1]));
                }

                f.setDNA(points[actOn - 1], tmp);
                
            }
        }

    }
}
