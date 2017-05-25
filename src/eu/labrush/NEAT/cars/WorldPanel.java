package eu.labrush.NEAT.cars;

import eu.labrush.car.simulation.Detector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class WorldPanel extends JPanel {

    private World world ;

    public WorldPanel(World world) {

        this.world = world ;

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();
    }

    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        String backGroundColor = "#26A65B";
        String roadColor = "#87D37C";
        String carColor = "#e67e22";

        g.setColor(Color.decode(backGroundColor));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.decode(roadColor));
        for(Rectangle2D r: world.getMap().getRoad()){
            g.fill(r);
        }

        g.setColor(Color.BLACK);
        for (Line2D l: world.boundaries){
            g.draw(l);
        }

        g.setColor(Color.RED);
        g.draw(world.getMap().getFinishLine());

        g.setColor(Color.BLACK);
        for(Car c: world.getCars()) {

            AffineTransform transform = new AffineTransform();
            transform.translate(c.getX(), c.getY());
            transform.rotate(c.getAngle());

            if(c.isRunning()) {
                g.setColor(Color.BLACK);
                for (Detector d : c.getDetectors()) {
                    Shape rotatedLine = transform.createTransformedShape(new Line2D.Double(0, 0, d.getDistance() * Math.cos(d.getAngle()), d.getDistance() * Math.sin(d.getAngle())));
                    g.draw(rotatedLine);
                }
            }

            if(c.isFinished()){
                g.setColor(Color.GREEN);
            } else if(c.isRunning()) {
                g.setColor(Color.decode(carColor));
            } else {
                g.setColor(Color.gray);
            }

            double w = c.getWidth(), h = c.getHeight() ;
            Shape rotatedRect = transform.createTransformedShape(new Rectangle2D.Double(-w/2, -h/2, w, h));
            g.draw(rotatedRect);
            g.fill(rotatedRect);

        }

        g.dispose();
    }
}
