package eu.labrush.traveller;

import eu.labrush.AbstractFellow;
import eu.labrush.AbstractNature;
import eu.labrush.Tuple;
import eu.labrush.traveller.data.PointSet;

import eu.labrush.traveller.operators.* ;

import java.util.Arrays;

public class Nature extends AbstractNature {

    private ReproductionOperator reproductionOperator ;
    private MutationOperator mutationOperator ;

    private PointSet problem ;

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, PointSet problem, ReproductionOperator ro, MutationOperator mo) {
        super();

        this.reproductionOperator = ro ;
        this.mutationOperator = mo ;

        this.PMUTATION = PMUTATION;
        this.PCROSSOVER = PCROSSOVER;
        setPOPSIZE(POPSIZE);

        this.problem = problem ;

        try {
            // Sets the DNACARD and DANSIZE at the fly
            Travel.setPlaces(problem.getPoints());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFellowType(Travel.class);
        initPopulation();
    }

    @Override
    protected Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female) {
        return this.reproductionOperator.reproduce(male, female);
    }

    @Override
    protected void mutate() {
        mutationOperator.mutate(population, PMUTATION);
    }

    public String introduceYourself() {
        Arrays.sort(population);
        Travel best = (Travel) population[population.length - 1] ;

        String str = "\n" ;

        str += "We try to solve the TSP problem, he says : \"";
        str += this.problem.introduceYourself() + "\"\n" ;

        str += "\nThe best travel found has a distance of " + best.getDistance() ;
        str += " and follows the path\n" + Arrays.toString(best.getDna());

        return str ;
    }

    public int getShortest(){
        Arrays.sort(population);
        Travel best = (Travel) population[population.length - 1] ;
        return best.getDistance() ;
    }
}