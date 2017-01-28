package eu.labrush.traveller;

import eu.labrush.agenetic.operators.*;
import eu.labrush.agenetic.operators.selection.WheelAndRandomSelector;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;
import eu.labrush.traveller.operators.mutation.*;
import eu.labrush.traveller.operators.reproduction.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        //System.out.println(factory.getProblems());

        runTests(new String[]{"usa13509"});
        //runTests(new String[]{"berlin52", "kroA100", "kroA150", "kroA200", "lin318", "pr439", "rat575", "rat783", "rl1304", "rl1889"});
        //runTestDuSwagPleinDeTestsYolo("berlin52");

        System.out.println("It's all done !");

    }

    private static void runTests(String[] names){
        PointSetFactory factory = new PointSetFactory() ;

        for(String name: names){
            PointSet problem = factory.getSet(name);
            System.out.println(problem.getDesc());

            Nature nature = new Nature(50, 1, 0.5, 0.05, 0.00, problem, new Order1(), new Im(), new WheelAndRandomSelector());
            Logger logger = new Logger("logs/" + problem.getName() + "_" + System.currentTimeMillis() + ".csv", nature);

            int i = 0, p = 1000;
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

        //MutationInterface[] mutations = new MutationInterface[]{new Cim(), new Im(), /*new Throas(),*/ new Thrors(), new Twor()};
        //CrossoverInterface[] crossover = new CrossoverInterface[]{new ArcCombination(), new AssortiPartiel(), new Cyclic(), new MaximalPreservation(), new Order1(), new Order2(), new Syswerda(), new Uniform() };

        MutationInterface[] mutations = new MutationInterface[]{new Im(), new Cim()};
        CrossoverInterface[] crossover = new CrossoverInterface[]{new Order1(), new Order2()};
        SelectorInterface[] selection = new SelectorInterface[]{new WheelAndRandomSelector()} ;

        try { // Todo: ajouter min, et max
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS_yyyy-MM-dd");
            PrintWriter logger = new PrintWriter("logs/FullTest # " + sdf.format(new Date()) + ".csv", "UTF-8");

            int total = mutations.length  * crossover.length * selection.length * nbTests ;
            int counter = 1 ;

            for (MutationInterface mo: mutations){
                for(CrossoverInterface co: crossover) {
                    for (SelectorInterface so : selection) {

                        for (int i = 0; i < nbTests; i++) {
                            int result = runTestForConf(set, co, mo, so);

                            String str = "";
                            str += set.getName() + ";";
                            str += mo.getClass().getSimpleName() + ";";
                            str += co.getClass().getSimpleName() + ";";
                            str += so.getClass().getSimpleName() + ";";
                            str += result;
                            str += "\n";

                            logger.print(str);

                            System.out.print(counter + " / " + total + " ");
                            counter++;
                        }

                        logger.flush();
                    }
                }

            }

            logger.print("\n") ;
            logger.flush();
            logger.close();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static int runTestForConf(PointSet problem, CrossoverInterface co, MutationInterface mo, SelectorInterface so) {
        Nature nature = new Nature(50, 3,0.5, 0.05, 0, problem, co, mo, so);

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

        System.out.println(
                co.getClass().getSimpleName() + " - " + mo.getClass().getSimpleName() + " - " + so.getClass().getSimpleName() + " - "
                + problem.getName() + " génération " + i + " : " + nature.getShortest() + " / "  + problem.getMinDist()
        );

        return i ;
    }

}

