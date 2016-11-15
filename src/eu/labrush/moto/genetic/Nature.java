package eu.labrush.moto.genetic;

import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractParrallelNature;
import eu.labrush.moto.GroundDesigner;
import eu.labrush.observer.Observable;
import eu.labrush.observer.Observer;

public class Nature extends AbstractParrallelNature implements Observable {

    private Observer observer = null;
    private GroundDesigner gd ;

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, AbstractFellowFactory factory, GroundDesigner gd) {
        super(POPSIZE, PCROSSOVER, PMUTATION, factory);
        setGroundDesigner(gd);
    }

    public void evolve(){
        evolve(true);
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
    private void setGroundDesigner(GroundDesigner gd){ this.gd = gd ;}
}
