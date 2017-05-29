package eu.labrush.numfun.functions;

import eu.labrush.numfun.NumFunction;

import java.util.Arrays;

/**
 * On
 */
public class Tobleron extends NumFunction {

    int[] prix = new int[]{0,1,5,8,9,10,17,17,20,24,26};

    @Override
    public long f(int[] dna) {

        int sum = 0 ;
        int[] taille = new int[10] ;
        int taille_totale = 0 ;

        for (int i = 0; i < 10; i++) {
            taille[i] = (int) getArguments(new int[]{ dna[4*i], dna[4*i + 1], dna[4*i + 2], dna[4*i + 3]}) % 11 ;
            taille_totale += taille[i];

            if(taille_totale >= prix.length){
                while(taille_totale >= prix.length){
                    taille[i]-- ;
                    taille_totale-- ;
                }
                break;
            }
        }

        System.out.println(Arrays.toString(taille));

        for (int i = 0; i < taille.length; i++) {
            sum += prix[taille[i]];
        }

        return sum ;

    }

    @Override
    public int getDNASIZE() {
        return 40;
    }

}
