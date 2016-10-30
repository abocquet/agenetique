package eu.labrush.traveller.operators;

import eu.labrush.agenetic.AbstractFellow;

public interface MutationOperator {
    void mutate(AbstractFellow[] population, double pmutation);
}
