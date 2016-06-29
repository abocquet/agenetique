package eu.labrush.agenetic.traveller.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.traveller.operators.MutationOperator;

public class im implements MutationOperator {

    @Override
    public void mutate(AbstractFellow[] population, double PMUTATION) {

        int DNASIZE = population[0].getDNASIZE() ;

        for(AbstractFellow f: population){
            if(Math.random() <= PMUTATION ){
                int s1 = (int) (Math.random() * 10000) % DNASIZE ;
                int s2 = (int) (Math.random() * 10000) % DNASIZE ;

                if(s2 < s1){
                    int tmp = s2 ;
                    s2 = s1 ;
                    s1 = tmp ;
                }

                int[] dna = f.getDna() ;

                int m = (s1 + s2) / 2 ;
                for(int i = 0 ; i < m - s1 ; i++){
                    int tmp = f.getDna()[s1 + i] ;
                    dna[s1+i] = dna[s2 - i];
                    dna[s2-i] = tmp ;
                }
            }
        }

    }
}
