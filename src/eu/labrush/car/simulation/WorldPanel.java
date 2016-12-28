package eu.labrush.car.simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import static java.awt.event.KeyEvent.*;


public class WorldPanel extends JPanel implements KeyListener {

    private World world ;

    private String backGroundColor = "#26A65B" ;
    private String roadColor = "#87D37C";
    private String carColor = "#e67e22" ;

    public WorldPanel(World world) {

        this.world = world ;
        this.addKeyListener(this);

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();
    }

    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
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

        g.setColor(Color.BLACK);
        for(Car c: world.getCars()) {

            AffineTransform transform = new AffineTransform();
            transform.translate(c.getX(), c.getY());
            transform.rotate(c.getAngle());

            if(c != world.user && c.isRunning()) {
                g.setColor(Color.BLACK);
                for (Detector d : c.getDetectors()) {
                    Shape rotatedLine = transform.createTransformedShape(new Line2D.Double(0, 0, d.getDistance() * Math.cos(d.getAngle()), d.getDistance() * Math.sin(d.getAngle())));
                    g.draw(rotatedLine);
                }
            }

            if(c.isRunning()) {
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

    @Override
    public void keyTyped (KeyEvent e){
    }

    @Override

    public void keyPressed (KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_LEFT:
                world.user.increaseAngle(-Math.PI / 30);
                break;
            case VK_RIGHT:
                world.user.increaseAngle(Math.PI / 30);
                break;
            case VK_UP:
                world.user.increaseSpeed(10);
                break;
            case VK_DOWN:
                world.user.increaseSpeed(-10);
                break ;
        }
    }

    @Override
    public void keyReleased (KeyEvent e){
    }


}
