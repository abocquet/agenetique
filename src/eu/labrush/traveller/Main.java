package eu.labrush.traveller;

import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.im;
import eu.labrush.traveller.operators.reproduction.Order1;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        //System.out.println(factory.getProblems());

        runTests(new String[]{"brd14051"});
        //runTests(new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"});

        System.out.println("DONE !!!");

    }


    private static void runTests(String[] names){
        PointSetFactory factory = new PointSetFactory() ;

        for(String name: names){
            PointSet problem = factory.getSet(name);
            System.out.println(problem.getDesc());

            Nature nature = new Nature(50, 0.5, 0.05, problem, new Order1(), new im());
            Logger logger = new Logger("logs/" + problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

            int i = 0, p = 1000;
            while (nature.getShortest().multiply(BigInteger.valueOf(100)).compareTo(problem.getMinDist().multiply(BigInteger.valueOf(105))) > 0) {
                if (i % p == 0) {
                    logger.log();
                    //System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
                }

                nature.evolve(false);
                i++;
            }

            logger.log();
            System.out.println("Problem " + name + " génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");
        }
    }

}

