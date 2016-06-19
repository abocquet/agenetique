package eu.labrush.traveller.operators;

import eu.labrush.AbstractFellow;

public interface MutationOperator {
    void mutate(AbstractFellow[] population, double pmutation);
}
