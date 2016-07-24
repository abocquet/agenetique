package eu.labrush.walker.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.numfun.Fellow;

import java.util.Arrays;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory) {
        super(POPSIZE, PCROSSOVER, PMUTATION, factory);
    }

    public Walker getBest(){
        Arrays.sort(population);
        return (Walker) population[population.length - 1] ;
    }

    public void evolve(){
        evolve(false);
    }

    public void evolve(boolean async){
        if(async){
            calc_pop_fitness();
        }

        crossover();
        mutate();
    }

    /**
     * This method is irrelevant only since the fellow "remember" its fitness
     * ie. it runs the simulation once
     */
    public void calc_pop_fitness() {

        Thread[] threads = new Thread[getPOPSIZE()];
        AbstractFellow[] pop = getPopulation() ;

        for(int i = 0, c = getPOPSIZE() ; i < c ; i++){

            threads[i] = new Thread((Walker) pop[i]);
            threads[i].start();

        }

        try {
            for(int i = 0, c = getPOPSIZE() ; i < c ; i++){
                threads[i].join();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
