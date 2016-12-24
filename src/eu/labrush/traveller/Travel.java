package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.traveller.data.Point;

import java.util.Arrays;

public class Travel extends AbstractFellow {

    private Point[] places ;
    private long distance ;

    public Travel(Point[] places){
        super(places.length, places.length);
        this.places = places ;

        //In this particular problem, DNACARD = DNASIZE
        int[] order = new int[getDNACARD()] ;

        //We create an ordered set
        for(int i = 0 ; i < getDNACARD() ; i++){
            order[i] = i ;
        }

        //Then we create a permutation
        for(int i = 0 ; i < getDNACARD() ; i++){
            int s1 = ((int) (Math.random() * 10000) % getDNACARD()) ;
            int s2 = ((int) (Math.random() * 10000) % getDNACARD()) ;

            int tmp = order[s1] ;
            order[s1] = order[s2] ;
            order[s2] = tmp ;
        }

        this.setDna(order);
    }

    public Travel(int[] dna, Point[] places){
        this(dna, places, false);
    }

    /**
     * @param dna the DNA to be assigned
     * @param safe checking if a dna is a permutation costs much so if dna
     *             is known to be a permutation there is no need to check
     */
    public Travel(int[] dna, Point[] places, boolean safe){
        super(dna, places.length);
        this.places = places ;

        if(!safe && !isPermutation()) {
            try {
                throw new Exception("DNA is not a permutation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPermutation() {
        int[] dna = this.cloneDNA();
        Arrays.sort(dna);

        for(int i = 0 ; i < getDNASIZE() ; i++){
            if(dna[i] != i) return false ;
        }

        return true ;
    }

    @Override
    public long calcFitness() {
        return - getDistance() ;
    }

    public long getDistance() {
        long distance  = 0 ;

        for(int i = 0 ; i < getDNACARD() - 1 ; i ++){
            distance += Point.distance(places[getDNA(i)], places[getDNA(i+1)]);
        }

        distance += Point.distance(places[0], places[getDNA(getDNACARD()-1)]);
        this.distance = distance ;

        return distance ;
    }

}
