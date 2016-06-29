package eu.labrush.agenetic.traveller.operators;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.traveller.TravelFactory;

public interface ReproductionOperator {
    Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, TravelFactory factory);
}
