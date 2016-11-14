package eu.labrush.car;

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

        double[] res = network.compute(new double[]{1,2,3});
        System.out.println(Arrays.toString(res));*/

        World world = new World();
        Renderer renderer = new Renderer(world);
        renderer.start();

    }
}
