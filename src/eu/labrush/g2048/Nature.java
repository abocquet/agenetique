package eu.labrush.g2048;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.AbstractNature;
import eu.labrush.agenetic.Tuple;
import eu.labrush.agenetic.operators.CrossoverInterface;
import eu.labrush.agenetic.operators.MutationInterface;
import eu.labrush.agenetic.operators.SelectorInterface;
import eu.labrush.g2048.GameEngine.Game2048;

public class Nature extends AbstractNature {

    Tuple<Integer, Integer>[] tilesSequence = genSequence(10_000);

    public Nature(int POPSIZE, int ELITISM, double PCROSSOVER, double PMUTATION, double PINSERTION, AbstractFellowFactory factory, CrossoverInterface ro, MutationInterface mo, SelectorInterface so) {
        super(POPSIZE, ELITISM, PCROSSOVER, PMUTATION, PINSERTION, factory, ro, mo, so);
    }

    public void evolve(){ evolve(true); }

    protected void calc_pop_fitness() {

        /*int cores = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[cores];
        AbstractFellow[] pop = getPopulation() ;

        for(int i = 0 ; i < cores ; i++){
            int j = i;
            int c = getPOPSIZE();

            threads[i] = new Thread(() -> {
                for(int k = cores * j ; k < cores*(j+1) && k < c ; k++) {
                    Player p = (Player) pop[k];
                    Game2048 game = new Game2048(tilesSequence);
                    p.playWith(game);
                }
            });

            threads[i].start();
        }

        try {
            for(int i = 0 ; i < cores ; i++){ threads[i].join(); }
        } catch (Exception e){
            e.printStackTrace();
        }*/

        AbstractFellow[] pop = getPopulation() ;
        for(int k = 0 ; k < pop.length ; k++) {
            Player p = (Player) pop[k];
            Game2048 game = new Game2048(tilesSequence);
            p.playWith(game);
        }
    }

    private Tuple<Integer, Integer>[] genSequence(int n){

        @SuppressWarnings("unchecked")
        Tuple<Integer, Integer>[] seq = new Tuple[n] ;

        for (int i = 0; i < n; i++) {
            seq[i] = new Tuple<>(
                    (int) (Math.random() * 16), Math.random() < 0.9 ? 2 : 4
            );
        }

        return seq ;
    }

}
