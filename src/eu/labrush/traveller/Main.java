package eu.labrush.traveller;

import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

public class Main {

    public static void main(String[] args) {

        PointSetFactory factory = new PointSetFactory() ;
        //System.out.println(factory.getProblems());

        PointSet problem = factory.getSet("brd14051") ;
        Nature nature = new Nature(50, 0.5, 0.05, problem, new Order1(), new im());
        //System.out.println(nature);

        Logger logger = new Logger("traveller.csv", nature);

        /*for(int i = 0, N = 10_000, p = 10 ; i < N+1 ; i++){
            if(i % p == 0) {
                logger.log();
                System.out.println(i/p + " / " + N/p);
            }
            nature.evolve();
        }*/

        int i = 0, p = 10 ;
        while(nature.getShortest() * 100 > problem.getMinDist() * 105){
            if(i % p == 0) {
                logger.log();
                System.out.println("Génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist());
            }
            nature.evolve();

            i++ ;
        }

        System.out.println("");
        System.out.println("Génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist());
        System.out.println(nature.introduceYourself());


    }
}
