package eu.labrush.traveller;

import eu.labrush.AbstractFellow;
import eu.labrush.traveller.data.Point;

import java.util.Arrays;

public class Travel extends AbstractFellow {

    static Point[] places ;

    public static void setPlaces(Point[] places) throws Exception {
        Travel.setDNACARD(places.length);
        Travel.setDNASIZE(places.length);

        Travel.places = places;
    }

    public Travel(){
        super();

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

    public Travel(int[] dna){
        this(dna, false);
    }

    /**
     * @param dna the DNA to be assigned
     * @param safe checking if a dna is a permutation costs much so if dna
     *             is known to be a permutation there is no need to check
     */
    public Travel(int[] dna, boolean safe){
        super(dna);
        if(!safe && !isPermutation()) {
            try {
                throw new Exception("DNA is not a permutation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPermutation() {
        int[] dna = this.getDna().clone() ;
        Arrays.sort(dna);

        for(int i = 0 ; i < getDNASIZE() ; i++){
            if(dna[i] != i) return false ;
        }

        return true ;
    }

    public int getDistance() {
        int[] order = this.getDna() ;
        int distance  = 0 ;

        for(int i = 0 ; i < getDNACARD() - 1 ; i ++){
            distance += Point.distance(places[order[i]], places[order[i+1]]);
        }

        distance += Point.distance(places[0], places[order[getDNACARD()-1]]);

        return distance ;
    }

    @Override
    public int getFitness() {
        return Integer.MAX_VALUE - getDistance(); // We reverse the process to transform it into a max research
    }
}
