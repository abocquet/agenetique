package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Rempli l'enfant à partir du parent 1,
 * tire 4 points dans le parent 2 et les insère dans 1 en fonction de leur ordre d'apparition
 */
public class Order2 implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        AbstractFellow[] parents = new AbstractFellow[]{f1, f2};
        AbstractFellow[] children = new AbstractFellow[2] ;
        int DNASIZE = f1.getDNASIZE();

        Tuple<Integer, Integer>[] cutPoints = new Tuple[4];
        for(int j = 0 ; j < cutPoints.length ; j++){
            cutPoints[j] = new Tuple<>((int) ((Math.random() * 2 * DNASIZE) % DNASIZE), 0);
        }

        for(int i = 0 ; i < 2 ; i++) {
            int ib = (1 + i) % 2 ;

            int[] dna = new int[DNASIZE] ;
            Arrays.fill(dna, -1);

            for (int j = 0; j < DNASIZE; j++) {
                for (int k = 0; k < cutPoints.length ; k++) {
                    if(cutPoints[k].fst == parents[ib].getDNA(j))
                        cutPoints[k].snd = j;
                }
            }

            Arrays.sort(cutPoints, Comparator.comparingInt(a -> a.snd)); // On trie les points par ordre d'appartion dans le second parent

            for(int j = 0 ; j < DNASIZE ; j++){
                dna[j] = parents[i].getDNA(j);

                for (int k = 0; k < cutPoints.length ; k++)
                    if (cutPoints[k].fst == parents[i].getDNA(j))
                        cutPoints[k].snd = j;
            }

            for (int j = 0; j < cutPoints.length; j++) {
                dna[cutPoints[j].snd] = cutPoints[(j+1) % cutPoints.length].fst ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, true);


        }

        return new Tuple<>(children[0], children[1]);
    }
}
