package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;


//TODO: TESTS

/**
 * Crée la matrice des villes adjacent pour chaque point puis
 * 1 - prend la ville qui a le plus petit nombre de voisins si elle existe
 * 2 - en choisie une au hasard sinon
 */
public class ArcCombination implements CrossoverInterface {
    @Override
    public Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow f1, AbstractFellow f2, AbstractFellowFactory factory) {
        int DNASIZE = f1.getDNASIZE();
        int[][] neighboors = new int[DNASIZE][4];
        int[] pos = new int[DNASIZE] ;
        
        cutDNA(f1, neighboors, pos);
        cutDNA(f2, neighboors, pos);

        for(int i = 0 ; i < DNASIZE ; i++){
            for(int j = 0 ; j < 4 ; j++){
                neighboors[i][j] = -1 ;
            }
        }

        AbstractFellow[] children = new AbstractFellow[2] ;

        for(int i = 0 ; i < 2 ; i++) {
            int[] dna = new int[DNASIZE];
            boolean[] usedGenes = new boolean[DNASIZE];

            int current = (int) ((Math.random() * 2 * DNASIZE) % DNASIZE);
            usedGenes[current] = true ;

            for(int j = 0 ; j < DNASIZE ; j++){
                dna[j] = current ;

                Tuple<Integer, Integer> next = new Tuple<>(-1, -1);
                for(int k = 0 ; k < 4 ; k++){
                    if(!usedGenes[k] && (pos[k] <= next.snd)){
                        next.fst = k ;
                    }
                }

                current = next.fst ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested
        }

        return new Tuple<>(children[0], children[1]);
    }

    private void cutDNA(AbstractFellow f, int[][] neighboors, int[] pos) {
        int DNASIZE = f.getDNASIZE() ;
        for(int i = 0 ; i < DNASIZE ; i++){
            for(int j = i - 1 ; j <= i + 1 ; i += 2) {
                int k = (j + DNASIZE) % DNASIZE ;

                if(!Arrays.asList(neighboors[k]).contains(f.getDNA(k))){
                    neighboors[k][pos[k]] = f.getDNA(k);
                    pos[k] += 1;
                }
            }
        }
    }
}
