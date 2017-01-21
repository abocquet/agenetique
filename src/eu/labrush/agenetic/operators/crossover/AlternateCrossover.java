package eu.labrush.agenetic.operators.crossover;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;

public class AlternateCrossover implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = factory.getDNASize();

        for (int i = 0; i < 2; i++) {
            int dna[] = new int[DNASIZE];
            for (int j = 0; j < DNASIZE; j++) {
                dna[j] = parents[(j+i)%2].getDNA(j);
            }
            children[i] = factory.newInstance(dna);
        }

        return new Tuple<>(children[0], children[1]);
        
    }
}
