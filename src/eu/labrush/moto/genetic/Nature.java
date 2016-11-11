package eu.labrush.moto.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.moto.GroundDesigner;
import eu.labrush.observer.Observable;
import eu.labrush.observer.Observer;

public class Nature extends AbstractNature implements Observable {

    Observer observer = null;
    int fitnessCalculated = 0 ;
    private GroundDesigner gd ;

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory, GroundDesigner gd) {
        super(POPSIZE, PCROSSOVER, PMUTATION, factory);
        setGroundDesigner(gd);
    }

    public void evolve(){
        evolve(true);
    }

    public void evolve(boolean async){
        if(async){ // On calcule une fois tous les fitness de façon asynchrone en profitant du multicoeur
            calc_pop_fitness();
        }

        crossover();
        mutate();
    }

    private void calc_pop_fitness() {

        fitnessCalculated = 0 ;
        Thread[] threads = new Thread[getPOPSIZE()];
        AbstractFellow[] pop = getPopulation() ;

        for(int i = 0, c = getPOPSIZE() ; i < c ; i++){
            Moto moto = (Moto) pop[i] ;

            threads[i] = new Thread(() -> {
                moto.setGroundDesigner(gd);
                moto.getFitness();
                fitnessCalculated++ ;
                notifyObserver(String.valueOf((100 * fitnessCalculated / getPOPSIZE())));
            });

            threads[i].start();
        }

        try {
            for(int i = 0, c = getPOPSIZE() ; i < c ; i++){ threads[i].join(); }
        } catch (Exception e){
            e.printStackTrace();
        }

        notifyObserver("done");

    }

    @Override
    public void setObserver(Observer obs) {
        observer = obs ;
    }

    @Override
    public void removeObserver() {
        observer = null;
    }

    @Override
    public void notifyObserver(String str) {
        if(observer != null)
            observer.update(str);
    }

    public GroundDesigner getGroundDesigner() {
        return gd;
    }
    public void setGroundDesigner(GroundDesigner gd){ this.gd = gd ;}
}
