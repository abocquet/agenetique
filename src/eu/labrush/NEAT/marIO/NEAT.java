package eu.labrush.NEAT.marIO;

import eu.labrush.NEAT.utils.Random;

import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("WeakerAccess")
public class NEAT {

    int Inputs = 2;
    int Outputs = 1;

    int Population = 300;
    double DeltaDisjoint = 2.0;
    double DeltaWeights = 0.4;
    double DeltaThreshold = 1.0;

    int StaleSpecies = 15;

    double MutateConnectionsChance = 0.25;
    double PerturbChance = 0.90;
    double CrossoverChance = 0.75;
    double LinkMutationChance = 2.0;
    double NodeMutationChance = 0.50;
    double BiasMutationChance = 0.40;
    double DisableMutationChance = 0.4;
    double EnableMutationChance = 0.2;
    double StepSize = 0.1;

    int MaxNodes = 1000000;

    int innovation = 0;

    Pool pool;

    public NEAT() {
        initializePool();
    }

    double sigmoid(double x) {
        return 2 / (1 + Math.exp(-4.9 * x)) - 1;
    }

    int newInnovation() {
        this.innovation++;
        return this.innovation;
    }

    Genome newGenome() {
        Genome genome = new Genome();
        genome.genes = new ArrayList();
        genome.fitness = 0;
        genome.adjustedFitness = 0;
        genome.network = new NN();
        genome.maxneuron = 0;
        genome.globalRank = 0;
        genome.mutationRates = new MutationRates();
        genome.mutationRates.connections = MutateConnectionsChance;
        genome.mutationRates.link = LinkMutationChance;
        genome.mutationRates.bias = BiasMutationChance;
        genome.mutationRates.node = NodeMutationChance;
        genome.mutationRates.enable = EnableMutationChance;
        genome.mutationRates.disable = DisableMutationChance;
        genome.mutationRates.step = StepSize;

        return genome;
    }

    Genome basicGenome() {
        Genome genome = newGenome();
        genome.maxneuron = Inputs;
        mutate(genome);

        return genome;
    }

    void generateNetwork(Genome genome) {
        NN network = new NN();
        network.neurons = new ArrayList<>();

        for (int i = 0; i < Inputs; i++) {
            network.neurons.set(i, new Neuron());
        }

        for (int i = 0; i < Outputs; i++) {
            network.neurons.set(MaxNodes + i, new Neuron());
        }

        genome.genes.sort((x, y) -> y.out - x.out);

        for (int i = 0; i < genome.genes.size(); i++) { //TODO: replace by foreach loop
            Gene gene = genome.genes.get(i);
            if (gene.enabled) {
                if (network.neurons.get(gene.out) == null) {
                    network.neurons.set(gene.out, new Neuron());
                }
                Neuron neuron = network.neurons.get(gene.out);
                neuron.incoming.add(gene);
                if (network.neurons.get(gene.into) == null) {
                    network.neurons.set(gene.into, new Neuron());
                }
            }
        }

        genome.network = network;
    }

    ArrayList evaluateNetwork(NN network, ArrayList<Double> inputs) {
        inputs.add(1.0);
        if (inputs.size() != Inputs) {
            System.err.println("Incorrect number of neural network inputs.");
            System.exit(-1);
        }


        for (int i = 0; i < Inputs; i++) {
            network.neurons.get(i).value = inputs.get(i);
        }

        for (Neuron neuron : network.neurons) {
            double sum = 0;
            for (int j = 0; j < neuron.incoming.size(); j++) {
                Gene incoming = neuron.incoming.get(j);
                Neuron other = network.neurons.get(incoming.into);
                sum = sum + incoming.weight * other.value;
            }

            if (neuron.incoming.size() > 0) {
                neuron.value = sigmoid(sum);
            }
        }

        ArrayList<Double> outputs = new ArrayList<>();
        for (int i = 0; i < Outputs; i++) { //TODO: modified, to check
            outputs.set(i, network.neurons.get(MaxNodes + i).value);
        }

        return outputs;
    }


    Genome crossover(Genome g1, Genome g2) {
        //Make sure g1 is the higher fitness genome
        if (g2.fitness > g1.fitness) {
            Genome tempg = g1;
            g1 = g2;
            g2 = tempg;
        }

        Genome child = newGenome();

        ArrayList<Gene> innovations2 = new ArrayList<>();
        for (int i = 0; i < g2.genes.size(); i++) {
            Gene gene = g2.genes.get(i);
            innovations2.set(gene.innovation, gene);
        }

        for (int i = 1; i < g1.genes.size(); i++) {
            Gene gene1 = g1.genes.get(i);
            Gene gene2 = innovations2.get(gene1.innovation);
            if (gene2 != null && eu.labrush.NEAT.utils.Random.randInt(2) == 1 && gene2.enabled) {
                child.genes.add(gene2.clone());
            } else {
                child.genes.add(gene1.clone());
            }
        }

        child.maxneuron = Math.max(g1.maxneuron, g2.maxneuron);
        child.mutationRates = g1.mutationRates.clone();

        return child;
    }

    int randomNeuron(ArrayList<Gene> genes, boolean nonInput) {
        ArrayList<Boolean> neurons = new ArrayList<>();
        if (nonInput) {
            for (int i = 0; i < Inputs; i++) {
                neurons.set(i, true);
            }
        }

        for (int o = 1; o < Outputs; o++) {
            neurons.set(MaxNodes + o, true);
        }

        for (int i = 0; i < genes.size(); i++) {
            if (!nonInput || genes.get(i).into > Inputs) {
                neurons.set(genes.get(i).into, true);
            }

            if (!nonInput || genes.get(i).out > Inputs) {
                neurons.set(genes.get(i).out, true);
            }
        }

        int count = neurons.size();
        int n = Random.randInt(count + 1) + 1;

        for (int k = 0; k < neurons.size(); k++) {
            n = n - 1;
            if (n == 0) {
                return k;
            }
        }

        return 0;
    }

    boolean containsLink(ArrayList<Gene> genes, Gene link) {
        for (int i = 0; i < genes.size(); i++) {
            Gene gene = genes.get(i);

            if (gene.into == link.into && gene.out == link.out) {
                return true;
            }
        }

        return false;
    }

    void pointMutate(Genome genome) {
        double step = genome.mutationRates.step;

        for (int i = 0; i < genome.genes.size(); i++) {
            Gene gene = genome.genes.get(i);
            if (Math.random() < PerturbChance) {
                gene.weight = gene.weight + Math.random() * step * 2 - step;
            } else {
                gene.weight = Math.random() * 4 - 2;
            }
        }
    }

    void linkMutate(Genome genome, boolean forceBias) {
        int neuron1 = randomNeuron(genome.genes, false);
        int neuron2 = randomNeuron(genome.genes, true);

        Gene newLink = new Gene();
        if (neuron1 <= Inputs && neuron2 <= Inputs) {
            return;
        }

        if (neuron2 <= Inputs) {
            int temp = neuron1;
            neuron1 = neuron2;
            neuron2 = temp;
        }

        newLink.into = neuron1;
        newLink.out = neuron2;
        if (forceBias) {
            newLink.into = Inputs;
        }

        if (containsLink(genome.genes, newLink)) {
            return;
        }

        newLink.innovation = newInnovation();
        newLink.weight = Math.random() * 4 - 2;

        genome.genes.add(newLink);
    }

    void nodeMutate(Genome genome) {
        if (genome.genes.size() == 0) {
            return;
        }

        genome.maxneuron = genome.maxneuron + 1;

        Gene gene = genome.genes.get(Random.randInt(genome.genes.size()));
        if (gene.enabled) {
            return;
        }

        gene.enabled = false;

        Gene gene1 = gene.clone();
        gene1.out = genome.maxneuron;
        gene1.weight = 1.0;
        gene1.innovation = newInnovation();
        gene1.enabled = true;
        genome.genes.add(gene1);

        Gene gene2 = gene.clone();
        gene2.into = genome.maxneuron;
        gene2.innovation = newInnovation();
        gene2.enabled = true;
        genome.genes.add(gene2);
    }

    void enableDisableMutate(Genome genome, boolean enable) {
        ArrayList<Gene> candidates = new ArrayList<>();
        for (Gene gene : genome.genes) {
            if (gene.enabled != enable) {
                candidates.add(gene);
            }
        }

        if (candidates.size() == 0) {
            return;
        }


        Gene gene = candidates.get(Random.randInt(candidates.size()));
        gene.enabled = !gene.enabled;
    }

    void mutate(Genome genome) {
        genome.mutationRates.connections = Math.random() <= .5 ? 0.95 * genome.mutationRates.connections : 1.05263 * genome.mutationRates.connections;
        genome.mutationRates.link = Math.random() <= .5 ? 0.95 * genome.mutationRates.link : 1.05263 * genome.mutationRates.link;
        genome.mutationRates.bias = Math.random() <= .5 ? 0.95 * genome.mutationRates.bias : 1.05263 * genome.mutationRates.bias;
        genome.mutationRates.node = Math.random() <= .5 ? 0.95 * genome.mutationRates.node : 1.05263 * genome.mutationRates.node;
        genome.mutationRates.enable = Math.random() <= .5 ? 0.95 * genome.mutationRates.enable : 1.05263 * genome.mutationRates.enable;
        genome.mutationRates.disable = Math.random() <= .5 ? 0.95 * genome.mutationRates.disable : 1.05263 * genome.mutationRates.disable;
        genome.mutationRates.step = Math.random() <= .5 ? 0.95 * genome.mutationRates.step : 1.05263 * genome.mutationRates.step;

        if (Math.random() < genome.mutationRates.connections) {
            pointMutate(genome);
        }

        double p = genome.mutationRates.link;
        while (p > 0) {
            if (Math.random() < p) {
                linkMutate(genome, false);
            }
            p = p - 1;
        }

        p = genome.mutationRates.bias;
        while (p > 0) {
            if (Math.random() < p) {
                linkMutate(genome, true);
            }
            p = p - 1;
        }

        p = genome.mutationRates.node;
        while (p > 0) {
            if (Math.random() < p) {
                nodeMutate(genome);
            }
            p = p - 1;
        }

        p = genome.mutationRates.enable;
        while (p > 0) {
            if (Math.random() < p) {
                enableDisableMutate(genome, true);
            }
            p = p - 1;
        }

        p = genome.mutationRates.disable;
        while (p > 0) {
            if (Math.random() < p) {
                enableDisableMutate(genome, false);
            }
            p = p - 1;
        }
    }

    double disjoint(ArrayList<Gene> genes1, ArrayList<Gene> genes2) {
        ArrayList<Boolean> i1 = new ArrayList<>();
        for (int i = 0; i < genes1.size(); i++) {
            Gene gene = genes1.get(i);
            i1.set(gene.innovation, true);
        }

        ArrayList<Boolean> i2 = new ArrayList<>();
        for (int i = 0; i < genes2.size() ; i++) {
            Gene gene = genes2.get(i);
            i2.set(gene.innovation, true);
        }

        int disjointGenes = 0;
        for (int i = 0; i < genes1.size(); i++) {
            Gene gene = genes1.get(i);
            if (!i2.get(gene.innovation)) {
                disjointGenes = disjointGenes + 1;
            }
        }

        for (int i = 0; i < genes2.size(); i++) {
            Gene gene = genes2.get(i);
            if (!i1.get(gene.innovation)) {
                disjointGenes = disjointGenes + 1;
            }
        }

        double n = Math.max(genes1.size(), genes2.size());

        return disjointGenes / n;
    }


    double weights(ArrayList<Gene> genes1, ArrayList<Gene> genes2) {
        ArrayList<Gene> i2 = new ArrayList<>();
        for (int i = 0; i < genes2.size(); i++) {
            Gene gene = genes2.get(i);
            i2.set(gene.innovation, gene);
        }

        double sum = 0;
        int coincident = 0;
        for (int i = 0; i < genes1.size(); i++) {
            Gene gene = genes1.get(i);
            if (i2.get(gene.innovation) != null) {
                Gene gene2 = i2.get(gene.innovation);
                sum = sum + Math.abs(gene.weight - gene2.weight);
                coincident = coincident + 1;
            }
        }

        return sum / coincident;
    }

    boolean sameSpecies(Genome genome1, Genome genome2) {
        double dd = DeltaDisjoint * disjoint(genome1.genes, genome2.genes);
        double dw = DeltaWeights * weights(genome1.genes, genome2.genes);
        return dd + dw < DeltaThreshold;
    }

    void rankGlobally() {
        ArrayList<Genome> global = new ArrayList<>();
        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            for (int g = 0; g < species.genomes.size(); g++) {
                global.add(species.genomes.get(g));
            }
        }

        global.sort(Comparator.comparingDouble(x -> x.fitness));

        for (int g = 0; g < global.size(); g++) {
            global.get(g).globalRank = g;
        }
    }

    void calculateAverageFitness(Species species) {
        double total = 0;

        for (int g = 0; g < species.genomes.size(); g++) {
            Genome genome = species.genomes.get(g);
            total = total + genome.globalRank;
        }

        species.averageFitness = total / species.genomes.size();
    }

    double totalAverageFitness() {
        double total = 0;
        for (int s = 0; s < pool.species.size(); s++) {
            total = total + pool.species.get(s).averageFitness;
        }

        return total;
    }

    void cullSpecies(boolean cutToOne) {
        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);

            species.genomes.sort((a, b) -> Double.compare(b.fitness, a.fitness));

            int remaining = (int) Math.ceil(species.genomes.size() / 2);
            if (cutToOne) {
                remaining = 1;
            }

            while (species.genomes.size() > remaining) {
                species.genomes.remove(species.genomes);
            }
        }
    }

    Genome breedChild(Species species) {
        Genome child;
        if (Math.random() < CrossoverChance) {
            Genome g1 = species.genomes.get(Random.randInt(species.genomes.size()));
            Genome g2 = species.genomes.get(Random.randInt(species.genomes.size()));
            child = crossover(g1, g2);
        } else {
            Genome g = species.genomes.get(Random.randInt(species.genomes.size()));
            child = g.clone();
        }

        mutate(child);

        return child;
    }

    void removeStaleSpecies() {
        ArrayList<Species> survived = new ArrayList<>();

        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            species.genomes.sort((a, b) -> Double.compare(b.fitness, a.fitness));

            if (species.genomes.get(1).fitness > species.topFitness) {
                species.topFitness = species.genomes.get(1).fitness;
                species.staleness = 0;
            } else {
                species.staleness = species.staleness + 1;
            }

            if (species.staleness < StaleSpecies || species.topFitness >= pool.maxFitness) {
                survived.add(species);
            }
        }

        pool.species = survived;
    }

    void removeWeakSpecies() {
        ArrayList<Species> survived = new ArrayList<>();

        Double sum = totalAverageFitness();
        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            double breed = Math.floor(species.averageFitness / sum * Population);
            if (breed >= 1) {
                survived.add(species);
            }
        }

        pool.species = survived;
    }

    void addToSpecies(Genome child) {
        boolean foundSpecies = false;
        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            if (!foundSpecies && sameSpecies(child, species.genomes.get(1))) {
                species.genomes.add(child);
                foundSpecies = true;
            }
        }

        if (!foundSpecies) {
            Species childSpecies = new Species();
            childSpecies.genomes.add(child);
            pool.species.add(childSpecies);
        }
    }

    void newGeneration() {
        cullSpecies(false); // Cull the bottom half of each species
        rankGlobally();
        removeStaleSpecies();
        rankGlobally();
        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            calculateAverageFitness(species);
        }

        removeWeakSpecies();
        double sum = totalAverageFitness();
        ArrayList<Genome> children = new ArrayList<>();

        for (int s = 0; s < pool.species.size(); s++) {
            Species species = pool.species.get(s);
            double breed = Math.floor(species.averageFitness / sum * Population) - 1;
            for (int i = 0; i < breed; i++) {
                children.add(breedChild(species));
            }
        }

        cullSpecies(true); //-- Cull all but the top member of each species
        while (children.size() + pool.species.size() < Population) {
            Species species = pool.species.get(Random.randInt(pool.species.size()));
            children.add(breedChild(species));
        }

        for (int c = 0; c < children.size(); c++) {
            addToSpecies(children.get(c));
        }

        pool.generation = pool.generation + 1;
    }

    void initializePool() {
        pool = new Pool(Outputs);

        for (int i = 0; i < Population; i++) {
            Genome basic = basicGenome();
            addToSpecies(basic);
        }
    }


    void evaluateCurrent(ArrayList<Double> inputs) {
        Species species = pool.species.get(pool.currentSpecies);
        Genome genome = species.genomes.get(pool.currentGenome);
        generateNetwork(genome);

        ArrayList<Double> controller = evaluateNetwork(genome.network, inputs);

        //TODO: erased, to replace

    }


    void nextGenome() {
        pool.currentGenome = pool.currentGenome + 1;
        if(pool.currentGenome > pool.species.get(pool.currentSpecies).genomes.size()) {
            pool.currentGenome = 1 ;
            pool.currentSpecies = pool.currentSpecies + 1 ;
            if(pool.currentSpecies > pool.species.size()) {
                newGeneration();
                pool.currentSpecies = 1;
            }
        }
    }

    boolean fitnessAlreadyMeasured() {
        Species species = pool.species.get(pool.currentSpecies);
        Genome genome = species.genomes.get(pool.currentGenome);

        return genome.fitness != 0;
    }
}
