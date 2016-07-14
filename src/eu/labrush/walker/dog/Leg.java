package eu.labrush.walker.dog;

import eu.labrush.walker.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Leg {

    Renderer2D.GameObject sup ;
    Renderer2D.GameObject inf ;
    RevoluteJoint legJoint ;
    RevoluteJoint bodyJoint ;

    public Leg() {
        sup = new Renderer2D.GameObject() ;
        sup.addFixture(new Rectangle(1, 3));
        sup.setMass(MassType.NORMAL);
        sup.rotate(Math.PI / 2);

        inf = new Renderer2D.GameObject() ;
        inf.addFixture(new Rectangle(1, 3));
        inf.setMass(MassType.NORMAL);
        inf.translate(0, -2);
        sup.rotate(Math.PI / 2);

        legJoint = new RevoluteJoint(sup, inf, new Vector2(0, -1.25));

        //legJoint.setMotorEnabled(true);
        legJoint.setMotorSpeed(Math.PI / 2);
        legJoint.setMaximumMotorTorque(10000);
    }

    public void attachTo(Body body, Vector2 pos){
        translate(pos);
        bodyJoint = new RevoluteJoint(sup, body, pos.add(0, 1));
        //bodyJoint.setMotorEnabled(true);
        bodyJoint.setMotorSpeed(Math.PI / 2);
        bodyJoint.setMaximumMotorTorque(10000);
    }

    public void translate(Vector2 pos){
        sup.translate(pos);
        inf.translate(pos);
    }

    public void insertInWorld(World world) {
        world.addBody(sup);
        world.addBody(inf);

        world.addJoint(bodyJoint);
        world.addJoint(legJoint);
    }

    public void setFilter(CategoryFilter filter) {
        inf.getFixtures().forEach(f -> f.setFilter(filter));
        sup.getFixtures().forEach(f -> f.setFilter(filter));
    }
}
