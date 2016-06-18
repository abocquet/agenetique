package eu.labrush.Traveller;

/**
 * Created by adrienbocquet on 24/04/2016.
 */

import eu.labrush.Tuple;

import java.util.Arrays;

public class Nature {

    protected Travel[] population ;

    private double PMUTATION  = 0.05 ;
    private double PCROSSOVER = 0.5 ;
    private int    POPSIZE    = 10 ;

    public Nature() {
        this.population = new Travel[POPSIZE];
        for(int i = 0 ; i < this.population.length ; i++) this.population[i] = new Travel();
    }

    public void evolve(){
        crossover();
        mutate();
    }

    protected void crossover() {
        Arrays.sort(this.population, (a, b) -> b.getFitness() - a.getFitness());

        int totalFitness = 0 ;
        for(Travel f : this.population){
            totalFitness += f.getFitness() ;
        }

        Travel[] newPop = new Travel[POPSIZE];
        int i = 0 ;
        double cursor = 0 ;
        while(i < POPSIZE){

            if(Math.random() <= PCROSSOVER){
                Tuple<Travel, Travel> children = this.reproduce(population[(int)cursor], population[(int)(Math.random() * 10000) % POPSIZE]);

                if(i < POPSIZE) population[i] = children.fst ; i++ ;
                if(i < POPSIZE) population[i] = children.snd ; i++ ;
            } else {
                newPop[i] = this.population[(int)cursor] ;
                i++ ;
            }

            cursor += population[(int) cursor].getFitness() / totalFitness ;
        }
    }

    protected void mutate() {
        for(Travel f: population){
            for(int i = 0 ; i < f.getDNASIZE() ; i++){
                if(Math.random() <= PMUTATION) f.getDna()[i] = !f.getDna()[i] ;
            }
        }
    }

    protected Tuple<Travel, Travel> reproduce(Travel male, Travel female){
        int dnasize = male.getDNASIZE() ;
        Travel[] children = new Travel[2] ;

        //System.out.println(male + " - " +  female);

        for(int i = 0 ; i < 2 ; i++) {
            int splitPoint = (int)(Math.random() * 1000) % dnasize ;
            boolean[] dna = new boolean[dnasize] ;

            for(int j = 0 ; j < splitPoint ; j++){
                dna[j] = male.getDna()[j] ;
            }

            for(int j = splitPoint ; j < dnasize ; j++){
                dna[j] = female.getDna()[j] ;
            }

            children[i] = new Travel(dna);
        }

        return new Tuple<>(children[0], children[1]);
    }

    @Override
    public String toString() {
        String str = "[" ;
        Arrays.sort(population);

        for(Travel f : population){
            str += f.getFitness() + " " ;
        }

        str += "]";

        return str ;
    }
}
