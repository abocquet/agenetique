package eu.labrush.g2048;

import eu.labrush.agenetic.operators.mutation.DefaultMutationOperator;
import eu.labrush.agenetic.operators.selection.ElitistSelector;
import eu.labrush.car.genetic.operators.DefaultReproductionOperator;
import eu.labrush.g2048.GameEngine.Game2048Panel;

public class Main {

    public static void main(String[] args) {

        Nature nature = new Nature(100, 3, .5 ,.1, .02, new PlayerFactory(), new DefaultReproductionOperator(), new DefaultMutationOperator(), new ElitistSelector());

        System.out.println("Let's play 2048 !");
        while(nature.getBest().getFitness() < 1000){
            nature.evolve();
            System.out.println(nature.getBest());
        }

        Game2048Panel game = new Game2048Panel();
        ((Player) nature.getBest()).playWith(game, true);

    }

}
