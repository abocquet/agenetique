package eu.labrush.NEAT;

import eu.labrush.NEAT.fellow.Fellow;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.random;

public class Species {

    ArrayList<Fellow> fellows = new ArrayList<>() ;
    private Fellow ambassador ;

    int age = 0 ;
    int last_improved = 0 ;
    double last_fitness = 0.0 ;

    Species(Fellow fellow) {
        this.fellows.add(fellow) ;
        this.ambassador = fellow ;
    }

    /**************************
        Fellows management
     **************************/

    ArrayList<Fellow> getFellows() {
        return fellows;
    }

    Fellow get(int i){
        return fellows.get(i);
    }

    Fellow getRandom(){
        return fellows.get((int) (random() * (fellows.size() + 1)) % fellows.size() );
    }

    void addFellow(Fellow f){
        this.fellows.add(f);
    }

    public void sortFellows(){
        //the order is reversed, so elite comes first
        this.fellows.sort(Comparator.comparingDouble(Fellow::getFitness).reversed());
    }

    /**************************
        NEAT
     **************************/

    boolean closeTo(Fellow f){
        return this.getAmbassador().distanceTo(f) < Config.SAME_SPECIES_THRESHOLD;
    }


    Fellow getAmbassador(){
        return ambassador;
    }

    public void setAmbassador(Fellow ambassador) {
        this.ambassador = ambassador;
    }

    double averageFitness(){
        double avg = 0.0 ;

        for(Fellow f: this.fellows){
            avg += f.getFitness();
        }

        avg /= this.fellows.size();

        return avg ;
    }

}
