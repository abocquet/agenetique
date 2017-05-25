package eu.labrush.NEAT;

import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.operators.Crossover;
import eu.labrush.NEAT.operators.FitnessEvaluator;
import eu.labrush.NEAT.operators.Mutation;

import java.util.*;
import static eu.labrush.NEAT.utils.Random.random;

public class Nature {

    private int POPSIZE = 20 ;

    private ArrayList<Species> species = new ArrayList<>(); // Representative, others
    private final FitnessEvaluator evaluator;

    public Nature(int POPSIZE, int sensors, int output, FitnessEvaluator evaluator) {

        this.POPSIZE = POPSIZE ;
        this.evaluator = evaluator ;

        Fellow original = new Fellow(sensors, output);

        for (int i = 0; i < POPSIZE; i++) {
            addFellow(original.clone());
        }

    }

    private void evaluate(){

        for(Fellow f: getFellows()){
            f.setFitness(evaluator.eval(f));
        }

    }

    public void evolve(){

        //Usual GAs stuff
        mutate();
        evaluate();
        crossover();

    }

    private void mutate(){

        for(Fellow f: getFellows()){
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
        }

    }

    @SuppressWarnings("ConstantConditions")
    private void crossover(){
        //Find minimum/maximum fitness across the entire population, for use in
        //species adjusted fitness computation.
        double[] all_fitnesses = new double[POPSIZE];
        int k = 0 ;
        for(Species s: species) {
            System.out.print(s.fellows.size() + " ");
            for (Fellow f : s.fellows) {
                all_fitnesses[k] = f.getFitness() ;
                k++ ;
            }
        }

        System.out.println("");

        double min_fitness = Double.POSITIVE_INFINITY ;
        double max_fitness = Double.NEGATIVE_INFINITY ;

        for (int i = 0; i < all_fitnesses.length; i++) {
            if(min_fitness > all_fitnesses[i]){
                min_fitness = all_fitnesses[i];
            }

            if(max_fitness < all_fitnesses[i]){
                max_fitness = all_fitnesses[i];
            }
        }

		//Do not allow the fitness range to be zero, as we divide by it below.
        double fitness_range = Math.max(1.0, max_fitness - min_fitness);

		//Filter out stagnated species, collect the set of non-stagnated
		//species members, and compute their average adjusted fitness.
		//The average adjusted fitness scheme (normalized to the interval
        // ([0, 1]) allows the use of negative fitness values without
		// interfering with the shared fitness scheme.
        Species[] species_t = new Species[species.size()]; //On travaille avec des tableaux statiques pour aller plus vite
        species_t = species.toArray(species_t);

        Species[] remaining_species = new Species[species_t.length];
        Double[] adjusted_fitness = new Double[species_t.length];

        Arrays.fill(adjusted_fitness, 0.0);

        for(int i = 0 ; i < species_t.length ; i++){
            Species s = species_t[i] ;
            if (!s.isStagnant()) {
                //Compute adjusted fitness.
                double msf = s.averageFitness();
                double af = (msf - min_fitness) / fitness_range;
                adjusted_fitness[i] = af;
                remaining_species[i] = s;
            }
        }

        if(remaining_species.length == 0) {
            try {
                throw new Exception("No species left");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		//Compute the number of new memebers for each species in the new generation.
        Integer[] previous_sizes = new Integer[species_t.length];
        for (int i = 0; i < species_t.length; i++) {
            previous_sizes[i] = species_t[i].fellows.size();
        }

        int[] spawn_amounts = new int[species_t.length];
        double af_sum = 0.0 ;
        for(int i  = 0 ; i < adjusted_fitness.length ; i++){
            af_sum += adjusted_fitness[i] ;
        }

        for(int i = 0 ; i < species_t.length ; i++){
            double size ;
            if(af_sum > 0){
                size = Math.max(Config.MIN_SPECIES_SIZE, adjusted_fitness[i] / af_sum * POPSIZE);
            } else {
                size = Config.MIN_SPECIES_SIZE ;
            }

            double d = (size - (double) previous_sizes[i]) * 0.5 ;
            int c = (int) Math.round(d);
            int spawn = previous_sizes[i];
            if(Math.abs(c) > 0) {
                spawn += c ;
            } else if (d > 0) {
                spawn += 1;
            } else if (d < 0) {
                spawn -= 1;
            }

            spawn_amounts[i] = spawn;
        }

        //On attribue à chaque espece une population pour le tour suivant
        double total_spawn = 0 ;
        for(int j = 0 ; j < spawn_amounts.length ; j++){
            double sp = spawn_amounts[j] ;
            total_spawn += sp ;
        }
        double norm = POPSIZE / total_spawn ;


        int[] tmp = new int[species_t.length] ;
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = Math.max(Config.MIN_SPECIES_SIZE, (int) Math.round(spawn_amounts[i] * norm));
        }

        spawn_amounts = tmp ;

        Fellow[] new_population = new Fellow[POPSIZE] ;
        for(int i = 0 ; i < remaining_species.length ; i++){
			//If elitism is enabled, each species always at least gets to retain its elites.
            double spawn = Math.max(spawn_amounts[i], Config.ELITISM);
            Species s = species_t[i];

            try {
                throw new Exception("Bad spawn value: " + spawn);
            } catch (Exception e) {
                if(spawn < 0){
                    e.printStackTrace();
                }
            }

			//The species has at least one member for the next generation, so retain it.
            ArrayList<Fellow> old_members = new ArrayList<>(s.getFellows());
            s.fellows.clear();

			//Sort members in order of descending fitness.
            old_members.sort(Comparator.comparingDouble(Fellow::getFitness).reversed());

            int j = 0 ;
			//Transfer elites to new generation.
            if(Config.ELITISM > 0) {
                for (; j < Config.ELITISM && j < old_members.size() ; j++) {
                    new_population[j] = old_members.get(j) ;
                    spawn -= 1;
                }
            }

            if(spawn <= 0)
                continue;

            int repro_cutoff = (int) Math.ceil(Config.SURVIVAL_SPECIES_THRESHOLD * old_members.size());

            //repro_cutoff = Math.max(repro_cutoff, 2);
            old_members = new ArrayList<>(old_members.subList(0, repro_cutoff));

			//Randomly choose parents and produce the number of offspring allotted to the species.
            while(spawn > 0) {
                spawn -= 1 ;

                Fellow parent1 = (Fellow) random(old_members);
                Fellow parent2 = (Fellow) random(old_members);

                Fellow child = Crossover.crossover(parent1, parent2);
                new_population[i] = child ;
                i++ ;
            }
        }

        species.clear();
        for (int i = 0; i < new_population.length; i++) {
            Fellow f  = new_population[i];
            if(f == null) {
                continue;
            }
            addFellow(f);
        }

        for (Species s: species){
            if(s.fellows.size() == 0){
                species.remove(s);
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

    private HashSet<Fellow> getFellows() {
        HashSet<Fellow> fellows = new HashSet<>();

        for(Species s: species) {
            fellows.addAll(s.getFellows());
        }

        return fellows ;
    }
}
