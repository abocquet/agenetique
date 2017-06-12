package eu.labrush.traveller.compare;

import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;

public class Main {

    public static void main(String[] args) {


        PointSetFactory factory = new PointSetFactory();
        PointSet problem = factory.getSet("rat783");


        //TSPSolver solver = new GeneticSolver();
        //System.out.println(solver.solveSet(problem, 0.1, 24 * 360 * 60_000));


        TSPSolver[] solvers = new TSPSolver[]{new GeneticSolver(), new AnnealingSolver()};
        for (TSPSolver s: solvers){
            System.out.print(s.getClass().getSimpleName() + " : ");
            System.out.println(s.solveSet(problem, 0.1, 24 * 360 * 60_000));
        }

    }


}

