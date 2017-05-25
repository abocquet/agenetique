package eu.labrush.NEAT.cars;

public class Main {

    public static void main(String[] args){
        World world = new World();

        Renderer renderer = new Renderer(world);
        renderer.start();
    }

}
