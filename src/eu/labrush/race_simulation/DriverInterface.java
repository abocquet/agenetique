package eu.labrush.race_simulation;

import eu.labrush.agenetic.FellowInterface;

public interface DriverInterface extends FellowInterface {

    void initBrain();
    double[] thinkAbout(double[] distances);
    int nbInputs();

    void setDistance(double distance);
}
