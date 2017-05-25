package eu.labrush.NEAT ;

import eu.labrush.NEAT.fellow.Connection;
import eu.labrush.NEAT.fellow.Fellow;
import eu.labrush.NEAT.fellow.Node;
import eu.labrush.NEAT.fellow.NodeType;

import java.util.Arrays;

public class NNTest {


    public static void main(String[] args) {

        Fellow f = new Fellow();

        Node n_2 = new Node(0, 1); n_2.type = NodeType.INPUT;
        Node n_1 = new Node(0, 1); n_1.type = NodeType.INPUT;
        Node n0 = new Node(1, 2);  n0.type = NodeType.OUTPUT ;
        Node n1 = new Node(1, 2);
        Node n2 = new Node(1, 1);

        n0.bias =-2.6698780222121243;
        n1.bias =-0.889232197480437;
        n2.bias =-0.504111936731011;

        f.addNode(n_2);
        f.addNode(n_1);
        f.addNode(n0);
        f.addNode(n1);
        f.addNode(n2);

        Connection c0 = new Connection(n_2, n1);
        c0.weight = 3.6848252127933447;
        Connection c1 = new Connection(n_2, n2);
        c1.weight = 1.1963924662756749;
        Connection c2 = new Connection(n_1, n0);
        c2.weight = -1.9772797957276107;
        Connection c3 = new Connection(n_1, n1);
        c3.weight = 3.4344932451151036;
        Connection c4 = new Connection(n1, n0);
        c4.weight = 5.596233452072331;
        Connection c5 = new Connection(n2, n0);
        c5.weight = -1.7022069400137825;


        f.addConnection(c0);
        f.addConnection(c1);
        f.addConnection(c2);
        f.addConnection(c3);
        f.addConnection(c4);
        f.addConnection(c5);

        System.out.println("");
        System.out.println(f.getNodes());
        System.out.println(f.getConnections());

        double[][] tests = new double[4][];

        tests[0] = new double[]{0, 0, 0}; // arg1 arg2 resultat attendu
        tests[1] = new double[]{1, 1, 0};
        tests[2] = new double[]{0, 1, 1};
        tests[3] = new double[]{1, 0, 1};

        System.out.println("");  // Affiche 1 si juste, 0 si faux

        for (int i = 0; i < tests.length; i++) {
            System.out.println(Arrays.toString(tests[i]) + " -> " + f.thinkAbout(tests[i])[0] + " " + (Math.round(f.thinkAbout(tests[i])[0]) == tests[i][2] ? "CORRECT" : "ERREUR"));
        }
    }
}