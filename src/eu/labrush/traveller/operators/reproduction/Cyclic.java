package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

/**
 * TODO: Descripion
 */
public class Cyclic implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int XP =  (int) ((Math.random() * 2 * DNASIZE) % DNASIZE) ;

        for(int i = 0 ; i < 2 ; i++) {
            int ib = (i+1) % 2 ; // the other parent

            int[] dna = new int[DNASIZE] ;

            int XPi = parents[i].getDNA(XP);
            int cursor = (XP + 1) % DNASIZE;

            dna[XP] = parents[i].getDNA(XP);

            while(parents[ib].getDNA(cursor) != XPi){
                dna[cursor] = parents[ib].getDNA(cursor);
                cursor = (cursor + 1) % DNASIZE ;
            }

            int start = cursor ;
            for (int j = start ; j < DNASIZE; j++) {
                while(Arrays.asList(dna).contains(parents[i].getDNA(cursor))){
                    cursor = (cursor + 1) % DNASIZE ;
                }

                dna[cursor] = parents[i].getDNA(cursor);
            }

            for (int j = 0; j < XP; j++) {
                while(Arrays.asList(dna).contains(parents[i].getDNA(cursor))){
                    cursor = (cursor + 1) % DNASIZE ;
                }

                dna[cursor] = parents[i].getDNA(cursor);
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested

        }

        return new Tuple<>(children[0], children[1]);
    }
}
