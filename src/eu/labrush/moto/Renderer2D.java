package eu.labrush.moto;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.awt.*;

public class Renderer2D extends org.dyn4j.Renderer2D {

    Body bodyFocus = null ;

    public void focusOn(Body body){
        bodyFocus = body ;
    }

    protected void render(Graphics2D g) {

        if(bodyFocus != null) {
            // lets draw over everything with a white background
            g.setColor(Color.WHITE);
            g.fillRect(-400, -300, 800, 600);

            Vector2 offset = bodyFocus.getWorldCenter() ;

            //BUG: le texte est écrit à l'envers sans les deux transformations successives...
            g.scale(-1 , 1);
            g.setFont(new Font("Arial", 0, -20));
            g.setColor(Color.BLACK);
            g.drawString("Position: " +  (Math.round(offset.x * 10) / 10.0), 390, 280);
            g.scale(-1 , 1);

            g.translate(0.0, -1.0 * SCALE);

            // draw all the objects in the world
            for (int i = 0; i < this.world.getBodyCount(); i++) {

                GameObject go = (GameObject) this.world.getBody(i);
                go.translate(offset.multiply(-1));
                go.render(g);
                go.translate(offset.multiply(-1));

            }

        } else super.render(g);
    }

}
