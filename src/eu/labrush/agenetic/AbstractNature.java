package eu.labrush.agenetic;

import java.lang.reflect.Array;
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

        this.POPSIZE = POPSIZE ;
        this.PMUTATION = PMUTATION ;
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
        evolve(false);
    }

    public void evolve(boolean async){
        if(async){ // On calcule une fois tous les fitness de façon asynchrone en profitant du multicoeur
            calc_pop_fitness();
        }

        crossover();
        mutate();

        genCounter++ ;
    }

    protected void calc_pop_fitness() {

        int cores = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[cores];
        AbstractFellow[] pop = getPopulation() ;

        for(int i = 0 ; i < cores ; i++){
            int j = i;
            int c = getPOPSIZE();

            threads[i] = new Thread(() -> {
                for(int k = cores * j ; k < cores*(j+1) && k < c ; k++) {
                    AbstractFellow t = pop[k];
                    t.getFitness();
                }
            });

            threads[i].start();
        }

        try {
            for(int i = 0 ; i < cores ; i++){ threads[i].join(); }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getGenerationNumber() { return this.genCounter ; }

    protected void crossover() {
        Arrays.sort(population, (a, b) -> b.getFitness().compareTo(a.getFitness()));

        BigInteger minFitness = this.population[0].getFitness();

        BigDecimal totalFitness = BigDecimal.valueOf(0) ;
        for(AbstractFellow f : this.population){
            if(f.getFitness().compareTo(minFitness) < 0){
                minFitness = f.getFitness() ;
            }
        }

        // if a fitness is negative, we add the absolute value of it to every fellow afterwards,
        // at once so as to avoid tests in the loop
        // As a result the worst fellow will have a score of 0
        minFitness = minFitness.negate() ; //Don't forget the minus to have a positive total !
        totalFitness.add(new BigDecimal(minFitness.multiply(BigInteger.valueOf(getPOPSIZE()))));

        for(AbstractFellow f : this.population){
            totalFitness = totalFitness.add(new BigDecimal(f.getFitness()));
        }

        AbstractFellow[] newPop = new AbstractFellow[this.POPSIZE];
        int i = 1 ;

        // We keep the best of each generation
        for(i = 0 ; i < getPOPSIZE() / 10 ; i++){
            newPop[i] = population[i];
        }

        BigDecimal cursor = BigDecimal.valueOf(0) ;
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

            //We add the lowest fitness to every fellow so it's always postive
            BigDecimal part = new BigDecimal(population[c].getFitness().add(minFitness));

            if(part.compareTo(BigDecimal.ZERO) < 0){
                System.err.print("ERREUR de part: " + part);
            }

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
                if(Math.random() * (DNACARD - 1) <= PMUTATION * DNACARD) f.setDNA(i, ((int) (Math.random() * 10000) % DNACARD));
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
                dna[j] = male.getDNA(j);
            }

            for (int j = splitPoint; j < DNASIZE; j++) {
                dna[j] = female.getDNA(j);
            }

            children[i] = this.factory.newInstance(dna) ;
        }

        return new Tuple<>(children[0], children[1]);
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

    public AbstractFellow getBest(){
        AbstractFellow best = population[0];
        for(int i = 1, c  = getPOPSIZE() ; i < c ; i++)
        {
            if(best.getFitness().compareTo(population[i].getFitness()) < 0){
                best = population[i] ;
            }
        }

        return best ;
    }


}
