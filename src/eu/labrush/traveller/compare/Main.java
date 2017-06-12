package eu.labrush.traveller.compare;

import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.data.PointSetFactory;

public class Main {

    public static void main(String[] args) {

        String[] problems = new String[]{ "eil51", "eil76", "lin105", "pr107", "pr124", "d198", "lin318", "p654"};

        for (String p: problems) {
            PointSetFactory factory = new PointSetFactory();
            PointSet problem = factory.getSet(p);

            System.out.println("\n" + p);

            TSPSolver[] solvers = new TSPSolver[]{new GeneticSolver(), new AnnealingSolver()};
            for (TSPSolver s : solvers) {
                System.out.print(s.getClass().getSimpleName() + " : ");
                System.out.println(s.solveSet(problem, 0.1, 24 * 360 * 60_000));
            }
        }

    }


}

