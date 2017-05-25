package eu.labrush.agenetic;


public interface FellowManagerInterface {
    FellowInterface[] getPopulation();
    FellowInterface getBest();
    int getGenerationNumber();
    void evolve();
}
