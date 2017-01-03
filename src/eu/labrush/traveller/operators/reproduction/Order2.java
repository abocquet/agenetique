package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

//TODO: TESTS
public class Order2 implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int[] cutPoints = new int[4];
        for(int j = 0 ; j < cutPoints.length ; j++){
            cutPoints[j] = (int) ((Math.random() * 2 * DNASIZE) % DNASIZE) ;
        }

        for(int i = 0 ; i < 2 ; i++) {
            int[] dna = new int[DNASIZE] ;

            for(int j = 0 ; j < DNASIZE ; j++){
                dna[j] = parents[i].getDNA(j);
            }

            for (int j = 0; j < cutPoints.length; j++) {
                dna[(cutPoints[j] + 1) % DNASIZE] = dna[cutPoints[j]];
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested

        }

        return new Tuple<>(children[0], children[1]);
    }
}
