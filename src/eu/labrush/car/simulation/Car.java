package eu.labrush.car.simulation;

import eu.labrush.car.genetic.Driver;
import eu.labrush.neural.NeuralNetwork;
import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;

class Car {

    private Vector2 position = new Vector2(120, 70);
    private double speed = 40; // Linear speed

    private double angle = 0;
    private Dimension dimension = new Dimension(20, 20);

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
    private Driver driver ;


    Car(Driver driver){
        this.driver = driver ;

        double weights[][][] = driver.getWeights() ;
        double maxAngle = Math.PI / 3 ;

        /*-------------------------------
          On initialise les detecteurs
        --------------------------------*/
        int nbDetectors = weights[0].length ;
        double angle = maxAngle / (nbDetectors - 1) ;

        detectors = new Detector[nbDetectors] ;
        for (int i = 0 ; i < nbDetectors ; i ++){
            detectors[i] = new Detector(angle * 2 * (double)i - maxAngle, 100);
        }

        /*-------------------------------
          Puis le réseau neural
        --------------------------------*/

        this.brain = new NeuralNetwork(detectors.length);
        for(int i = 0 ; i < weights.length ; i++){
            this.brain.addLayer(weights[i]);
        }
    }

    Car(){
        this(new Driver());
    }


    /**
     * Utilise le réseau de neurones pour décider de tourner à gauche ou a droite selon la distance des détecteurs
     */
    void drive(ArrayList<Line2D> boundaries){

        double distances[] = new double[detectors.length] ;

        for (int i = 0 ; i < detectors.length ; i++) {
            detectors[i].performDetection(boundaries, this.position, this.angle);
            distances[i] = detectors[i].getDistance();
        }

        double[] res = this.brain.compute(distances);
        if(res[1] - res[0] > .2){
            angle += 0.02 ;
        } else if(res[1] - res[0] < -.2){
            angle -= 0.02 ;
        }

    }

    /*private double[][] randomBidimentionnalArray(int x, int y){
        double[][] t = new double[x][y] ;
        for(int i = 0 ; i < x ; i++){
            for(int j = 0 ; j < y ; j++){
                t[i][j] = (Math.random()  - .5) * 10 ;
            }
        }

        return t ;
    }*/

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

    public Driver getDriver() {
        return driver;
    }
}
