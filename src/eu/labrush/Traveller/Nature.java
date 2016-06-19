package eu.labrush.Traveller;

/**
 * Created by adrienbocquet on 24/04/2016.
 */

import eu.labrush.Tuple;

import java.util.Arrays;

public class Nature {

    protected Travel[] population ;

    @Override
    public String toString() {
        String str = "[" ;
        Arrays.sort(population);

        for(Travel f : population){
            str += f.getFitness() + " " ;
        }

        str += "]";

        return str ;
    }
}
