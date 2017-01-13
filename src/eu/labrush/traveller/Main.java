package eu.labrush.traveller;

import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.*;
import eu.labrush.traveller.operators.reproduction.*;

public class Main {

    public static void main(String[] args) {

        //System.out.println(factory.getProblems());

        //runTests(new String[]{"brd14051"});
        //runTests(new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"});

        //runTestDuSwagPleinDeTestsYolo("berlin52");

         //Cim Im Throas Thrors Twor
         // Cyclic MaximalPreservation Order1 Order2 Syswerda Uniform ArcCombination


        PointSetFactory factory = new PointSetFactory() ;
        PointSet problem = factory.getSet("berlin52");
        Nature nature = new Nature(50, 1,0.5, 0.05, 0, problem, new Cyclic(), new Im());
        Logger logger = new Logger("logs/" + problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

        int i = 1, p = 1000;
        while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
            if (i % p == 0) {
                System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
            }

            nature.evolve(false);
            i++;
        }

        logger.log();
        System.out.println(problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");


        System.out.println("DONE !!!");

    }

    private static void runTests(String[] names){
        PointSetFactory factory = new PointSetFactory() ;

        for(String name: names){
            PointSet problem = factory.getSet(name);
            System.out.println(problem.getDesc());

            Nature nature = new Nature(50, 1, 0.5, 0.05, 0.00, problem, new Order1(), new Im());
            Logger logger = new Logger("logs/" + problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

            int i = 0, p = 100;
            while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
                if (i % p == 0) {
                    logger.log();
                    System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
                }

                nature.evolve(false);
                i++;
            }

            logger.log();
            System.out.println("Problem " + name + " génération " + i + " " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");
        }
    }

    private static void runTestDuSwagPleinDeTestsYolo(String name){

        PointSetFactory factory = new PointSetFactory();
        PointSet set = factory.getSet(name);

        MutationInterface[] mutations = new MutationInterface[]{new Cim(), new Im(), new Throas(), new Thrors(), new Twor()};
        CrossoverInterface[] crossover = new CrossoverInterface[]{new ArcCombination(), new AssortiPartiel(), new Cyclic(), new MaximalPreservation(), new Order1(), new Order2(), new Syswerda(), new Uniform() };

        for(CrossoverInterface co: crossover){
            for (MutationInterface mo: mutations){
                runTestForConf(set, co, mo);
            }
        }

    }

    private static void runTestForConf(PointSet problem, CrossoverInterface co, MutationInterface mo){
        System.out.println(problem.getDesc());

        Nature nature = new Nature(50, 1,0.5, 0.05, 0, problem, co, mo);
        Logger logger = new Logger("logs/" + problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

        int i = 0, p = 1000;
        while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
            if (i % p == 0) {
                System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
            }

            nature.evolve(false);
            i++;
        }

        logger.log();
        System.out.println(co.getClass().toString() + " - " + mo.getClass().toString() + " - " + problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");
    }

}

