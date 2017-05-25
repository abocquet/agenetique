package eu.labrush.agenetic.operators.crossover;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;

public class OnePointCrossover implements CrossoverInterface {

    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female, AbstractFellowFactory factory)  {

        int DNASIZE = factory.getDNASize() ;
        AbstractFellow[] children = new AbstractFellow[2] ;

        for(int i = 0 ; i < 2 ; i++) {
            int splitPoint = (int) (Math.random() * 1000) % DNASIZE;
            int[] dna = new int[DNASIZE];

            for (int j = 0; j < splitPoint; j++) {
                dna[j] = male.getDNA(j);
            }

            for (int j = splitPoint; j < DNASIZE; j++) {
                dna[j] = female.getDNA(j);
            }

            children[i] = factory.newInstance(dna) ;
        }

        return new Tuple<>(children[0], children[1]);
    }

}
