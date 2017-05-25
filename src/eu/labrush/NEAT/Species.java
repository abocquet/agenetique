package eu.labrush.NEAT;

import eu.labrush.NEAT.fellow.Fellow;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.random;

public class Species {

    ArrayList<Fellow> fellows = new ArrayList<>() ;
    private Fellow[] elite = null ;
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

    private void sortFellows(){
        this.fellows.sort(Comparator.comparingDouble(Fellow::getFitness));
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

    double averageFitness(){
        double avg = 0.0 ;

        for(Fellow f: this.fellows){
            avg += f.getFitness();
        }

        avg /= this.fellows.size();

        return avg ;
    }

    public boolean isStagnant() {
        return age - last_improved > Config.STAGNATION_AGE;
    }

}
