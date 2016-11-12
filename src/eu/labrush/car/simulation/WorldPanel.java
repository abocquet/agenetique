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

    public WorldPanel(World world) {

        this.world = world ;
        this.addKeyListener(this);

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.requestFocusInWindow();
    }

    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());

        g.setColor(Color.BLACK);

        for(Car c: world.getCars()) {
            AffineTransform transform = new AffineTransform();
            transform.translate(c.getX(), c.getY());
            transform.rotate(c.getAngle());

            double w = c.getWidth(), h = c.getHeight() ;
            Shape rotatedRect = transform.createTransformedShape(new Rectangle2D.Double(-w/2, -h/2, w, h));
            g.draw(rotatedRect);

            for(Detector d: c.getDetectors()){
                Shape rotatedLine = transform.createTransformedShape(new Line2D.Double(0, 0, d.getDistance() * Math.cos(d.getAngle()), d.getDistance() * Math.sin(d.getAngle())));
                g.draw(rotatedLine);
            }
        }

        for (Line2D l: world.boundaries){
            g.draw(l);
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
                world.userCar.increaseAngle(-Math.PI / 30);
                break;
            case VK_RIGHT:
                world.userCar.increaseAngle(Math.PI / 30);
                break;
            case VK_UP:
                world.userCar.increaseSpeed(10);
                break;
            case VK_DOWN:
                world.userCar.increaseSpeed(-10);
                break ;
        }
    }

    @Override
    public void keyReleased (KeyEvent e){
    }


}
