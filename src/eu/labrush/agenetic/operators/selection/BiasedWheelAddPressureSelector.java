package eu.labrush.agenetic.operators.selection;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.SelectorInterface;


public class BiasedWheelAddPressureSelector implements SelectorInterface {

    double[] parts ;
    AbstractFellow[] population ;

    @Override
    public void processPop(AbstractFellow[] pop, int generation) {

        this.population = pop ;
        long minFitness = this.population[0].getFitness();

        double totalFitness = 0 ;
        for(AbstractFellow f : this.population){
            if(f.getFitness() < minFitness){
                minFitness = f.getFitness() ;
            }
        }

        // if a fitness is negative, we add the absolute value of it to every fellow afterwards,
        // at once so as to avoid tests in the loop
        // As a result the worst fellow will have a score of 0

        for(AbstractFellow f : this.population){
            totalFitness += af(f, generation);
        }

        this.parts = new double[population.length];
        for(int i = 0, c = population.length ; i < c ; i++){
            parts[i] =  (af(population[i], generation) + minFitness) / totalFitness;

            if(parts[i]< 0){
                System.err.println("ERREUR de part: " + parts[i]);
            }
        }
    }

    // af: adjusted fitness
    private double af(AbstractFellow f, int generation){
        return Math.pow(f.getFitness(), 1 + 0.1 * generation);
    }

    @Override
    public Tuple<AbstractFellow, AbstractFellow> next(){
        return new Tuple<>(nextFellow(), nextFellow());
    }

    public AbstractFellow nextFellow(){
        double rand = Math.random();
        double current = 0;

        for(int i = 0 ; i < population.length - 1 ; i++){
            if(rand >= current && rand < current + parts[i]){
                return population[i];
            }

            current += parts[i];
        }

        if(rand >= current){
            return population[population.length - 1];
        }

        System.err.println("Bla ?");
        return null;
    }
}
