package eu.labrush.NEAT;

/**
 * Attention !
 * Le numéro d'évolution est utilisé à la fois pour les numeros de noeud ET de connection entre eux,
 * puisque l'article de Stanley ne décrit pas comment gérer la numération des noeuds
 *
 * http://nn.cs.utexas.edu/downloads/papers/stanley.phd04.pdf
 */

public class main {

    public static double eval(Fellow f){
        double score = 0 ;

        if(f.thinkAbout(new double[]{0, 0})[0] < 0.5){ score++ ; }
        if(f.thinkAbout(new double[]{1, 0})[0] > 0.5){ score++ ; }
        if(f.thinkAbout(new double[]{0, 1})[0] > 0.5){ score++ ; }
        if(f.thinkAbout(new double[]{1, 1})[0] < 0.5){ score++ ; }

        return score ;
    }

    public static void main(String[] args){

        Nature nature = new Nature(20, 1, 2, 1);

        int NGEN = 20 ;
        for (int i = 0; i < NGEN; i++) {
            System.out.println(i);
            for(Fellow f: nature.getFellows()){
                f.setFitness(eval(f));
            }

            nature.evolve();
        }

        eval(nature.getBest());
    }

}
