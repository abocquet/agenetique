package eu.labrush.agenetic.operators.selection;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.selection.BiasedWheelSelector;


public class WheelAndRandomSelector extends BiasedWheelSelector {

    public Tuple<AbstractFellow, AbstractFellow> next(){
        return new Tuple<>(nextFellow(), this.population[(int) (Math.random() * population.length)]) ;
    }

}
