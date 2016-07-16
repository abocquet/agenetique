package eu.labrush.walker;

import eu.labrush.walker.dog.Walker;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Main {

    public static void main(String[] args) {

        Renderer2D window = new Renderer2D();
        window.setVisible(true);

        World world = new World() ;

        Rectangle f1 = new Rectangle(16, 1);
        /*Rectangle f2 = new Rectangle(1, 15);
        Rectangle f3 = new Rectangle(1, 15);

        f2.translate(8, 0);
        f3.translate(-8, 0);*/

        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -5.0);

        floor.addFixture(f1);
        //floor.addFixture(f2);
        //floor.addFixture(f3);

        world.addBody(floor);

        Walker walker = new Walker();
        walker.insertInWorld(world);

        /*Leg leg = new Leg();
        leg.translate(new Vector2(0, 4));
        leg.insertInWorld(world);*/

        Rectangle brect = new Rectangle(1.0, 1.0);
        BodyFixture bfix = new BodyFixture(brect);
        Renderer2D.GameObject bullet = new Renderer2D.GameObject();
        bullet.translate(-5, -2);
        bullet.addFixture(bfix);
        bullet.setLinearVelocity(new Vector2(30,0));
        bullet.setMass(new Mass(new Vector2(), 100, 10));
        world.addBody(bullet);

        window.setWorld(world);
        window.start();

    }
}
