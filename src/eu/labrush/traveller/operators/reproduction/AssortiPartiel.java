package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

/**
 * fait une partition en 3 de chaque parent,
 * recopie la premiere et la deniere partie du parent A dans un enfant A et complete
 * la 2eme avec avec celui du parent B
 */
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
            int i1 = (i+1) % 2 ;

            int dna[] = new int[DNASIZE];
            Arrays.fill(dna, -1);

            for (int j = 0 ; j < XP1; j++)       { dna[j] = parents[i].getDNA(j); }
            for (int j = XP2 ; j < DNASIZE; j++) { dna[j] = parents[i].getDNA(j); }

            int cursor = XP1 ;
            for (int j = XP1; j < XP2; j++) {
                while(arrayContains(dna, parents[i1].getDNA(cursor))) {
                    cursor = (cursor + 1) % DNASIZE;
                }

                dna[j] = parents[i1].getDNA(cursor) ;
                cursor = (cursor + 1) % DNASIZE ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, true);
        }

        return new Tuple<>(children[0], children[1]);
    }

    private boolean arrayContains(int[] u, int e){
        for(int i = 0 ; i < u.length ; i++){
            if(u[i] == e) return true ;
        }

        return false ;
    }
}
