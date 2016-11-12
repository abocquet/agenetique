package eu.labrush.car.simulation;

import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

class Car {

    private Vector2 position = new Vector2(100, 300);
    private double speed = 0; // Linear speed

    private double angle = 0;
    private Dimension dimension = new Dimension(50, 30);

    public double getX() { return position.x; }
    public double getY() { return position.y; }
    public double getWidth()  { return dimension.width ; }
    public double getHeight() { return dimension.height ; }

    public Vector2 getPosition() { return position; }
    public double getSpeed() { return speed; }
    public double getAngle() { return angle; }

    private Detector[] detectors ;

    Car() {
        detectors = new Detector[15] ;
        for (int i = -7 ; i <= 7 ; i ++){
            detectors[i + 7] = new Detector(i * Math.PI / 12, 180);
        }
    }

    public void updateDetectors(ArrayList<Line2D> boundaries){

        for (int i = 0 ; i < detectors.length ; i++) {
            detectors[i].performDetection(boundaries, this.position, this.angle);
        }

    }

    /**
     * Utilise le réseau de neurones pour décider de tourner à gauche ou a droite selon la distance des détecteurs
     */
    public void drive() {
    }

    public Detector[] getDetectors() {
        return detectors;
    }
    public void increaseAngle(double v) {
        this.angle += v ;
    }
    public void increaseSpeed(double s) { this.speed += s ; }

}
