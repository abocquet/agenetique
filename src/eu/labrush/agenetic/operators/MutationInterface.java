package eu.labrush.agenetic.operators;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;

public interface MutationInterface {
    void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory);
}
