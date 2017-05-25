package eu.labrush.moto;

import org.dyn4j.Renderer2D;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

import java.util.Arrays;
import java.util.Random;

public class GroundDesigner {

    private int dist, nbBlocks ;
    private Vector2[] ground = null ;
    private Vector2 offset = new Vector2();

    GroundDesigner() {
        this.dist = 2000 ;
        this.nbBlocks = 200 ;
    }

    GroundDesigner(int dist, int nbBlocks) {
        this.dist = dist ;
        this.nbBlocks = nbBlocks ;
    }


    public Vector2[] generateGround(){

        double[] xAxe = new double[this.nbBlocks + 1] ;
        double[] yAxe = new double[this.nbBlocks + 1] ;

        double maxSteep = 0.9 ;

        xAxe[0] = .0 ;
        yAxe[0] = .0 ;

        Random random = new Random();

        // On tire les points de rupture du terrain dans l'intervalle [0, distance] puis on les trie
        for(int i = 1 ; i <= this.nbBlocks ; i ++) {
            xAxe[i] = this.dist * random.nextDouble();
        }

        Arrays.sort(xAxe);

        for(int i = 1 ; i <= this.nbBlocks ; i ++){
            yAxe[i] = maxSteep * (2*random.nextFloat() - 1) * (xAxe[i] - xAxe[i - 1]) + yAxe[i - 1] ;
        }

        Vector2[] points = new Vector2[this.nbBlocks + 3] ;

        points[0] = new Vector2(xAxe[0] - 20, yAxe[0]);
        for(int i = 0 ; i <= this.nbBlocks ; i++){
            points[i + 1] = new Vector2(xAxe[i], yAxe[i]);
        }

        points[this.nbBlocks + 2] = new Vector2(xAxe[this.nbBlocks], 10_000) ;

        return points ;

    }

    public void addToWorld(World world){

        if(this.ground == null){
            this.ground = generateGround();
        }

        for (int i = 0 ; i < this.ground.length - 1 ; i++){

            Vector2 p1 = this.ground[i];
            Vector2 p2 = this.ground[i+1];

            Renderer2D.GameObject body = new Renderer2D.GameObject() ;
            body.addFixture(new Segment(p1, p2));
            body.translate(offset);

            world.addBody(body);
        }
    }

    public int getDist() {
        return dist;
    }

    public GroundDesigner setDist(int dist) {
        this.dist = dist;
        return this ;
    }

    public int getNbBlocks() {
        return nbBlocks;
    }

    public GroundDesigner setNbBlocks(int nbBlocks) {
        this.nbBlocks = nbBlocks;
        return this ;
    }

    public Vector2[] getGround() {
        return ground;
    }

    public GroundDesigner setGround(Vector2[] ground) {
        this.ground = ground;
        return this ;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }
}
