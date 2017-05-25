package eu.labrush.race_simulation;

import eu.labrush.agenetic.FellowManagerInterface;

public interface DriverManagerInterface extends FellowManagerInterface {

    DriverInterface newDriver();
    DriverInterface[] getDrivers();

}
