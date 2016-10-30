package eu.labrush.agenetic.numfun;

import eu.labrush.agenetic.AbstractNature;

import java.util.Arrays;

public class Nature extends AbstractNature {

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, NumFunction fun) {
        super();

        this.PMUTATION = PMUTATION;
        this.PCROSSOVER = PCROSSOVER;
        setPOPSIZE(POPSIZE);

        this.factory = new NumFunctionFactory(fun);

        initPopulation();
    }

    public String introduceYourself(){

        String str = "" ;
        str += "Hi ! I aim to find the maximum of a function... " ;
        str += "So far the maximum is \n" + getBest() ;
        str += "\nIsn't it great ?" ;

        return str ;
    }

    public String introduceBest() {
        Arrays.sort(this.population);
        Fellow best = (Fellow) this.population[this.population.length - 1] ;

        return "f(" + best.getX()  + ") = " + best.getFitness() ;
    }
}
