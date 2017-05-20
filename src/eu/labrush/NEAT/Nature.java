package eu.labrush.NEAT;

import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.random;

public class Nature {

    int POPSIZE = 20 ;
    private int ELITISM = 1;

    ArrayList<Species> species = new ArrayList<>(); // Representative, others

    private final FitnessEvaluator evaluator;

    public Nature(int POPSIZE, int ELITISM, int sensors, int output, FitnessEvaluator evaluator) {

        this.POPSIZE = POPSIZE ;
        this.ELITISM = ELITISM ;
        this.evaluator = evaluator ;

        Fellow original = new Fellow(sensors, output);

        for (int i = 0; i < POPSIZE; i++) {
            addFellow(original.clone().changeConnectionWeights());
        }
    }

    public void evaluate(){

        for(Fellow f: getFellows()){
            f.setFitness(evaluator.eval(f));
        }

    }

    public void evolve(){

        // We save best elements
        for(Species s: species) {
            s.saveElite(ELITISM);
        }

        //Usual GAs stuff
        evaluate();
        crossover();
        mutate();

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
                if (random() <= Config.P_CONNECTION_ADD_MUTATION) {
                    Mutation.addConnectionMutation(f);
                }

                if (random() <= Config.P_CONNECTION_DEL_MUTATION) {
                    Mutation.delConnectionMutation(f);
                }

                if (random() <= Config.P_NODE_ADD_MUTATION) {
                    Mutation.addNodeMutation(f);
                }

                if (random() <= Config.P_NODE_DEL_MUTATION) {
                    Mutation.delNodeMutation(f);
                }
            }
        }

    }

    public void crossover(){

        for(Species s: species){
            if(s.adjustedFitness() <= Config.SURVIVAL_SPECIES_THRESHOLD){
                species.remove(s);
            }
        }

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
            ntot-- ;
            k++ ;
        }

        k = 0 ; // on fait deux choses opposées mais on a disjonction des cas
        while(ntot < POPSIZE){
            n[k]++ ;
            ntot++ ;
            k++ ;
        }

        for(Species s: species){
            s.age++ ;

            double af = s.adjustedFitness() ;
            if(af <= s.last_fitness + 0.01){
                s.last_improved++ ;
            }
            s.last_fitness = af ;
        }

        for (int i = 0, c = species.size() ; i < c ; i++) {
            Species s = species.get(i);
            s.dumpDummies();

            int m = s.fellows.size() ;
            int j = m ;

            if(m == 0){
                continue;
            }

            while(j < n[i]){
                if(random() <= Config.INTERSPECIES_RATE){

                    int ms = species.size();

                    addFellow(Crossover.crossover(
                        species.get((int) (random() * (ms + 1)) % ms).getRandom(),
                        s.getRandom()
                    ));

                } else {
                    addFellow(Crossover.crossover(
                        s.getRandom(),
                        s.getRandom()
                    ));
                }
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
                s.addFellow(f);
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
