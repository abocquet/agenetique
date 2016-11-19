package eu.labrush.traveller;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.Tuple;
import eu.labrush.traveller.data.PointSet;
import eu.labrush.traveller.operators.MutationOperator;
import eu.labrush.traveller.operators.ReproductionOperator;

import java.util.Arrays;

public class Nature extends AbstractNature {

    private ReproductionOperator reproductionOperator ;
    private MutationOperator mutationOperator ;

    private PointSet problem ;

    public Nature(int POPSIZE, double PCROSSOVER, double PMUTATION, PointSet problem, ReproductionOperator ro, MutationOperator mo) {
        super(POPSIZE, PCROSSOVER, PMUTATION, new TravelFactory(problem.getPoints()));

        this.reproductionOperator = ro ;
        this.mutationOperator = mo ;

        this.problem = problem ;
    }

    @Override
    protected Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female) {
        return this.reproductionOperator.reproduce(male, female, (TravelFactory) this.factory);
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
        str += " and follows the path\n" + Arrays.toString(best.cloneDNA());

        return str ;
    }

    public int getShortest(){
        Travel best = (Travel) population[0];

        for(int i = 0, c = population.length ; i < c ; i++){
            Travel current = (Travel) population[i] ;
            if(best.getDistance() > current.getDistance()){
                best = current ;
            }
        }

        return best.getDistance() ;
    }
}