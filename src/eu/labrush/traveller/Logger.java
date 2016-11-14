package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractNature;

import java.math.BigDecimal;

public class Logger extends eu.labrush.agenetic.Logger {
    public Logger(String filename, AbstractNature nature) {
        super(filename, nature, "generation;trajet le plus long;trajet le plus court;distance moyenne;");
    }

    @Override
    protected String getCSVStats(){
        AbstractFellow[] population = nature.getPopulation();

        int min = ((Travel) population[0]).getDistance() ;
        int max ;

        BigDecimal sum = BigDecimal.valueOf(min) ;
        max = min ;

        for(int i = 0, c = population.length ; i < c ; i++){
            int d = ((Travel) population[i]).getDistance();
            sum = sum.add(BigDecimal.valueOf(d)) ;

            if(min > d) min = d ;
            if(max < d) max = d ;
        }

        return max + ";" + min + ";" + sum.divide(BigDecimal.valueOf(population.length), 5).toString() + ";"  ;
    }
}
