package eu.labrush.walker.dog;

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

public class Dog {

    private ArrayList<Leg> legs = new ArrayList<>();

    private HashMap<String, Body> parts = new HashMap<>();
    private HashMap<String, Joint> joints = new HashMap<>();

    public Dog() {

        double height = 3 ;

        Renderer2D.GameObject trunc = new Renderer2D.GameObject() ;
        BodyFixture tfix = new BodyFixture(new Rectangle(2, height));
        trunc.addFixture(tfix);
        trunc.setMass(MassType.NORMAL);
        trunc.translate(0, height / 2);

        tfix.setFilter(new CategoryFilter(1,1));

        this.parts.put("trunc", trunc);

        Vector2[] legPos = {
                new Vector2(-.5, 1),
                new Vector2(.5, 1)
        };

        for(int i = 0 ; i < legPos.length ; i++){
            Leg leg = new Leg();
            leg.attachTo(trunc, legPos[i]);
            leg.setFilter(new CategoryFilter(2,2));
            legs.add(leg);
        }
    }

    public void insertInWorld(World world){
        parts.forEach((s, body) -> world.addBody(body));
        legs.forEach(leg -> leg.insertInWorld(world));
        joints.forEach((s,joint) -> world.addJoint(joint));
    }

}
