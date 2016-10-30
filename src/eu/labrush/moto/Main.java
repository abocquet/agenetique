package eu.labrush.moto;

import eu.labrush.moto.genetic.Moto;
import eu.labrush.moto.genetic.MotoFactory;
import eu.labrush.moto.genetic.Nature;
import org.dyn4j.dynamics.World;

public class Main {

    public static void main(String[] args) {

        /*Moto.setGroundDesigner(new GroundDesigner().setNbBlocks(5000).setDist(10_000));
        Moto.setPeakNumber(8);*/

        /*System.out.println("Terrain généré");

        Nature nature = new Nature(20, 0.5, 0.05, new MotoFactory());
        System.out.println(nature);

        for(int i = 0 ; i < 100 ; i++){
            System.out.print(".");

            if(i % 10 == 0) {
                System.out.print("\n");
                System.out.println(nature.getStats());
            }
            nature.evolve();
        }

        System.out.println("");
        System.out.println(nature);*/

        /*Renderer2D window = new Renderer2D();
        window.setVisible(true);

        Moto moto = new Moto();
        World world = moto.getSim() ;

        window.setWorld(world);
        window.focusOn(world.getBody(0));
        window.start();*/

        GraphicInterface fenetre = new GraphicInterface();

    }
}
