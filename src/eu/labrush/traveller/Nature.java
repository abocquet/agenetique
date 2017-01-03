package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.data.PointSet;

public class Nature extends AbstractNature {

    public  Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, PointSet problem, CrossoverInterface ro, MutationInterface mo) {
        super(POPSIZE, PCROSSOVER, PMUTATION, new TravelFactory(problem.getPoints()), ro, mo);
    }


    public long getShortest(){
        return ((Travel)this.getBest()).getDistance();
    }
}