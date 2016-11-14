package eu.labrush.agenetic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;

public abstract class AbstractNature {

    protected AbstractFellow[] population;

    protected AbstractFellowFactory factory ;

    protected double PMUTATION = 0.05;
    protected double PCROSSOVER = 0.5;
    private int POPSIZE = 10;

    private int genCounter = 0;

    protected AbstractNature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory) {

        this.PMUTATION = PMUTATION;
        this.POPSIZE = POPSIZE;

        this.PCROSSOVER = PCROSSOVER;

        this.factory = factory ;
        initPopulation();
    }

    protected AbstractNature() {}

    protected void initPopulation(){
        this.population = new AbstractFellow[POPSIZE];

        for (int i = 0; i < this.population.length; i++) {
            this.population[i] = this.factory.newInstance();
        }
    }


    public void evolve(){
        crossover();
        mutate();

        genCounter++ ;
    }

    public int getGenerationNumber() { return this.genCounter ; }

    protected void crossover() {
        Arrays.sort(this.population, (a, b) -> b.getFitness() - a.getFitness());
        int minFitness = this.population[getPOPSIZE() - 1].getFitness(); // the population is already sorted by fitness, the first fellow hqs the lower fitness

        BigDecimal totalFitness = BigDecimal.ZERO ;
        for(AbstractFellow f : this.population){
            totalFitness = totalFitness.add(BigDecimal.valueOf(f.getFitness()));
        }

        // if a fitness is negative, we add the absolute value of it to every fellow afterwards,
        // at once so as to avoid tests in the loop
        // As a result the worst fellow will have a score of 0
        if(minFitness < 0){
            totalFitness = totalFitness.add(BigDecimal.valueOf(-minFitness * getPOPSIZE())); //Don't forget the minus to have a positive total !
        }

        AbstractFellow[] newPop = new AbstractFellow[this.POPSIZE];
        int i;

        // We keep a tenth of the best of each generation
        for(i = 0 ; i < population.length / 10 ; i++){
            newPop[i] = population[i] ;
        }

        BigDecimal cursor = BigDecimal.ZERO ;
        while(i < POPSIZE){

            int c = cursor.intValue() ;

            if(Math.random() <= PCROSSOVER){
                Tuple<AbstractFellow, AbstractFellow> children = this.reproduce(population[c], population[(int)(Math.random() * 10000) % POPSIZE]);
                if(i < POPSIZE) { newPop[i] = children.fst ; i++ ; }
                if(i < POPSIZE) { newPop[i] = children.snd ; i++ ; }
            } else {
                newPop[i] = this.population[c % POPSIZE] ;
                i++ ;
            }

            BigDecimal part = BigDecimal.valueOf(population[c].getFitness());
            part = part.divide(totalFitness, 10, RoundingMode.HALF_UP);
            cursor = cursor.add(part) ;
        }

        this.population = newPop ;

    }

    protected void mutate() {

        int DNACARD = this.factory.getDNACard() ;

        for(AbstractFellow f: population){
            for(int i = 0 ; i < f.getDNASIZE() ; i++){
                /*
                "flip": on choisit de muter
                "mutation" on a une mutation
                "indentique" le gene n'a pas changé après tirage

                p(mutation) = p(flip inter non(identique))
                or flip et indentique sont indépendants
                d'ou p(mutation) = p(flip) * p(non(identique))
                ie p(flip) = p(mutation) / p(non(non(identique)) = p(mutation) / (1 - 1 / DNACARD))

                finalement on passe de l'autre coté pour éviter de manipuler des flottants)
                 */
                if(Math.random() * (DNACARD - 1) <= PMUTATION * DNACARD) f.getDna()[i] = ((int) (Math.random() * 10000) % DNACARD);
            }
        }
    }

    protected Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female)  {

        int DNASIZE = this.factory.getDNASize() ;

        AbstractFellow[] children = new AbstractFellow[2] ;

        for(int i = 0 ; i < 2 ; i++) {
            int splitPoint = (int) (Math.random() * 1000) % DNASIZE;
            int[] dna = new int[DNASIZE];

            for (int j = 0; j < splitPoint; j++) {
                dna[j] = male.getDna()[j];
            }

            for (int j = splitPoint; j < DNASIZE; j++) {
                dna[j] = female.getDna()[j];
            }

            children[i] = this.factory.newInstance(dna) ;
        }

        return new Tuple<>(children[0], children[1]);
        }

    @Override
    public String toString() {
        String str = "[" ;
        Arrays.sort(population);

        for(AbstractFellow f : population){
            str += f.getFitness() + " " ;
        }

        str += "]";

        return str ;
    }


    public AbstractFellow[] getPopulation() {
        return population;
    }

    protected void setPOPSIZE(int POPSIZE) {
        this.POPSIZE = POPSIZE;
    }

    public int getPOPSIZE() {
        return POPSIZE;
    }

    protected AbstractFellow getBest(){
        AbstractFellow best = population[0];
        for(int i = 1, c  = getPOPSIZE() ; i < c ; i++)
        {
            if(best.getFitness() < population[i].getFitness()){
                best = population[i] ;
            }
        }

        return best ;
    }


}
