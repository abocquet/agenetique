package eu.labrush.NEAT;

import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.operators.Crossover;
import eu.labrush.NEAT.operators.FitnessEvaluator;
import eu.labrush.NEAT.operators.Mutation;
import eu.labrush.NEAT.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Nature {

    private int POPSIZE  ;
    protected int generationNumber = 0 ;

    ArrayList<Species> species = new ArrayList<>(); // Representative, others
    private final FitnessEvaluator evaluator;

    public Nature(int POPSIZE, int sensors, int output, FitnessEvaluator evaluator) {

        this.POPSIZE = POPSIZE ;
        this.evaluator = evaluator ;

        Fellow original = new Fellow(sensors, output);
        for (int i = 0; i < POPSIZE; i++) {
            addFellow(original.clone());
        }

    }

    public void evolve(){

        evaluate();
        crossover();

        generationNumber++ ;

    }

    private void mutate(Fellow f){
        f.id = Fellow.index.next();

        if (Math.random() <= Config.P_CONNECTION_ADD_MUTATION) {
            Mutation.addConnectionMutation(f);
        }

        if (Math.random() < Config.P_CONNECTION_DEL_MUTATION) {
            Mutation.delConnectionMutation(f);
        }

        if (Math.random() <= Config.P_NODE_ADD_MUTATION) {
            Mutation.addNodeMutation(f);
        }

        if (Math.random() <= Config.P_NODE_DEL_MUTATION) {
            Mutation.delNodeMutation(f);
        }

        if (Math.random() <= Config.P_NODE_BIAS_MUTATION) {
            Mutation.changeNodeBias(f);
        }

        if(Math.random() <= Config.P_CONNECTION_WEIGHT_MUTATION){
            Mutation.changeConnectionWeightMutation(f);
        }

    }

    private void evaluate(){

        for(Fellow f: getFellows()){
            f.setFitness(evaluator.eval(f));
        }

    }

    @SuppressWarnings("ConstantConditions")
    protected void crossover(){
        Fellow[] next_pop = new Fellow[POPSIZE] ;
        int k = 0 ;

        ArrayList<Fellow> old_pop = new ArrayList(getFellows());
        old_pop.sort((x,y) -> Double.compare(y.getFitness(), x.getFitness()));

        for (int i = 0; i < Config.ELITISM; i++) {
            next_pop[k] = old_pop.get(k).clone();
            k++ ;
        }

        // On retire les especes stagnantes
        Iterator<Species> it = species.iterator();
        while(it.hasNext()){
            Species s = it.next();
            if(s.last_improved >= Config.STAGNATION_AGE){
                it.remove();
            } else {
                s.last_fitness = s.averageFitness();
            }
        }


        Species[] species_t = new Species[species.size()];
        species_t = species.toArray(species_t);

        int[] breed = new int[species_t.length];

        //on calule le nombre d'enfant que peut avoir chaque espece
        double[] all_fitnesses = new double[species_t.length];
        double sum_all_fitnesses = 0;

        for(int i = 0; i < species_t.length ; i++) {
            Species s = species_t[i];
            for (Fellow f : s.fellows) {
                all_fitnesses[i] += f.getFitness() ;
            }
            all_fitnesses[i] /= s.fellows.size();

            if(species_t[i].age <= Config.MINORITY) all_fitnesses[i] *= Config.MINORITY_HELP_MULTIPLIER ;
            else if(s.last_improved >= Config.STAGNATION_AGE) { all_fitnesses[i] *= .5 ; }

            sum_all_fitnesses += all_fitnesses[i] ;
        }

        sum_all_fitnesses = Math.max(sum_all_fitnesses, 1);

        for (int i = 0; i < species_t.length; i++) {
            breed[i] = (int) (all_fitnesses[i] / sum_all_fitnesses * POPSIZE);
        }

        // On génère la descendance,
        // en sauvegardant au préalable les meilleurs elements de chaque espece

        for (int i = 0; i < species_t.length; i++) {

            Species s = species_t[i];
            s.sortFellows();
            for (int j = 0; j < Config.ELITISM && breed[i] > 0 && j < s.getFellows().size() && k < POPSIZE; j++) {
                next_pop[k] = s.getFellows().get(j).clone();
                breed[i]--;
                k++;
            }
        }

        for (int i = 0; i < species_t.length; i++) {
            Species s = species_t[i];

            while(breed[i] > 0 && k < POPSIZE){
                if(Math.random() <= Config.P_CROSSOVER){
                    next_pop[k] = ((Fellow) Random.random(s.getFellows())).clone();
                } else {
                    next_pop[k] = Crossover.crossover(
                            (Fellow) Random.random(s.getFellows()),
                            (Fellow) Random.random(s.getFellows())
                    );
                }

                mutate(next_pop[k]);

                k++ ;
                breed[i]--;
            }


            s.getFellows().clear();
        }

        System.out.println(Arrays.toString(next_pop));


        it = species.iterator() ;
        //for (int i = 0; i < species_t.length; i++) {
        while (it.hasNext()){
            //Species s = species_t[i];
            Species s = it.next() ;
            Fellow ambassador = s.getAmbassador();

            Fellow next_ambassador = null ;
            double ambassador_distance = Double.POSITIVE_INFINITY ;

            for (int j = 0; j < next_pop.length; j++) {
                if(next_pop[j] != null && s.closeTo(next_pop[j])){
                    s.addFellow(next_pop[j]);

                    double d = ambassador.distanceTo(next_pop[j]);
                    if(d < ambassador_distance){
                        ambassador_distance = d ;
                        next_ambassador = next_pop[j];
                    }

                    next_pop[j] = null ;
                }
            }

            s.setAmbassador(next_ambassador);
            if(next_ambassador == null){
                it.remove();
            }
        }

        for (int i = 0; i < next_pop.length; i++) {
            if(next_pop[i] != null) { //si un individu n'a pas été ajouté, il appartient à une nouvelle espèce
                addFellow(next_pop[i]);
            }
        }


        //On supprime les espèces vides et on fait veillir les autres
        it = species.iterator();
        while(it.hasNext()){
            Species s = it.next();
            if(s.fellows.isEmpty()){
                it.remove();
            } else {
                s.age++ ;

                if(s.averageFitness() <= s.last_fitness){
                    s.last_improved++ ;
                } else {
                    s.last_improved = 0 ;
                }
            }
        }

    }

    public Fellow getBest(){

        evaluate();

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

    private void addFellow(Fellow f){

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

    public int getGenerationNumber() {
        return generationNumber;
    }
}
