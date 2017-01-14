package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;

/**
 * On séléctionne un gène G.X à la position X du parent 1, le place à la position X dans l'enfant
 * ensuite on rempli l'adn avec celui du parent 2 jusqu'à ce qu'on retrouve G.X dans le parent 2
 * et enfin on termine avec les gènes du parent 1
 */
public class Cyclic implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        int XP =  (int) ((Math.random() * 2 * DNASIZE) % DNASIZE) ;

        for(int i = 0 ; i < 2 ; i++) {
            int i1 = (i + 1) % 2; // the other parent

            int[] dna = new int[DNASIZE];
            Arrays.fill(dna, -1);

            int XPi = parents[i].getDNA(XP);
            int cursorP = (XP + 1) % DNASIZE;
            int cursorE = cursorP;

            dna[XP] = parents[i].getDNA(XP);

            while (parents[i1].getDNA(cursorP) != XPi) {
                dna[cursorP] = parents[i1].getDNA(cursorP);
                cursorP = (cursorP + 1) % DNASIZE;
            }

            cursorE = cursorP;

            while (cursorE != XP) {
                while (arrayContains(dna, parents[i].getDNA(cursorP))) {
                    cursorP = (cursorP + 1) % DNASIZE;
                }

                dna[cursorE] = parents[i].getDNA(cursorP);
                cursorE = (cursorE + 1) % DNASIZE;

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
