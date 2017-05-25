package eu.labrush.traveller;

import eu.labrush.agenetic.FellowInterface;
import eu.labrush.agenetic.FellowManagerInterface;

import java.math.BigDecimal;

public class Logger extends eu.labrush.agenetic.Logger {
    public Logger(String folder, String filename, FellowManagerInterface nature) {
        super(folder, filename, nature, "timestamp;generation;trajet le plus long;trajet le plus court;distance moyenne;");
    }

    @Override
    protected String getCSVStats(){
        FellowInterface[] population = manager.getPopulation();

        long min = ((Travel) population[0]).getDistance() ;
        long max ;

        BigDecimal sum = new BigDecimal(min) ;
        max = min ;

        for (FellowInterface aPopulation : population) {
            long d = ((Travel) aPopulation).getDistance();
            sum = sum.add(new BigDecimal(d));

            if (min > d) min = d;
            if (max < d) max = d;
        }

        return System.currentTimeMillis() + ";" + max + ";" + min + ";" + sum.divide(BigDecimal.valueOf(population.length), 5).toString() + ";"  ;
    }
}
