package eu.labrush.agenetic.operators.selection;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.SelectorInterface;

/**
 * Select the best element from a set of n fellows
 */
public class ElitistSelector implements SelectorInterface {

    private AbstractFellow[] population;
    private int n = 5 ;

    @Override
    public void processPop(AbstractFellow[] pop) {
        this.population = pop ;
    }


    public Tuple<AbstractFellow, AbstractFellow> next(){
        return new Tuple<>(nextFellow(), nextFellow());
    }

    public AbstractFellow nextFellow() {
        AbstractFellow selected = population[(int) (Math.random() * population.length)] ;

        for (int i = 0; i < n - 1; i++) {
            AbstractFellow current = population[(int) (Math.random() * population.length)] ;
            if(current.getFitness() > selected.getFitness()){
                selected = current ;
            }
        }

        return selected ;
    }
}
