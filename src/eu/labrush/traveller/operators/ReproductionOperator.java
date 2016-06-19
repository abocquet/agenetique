package eu.labrush.traveller.operators;

import eu.labrush.AbstractFellow;
import eu.labrush.Tuple;

public interface ReproductionOperator {
    Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2);
}
