package eu.labrush.moto;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.*;

public class Main {

    public static void main(String[] args) {

        Renderer2D window = new Renderer2D();
        window.setVisible(true);

        World world = new World();

        window.setWorld(world);
        window.start();

        Renderer2D.GameObject body = new Renderer2D.GameObject() ;

        Vector2[] points = {
                Vector2.create(2, -Math.PI / 4),
                Vector2.create(3, Math.PI / 4),
                Vector2.create(5, Math.PI),
        };

        Vector2 center = new Vector2();

        for(int i = 0, c = points.length ; i < c ; i++){
            Vector2 p1 = points[i], p2 = points[(i+1) % c] ;


            if(p1.getAngleBetween(p2) > 0) {
                body.addFixture(new Triangle(center, p1, p2));
            } else {
                body.addFixture(new Triangle(center, p2, p1));
            }
        }

        body.setMass(new Mass(new Vector2(), 100, 10));
        world.addBody(body);

        Vector2[] wheels = {
                Vector2.create(1.5, -Math.PI / 4),
                Vector2.create(4, Math.PI),
        };

        for(int i = 0, c = wheels.length ; i < c  ; i++){
            Renderer2D.GameObject wheel = new Renderer2D.GameObject();
            wheel.addFixture(new Circle(1.5));
            wheel.translate(wheels[i]);

            RevoluteJoint joint = new RevoluteJoint(body, wheel, wheels[i]);
            joint.setMotorSpeed(Math.PI);
            joint.setMaximumMotorTorque(1000);
            joint.setMotorEnabled(true);

            wheel.setMass(new Mass(new Vector2(), 100, 10));

            world.addBody(wheel);
            world.addJoint(joint);
        }

        Rectangle frect = new Rectangle(10_000, 1);
        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -6.0);
        floor.addFixture(frect);

        world.addBody(floor);

        window.setWorld(world);
        window.start();

    }
}
