package eu.labrush.walker.genetic;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.walker.Renderer2D;
import eu.labrush.walker.actionner.ActionnerSettings;
import eu.labrush.walker.actionner.BipedeBodyActionner;
import eu.labrush.walker.walker.BipedBody;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import static java.lang.Math.toRadians;

/**
 * A wrapper which interfaces genetic algorithm and the dyn4j walker
 */
public class Walker extends AbstractFellow implements Runnable {

    static final int AskedDNASIZE = 48 ;
    private int fitness = Integer.MIN_VALUE ;

    public Walker() {
        super(AskedDNASIZE, 2);
    }

    public Walker(int[] dna) throws Exception {
        super(dna, 2);

        if(dna.length != AskedDNASIZE){
            throw new Exception("dna chain length doesn't match (" + AskedDNASIZE + " exepected, " + dna.length + " given)" );
        }
    }

    public void run() {
        getFitness() ;
    }

    @Override
    public int getFitness() {
        if(this.fitness != Integer.MIN_VALUE){
            return  this.fitness ;
        }

        Rectangle frect = new Rectangle(10_000, 1);
        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -6.0);
        floor.addFixture(frect);

        BipedBody walker = new BipedBody() ;
        BipedeBodyActionner actionner = new BipedeBodyActionner(walker);

        actionner.setLeftHip(  readDNA(this.getDna(), 0, 3, 2, 3, 4));
        actionner.setLeftKnee( readDNA(this.getDna(), 12, 3, 2, 3, 4));
        actionner.setRightHip( readDNA(this.getDna(), 24, 3, 2, 3, 4));
        actionner.setRightKnee(readDNA(this.getDna(), 36, 3, 2, 3, 4));

        World world = new World();
        world.addBody(floor);
        walker.insertInWorld(world);
        actionner.listen(world);

        world.step(10_000);
        System.out.print(".");

        this.fitness = (int) (walker.getDistance() * 1000);
        return this.fitness ;
    }

    //TODO: delete once tests completed
    public World newSimulation() {
        Rectangle frect = new Rectangle(10_000, 1);
        Renderer2D.GameObject floor = new Renderer2D.GameObject();
        floor.setMass(MassType.INFINITE);
        floor.translate(0.0, -6.0);
        floor.addFixture(frect);

        BipedBody walker = new BipedBody() ;
        BipedeBodyActionner actionner = new BipedeBodyActionner(walker);

        actionner.setLeftHip(  readDNA(this.getDna(), 0, 3, 2, 3, 4));
        actionner.setLeftKnee( readDNA(this.getDna(), 12, 3, 2, 3, 4).addMaxAngle(toRadians(30)));

        actionner.setRightHip( readDNA(this.getDna(), 24, 3, 2, 3, 4));
        actionner.setRightKnee(readDNA(this.getDna(), 36, 3, 2, 3, 4).addMaxAngle(toRadians(30)));

        World world = new World();
        world.addBody(floor);
        walker.insertInWorld(world);
        actionner.listen(world);

        return world ;
    }

    /**
     * @param dna the binary dna string containing the settings
     * @param freq_length the length of the frequence dna string
     * @param min_length the length of the minimum angle dna string
     * @param max_length the length of the maximum angle dna string
     * @param phase_length the length of the phase dna string
     * @return the settings to animate a joint
     */
    private ActionnerSettings readDNA(int[] dna, int start, int freq_length, int min_length, int max_length, int phase_length){
        ActionnerSettings settings = new ActionnerSettings();

        int pos = start ;

        settings.freq = readIntFromDNA(dna, pos, freq_length) + 1 ; // A null freq is useless
        pos += freq_length ;

        settings.minAngle = readIntFromDNA(dna, pos, min_length);
        settings.minAngle = toRadians(10 * settings.minAngle) ;
        pos += min_length ;

        settings.maxAngle = readIntFromDNA(dna, pos, max_length);
        settings.maxAngle = toRadians(5 * settings.maxAngle) ;
        pos += max_length ;

        settings.phase = readIntFromDNA(dna, pos, phase_length);
        settings.phase = 2 * Math.PI / (2 ^ phase_length) * settings.phase ;

        return settings ;
    }

    private int readIntFromDNA(int[] dna, int start, int length) {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = start ; i < start + length ; i++){
            if(dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }
}
