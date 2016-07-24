package eu.labrush.walker;

import eu.labrush.walker.genetic.Nature;
import eu.labrush.walker.genetic.WalkerFactory;

public class Main {

    public static void main(String[] args) {

        /*Renderer2D window = new Renderer2D();
        window.setVisible(true);

        World world = new World() ;

        Rectangle f1 = new Rectangle(16, 1);

        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -5.0);
        floor.addFixture(f1);

        world.addBody(floor);

        BipedBody walker = new BipedBody();
        walker.insertInWorld(world);

        Rectangle brect = new Rectangle(1.0, 1.0);
        BodyFixture bfix = new BodyFixture(brect);
        Renderer2D.GameObject bullet = new Renderer2D.GameObject();
        bullet.translate(-5, -2);
        bullet.addFixture(bfix);
        bullet.setLinearVelocity(new Vector2(6,0));
        bullet.setMass(new Mass(new Vector2(), 100, 10));
        world.addBody(bullet);

        window.setWorld(world);*/

        Nature nature = new Nature(20, .5, .001, new WalkerFactory());

        /*System.out.println(nature);

        for(int i = 0 ; i < 1 ; i++){
            System.out.println(nature.getBest());
            nature.evolve();
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
