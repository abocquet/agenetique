package eu.labrush.traveller.operators.reproduction;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.traveller.TravelFactory;

import java.util.Arrays;


//TODO: TESTS
// TODO: same issue as order 2

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

        for(int i = 0 ; i < DNASIZE ; i++){
            for(int j = 0 ; j < 4 ; j++){
                neighboors[i][j] = -1 ;
            }
        }

        cutDNA(f1, neighboors, pos);
        cutDNA(f2, neighboors, pos);


        AbstractFellow[] children = new AbstractFellow[2] ;

        for(int i = 0 ; i < 2 ; i++) {
            int[] dna = new int[DNASIZE];
            Arrays.fill(dna, -1);

            boolean[] usedGenes = new boolean[DNASIZE];

            int current = (int) ((Math.random() * 2 * DNASIZE) % DNASIZE);

            for(int j = 0 ; j < DNASIZE ; j++){
                //fst: la valeur, snd: le nombre de voisins
                Tuple<Integer, Integer> next = new Tuple<>(-1, -1);
                for(int k = 0 ; k < 4 ; k++){
                    if(neighboors[current][k] != -1 && !usedGenes[neighboors[current][k]] && (next.fst < 0 || (pseudoLenght(neighboors[neighboors[current][k]]) <= next.snd))){
                        next.fst = neighboors[j][k] ;
                        next.snd = pseudoLenght(neighboors[k]);
                    }
                }

                current = next.fst ;

                while(current == -1 || usedGenes[current]){
                    current = (current+1) % DNASIZE ;
                }

                dna[j] = current ;
                usedGenes[current] = true ;
            }

            children[i] = ((TravelFactory)factory).newInstance(dna, false); // TODO: set false to true once tested
        }

        return new Tuple<>(children[0], children[1]);
    }

    private void cutDNA(AbstractFellow f, int[][] neighboors, int[] pos) {
        int DNASIZE = f.getDNASIZE() ;
        for(int i = 0 ; i < DNASIZE ; i++){
            for(int j = i - 1 ; j <= i + 1 ; j += 2) {
                int k = (j + DNASIZE) % DNASIZE ;

                if(!arrayContains(neighboors[i], f.getDNA(k))){
                    neighboors[i][pos[i]] = f.getDNA(k);
                    pos[i] += 1;
                }
            }
        }
    }

    private static boolean arrayContains(int[] u, int e){
        for(int i = 0 ; i < u.length ; i++){
            if(u[i] == e) return true ;
        }

        return false ;
    }

    private int pseudoLenght(int[] u ){
        int c = 0 ;
        for (int i: u){
            if (i > 0){
                c++ ;
            }
        }
        return c ;
    }
}
