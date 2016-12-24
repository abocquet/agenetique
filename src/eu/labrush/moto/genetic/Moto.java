package eu.labrush.moto.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.moto.GroundDesigner;
import eu.labrush.moto.Renderer2D;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.*;

import java.util.LinkedList;

public class Moto extends AbstractFellow implements Runnable {

    private GroundDesigner gd = null ;
    public static int AskedDNASIZE = 48 ;
    private static int PeakNumber = -1 ;
    private static int WheelAdressLenght = -1 ;

    private static double width = 3 ;
    private static double height = 2 ;

    private static int Resolution = 4 ;

    private long fitness = Long.MIN_VALUE ;
    private Vector2[] peaks ;

    public Moto() {
        super(AskedDNASIZE, 2);
    }

    public Moto(int[] dna) throws Exception {
        super(dna, 2);

        if(dna.length != AskedDNASIZE){
            throw new Exception("dna chain length doesn't match (" + AskedDNASIZE + " exepected, " + dna.length + " given)" );
        }
    }

    public void run() {
        getFitness() ;
    }

    @Override
    public long calcFitness() {

        if(this.fitness != Long.MIN_VALUE){
            return this.fitness ;
        }

        World world = this.getSim();
        Body moto = (Body) world.getUserData();
        LinkedList<Integer> takenPositions = new LinkedList<>() ;

        int stepsToRun = 1000 ;
        int stepSamplingRate = 100 ; // La fréquence avec laquelle on enregistre les positions
        int sum = 0 ;

        while(stepsToRun > 0){
            world.step(stepSamplingRate);

            int currentPos = (int) (1000 * moto.getWorldCenter().x) ;
            takenPositions.addLast(currentPos);
            sum += currentPos ;

            if(takenPositions.size() > 100) {
                sum -= takenPositions.pop();
            }

            if(currentPos > sum / 100){
                stepsToRun += stepSamplingRate ;
            }

            stepsToRun -= stepSamplingRate ;
        }

        this.fitness = (long)(moto.getWorldCenter().x * 10);
        return this.fitness ;
    }

    public World getSim(){
        World world = new World();

        Vector2 center = new Vector2(); // Le barycentre des points de la voiture

        this.peaks = new Vector2[PeakNumber];
        int pow2res = 2 ^ Resolution;

        int peaksCounter = 0 ; //semblable a peakNumber, mais peut prendre des valeurs inférieures car il indique le nombre de sommets différents

        for(int i = 0 ; i < PeakNumber ; i++){
            int x = readIntFromDNA(this, 2 * i * Resolution, Resolution);
            int y = readIntFromDNA(this, (2*i + 1) * Resolution, Resolution);

            Vector2 newPeak = new Vector2(width * ((double)x) / pow2res, height * ((double)y) / pow2res);
            boolean alreadyIn = false ;

            for(int j = 0 ; j < peaksCounter ; j++){
                if(peaks[j].equals(newPeak)){
                    alreadyIn = true ;
                    break ;
                }
            }

            if(!alreadyIn) {
                peaks[peaksCounter] = newPeak;
                peaksCounter++;
                center.add(newPeak);
            }
        }

        center.multiply(1 / (double)peaksCounter);

        Renderer2D.GameObject body = new Renderer2D.GameObject() ;
        body.setMassType(MassType.NORMAL);

        for(int i = 0 ; i < peaksCounter ; i++){
            Vector2 p1 = peaks[i % peaksCounter], p2 = peaks[(i+1) % peaksCounter] ;

            if(p1.equals(center) || p2.equals(center) || p1.equals(p2)){
                continue ;
            }

            BodyFixture fixture ;
            try {
                fixture = new BodyFixture(new Triangle(center, p1, p2));
            } catch (IllegalArgumentException e){
                fixture = new BodyFixture(new Triangle(center, p2, p1));
            }

            fixture.setFriction(50_000);
            fixture.setDensity(2.0);
            body.addFixture(fixture);

        }

        int wheelAdress[] = {
                readIntFromDNA(this, PeakNumber * Resolution * 2, WheelAdressLenght),
                readIntFromDNA(this, PeakNumber * Resolution * 2+ WheelAdressLenght, WheelAdressLenght)
        } ;

        if(wheelAdress[0] == wheelAdress[1]){
            wheelAdress[1] = (wheelAdress[1] + 1);
        }

        for(int i = 0 ; i < 2 ; i++){
            Vector2 selectedPeak = peaks[wheelAdress[i] % peaksCounter];

            WheelParam param = new WheelParam(this, (PeakNumber * Resolution + WheelAdressLenght) * 2 + 6 * i );

            Renderer2D.GameObject wheel = new Renderer2D.GameObject();
            wheel.addFixture(new Circle(param.size));
            wheel.translate(selectedPeak);

            RevoluteJoint joint = new RevoluteJoint(body, wheel, selectedPeak);
            joint.setMotorSpeed(param.speed);
            joint.setMaximumMotorTorque(param.torque);
            joint.setMotorEnabled(true);

            wheel.setMass(new Mass(new Vector2(), 100, 10));
            wheel.getFixture(0).setFriction(100);

            world.addBody(wheel);
            world.addJoint(joint);

            int mask = 2^(i+1);
            wheel.getFixture(0).setFilter(new CategoryFilter(mask,mask));
        }

        body.setMass(new Mass(new Vector2(), 100, 100));
        world.addBody(body);
        world.setUserData(body);

        if(gd != null){
            gd.addToWorld(world);
        }

        return world ;
    }

    /**
     * @param f a fellow
     * @param start
     * @param length
     * @return binary input as int
     */
    private int readIntFromDNA(AbstractFellow f, int start, int length) {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = start ; i < start + length ; i++){
            if(f.getDNA(i) == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }

    public static void setPeakNumber(int number){
        PeakNumber = number ;
        WheelAdressLenght = (int) Math.ceil(
                Math.log(number) / Math.log(2)
        ) ;

        // - Résolution de 1/2**6 par gene, on double longueur et largeur
        // - 2 bits par : taille, vitesse et couple des roues / roue
        // - ceil(log2(nombre de roues)) bits pour l'adresse des pics avec une roue
        AskedDNASIZE = 2 * (WheelAdressLenght + number * Resolution) + 2 * 2 * 3 ;
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        Moto.width = width;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        Moto.height = height;
    }

    private Vector2 funSubstract(Vector2 p1, Vector2 p2){
        return new Vector2(p1.x - p2.x, p1.y - p2.y);
    } //version fonctionnelle pour soustraire deux vecteurs

    public void setGroundDesigner(GroundDesigner gd) {
        this.gd = gd;
    }

    public GroundDesigner getGroundDesigner() {
        return gd;
    }


}
