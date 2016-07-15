package eu.labrush.walker.dog;

import eu.labrush.walker.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Leg {

    private Renderer2D.GameObject sup ;
    private Renderer2D.GameObject inf ;
    private Renderer2D.GameObject foot ;

    private DistanceJoint foot_distance_joint;
    private RevoluteJoint foot_revolute_joint;

    private RevoluteJoint knee_revolute_joint;
    private RevoluteJoint body_joint;

    private DistanceJoint knee_left_joint;
    private DistanceJoint knee_right_joint;

    private DistanceJoint body_left_joint;
    private DistanceJoint body_right_joint;

    public Leg() {
        sup = new Renderer2D.GameObject();
        sup.addFixture(new Rectangle(.4, 2));
        sup.setMass(MassType.NORMAL);
        sup.translate(0, -1);

        inf = new Renderer2D.GameObject();
        inf.addFixture(new Rectangle(.4, 2));
        inf.setMass(MassType.NORMAL);
        inf.translate(0, -3);

        foot = new Renderer2D.GameObject();
        foot.addFixture(new Rectangle(1, .4));
        foot.setMass(MassType.NORMAL);
        foot.translate(.2, -4);

        translate(new Vector2(0, -1));

        Vector2 foot1 = new Vector2(0, -4), foot2 = new Vector2(.5, -5) ;

        knee_left_joint = new DistanceJoint(sup, inf, new Vector2(-.3, -2), new Vector2(-.3, -4));
        knee_right_joint = new DistanceJoint(sup, inf, new Vector2(.3, -2), new Vector2(.3, -4));
        foot_distance_joint = new DistanceJoint(inf, foot, foot1, foot2);

        knee_left_joint.setDampingRatio(1);
        knee_right_joint.setDampingRatio(1);
        foot_distance_joint.setDampingRatio(0);

        knee_left_joint.setDistance(2);
        knee_right_joint.setDistance(2);
        foot_distance_joint.setDistance(1.07);

        double freq = 10 ;
        knee_left_joint.setFrequency(freq);
        knee_right_joint.setFrequency(freq);
        foot_distance_joint.setFrequency(freq * 4);

        knee_revolute_joint = new RevoluteJoint(sup, inf, new Vector2(0, -3));
        foot_revolute_joint = new RevoluteJoint(foot, inf, new Vector2(0, -5));
    }

    public void attachTo(Body body, Vector2 pos){
        translate(pos);
        body_joint = new RevoluteJoint(sup, body, pos.copy().add(0, -1));

        body_left_joint = new DistanceJoint(sup, body, pos.copy().add(.3, -.5), pos.copy().add(.3, -1.5));
        body_right_joint = new DistanceJoint(sup, body, pos.copy().add(-.3, -.5), pos.copy().add(-.3, -1.5));

        body_left_joint.setDampingRatio(1);
        body_right_joint.setDampingRatio(1);

        body_left_joint.setDistance(1);
        body_right_joint.setDistance(1);

        double freq = 10;
        body_left_joint.setFrequency(freq);
        body_right_joint.setFrequency(freq);
    }

    public void translate(Vector2 pos){
        sup.translate(pos);
        inf.translate(pos);
        foot.translate(pos);
    }

    public void insertInWorld(World world) {
        world.addBody(sup);
        world.addBody(inf);
        world.addBody(foot);

        //Les articulations
        world.addJoint(knee_revolute_joint);
        world.addJoint(foot_revolute_joint);
        world.addJoint(body_joint);

        //Les tendons
        world.addJoint(knee_left_joint);
        world.addJoint(knee_right_joint);

        world.addJoint(body_left_joint);
        world.addJoint(body_right_joint);

        world.addJoint(foot_distance_joint);
    }

    public void setFilter(CategoryFilter filter) {
        inf.getFixtures().forEach(f -> f.setFilter(filter));
        sup.getFixtures().forEach(f -> f.setFilter(filter));
        foot.getFixtures().forEach(f -> f.setFilter(filter));
    }
}
