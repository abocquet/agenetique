package eu.labrush;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class AbstractNature {

        protected AbstractFellow[] population ;

        private double PMUTATION  = 0.05 ;
        private double PCROSSOVER = 0.5 ;
        private int    POPSIZE    = 10 ;

        // the following props need to be adjusted for each problem

        private int DNACARD = 2 ;
        private int DNASIZE = 10 ;

        private Class fellowType = AbstractFellow.class ;

        public AbstractNature(int POPSIZE, double PCROSSOVER, double PMUTATION) {

            this.PMUTATION = PMUTATION ;
            this.POPSIZE = POPSIZE ;
            this.PCROSSOVER = PCROSSOVER ;

            this.population = new AbstractFellow[POPSIZE];

            try {
                Constructor ct = AbstractFellow.class.getConstructor(Integer.class, Integer.class);

                for(int i = 0 ; i < this.population.length ; i++) {
                    this.population[i] = (AbstractFellow) ct.newInstance(this.DNASIZE, this.DNACARD);
                }

            } catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

        }

        public void evolve(){
            crossover();
            mutate();
        }

        protected void crossover() {
            Arrays.sort(this.population, (a, b) -> b.getFitness() - a.getFitness());

            int totalFitness = 0 ;
            for(AbstractFellow f : this.population){
                totalFitness += f.getFitness() ;
            }

            AbstractFellow[] newPop = new AbstractFellow[this.POPSIZE];
            int i = 0 ;
            double cursor = 0 ;
            while(i < POPSIZE){

                if(Math.random() <= PCROSSOVER){
                    Tuple<AbstractFellow, AbstractFellow> children = this.reproduce(population[(int)cursor], population[(int)(Math.random() * 10000) % POPSIZE]);

                    if(i < POPSIZE) newPop[i] = children.fst ; i++ ;
                    if(i < POPSIZE) newPop[i] = children.snd ; i++ ;
                } else {
                    newPop[i] = this.population[(int)cursor] ;
                    i++ ;
                }

                cursor += population[(int) cursor].getFitness() / totalFitness ;
            }

            this.population = newPop ;
        }

        protected void mutate() {

            for(AbstractFellow f: population){
                for(int i = 0 ; i < f.getDNASIZE() ; i++){
                    /*
                    "flip": on chosit de muter
                    "mutation" on a une mutation
                    "indentique" le gene n'a pas changé après tirage

                    p(mutation) = p(flip inter non(identique))
                    or flip et indentique sont indépendants
                    d'ou p(mutation) = p(flip) * p(non(identique))
                    ie p(flip) = p(mutation) / p(non(non(identique)) = p(mutation) / (1 - 1 / DNACARD))

                    finalement on passe de l'autre coté pour éviter de manipuler des flottants)
                     */
                    if(Math.random() * (DNACARD - 1) <= PMUTATION * DNACARD) f.getDna()[i] = (byte) ((int) (Math.random() * 10000) % DNACARD);
                }
            }
        }

        protected Tuple<AbstractFellow, AbstractFellow> reproduce(AbstractFellow male, AbstractFellow female)  {

            AbstractFellow[] children = new AbstractFellow[2] ;

            for(int i = 0 ; i < 2 ; i++) {
                int splitPoint = (int) (Math.random() * 1000) % this.DNASIZE;
                byte[] dna = new byte[this.DNASIZE];

                for (int j = 0; j < splitPoint; j++) {
                    dna[j] = male.getDna()[j];
                }

                for (int j = splitPoint; j < this.DNASIZE; j++) {
                    dna[j] = female.getDna()[j];
                }


                try {
                    Constructor ct = AbstractFellow.class.getConstructor(byte[].class, int.class);
                    children[i] = (AbstractFellow) ct.newInstance(dna, this.DNACARD);

                } catch (SecurityException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            return new Tuple<>(children[0], children[1]);
            }

        @Override
        public String toString() {
            String str = "[" ;
            Arrays.sort(population);

            for(AbstractFellow f : population){
                str += f.getFitness() + " " ;
            }

            str += "]";

            return str ;
        }
}
