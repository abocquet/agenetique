package eu.labrush.agenetic.operators.selection;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.SelectorInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BiasedWheelSelector implements SelectorInterface {

    BigDecimal[] parts ;
    AbstractFellow[] population ;

    @Override
    public void processPop(AbstractFellow[] pop) {

        this.population = pop ;
        long minFitness = this.population[0].getFitness();

        BigDecimal totalFitness = BigDecimal.valueOf(0) ;
        for(AbstractFellow f : this.population){
            if(f.getFitness() < minFitness){
                minFitness = f.getFitness() ;
            }
        }

        // if a fitness is negative, we add the absolute value of it to every fellow afterwards,
        // at once so as to avoid tests in the loop
        // As a result the worst fellow will have a score of 0
        BigDecimal bigMinFitness = BigDecimal.valueOf(minFitness);

        for(AbstractFellow f : this.population){
            totalFitness = totalFitness.add(new BigDecimal(f.getFitness()));
        }

        this.parts = new BigDecimal[population.length];
        for(int i = 0, c = population.length ; i < c ; i++){
            parts[i] = new BigDecimal(population[i].getFitness()).add(bigMinFitness).divide(totalFitness, 10, RoundingMode.HALF_UP);

            if(parts[i].compareTo(BigDecimal.ZERO) < 0){
                System.err.println("ERREUR de part: " + parts[i]);
            }
        }

    }

    @Override
    public Tuple<AbstractFellow, AbstractFellow> next(){
        return new Tuple<>(nextFellow(), nextFellow());
    }

    public AbstractFellow nextFellow(){
        BigDecimal rand = BigDecimal.valueOf(Math.random());
        BigDecimal current = BigDecimal.valueOf(0);

        for(int i = 0 ; i < population.length - 1 ; i++){
            if(rand.compareTo(current) >= 0 && rand.compareTo(current.add(parts[i])) < 0){
                return population[i];
            }

            current = current.add(parts[i]);
        }

        if(rand.compareTo(current) >= 0){
            return population[population.length - 1];
        }

        System.err.println("Bla ?");
        return null;
    }
}
