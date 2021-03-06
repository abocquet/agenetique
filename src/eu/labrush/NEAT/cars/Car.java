package eu.labrush.NEAT.cars;

import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.car.simulation.Detector;
import eu.labrush.neural.NeuralNetwork;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Car {

    private Vector2 position = new Vector2(120, 70);
    private double speed = 50; // Linear speed
    private double minSpeed = 50 ;

    private double angle = 0;
    private Dimension dimension = new Dimension(10, 10);

    public double getX() { return position.x; }
    public double getY() { return position.y; }
    public double getWidth()  { return dimension.width ; }
    public double getHeight() { return dimension.height ; }

    public Vector2 getPosition() { return position; }
    public double getSpeed() { return speed; }
    public double getAngle() { return angle; }

    private Detector[] detectors ;
    private NeuralNetwork brain ;

    private boolean running = true ;
    private boolean finished = false ; // achieved a turn
    private Fellow driver ;

    private double distance = .0;

    Car(Fellow driver){
        this.driver = driver ;
        double maxAngle = Math.PI / 3 ;

        /*-------------------------------
          On initialise les detecteurs
        --------------------------------*/
        int nbDetectors = driver.getInputsNumber() ;
        double angle = maxAngle / (nbDetectors - 1) ;

        detectors = new Detector[nbDetectors] ;
        for (int i = 0 ; i < nbDetectors ; i ++){
            detectors[i] = new Detector(angle * 2 * (double)i - maxAngle, 200);
        }

    }

    /**
     * Utilise le réseau de neurones pour décider de tourner à gauche ou a droite selon la distance des détecteurs
     */
    void drive(ArrayList<Line2D> boundaries){

        double distances[] = new double[detectors.length] ;

        for (int i = 0 ; i < detectors.length ; i++) {
            detectors[i].performDetection(boundaries, this.position, this.angle);
            distances[i] = detectors[i].getDistance() / 100 ;
        }


        double[] res = this.driver.thinkAbout(distances);
        double S = 0 ;

        for (int i = 0; i < 3; i++) {
            res[i] = Math.exp(res[i]);
            S += res[i] ;
        }

        for (int i = 0; i < 3; i++) {
            res[i] /= S ;
        }

        S = 0 ;
        for (int i = 3; i < 6; i++) {
            res[i] = Math.exp(res[i]);
            S += res[i] ;
        }

        for (int i = 3 ; i < 6 ; i++) {
            res[i] /= S ;
        }

        if(res[0] > .33){
            angle += 0.02 ;
        } else if (res[2] > .33){
            angle -= 0.02 ;
        }

        if(res[3] > .33){
            speed += 1 ;
        } else if (res[5] > .33 && speed > minSpeed){
            speed -= 1 ;
        }


    }

    public void increaseDistance(double d){
        this.distance += d ;
    }

    public double getDistance() {
        return distance;
    }

    Detector[] getDetectors() {
        return detectors;
    }
    void increaseAngle(double v) {
        this.angle += v ;
    }
    void increaseSpeed(double s) { this.speed += s ; }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Fellow getDriver() {
        return driver;
    }

    public void setPosition(Point2D position) {
        this.position = new Vector2(position.getX(), position.getY());
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
