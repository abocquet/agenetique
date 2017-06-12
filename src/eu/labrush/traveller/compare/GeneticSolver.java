package eu.labrush.traveller.compare;

import eu.labrush.agenetic.operators.selection.WheelAndRandomSelector;
import eu.labrush.traveller.Nature;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.operators.mutation.Im;
import eu.labrush.traveller.operators.reproduction.AssortiPartiel;

public class GeneticSolver implements TSPSolver {

    @Override
    public long solveSet(PointSet set, double error, long maxtime) {

        long t1 = System.currentTimeMillis() ;

        Nature nature = new Nature(50, 1, 0.5, 0.05, 0.01, set, new AssortiPartiel(), new Im(), new WheelAndRandomSelector());

        int i = 0, p = 1000;
        while (nature.getShortest() * 100 > set.getMinDist() * (1 + error) * 100) {
            nature.evolve(false);
            i++;
        }

        long t2 = System.currentTimeMillis();
        return t2 - t1 ;

    }
}
