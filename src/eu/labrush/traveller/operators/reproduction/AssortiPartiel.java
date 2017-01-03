package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

//TODO: TESTS
public class AssortiPartiel implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int XP1 = (int) ((Math.random() * 2 * DNASIZE) % DNASIZE);
        int XP2 = (int) ((Math.random() * 2 * DNASIZE) % DNASIZE);

        if(XP2 < XP1){
            int tmp = XP2 ;
            XP2 = XP1 ;
            XP1 = tmp ;
        }

        for (int i = 0; i < 2; i++) {
            int dna[] = new int[DNASIZE];

            for (int j = 0 ; j < XP1; j++)       { dna[j] = parents[i].getDNA(j); }
            for (int j = XP2 ; j < DNASIZE; j++) { dna[j] = parents[i].getDNA(j); }

            int cursor = 0 ;
            for (int j = XP1; j < XP2; j++) {
                while(Arrays.asList(dna).contains(parents[(i+1 % 2)].getDNA(cursor))){
                    cursor = (cursor + 1) % DNASIZE ;
                }
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested
        }

        return new Tuple<>(children[0], children[1]);
    }
}
