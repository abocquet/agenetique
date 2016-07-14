package eu.labrush.walker;

import eu.labrush.walker.dog.Dog;
import eu.labrush.walker.dog.Leg;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Main {

    public static void main(String[] args) {

        Renderer2D window = new Renderer2D();
        window.setVisible(true);

        World world = new World() ;

        Rectangle floorRect = new Rectangle(15.0, 1.0);
        BodyFixture ffix = new BodyFixture(floorRect);
        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.translate(0.0, -5.0);
        floor.addFixture(ffix);
        floor.setMass(MassType.INFINITE);
        world.addBody(floor);

        Dog dog = new Dog();
        dog.insertInWorld(world);

        //Leg leg = new Leg();
        //leg.translate(new Vector2(3, 0));
        //leg.attachTo(floor, new Vector2(0, 4));
        //leg.insertInWorld(world);

        Rectangle brect = new Rectangle(1.0, 1.0);
        BodyFixture bfix = new BodyFixture(brect);
        Renderer2D.GameObject bullet = new Renderer2D.GameObject();
        bullet.translate(-7.0, -.5);
        bullet.addFixture(bfix);
        bullet.setLinearVelocity(new Vector2(20,4));
        bullet.setMass(MassType.NORMAL);
        world.addBody(bullet);

        CategoryFilter f1 = new CategoryFilter(2,2);
        bfix.setFilter(f1);

        window.setWorld(world);
        window.start();

    }
}
