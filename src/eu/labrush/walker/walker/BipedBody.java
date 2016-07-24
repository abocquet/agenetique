package eu.labrush.walker.walker;

import eu.labrush.walker.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class BipedBody {

    private ArrayList<SpringLeg> legs = new ArrayList<>();

    private HashMap<String, Body> parts = new HashMap<>();
    private HashMap<String, Joint> joints = new HashMap<>();

    public BipedBody() {

        double height = 3 ;

        Renderer2D.GameObject trunc = new Renderer2D.GameObject() ;
        BodyFixture tfix = new BodyFixture(new Rectangle(2, height));
        trunc.addFixture(tfix);
        trunc.setMass(MassType.NORMAL);
        trunc.translate(0, height / 2);

        tfix.setFilter(new CategoryFilter(1,1));

        this.parts.put("trunc", trunc);

        Vector2[] legPos = {
                new Vector2(-.7, 1),
                new Vector2(.7, 1)
        };

        for(int i = 0 ; i < legPos.length ; i++){
            SpringLeg leg = new SpringLeg();
            leg.attachTo(trunc, legPos[i]);

            int mask = 2^(i+1);
            leg.setFilter(new CategoryFilter(mask,mask));
            legs.add(leg);
        }
    }

    public void insertInWorld(World world){
        parts.forEach((s, body) -> world.addBody(body));
        legs.forEach(leg -> leg.insertInWorld(world));
        joints.forEach((s,joint) -> world.addJoint(joint));
    }

    public ArrayList<SpringLeg> getLegs() {
        return legs;
    }

    public double getDistance() {
        return this.parts.get("trunc").getWorldCenter().x;
    }
}
