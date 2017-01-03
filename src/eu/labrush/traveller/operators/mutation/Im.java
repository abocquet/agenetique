package eu.labrush.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

public class Im implements MutationInterface {

    @Override
    public void mutate(AbstractFellow[] population, double PMUTATION, AbstractFellowFactory factory) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= PMUTATION ){
                int s1 = (int) (Math.random() * 100000) % DNASIZE ;
                int s2 = (int) (Math.random() * 100000) % DNASIZE ;

                if(s2 < s1){
                    int tmp = s2 ;
                    s2 = s1 ;
                    s1 = tmp ;
                }

                int m = (s1 + s2) / 2 ;

                for(int i = 0 ; i < m - s1 ; i++){
                    int tmp = f.getDNA(s1 + i) ;
                    f.setDNA(s1+i, f.getDNA(s2 - i));
                    f.setDNA(s2-i, tmp) ;
                }
            }
        }
    }
}
