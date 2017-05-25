package eu.labrush.agenetic;

import eu.labrush.agenetic.operators.*;
import eu.labrush.agenetic.operators.crossover.OnePointCrossover;
import eu.labrush.agenetic.operators.mutation.DefaultMutationOperator;
import eu.labrush.agenetic.operators.selection.BiasedWheelSelector;

import java.util.Arrays;

public abstract class AbstractNature {

    private AbstractFellow[] population;
    private AbstractFellowFactory factory ;

    private CrossoverInterface reproductionOperator ;
    private MutationInterface mutationOperator ;
    private SelectorInterface selectionOperator ;

    private double PMUTATION = 0.05;
    private double PCROSSOVER = 0.5;
    private int POPSIZE = 10;
    private int ELITISM = 1;
    private double PINSERTION = 0.5 ;

    private int genCounter = 0;

    /**
     *
     * @param POPSIZE the Nature size
     * @param ELITISM the number of the best fellows kept every generation
     * @param PCROSSOVER the probability a fellow reproduces vs is introduced in the next generation
     * @param PMUTATION the probability a fellow is mutated
     * @param PINSERTION the probabilty a new random fellow is introduced each generation
     * @param factory the fellow factory
     * @param ro the reproduction operator
     * @param mo the mutation operator
     */
    protected AbstractNature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, AbstractFellowFactory factory, CrossoverInterface ro, MutationInterface mo, SelectorInterface so) {

        this.POPSIZE = POPSIZE ;
        this.PMUTATION = Math.min(PMUTATION, 1) ;
        this.PCROSSOVER = Math.min(PCROSSOVER, 1);
        this.ELITISM = Math.min(ELITISM, POPSIZE) ;
        this.PINSERTION = Math.min(PINSERTION, 1) ;

        this.factory = factory ;

        this.reproductionOperator = ro ;
        this.mutationOperator = mo ;
        this.selectionOperator = so ;

        initPopulation();
    }

    protected AbstractNature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, AbstractFellowFactory factory) {
        this(POPSIZE, ELITISM, PCROSSOVER, PMUTATION, PINSERTION, factory, new OnePointCrossover(), new DefaultMutationOperator(), new BiasedWheelSelector());
    }

    protected AbstractNature() {}

    private void initPopulation(){
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

        int[][] elite = new int[ELITISM][] ;

        Arrays.sort(population, (a, b) -> Long.compare(b.getFitness(), a.getFitness()));

        // We keep the best of each generation
        for(int i = 0 ; i < ELITISM ; i++){
            elite[i] = population[i].cloneDNA() ;
        }

        crossover();
        mutate();

        if(Math.random() < PINSERTION) {
            population[POPSIZE - 1] = factory.newInstance();
        }

        for (int j = 0; j < ELITISM ; j++) {
            population[(int) Math.random() * POPSIZE] = factory.newInstance(elite[j]);
        }

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

    private void crossover() {

        AbstractFellow[] newPop = new AbstractFellow[this.POPSIZE];
        selectionOperator.processPop(population);
        int i = 0 ;

        while(i < POPSIZE) {

            Tuple<AbstractFellow, AbstractFellow> mates = selectionOperator.next();

            if (Math.random() <= PCROSSOVER) {
                Tuple<AbstractFellow, AbstractFellow> children = this.reproduce(mates.fst, mates.snd);

                if (i < POPSIZE) {
                    newPop[i] = children.fst;
                    i++;
                }
                if (i < POPSIZE) {
                    newPop[i] = children.snd;
                    i++;
                }

            } else {
                newPop[i] = mates.fst;
                i++;
            }

        }

        this.population = newPop ;
    }

    private void mutate() {
        mutationOperator.mutate(population, PMUTATION, factory);
    }

    private Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female) {
        return reproductionOperator.reproduce(male, female, this.factory);
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
