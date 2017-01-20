package eu.labrush.traveller;

import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.*;
import eu.labrush.traveller.operators.reproduction.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {

        //System.out.println(factory.getProblems());

        //runTests(new String[]{"brd14051"});
        //runTests(new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"});
        runTestDuSwagPleinDeTestsYolo("lin318");

        System.out.println("It's all done !");

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

        int nbTests = 10 ; // Number of tests to process so as to do an average

        MutationInterface[] mutations = new MutationInterface[]{new Cim(), new Im(), /*new Throas(),*/ new Thrors(), new Twor()};
        CrossoverInterface[] crossover = new CrossoverInterface[]{new ArcCombination(), new AssortiPartiel(), new Cyclic(), new MaximalPreservation(), new Order1(), new Order2(), new Syswerda(), new Uniform() };

        //MutationInterface[] mutations = new MutationInterface[]{new Cim(), new Im()};
        //CrossoverInterface[] crossover = new CrossoverInterface[]{new Order1(), new Order2()};

        try { // Todo: ajouter min, et max
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS_yyyy-MM-dd");
            PrintWriter logger = new PrintWriter("logs/Yolo#" + sdf.format(new Date()) + ".csv", "UTF-8");

            logger.print(set.getName() + ";");

            for (CrossoverInterface co: crossover){
                logger.print("score moyen,ecart-type;");
            }

            logger.print("\n");

            for (CrossoverInterface co: crossover){
                logger.print(co.getClass().getSimpleName() + ";") ;
            }

            logger.print("Operateur de mutation;") ;


            int total = mutations.length  * crossover.length * nbTests ;
            int counter = 1 ;

            for (MutationInterface mo: mutations){
                logger.print("\n" + mo.getClass().getSimpleName() + ";") ;
                for(CrossoverInterface co: crossover){

                    double results[] = new double[nbTests];
                    for (int i = 0; i < nbTests ; i++) {
                        results[i] = (double) runTestForConf(set, co, mo);
                        System.out.println(counter + " / " + total);
                    }

                    logger.print(String.format(Locale.FRANCE, "%.2f", SimpleStats.avg(results)) + ";");
                    logger.print(String.format(Locale.FRANCE, "%.2f", SimpleStats.ecartType(results)) + ";");
                    logger.flush();
                }


            }

            logger.print("\n") ;
            logger.flush();
            logger.close();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static int runTestForConf(PointSet problem, CrossoverInterface co, MutationInterface mo, String folder) {
        Nature nature = new Nature(50, 3,0.5, 0.05, 0, problem, co, mo);

        int limit = 100000 ;
        int i = 0, p = 1000;
        while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
            if(i > limit){
                System.err.println("Too long...");
                break ;
            }

            nature.evolve(false);
            i++;
        }

        System.out.println(co.getClass().getSimpleName() + " - " + mo.getClass().getSimpleName() + " - " + problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");

        return i ;
    }

    private static int runTestForConfAndLog(PointSet problem, CrossoverInterface co, MutationInterface mo, boolean log){
        System.out.println(problem.getDesc());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS_yyyy-MM-dd");

        Nature nature = new Nature(50, 3,0.5, 0.05, 0, problem, co, mo);
        Logger logger = new Logger("logs/" + problem.getName() + "_" + sdf.format(new Date()) + ".csv", nature);

        int limit = 100000 ;
        int i = 0, p = 1000;
        while (nature.getShortest() * 100 > problem.getMinDist() * 105) {
            /*if (i % p == 0) {
                System.out.println("Génération " + i + " " + nature.getShortest() + " / " + problem.getMinDist());
            }*/

            if(i > limit){
                System.err.println("Too long...");
                break ;
            }

            nature.evolve(false);
            i++;

            logger.log();
        }


        System.out.println(co.getClass().toString() + " - " + mo.getClass().toString() + " - " + problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist() + "\n");

        return i ;
    }

}

