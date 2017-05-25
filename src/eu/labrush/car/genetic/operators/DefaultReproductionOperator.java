package eu.labrush.car.genetic.operators;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;

public class DefaultReproductionOperator implements CrossoverInterface {

    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        int DNASIZE = f1.getDNASIZE();

        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[]{factory.newInstance(new int[DNASIZE]), factory.newInstance(new int[DNASIZE])} ;

        for(int i = 0 ; i < DNASIZE ; i++) {
            children[(i+1)%2].setDNA(i,parents[0].getDNA(i));
            children[i%2].setDNA(i,parents[1].getDNA(i));
        }

        return new Tuple<>(children[0], children[1]);
    }
}
