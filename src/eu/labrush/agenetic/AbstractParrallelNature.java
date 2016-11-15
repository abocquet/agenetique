package eu.labrush.agenetic;

/**
 * A nature that runs fitness calculation before crossover and thus takes advantage of multithreading
 * This class is relevant only if the fellows remember their fitness
 */
abstract public class AbstractParrallelNature extends AbstractNature {

    public AbstractParrallelNature(int popsize, double pcrossover, double pmutation, AbstractFellowFactory factory) {
        super(popsize, pcrossover, pmutation, factory);
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
    }

    protected void calc_pop_fitness() {

        Thread[] threads = new Thread[getPOPSIZE()];
        AbstractFellow[] pop = getPopulation() ;

        for(int i = 0, c = getPOPSIZE() ; i < c ; i++){
            AbstractFellow t = pop[i] ;

            threads[i] = new Thread(() -> {
                t.getFitness();
            });

            threads[i].start();
        }

        try {
            for(int i = 0, c = getPOPSIZE() ; i < c ; i++){ threads[i].join(); }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
