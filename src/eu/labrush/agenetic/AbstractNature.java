package eu.labrush.agenetic;

import eu.labrush.agenetic.operators.DefaultMutationOperator;
import eu.labrush.agenetic.operators.OnePointCrossover;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.CrossoverInterface;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public abstract class AbstractNature {

    protected AbstractFellow[] population;
    protected AbstractFellowFactory factory ;

    private CrossoverInterface reproductionOperator ;
    private MutationInterface mutationOperator ;

    protected double PMUTATION = 0.05;
    protected double PCROSSOVER = 0.5;
    private int POPSIZE = 10;

    private int genCounter = 0;

    public AbstractNature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory, CrossoverInterface ro, MutationInterface mo) {

        this.POPSIZE = POPSIZE ;
        this.PMUTATION = PMUTATION ;
        this.PCROSSOVER = PCROSSOVER;

        this.factory = factory ;

        this.reproductionOperator = ro ;
        this.mutationOperator = mo ;

        initPopulation();
    }

    public AbstractNature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory) {
        this(POPSIZE, PCROSSOVER, PMUTATION, factory, new OnePointCrossover(), new DefaultMutationOperator());
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
        Arrays.sort(population, (a, b) -> Long.compare(b.getFitness(), a.getFitness()));

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
        minFitness = - minFitness ; //Don't forget the minus to have a positive total !
        //totalFitness = totalFitness.add(bigMinFitness.multiply(BigDecimal.valueOf(getPOPSIZE())));

        for(AbstractFellow f : this.population){
            totalFitness = totalFitness.add(new BigDecimal(f.getFitness()));
        }

        BigDecimal[] parts = new BigDecimal[getPOPSIZE()];
        for(int i = 0 ; i < getPOPSIZE() ; i++){
            parts[i] = new BigDecimal(population[i].getFitness()).add(bigMinFitness).divide(totalFitness, 10, RoundingMode.HALF_UP);

            if(parts[i].compareTo(BigDecimal.ZERO) < 0){
                System.err.println("ERREUR de part: " + parts[i]);
            }
        }


        AbstractFellow[] newPop = new AbstractFellow[this.POPSIZE];
        int i ;

        // We keep the best of each generation
        for(i = 0 ; i <= (double)getPOPSIZE() / 10 ; i++){
            newPop[i] = population[i];
        }

        while(i < POPSIZE) {

            int c = biasedWheel(parts);

            if (Math.random() <= PCROSSOVER) {
                Tuple<AbstractFellow, AbstractFellow> children = this.reproduce(population[c], population[(int) (Math.random() * 10000) % POPSIZE]);
                if (i < POPSIZE) {
                    newPop[i] = children.fst;
                    i++;
                }
                if (i < POPSIZE) {
                    newPop[i] = children.snd;
                    i++;
                }
            } else {
                newPop[i] = this.population[c % POPSIZE];
                i++;
            }
        }

        this.population = newPop ;
    }

    protected void mutate() {
        mutationOperator.mutate(population, PMUTATION, factory);
    }

    protected Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female) {
        return reproductionOperator.reproduce(male, female, this.factory);
    }

    private int biasedWheel(BigDecimal[] parts){
        BigDecimal rand = BigDecimal.valueOf(Math.random());
        BigDecimal current = BigDecimal.valueOf(0);

        for(int i = 0 ; i < getPOPSIZE() - 1 ; i++){
            if(rand.compareTo(current) >= 0 && rand.compareTo(current.add(parts[i])) < 0){
                return i;
            }

            current = current.add(parts[i]);
        }

        if(rand.compareTo(current) >= 0){
            return getPOPSIZE() - 1;
        }

        System.err.println("Bla ?");
        return 0;
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
            if(best.getFitness() < population[i].getFitness()){
                best = population[i] ;
            }
        }

        return best ;
    }


    public AbstractFellowFactory getFactory() {
        return factory;
    }
}
