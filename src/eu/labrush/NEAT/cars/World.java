package eu.labrush.NEAT.cars;

import eu.labrush.NEAT.Nature;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.operators.FitnessEvaluator;
import eu.labrush.car.simulation.MapGenerator;
import org.dyn4j.geometry.Vector2;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class World implements FitnessEvaluator{

    private Car[] cars ;
    private int carsAlive ;

    ArrayList<Line2D> boundaries = new ArrayList<>();

    private Nature nature ;

    private MapGenerator map = new MapGenerator(50, 50, 750, 750);
    private HashMap<Fellow, Double> distances = new HashMap<>();

    public World() {
        this.nature = new Nature(150, 8, 6, this);
        setup();
    }

    public double eval(Fellow f){
        if(distances.containsKey(f)){
            return distances.get(f);
        } else {
            return 0 ;
        }
    }

    private void setup() {
        if (this.nature == null) return;
        distances.clear();

        boundaries.clear();
        addBoudaries(map.getSquareGrid());


        HashSet<Fellow> fellows = nature.getFellows();
        Fellow[] drivers = new Fellow[fellows.size()] ;
        drivers = fellows.toArray(drivers);

        int nbCars = drivers.length;
        cars = new Car[nbCars];

        for (int i = 0; i < nbCars; i++) {
            Fellow d = drivers[i];
            cars[i] = new Car(d);
            cars[i].setPosition(map.getStart());
            cars[i].getPosition().add(i,0);
            //cars[i].setAngle(Math.PI / 2);
        }

        carsAlive = nbCars;
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

            if(c.getDriver() != null) {
                c.increaseDistance(dpos.getMagnitude());
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

            searchCollision:
            for (Line2D boundary: boundaries) {
                for(Line2D cB: carBorders){
                    if(cB.intersectsLine(boundary)){
                        c.setRunning(false) ;
                        carsAlive-- ;
                        break searchCollision ;
                    }
                }
            }


            for (Line2D cB: carBorders){
                if(cB.intersectsLine(map.getFinishLine()) && !c.isFinished()){
                    c.setFinished(true);
                    carsAlive-- ;
                }
            }

            c.drive(boundaries);
        }

        if(this.carsAlive == 0){
            for(Car c: cars){
                distances.put(c.getDriver(), c.getDistance());
            }

            nature.evolve();
            setup();
        }

    }

    public Car[] getCars() {
        return cars;
    }

    public void addBoudaries(ArrayList<Line2D> lines){
        this.boundaries.addAll(lines);
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

    public MapGenerator getMap() {
        return map;
    }
}
