package eu.labrush.traveller.compare;

import eu.labrush.traveller.data.PointSet;

public interface TSPSolver {

    long solveSet(PointSet set, double error, long maxtime); // Takes a required precision and returns the number of iterations

}
