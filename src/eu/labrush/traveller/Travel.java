package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.traveller.data.PointSet;

import java.util.Arrays;

public class Travel extends AbstractFellow {

    private PointSet points ;

    public Travel(PointSet points){
        super(points.getPoints().length, points.getPoints().length);
        this.points = points ;

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

    public Travel(int[] dna, PointSet points){
        this(dna, points, false);
    }

    /**
     * @param dna the DNA to be assigned
     * @param safe checking if a dna is a permutation costs much so if dna
     *             is known to be a permutation there is no need to check
     */
    public Travel(int[] dna, PointSet points, boolean safe){
        super(dna, points.getPoints().length);
        this.points = points ;

        if(!safe && !isPermutation()) {
            System.err.println("DNA is not a permutation");
            System.exit(-42);
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
            distance += points.distBetween(getDNA(i), getDNA(i+1));
        }

        distance += points.distBetween(getDNA(0), getDNA(getDNACARD()-1));

        return distance ;
    }

}
