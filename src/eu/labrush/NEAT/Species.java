package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.Comparator;

public class Species {

    ArrayList<Fellow> fellows = new ArrayList<>() ;
    private double threshold = 30.0 ;

    private Fellow[] elite = null ;

    Species(Fellow fellow) {
        this.fellows.add(fellow) ;
    }

    ArrayList<Fellow> getFellows() {
        return fellows;
    }

    Fellow get(int i){
        return fellows.get(i);
    }

    void add(Fellow f){
        this.fellows.add(f);
    }

    boolean closeTo(Fellow f){
        if(this.getAmbassador().distanceTo(f) < threshold){
            return true ;
        } else {
            return false ;
        }
    }

    Fellow getAmbassador(){
        return fellows.get(0);
    }

    void dumpDummies() // remove half of the pop, choosing the less performing fellows
    {
        sortFellows();
        int n = (int) Math.floor(this.fellows.size());

        for (int i = 0; i < n; i++) {
            fellows.remove(0 );
        }
    }

    void saveElite(int i){
        elite = new Fellow[i] ;
        sortFellows();
        for (int j = 0; j < i; j++) {
            elite[j] = this.fellows.get(i).clone();
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

    double averageFitness(){
        double avg = 0.0 ;

        for(Fellow f: this.fellows){
            avg += f.getFitness();
        }

        avg /= this.fellows.size();

        return avg ;
    }

    private void sortFellows(){
        this.fellows.sort(Comparator.comparingDouble(Fellow::getFitness));
    }
}
