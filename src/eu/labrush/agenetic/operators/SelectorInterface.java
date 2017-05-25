package eu.labrush.agenetic.operators;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;

public interface SelectorInterface {

    void processPop(AbstractFellow[] pop, int generation);
    Tuple<AbstractFellow, AbstractFellow> next();

}
