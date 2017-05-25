package eu.labrush.race_simulation;

import org.dyn4j.Epsilon;
import org.dyn4j.geometry.Vector2;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Detector {

    private double angle ;
    private double distance;
    private double range;

    private Line2D target = null ;

    public Detector(double angle, double range) {
        this.angle = angle;
        this.range = range;
        this.distance = range ;
    }

    public void performDetection(ArrayList<Line2D> boundaries, Vector2 position, double carAngle){
        this.distance = range;
        this.target = null ;

        double angle = this.angle + carAngle ;
        double a = Math.sin(angle), b = - Math.cos(angle); // a(x + (x - (cos t + x)) + b (y - (y + sin t)) = 0 <= a = sin t , b = -cos t
        double c = (a * position.x + b * position.y);
        Line2D segment = new Line2D.Double(position.x, position.y, position.x + range * Math.cos(angle), position.y + range * Math.sin(angle));

        for(Line2D boundary: boundaries){
            double ap = boundary.getY2() - boundary.getY1() ;
            double bp = -(boundary.getX2() - boundary.getX1()) ;
            double cp = (ap * boundary.getX1() + bp * boundary.getY1()) ;

            double det = a*bp - b*ap ;

            if(Math.abs(det) > Epsilon.E){
                double x = (c*bp - cp*b) / det ;
                double y = (cp*a - c * ap) / det ;

                if(pointOnSegment(segment, x, y) && pointOnSegment(boundary, x, y)){

                    double newRange = Math.sqrt(
                            Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2)
                    );

                    if(newRange < this.distance){
                        this.distance = newRange ;
                        this.target = boundary ;
                    }
                }
            }
        }
    }

    private boolean pointOnSegment(Line2D line, double x, double y){
        return line.ptSegDist(x, y) < 0.1 ;
    }

    public double getAngle() {
        return angle;
    }

    public Line2D getTarget() {
        return target;
    }

    public double getDistance() {
        return distance;
    }
}