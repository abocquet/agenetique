package eu.labrush.traveller.compare;

import eu.labrush.NEAT.utils.Random;
import eu.labrush.traveller.data.PointSet;

import java.util.Arrays;

public class AntsSolver implements TSPSolver {

    protected PointSet set ;

    int nb_points ;
    double P[][] ;

    @Override
    public long solveSet(PointSet set, double error, long maxtime) {
        long t1 = System.currentTimeMillis() ;

        this.set = set ;
        nb_points = set.getPoints().length ;

        int N = 50 ; // Nombre de fourmis
        double lambda = .5 ; // coefficient d'évaporation: 0 totale / 1 aucune évaporation
        P = new double[nb_points][] ; // Table des phéromones

        for (int i = 0; i < nb_points; i++) {
            P[i] = new double[nb_points] ;
            for (int j = 0; j < nb_points; j++) {
                P[i][j] = .0 ;
            }
        }

        double dist = Double.POSITIVE_INFINITY;
        int C = Random.randInt(nb_points); // Current points


        while(dist > set.getMinDist() * (1 + error) && System.currentTimeMillis() - t1 <= maxtime) {
            for (int i = 0; i < N; i++) {
                int[] points = new int[nb_points]; // i >= 0 -> la ville i ; i = -1 -> pas de ville
                for (int j = 0; j < nb_points; j++) { points[j] = j ; }

                int nb_remaining_points = nb_points ;

                points[C] = -1 ;
                nb_remaining_points-- ;

                int j = 0 ;
                int[] route = new int[nb_points];
                route[j] = C ;
                while(nb_remaining_points > 0){
                    C = choosePoint(C, points);
                    j++ ;
                    route[j] = C ;

                    points[C] = -1 ;
                    nb_remaining_points-- ;
                }

                dist = calcDist(route);

                for (int k = 0; k < nb_points - 1; k++) {
                    P[route[k]][route[k+1]] += 1 / dist ;
                    P[route[k+1]][route[k]] += 1 / dist ;
                }

                P[route[0]][route[route.length - 1]] += 1 / dist ;
                P[route[route.length - 1]][route[0]] += 1 / dist ;

                for (int k = 0; k < nb_points; k++) {
                    for (int l = 0; l < nb_points; l++) {
                        P[k][l] = P[k][l] * lambda ;
                    }
                }
            }

            //On calcul la distance en suivant le chemin avec le plus de phéromones
            dist = 0 ;
            int current = 0 ; // the visiting point
            boolean[] visited = new boolean[nb_points];
            Arrays.fill(visited, false);
            for (int i = 0; i < nb_points - 1; i++) {
                visited[current] = true ;

                int next = -1 ;
                double nextP = Double.POSITIVE_INFINITY ;

                for (int j = 0; j < nb_points; j++) {
                    if(P[i][j] < nextP && !visited[j]){
                        next = j ;
                        nextP = P[current][j];
                    }
                }

                dist += set.distBetween(next, current);
                current = next ;
            }
            dist += set.distBetween(current, 0);
        }

        long t2 = System.currentTimeMillis();
        return t2 - t1 ;
    }

    private int choosePoint(int current, int[] points) {
        double totalP = 0 ;
        double totalD = 0 ;

        double mD = .3 ;
        double mP = 1 - mD ;

        double[] probability = new double[nb_points] ;

        for (int i = 0; i < nb_points; i++) {
            if(i != current && points[i] >= 0) {
                totalP += P[current][i];
                totalD += 1 / set.distBetween(current, i);
            }
        }

        totalP = Math.max(totalP, 0.1);

        for (int i = 0; i < nb_points; i++) {
            if(i == current && points[i] >= 0){
                probability[i] = 0 ;
            } else {
                probability[i] = mD * totalD / set.distBetween(current, i) + mP * P[current][i] / totalP ;
            }
        }

        double P = Math.random();
        double acc = 0.0 ;

        int k = 0 ;
        while(k < nb_points){
            if(acc <= P && P < acc + probability[k]){
                return k ;
            }
            acc += probability[k] ;
            k++ ;
        }

        while(points[k] < 0){
            k-- ;
        }

        return k ;
    }

    protected double calcDist(int[] route){
        long distance  = 0 ;

        for(int i = 0 ; i < route.length - 1 ; i ++){
            distance += set.distBetween(route[i], route[i+1]);
        }

        distance += set.distBetween(route[0], route[route.length - 1]);

        return distance ;
    }


}
