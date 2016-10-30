package eu.labrush.moto;

import eu.labrush.moto.genetic.Moto;
import org.dyn4j.dynamics.World;

public class Main {

    public static void main(String[] args) {

        Moto.setPeakNumber(4);
        Moto.setGroundDesigner(
                new GroundDesigner().setDist(1000).setNbBlocks(500)
        );

        int dna[] = {
                0,0,0,0,
                0,0,0,0,

                0,0,0,0,
                0,0,0,1,

                0,0,0,1,
                0,0,0,1,

                0,0,0,1,
                0,0,0,0,

                1,1,
                1,1,

                1,0,
                0,1,
                0,0,

                1,0,
                0,1,
                0,1, };

        Moto moto = null;
        try {
            moto = new Moto(dna);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Fitness: " + moto.getFitness());

        Renderer2D window = new Renderer2D();
        window.setVisible(true);

        World world = moto.getSim() ;

        window.setWorld(world);
        window.focusOn(world.getBody(0));
        window.start();

    }
}
