package eu.labrush.NEAT;

import java.util.Arrays;

/**
 * http://nn.cs.utexas.edu/downloads/papers/stanley.phd04.pdf
 */

public class Main {


    public static void main(String[] args){
        Nature nature = new Nature(150, 2, 1, f -> {
            double score = 0 ;

            score += Math.pow(1.0 - f.thinkAbout(new double[]{-1, -1})[0], 2);
            score += Math.pow(f.thinkAbout(new double[]{1, -1})[0], 2);
            score += Math.pow(f.thinkAbout(new double[]{-1, 1})[0], 2);
            score += Math.pow(1.0 - f.thinkAbout(new double[]{1, 1})[0], 2);

            return score ;
        });

        int NGEN = 150 ;
        for (int i = 0; i < NGEN; i++) {
            nature.evolve();
        }

        Fellow f = nature.getBest();

        System.out.println("");
        System.out.println(f.getNodes());
        System.out.println(f.getConnections());

        double[][] tests = new double[4][];

        tests[0] = new double[]{-1, -1, 0} ; // arg1 arg2 resultat attendu
        tests[1] = new double[]{1, 1, 0} ;
        tests[2] = new double[]{-1, 1, 1} ;
        tests[3] = new double[]{1, -1, 1} ;

        System.out.println("");  // Affiche 1 si juste, 0 si faux

        for (int i = 0; i < tests.length; i++) {
            System.out.println(Arrays.toString(tests[i]) + " -> " + (Math.round(f.thinkAbout(tests[i])[0]) == tests[i][2] ? "CORRECT" : "ERREUR"));
        }

    }

}
