package eu.labrush.NEAT;

/**
 * Attention !
 * Le numéro d'évolution est utilisé à la fois pour les numeros de noeud ET de connection entre eux,
 * puisque l'article de Stanley ne décrit pas comment gérer la numération des noeuds
 *
 * http://nn.cs.utexas.edu/downloads/papers/stanley.phd04.pdf
 */

public class main {


    public static void main(String[] args){
        Nature nature = new Nature(150, 1, 2, 1, f -> {
            double score = 0 ;

            score += 1.0 - f.thinkAbout(new double[]{-1, -1})[0];
            score += f.thinkAbout(new double[]{1, -1})[0];
            score += f.thinkAbout(new double[]{-1, 1})[0];
            score += 1.0 - f.thinkAbout(new double[]{1, 1})[0];

            return score * score ;
        });
        int NGEN = 70 ;
        for (int i = 0; i < NGEN; i++) {
            System.out.print(".");
            nature.evolve();
        }

        Fellow f = nature.getBest();

        System.out.println("");
        System.out.println(f.getNodes());
        System.out.println(f.getConnections());

        System.out.println("");
        System.out.println(f.thinkAbout(new double[]{-1, -1})[0]);
        System.out.println(f.thinkAbout(new double[]{1, 1})[0]);
        System.out.println(f.thinkAbout(new double[]{-1, 1})[0]);
        System.out.println(f.thinkAbout(new double[]{1, -1})[0]);
        System.out.println("");

        System.out.println(f.getFitness());
    }

}
