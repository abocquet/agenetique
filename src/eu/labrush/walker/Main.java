package eu.labrush.walker;

import eu.labrush.walker.genetic.Walker;
import eu.labrush.walker.genetic.WalkerFactory;
import org.dyn4j.Renderer2D;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Main {

    public static void main(String[] args) {

        Renderer2D window = new Renderer2D();
        window.setVisible(true);

        WalkerFactory wf = new WalkerFactory();
        Walker walker = (Walker) wf.newInstance(new int[]{
        //  freq   min  max    phase
            0,0,0, 0,0, 0,0,0, 0,0,0,0, // left hip
            0,0,0, 0,0, 0,0,0, 0,0,0,0, // left knee
            0,0,0, 0,0, 0,0,0, 0,0,0,0, // right hip
            0,0,0, 0,0, 0,0,0, 0,0,0,0  // right knee
        });
        World world = walker.newSimulation() ;

        Rectangle brect = new Rectangle(1.0, 1.0);
        BodyFixture bfix = new BodyFixture(brect);
        Renderer2D.GameObject bullet = new Renderer2D.GameObject();
        bullet.translate(-5, -2);
        bullet.addFixture(bfix);
        bullet.setLinearVelocity(new Vector2(6,0));
        bullet.setMass(new Mass(new Vector2(), 100, 10));
        //world.addBody(bullet);

        window.setWorld(world);
        window.start();

        /*Nature nature = new Nature(50, .5, .001, new WalkerFactory());

        nature.calc_pop_fitness();
        System.out.println(nature);

        for(int i = 0 ; i < 100 ; i++){
            nature.evolve();
            nature.calc_pop_fitness();
            System.out.println("\nGen " + i + " : " + nature.getBest());
        }
        System.out.println("");
        System.out.println(nature);

        System.out.print("Render done, display ? (press enter)");
        Scanner scan = new Scanner(System.in);
        scan.next();

        Renderer2D window = new Renderer2D();
        window.setVisible(true);
        window.setWorld(nature.getBest().newSimulation());
        window.start();*/

    }
}
