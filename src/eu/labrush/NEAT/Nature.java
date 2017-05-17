package eu.labrush.NEAT;

import eu.labrush.agenetic.Tuple;

import java.util.ArrayList;
import java.util.HashSet;

public class Nature {

    int POPSIZE = 20 ;
    private int ELITISM = 1;

    double P_NODE_MUTATION = .7 ;
    double P_CONNECTION_MUTATION = .7 ;

    ArrayList<Species> species = new ArrayList<>(); // Representative, others

    public Nature(int POPSIZE, int ELITISM, int sensors, int output) {

        Fellow initial = new Fellow(sensors, output);
        this.POPSIZE = POPSIZE ;
        this.ELITISM = ELITISM ;

        for (int i = 0; i < POPSIZE; i++) {
            addFellow(initial.clone());
        }
    }

    public void evolve(){

        ArrayList<Tuple<String, Fellow>> elite = new ArrayList<>(species.size());
        Fellow[] newPop = new Fellow[POPSIZE] ;
        int c = 0 ;

        // We save best elements
        int k = 0 ;
        for(Species s: species) {
            s.saveElite(ELITISM);
        }

        //Usual GAs stuff
        mutate();
        crossover();

        //Put the elite back
        for(Species s: species) {
            s.loadElite();
        }

        // TODO: si on prend l'élite de chaque espèce, une espece ne peut jamais disparaitre donc on fini
        // par stagner si on a NPOP = NESPECES
        /*for(Species s: species){
            if(s.fellows.size() == 0){
                species.remove(s);
            }
        }*/
    }

    public void mutate(){

        for(Species s: species){
            for(Fellow f: s.fellows){
                //TODO: connection mutation is favored, set it unbiased
                if (Math.random() <= P_CONNECTION_MUTATION){
                    Mutation.connectionMutation(f);
                } else if(Math.random() <= P_NODE_MUTATION){
                    Mutation.nodeMutation(f);
                }
            }
        }

    }

    public void crossover(){

        double[] avgFitness = new double[species.size()]; // the avg fitness of every species
        int[] n = new int[species.size()] ; //the number of fellows every species gets in the next generation
        double totalAvgFitness = 0.0 ;
        for (int i = 0, c = species.size(); i < c; i++) {
            avgFitness[i] = species.get(i).averageFitness();
            totalAvgFitness += avgFitness[i] ;
        }

        int ntot = 0 ;
        for (int i = 0, c = species.size() ; i < c; i++) {
            n[i] = (int) (avgFitness[i] / totalAvgFitness * POPSIZE);
            ntot += n[i] ;
        }

        // On peut dépasser en calculant les n[k], donc on corrige
        int k = 0 ;
        while(ntot > POPSIZE){
            if(n[k] >= 2){
                n[k]-- ;
            }
            k++ ;
        }

        k = 0 ; // on fait deux choses opposées mais on a disjonction des cas
        while(ntot < POPSIZE){
            n[k]++ ;
            k++ ;
        }

        for(Species s: species){
            s.dumpDummies();
        }

        for (int i = 0, c = species.size() ; i < c ; i++) {
            Species s = species.get(i);
            int m = s.fellows.size() ;
            int j = m ;

            if(m == 0){
                continue;
            }

            while(j < n[i]){
                addFellow(Crossover.crossover(
                        s.get((int) (Math.random() * (m + 1)) % m),
                        s.get((int) (Math.random() * (m + 1)) % m)
                ));
                j++ ;
            }
        }

    }

    public Fellow getBest(){

        Fellow best = species.get(0).get(0);
        for(Species s: species) {
            for (Fellow f: s.getFellows()) {
                if (best.getFitness() < f.getFitness()) {
                    best = f;
                }
            }
        }

        return best ;
    }

    public void addFellow(Fellow f){
        for(Species s: species){
            if(s.closeTo(f)){
                s.add(f);
                return ;
            }
        }

        // Si on a affaire à une nouvelle espèce, on la crée
        species.add(new Species(f));
    }

    public HashSet<Fellow> getFellows() {
        HashSet<Fellow> fellows = new HashSet<>();

        for(Species s: species) {
            fellows.addAll(s.getFellows());
        }

        return fellows ;
    }
}
