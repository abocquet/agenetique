package eu.labrush.NEAT;


/**
 * On représente les couches du réseau par des rationnels entre 0 et 1 pour éviter les cycles
 * Une connection ne peut aller de x vers y ssi x < y ou y.above(x) en java
 */
public class Node {
    private int id ;
    int numerator ;
    int denominator ;

    double bias ;

    static private int globalInnovationNumber = 0 ;


    NodeType type = NodeType.HIDDEN;

    public Node(int numerator, int denominator) {
        this.id = nextInnovationNumber();

        int pgcd = pgcd(numerator, denominator);

        this.numerator = numerator / pgcd;
        this.denominator = denominator / pgcd;
        this.bias = Random.gauss(Config.STDEV_NODE_BIAS, Config.MIN_NODE_BIAS, Config.MAX_NODE_BIAS) ;
    }

    public Node(NodeType type) {
        if(type == NodeType.OUTPUT) {
            this.numerator = 1 ;
            this.denominator = 1 ;
        } else if(type == NodeType.SENSOR){
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
        this.id = nextInnovationNumber();
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

    static Node avg(Node n1, Node n2){
        return new Node(n1.numerator * n2.denominator + n2.numerator * n1.denominator, n1.denominator * n2.denominator * 2);
    }

    /*************************
     Evolution monitoring
     *************************/
    public static int getInnovationNumber() {
        return globalInnovationNumber;
    }

    public static int nextInnovationNumber() {
        globalInnovationNumber++ ;
        return getInnovationNumber();
    }

    public int getId() {
        return id;
    }
}
