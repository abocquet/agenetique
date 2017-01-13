package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

/**
 * TODO: description
 * TODO: tests
 */
public class MaximalPreservation implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int XP1 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE;
        int XP2 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE;

        for(int i = 0 ; i < 2 ; i++) {
            int ib = (1+i) % 2 ;

            int[] dna = new int[DNASIZE] ;

            int start = 0 ;
            while(parents[i].getDNA(start) != XP1){
                start += 1 ;
            }

            int end = 0 ;
            while(parents[i].getDNA(end) != XP2){
                end += 1 ;
            }

            if(end < start){
                int tmp = end ;
                end = start ;
                start = tmp ;
            }

            for (int j = start; j < end; j++) {
                dna[j] = parents[i].getDNA(j);
            }

            int cursor = end ;
            for (int j = end ; j < DNASIZE; j++) {
                while(Arrays.asList(dna).contains(parents[ib].getDNA(cursor))){
                    cursor = (cursor + 1) % DNASIZE ;
                }

                dna[cursor] = parents[ib].getDNA(cursor);
            }

            for (int j = 0; j < start ; j++) {
                while(Arrays.asList(dna).contains(parents[ib].getDNA(cursor))){
                    cursor = (cursor + 1) % DNASIZE ;
                }

                dna[cursor] = parents[ib].getDNA(cursor);
            }


            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested

        }

        return new Tuple<>(children[0], children[1]);
    }
}
