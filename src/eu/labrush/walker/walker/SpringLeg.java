package eu.labrush.walker.walker;

import eu.labrush.walker.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class SpringLeg {

    private Renderer2D.GameObject sup ;
    private Renderer2D.GameObject inf ;
    private Renderer2D.GameObject foot ;

    private RevoluteJoint hip_revolute_joint;
    private RevoluteJoint knee_revolute_joint;
    private RevoluteJoint foot_revolute_joint;

    private DistanceJoint hip_distance_joint;
    private DistanceJoint knee_distance_joint;
    private DistanceJoint foot_distance_joint;

    private Vector2 knee_distance_joint_anchor1 = new Vector2(-.3, -2);
    private Vector2 knee_distance_joint_anchor2 = new Vector2(-.3, -4);
    private Vector2 knee_revolute_joint_pos = new Vector2(0, -3);

    private Vector2 hip_distance_joint_anchor1 = new Vector2(-.4, -.5) ;
    private Vector2 hip_distance_joint_anchor2 = new Vector2(-.4, -1.5);
    private Vector2 hip_revolute_joint_pos = new Vector2(0, -1);

    double spring_frequency = 20 ;

    public SpringLeg() {
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

        knee_distance_joint = new DistanceJoint(sup, inf, knee_distance_joint_anchor1, knee_distance_joint_anchor2);
        foot_distance_joint = new DistanceJoint(inf, foot, new Vector2(0, -4), new Vector2(.5, -5));

        knee_distance_joint.setDampingRatio(1);
        foot_distance_joint.setDampingRatio(0);

        knee_distance_joint.setDistance(2);
        foot_distance_joint.setDistance(1.07);

        knee_distance_joint.setFrequency(spring_frequency);
        foot_distance_joint.setFrequency(spring_frequency);

        knee_revolute_joint = new RevoluteJoint(sup, inf, knee_revolute_joint_pos);
        knee_revolute_joint.setLimitEnabled(true);
        knee_revolute_joint.setLimits(0, 180);
        foot_revolute_joint = new RevoluteJoint(foot, inf, new Vector2(0, -5));
    }

    public void attachTo(Body body, Vector2 pos){
        translate(pos);

        hip_revolute_joint = new RevoluteJoint(sup, body, pos.copy().add(hip_revolute_joint_pos));
        hip_distance_joint = new DistanceJoint(sup, body, pos.copy().add(hip_distance_joint_anchor1), pos.copy().add(hip_distance_joint_anchor2));

        hip_distance_joint.setDampingRatio(1);
        hip_distance_joint.setDistance(hip_distance_joint_anchor1.distance(hip_distance_joint_anchor2));
        hip_distance_joint.setFrequency(spring_frequency);
    }

    public void translate(Vector2 pos){
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
        world.addJoint(knee_distance_joint);
        world.addJoint(hip_distance_joint);
        world.addJoint(foot_distance_joint);
    }

    public void setFilter(CategoryFilter filter) {
        inf.getFixtures().forEach(f -> f.setFilter(filter));
        sup.getFixtures().forEach(f -> f.setFilter(filter));
        foot.getFixtures().forEach(f -> f.setFilter(filter));
    }

    /**
     * @param angle in radians
     */
    public void setKneeAngle(double angle){
        knee_distance_joint.setDistance(calcSpringDist(angle, knee_distance_joint_anchor1, knee_distance_joint_anchor2, knee_revolute_joint_pos));
    }

    /**
     * @param angle in radians
     */
    public void setHipAngle(double angle){
        hip_distance_joint.setDistance(calcSpringDist(angle, hip_distance_joint_anchor1, hip_distance_joint_anchor2, hip_revolute_joint_pos));
    }

    private double calcSpringDist(double angle, Vector2 anchor1, Vector2 anchor2, Vector2 origin){
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //On place le centre au niveau de l'axe de révolution de l'articulation
        double x1 = anchor1.x - origin.x ;
        double y1 = anchor1.y - origin.y ;
        double x2 = anchor2.x - origin.x ;
        double y2 = anchor2.y - origin.y ;

        double dist = Math.pow((x2 * cos - (y2 * sin) - x1), 2) + Math.pow((x2 * sin + y2 * cos - y1), 2);

        return Math.sqrt(dist);
    }
}
