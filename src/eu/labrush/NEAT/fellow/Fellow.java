package eu.labrush.NEAT.fellow;


import eu.labrush.NEAT.Config;

import java.util.*;

public class Fellow {

    private HashMap<Integer, Node> nodes = new HashMap<>() ;
    private HashMap<Integer, Connection> connections = new HashMap<>();

    protected double fitness = 0 ;
    protected int output_number = 0 ;
    protected int inputs_number = 0 ;


    public Fellow() {}
    public Fellow(int inputs, int outputs){

        Node[] sensors_id = new Node[inputs];

        for (int i = 0; i < inputs; i++) {
            Node newNode = new Node(NodeType.INPUT);
            addNode(newNode);
            sensors_id[i] = newNode ;
        }

        for (int i = 0; i < outputs; i++) {
            Node newNode = new Node(NodeType.OUTPUT) ;
            addNode(newNode);

            for (int j = 0; j < inputs; j++) {
                this.addConnection(new Connection(sensors_id[j], newNode));
            }
        }
    }

    /*************************
        Topology management
     *************************/
    public void addNode(Node n){
        if(n.type == NodeType.OUTPUT) output_number++;
        else if(n.type == NodeType.INPUT) inputs_number++;
        nodes.put(n.getId(), n);
    }

    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    public void removeNode(Node node){
        Iterator<Map.Entry<Integer,Connection>> iter = connections.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Connection> entry = iter.next();
            Connection c0 = entry.getValue();
            if((c0.getFrom().getId() == node.getId()) || (c0.getTo().getId() == node.getId())){
                iter.remove();
            }
        }

        nodes.remove(node.getId());
    }

    public HashMap<Integer, Connection> getConnections() {
        return connections;
    }

    public void addConnection(Connection c) {
        Iterator<Map.Entry<Integer,Connection>> iter = connections.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Connection> entry = iter.next();
            Connection c0 = entry.getValue();
            if((c0.getFrom().getId() == c.getFrom().getId() && c.getTo().getId() == c0.getTo().getId()) || (c0.getFrom().getId() == c.getTo().getId() && c.getFrom().getId() == c0.getTo().getId())){
                iter.remove();
            }
        }

        this.connections.put(c.id, c);
    }

    public void removeConnection(Connection c) {
        connections.remove(c.id);
    }

    /*************************
        GA stuffs
     *************************/

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    // Cf eq 3.1 in NEAT PhD
    public double distanceTo(Fellow f){

        double D = 0, E = 0, W = 0 ;

        int max = 0 ;

        for(int i: getConnections().keySet()){
            if(i > max){
                max = i ;
            }
        }

        int c = 0 ;

        for (Integer key: f.connections.keySet()) {

            if(key > max) {
                E++ ;
            } else if(this.getConnections().containsKey(key)){
                c++ ;
                W += Math.abs(f.connections.get(key).weight - this.connections.get(key).weight);
            } else {
                D++ ;
            }

        }

        for (Integer key: this.connections.keySet()){
            if(!f.connections.containsKey(key)){
                D++ ;
            }
        }

        double dist = D * Config.DISJOINT_COEFF + E * Config.EXCESS_COEFF ;

        if(c != 0){
            dist += W / c * Config.DIFF_COEFF ;
        }

        return dist;
    }

    /*************************
        Neural network
     *************************/

    double sigmoid(double x){
        x = Math.max(-60.0, Math.min(60.0, 5.0 * x));
        return 1.0 / (1.0 + Math.exp(-x / 10));
    }

    // On procède récursivement sur les noeuds en utilisant la programmation dynamique
    // ex:
    //         7        8
    //        / \     /
    //       6   \   /
    //      /  \<- 5
    //     4      /  \
    //       \ -> 3   2
    //
    // 7
    //  => 6
    //      => 4
    //          => 3 OK
    //      => 5
    //          => 3 OK
    //          => 2 OK
    //  => 5 OK (mémoisé)
    //  8
    //  => 5 OK (mémoisé)
    //
    private double thinkAboutAux(int n, double[] values){
        if(Double.isNaN(values[n])){

            double s = 0.0 ;
            for(Connection c: connections.values()){
                if(c.getTo().getId() == n && c.enabled){
                    s += c.weight * thinkAboutAux(c.getFrom().getId(), values);
                }
            }

            s += this.nodes.get(n).bias ;
            values[n] = sigmoid(s) ;
        }

        return values[n] ;

    }

    public double[] thinkAbout(double[] input){ // Activates neural network

        double[] values = new double[Node.indexer.current() + 1]; // We guarantee the parameters are always given in the same order to the network
        Arrays.fill(values, 0, Node.indexer.current(), Double.NaN);

        int c = 0 ;
        for (Integer key: asSortedList(nodes.keySet()))
        {
            if(nodes.get(key).type == NodeType.INPUT){
                values[key] = input[c] ;
                c++ ;
            }
        }

        double[] output = new double[output_number] ;

        try {
            c = 0;
            for (Integer key : asSortedList(nodes.keySet())) {
                if (nodes.get(key).type == NodeType.OUTPUT) {
                    output[c] = thinkAboutAux(key, values);
                    c++;
                }
            }
        } catch(StackOverflowError e){
            e.printStackTrace();
            System.out.println(nodes);
            System.out.println(connections);

            System.exit(-1);
        }

        return output ;
    }

    /*************************
        MISC
     *************************/

    @Override
    public Fellow clone()  {
        Fellow f = new Fellow();

        f.nodes = (HashMap<Integer, Node>) this.nodes.clone();
        f.connections = (HashMap<Integer, Connection>) this.connections.clone();
        f.output_number = this.output_number ;
        f.inputs_number = this.inputs_number ;
        f.fitness = this.fitness ;

        return f;
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        Collections.sort(list);
        return list;
    }

    @Override
    public String toString() {
        return "fellow{" +
                "fitness=" + fitness +
                '}';
    }

    public int getOutputNumber() {
        return output_number;
    }

    public int getInputsNumber() {
        return inputs_number;
    }
}
