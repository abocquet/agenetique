package eu.labrush.traveller;

import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

public class Main {

    public static void main(String[] args) {

        PointSetFactory factory = new PointSetFactory() ;
        //System.out.println(factory.getProblems());

        for(String name: new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"}){
            PointSet problem = factory.getSet(name);
            System.out.println(problem.getDesc());

            Nature nature = new Nature(50, 0.5, 0.05, problem, new Order1(), new im());
            Logger logger = new Logger("logs/" + problem.getName() + ".csv", nature);

            int i = 0, p = 100;
            nature.evolve(true);
            while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
                if (i % p == 0) {
                    logger.log();
                     System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
                }

                nature.evolve(true);
                i++;
            }

            System.out.println("Problem " + name + " génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");
        }

        //System.out.println("");
        //System.out.println("Génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist());
        //System.out.println(nature.introduceYourself());


    }
}

/*for(int i = 0, N = 10_000, p = 10 ; i < N+1 ; i++){
            if(i % p == 0) {
                logger.log();
                System.out.println(i/p + " / " + N/p);
            }
            nature.evolve();
        }*/