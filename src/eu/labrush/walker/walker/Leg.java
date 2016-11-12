package eu.labrush.walker.walker;

import org.dyn4j.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

class Leg {

    private Renderer2D.GameObject sup ;
    private Renderer2D.GameObject inf ;
    private Renderer2D.GameObject foot ;

    private RevoluteJoint hip_revolute_joint;
    private RevoluteJoint knee_revolute_joint;
    private RevoluteJoint foot_revolute_joint;

    private DistanceJoint foot_distance_joint;

    private Vector2 knee_revolute_joint_pos = new Vector2(0, -3);
    private Vector2 hip_revolute_joint_pos = new Vector2(0, -1);

    private double spring_frequency = 10 ;

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

        foot_distance_joint = new DistanceJoint(inf, foot, new Vector2(0, -4), new Vector2(.5, -5));
        foot_distance_joint.setDampingRatio(0);
        foot_distance_joint.setDistance(1.07);
        foot_distance_joint.setFrequency(spring_frequency * 4);

        knee_revolute_joint = new RevoluteJoint(sup, inf, knee_revolute_joint_pos);
        knee_revolute_joint.setLimitEnabled(true);
        knee_revolute_joint.setLimits(0, 180);
        foot_revolute_joint = new RevoluteJoint(foot, inf, new Vector2(0, -5));
    }

    public void attachTo(Body body, Vector2 pos){
        translate(pos);

        hip_revolute_joint = new RevoluteJoint(sup, body, pos.copy().add(hip_revolute_joint_pos));
    }

    private void translate(Vector2 pos){
        sup.translate(pos);
        inf.translate(pos);
        foot.translate(pos);
    }

    public void insertInWorld(World world) {
        //Les membres
        world.addBody(sup);
        world.addBody(inf);
        world.addBody(foot);

        //Les articulations
        world.addJoint(knee_revolute_joint);
        world.addJoint(foot_revolute_joint);
        world.addJoint(hip_revolute_joint);

        //Les tendons
        world.addJoint(foot_distance_joint);
    }

    public void setFilter(CategoryFilter filter) {
        inf.getFixtures().forEach(f -> f.setFilter(filter));
        sup.getFixtures().forEach(f -> f.setFilter(filter));
        foot.getFixtures().forEach(f -> f.setFilter(filter));
    }

    public RevoluteJoint getHipJoint() {
        return hip_revolute_joint;
    }

    public RevoluteJoint getKneeJoint() {
        return knee_revolute_joint;
    }
}
