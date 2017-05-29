package eu.labrush.car;

import eu.labrush.agenetic.operators.crossover.AlternateCrossover;
import eu.labrush.agenetic.operators.mutation.DefaultMutationOperator;
import eu.labrush.agenetic.operators.selection.ElitistSelector;
import eu.labrush.car.genetic.DriverFactory;
import eu.labrush.car.genetic.Nature;
import eu.labrush.car.neural.BinaryWeightEncoder;
import eu.labrush.car.simulation.Renderer;
import eu.labrush.car.simulation.World;

public class Main {
    public static void main(String[] args) {


        /*double[][] w2 = new double[][]{{5.},{1.},{1.}};
        double[][] w1 = new double[][]{
                {1,1,1},
                {0,0,0},
                {0,0,0}
        };

        Network network = new Network(3);
        network.addLayer(w1);
        network.addLayer(w2);

        double[] res = network.compute(new double[]{1,2,3});Bis
        System.out.println(Arrays.toString(res));*/

        World world = new World();
        Nature nature;
        nature = new Nature(30, 3, 0.5, 0.1, 0.05,
                new DriverFactory(new BinaryWeightEncoder()), new AlternateCrossover(), new DefaultMutationOperator(), new ElitistSelector());
        world.setNature(nature);

        Renderer renderer = new Renderer(world);
        renderer.start();

        //MapLoader mp = new MapLoader(0, 0, 100, 100);
        //System.out.println(mp.loadMap("maps/bow.map"));
        //System.out.println(mp.loadMap("maps/heart.map"));
        //System.out.println(mp.loadMap("maps/sablier.map"));
        //System.out.println(mp.loadMap("maps/square.map"));
        //System.out.println(mp.loadMap("maps/k.map"));
        //System.out.println(mp.loadMap("maps/p.map"));
        //System.out.println(mp.loadMap("maps/hexagone.map"));
    }
}
