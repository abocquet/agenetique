package eu.labrush.car.simulation;

import org.dyn4j.geometry.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

public class World {

    Car[] cars ;
    Car user;
    private int carsAlive ;

    ArrayList<Line2D> boundaries = new ArrayList<>();

    public World() {
        initialize();
    }

    private void initialize() {
        int nbCars = 1 ;
        cars = new Car[nbCars];
        for(int i = 0 ; i < nbCars ; i++){
            cars[i] = new Car();
        }

        //user = cars[0] ;
        carsAlive = nbCars ;

        addRectBoundary(50, 50, 620, 380);
        addRectBoundary(120, 120, 480, 240);
    }

    /**
     * @param time time elapsed in ms
     */
    public void step(double time){

        for(int i = 0 ; i < cars.length ; i++) {
            Car c = cars[i] ;

            if(!c.isRunning())
                continue;

            Vector2 dpos = new Vector2(c.getAngle()).product(c.getSpeed() * time / 1000);
            c.getPosition().add(dpos);

            if(cars[i].getDriver() != null) {
                cars[i].getDriver().increaseDistance(dpos.getMagnitude());
            }

            double x = c.getX(), y = c.getY(), w =  c.getWidth()/2, h =  c.getHeight()/2 ;
            double cos = Math.cos(c.getAngle()), sin = Math.sin(c.getAngle()) ;

            double  p1x = x + w*cos + h * sin, p1y = y - w*sin + h*cos,
                    p2x = x - w*cos + h * sin, p2y = y + w*sin + h*cos,
                    p3x = x - w*cos - h * sin, p3y = y + w*sin - h*cos,
                    p4x = x + w*cos + h * sin, p4y = y + w*sin - h*cos ;


            Line2D[] carBorders = new Line2D[]{
                    new Line2D.Double(p1x, p1y, p2x, p2y),
                    new Line2D.Double(p2x, p2y, p3x, p3y),
                    new Line2D.Double(p3x, p3y, p4x, p4y),
                    new Line2D.Double(p4x, p4y, p1x, p1y)
            };

            for (Line2D boundary: boundaries) {
                for(Line2D cB: carBorders){
                    if(cB.intersectsLine(boundary)){
                        c.setRunning(false) ;
                        carsAlive-- ;
                        break ;
                    }
                }
            }

            if(carsAlive <= 0){
                initialize();
            }

            if(c != this.user)
                c.drive(boundaries);
        }

    }

    public Car[] getCars() {
        return cars;
    }

    public ArrayList<Line2D> getBoundaries() {
        return boundaries;
    }

    public void addBoundary(double sx, double sy, double ex, double ey){
        this.boundaries.add(new Line2D.Double(sx, sy, ex, ey));
    }

    public void addRectBoundary(double x, double y, double w, double h){
        addBoundary(x, y, x+w, y);
        addBoundary(x+w, y, x+w, y+h);
        addBoundary(x, y+h, x+w, y+h);
        addBoundary(x, y+h, x, y);
    }
}
