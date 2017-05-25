package eu.labrush.NEAT.fellow;


import eu.labrush.NEAT.Config;
import eu.labrush.NEAT.utils.Indexer;
import eu.labrush.NEAT.utils.Random;

/**
 * On représente les couches du réseau par des rationnels entre 0 et 1 pour éviter les cycles
 * Une connection ne peut aller de x vers y ssi x < y ou y.above(x) en java
 */
public class Node {
    private int id ;
    public int numerator ;
    public int denominator ;

    public double bias ;

    public NodeType type = NodeType.HIDDEN;

    public Node(int numerator, int denominator) {
        this.id = indexer.next();

        int pgcd = pgcd(numerator, denominator);

        this.numerator = numerator / pgcd;
        this.denominator = denominator / pgcd;
        this.bias = Random.gauss(Config.STDEV_NODE_BIAS, Config.MAX_NODE_BIAS, Config.MIN_NODE_BIAS) ;
    }

    public Node(Node n1, Node n2){
        this(n1.numerator * n2.denominator + n2.numerator * n1.denominator, n1.denominator * n2.denominator * 2);
        this.bias = Math.random() >= .5 ? n1.bias : n2.bias ;
    }

    public Node(NodeType type) {
        if(type == NodeType.OUTPUT) {
            this.numerator = 1 ;
            this.denominator = 1 ;
        } else if(type == NodeType.INPUT){
            this.numerator = 0 ;
            this.denominator = 1 ;
        } else {
            try {
                throw new Exception("Cannot create node without knowing type nor fraction");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.type = type;
        this.id = indexer.next();
    }

    static int pgcd(int a, int b){
        if(a == 0){
            return b ;
        } else if (a == 1 || b == 1){
            return 1 ;
        } else if (b == 0){
            return 0 ;
        } else if (b >= a){
            return pgcd(b % a, a);
        } else {
            return pgcd(a % b, b);
        }
    }

    // A/B > nA/nB
    public boolean above(Node n){
        return numerator * n.denominator > n.numerator * denominator ;
    }

    public boolean frontOf(Node n) {
        return numerator * n.denominator == n.numerator * denominator ;
    }

    /*************************
     Evolution monitoring
     *************************/

    public static Indexer indexer = new Indexer(0);

    public int getId() {
        return id;
    }
}
