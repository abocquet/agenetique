package eu.labrush.moto;

import eu.labrush.moto.genetic.Moto;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

public class Main {

    public static void main(String[] args) {

        Moto.setPeakNumber(4);


        int dna[] = {
                0,0,0,0,
                0,0,0,0,

                0,0,0,0,
                0,0,0,1,

                0,0,0,1,
                0,0,0,1,

                0,0,0,1,
                0,0,1,0,

                0,0,
                0,0,

                1,0,
                1,1,
                0,0,

                1,0,
                1,1,
                0,0,
        };

        Moto moto = null;
        try {
            moto = new Moto(dna);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Nature nature = new Nature(20, 0.5, 0.05, new MotoFactory(), new GroundDesigner(200, 20));
        System.out.println(nature);

        /*for(int i = 0 ; i < 100 ; i++){
            System.out.print(".");

            if(i % 10 == 0) {
                System.out.print("\n");
                System.out.println(nature.getStats());
            }
            nature.evolve();
        }

        System.out.println("");
        System.out.println(nature);*/

       /* Renderer2D window = new Renderer2D();
        window.setVisible(true);

        //Moto moto = new Moto();

        GroundDesigner gd = new GroundDesigner(2000, 200);
        moto.setGroundDesigner(gd);
        gd.setOffset(new Vector2(0,-5));

        System.out.println(moto.getFitness());

        World world = moto.getSim() ;
        window.setWorld(world);
        window.focusOn(world.getBody(0));
        window.start();*/

        GraphicInterface fenetre = new GraphicInterface();

    }
}
