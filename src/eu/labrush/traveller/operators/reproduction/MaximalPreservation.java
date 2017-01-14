package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

/**
 * Tire deux positions XP1 et XP2, cherche la position X du gène de XP1
 * dans le  parent 2 et recopie une séquence de (XP2 - XP1) allèles dans
 * l'enfant. On complète ensuite avec le reste du génome du parent 1
 */
public class MaximalPreservation implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int XP1 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE;
        int XP2 = (int) (Math.random() * 2 * DNASIZE) % DNASIZE;

        if(XP1 > XP2){
            int tmp = XP2 ;
            XP2 = XP1 ;
            XP1 = tmp ;
        }

        for(int i = 0 ; i < 2 ; i++) {
            int ib = (1+i) % 2 ;

            int[] dna = new int[DNASIZE] ;
            Arrays.fill(dna, -1);

            int cursorE = 0 ;
            while(parents[ib].getDNA(cursorE) != parents[i].getDNA(XP1)){
                cursorE++ ;
            }

            for (int j = 0 ; j < XP2 - XP1; j++) {
                dna[cursorE] = parents[ib].getDNA(cursorE);
                cursorE = (cursorE + 1) % DNASIZE ;
            }

            int cursorP = cursorE ;

            for (int j = 0; j < DNASIZE - (XP2 - XP1) ; j++) {
                while(arrayContains(dna, parents[ib].getDNA(cursorP))){
                    cursorP = (cursorP + 1) % DNASIZE ;
                }

                dna[cursorE] = parents[ib].getDNA(cursorP);
                cursorE = (cursorE + 1) % DNASIZE ;
                cursorP = (cursorP + 1) % DNASIZE ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, true);

        }

        return new Tuple<>(children[0], children[1]);
    }

    private static boolean arrayContains(int[] u, int e){
        for(int i = 0 ; i < u.length ; i++){
            if(u[i] == e) return true ;
        }

        return false ;
    }
}
