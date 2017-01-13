package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.traveller.data.PointSet;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, PointSet problem, CrossoverInterface ro, MutationInterface mo) {
        super(POPSIZE, ELITISM, PCROSSOVER, PMUTATION, PINSERTION, new TravelFactory(problem), ro, mo);
    }

    public long getShortest(){
        return ((Travel)this.getBest()).getDistance();
    }
}