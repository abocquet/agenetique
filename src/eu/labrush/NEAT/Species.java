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

    void removeFellow(int i){
        removeFellow(fellows.get(i));
    }

    void removeFellow(Fellow f){
        if(fellows.size() <= 1){
            try {
                throw new Exception("Species will be empty !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(f == ambassador){
            /**
             * Replaces to ambassador by the closest fellow to the current one
             */

            Fellow best = null ;
            double minDist = Double.POSITIVE_INFINITY ;
            for (Fellow f1: fellows){
                if(f1 != ambassador && f1.distanceTo(ambassador) < minDist){
                    best = f1 ;
                }
            }

            ambassador = best ;
        }

        fellows.remove(f);
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

    void dumpDummies() // remove half of the pop, choosing the less performing fellows
    {
        int n = (int) Math.floor(this.fellows.size()) / 2;

        for (int i = 0; i < n; i++) {
            removeFellow(0 );
        }
    }

    double averageFitness(){
        double avg = 0.0 ;

        for(Fellow f: this.fellows){
            avg += f.getFitness();
        }

        avg /= this.fellows.size();

        return avg ;
    }

    double adjustedFitness(){

        double shared_fitness = averageFitness() ;

        if(age < Config.MINORITY){
            shared_fitness *= Config.MINORITY_HELP_MULTIPLIER ;
        } else if(age - last_improved - Config.STAGNATION_AGE <= 0){
            shared_fitness *= Config.STAGNATION_MULTIPLIER ;
        }

        return shared_fitness ;
    }

    /**************************
        Elite
     **************************/

    void saveElite(int i){
        elite = new Fellow[i] ;
        sortFellows();
        for (int j = 0, n = this.fellows.size() ; j < i ; j++) {
            elite[j] = this.fellows.get(n - 1 - j).clone();
        }
    }

    void loadElite(){
        if(elite == null){
            return ;
        }

        sortFellows();
        for (int i = 0; i < elite.length; i++) {
            this.fellows.set(i, elite[i]);
        }

        elite = null ;
    }

}
