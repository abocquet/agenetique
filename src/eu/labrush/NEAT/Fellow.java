package eu.labrush.NEAT;


import java.util.*;

public class Fellow {

    private HashMap<Integer, NodeType> nodes = new HashMap<>() ;
    private HashMap<Integer, Connection> connections = new HashMap<>();

    private double fitness = 0 ;
    private int output_number = 0 ;

    static private int globalInnovationNumber = 0 ;

    public Fellow() {}
    public Fellow(int sensors, int outputs){

        int[] sensors_id = new int[sensors];

        for (int i = 0; i < sensors; i++) {
            addNode(NodeType.SENSOR);
            sensors_id[i] = getInnovationNumber();
        }

        for (int i = 0; i < outputs; i++) {
            int m = nextInnovationNumber();
            addNode(NodeType.OUTPUT, m);

            for (int j = 0; j < sensors; j++) {
                int n = nextInnovationNumber();
                this.connections.put(n, new Connection(sensors_id[j], m, n));
            }
        }
    }


    /*************************
        Topology management
     *************************/
    void addNode(NodeType t){
        addNode(t, nextInnovationNumber());
    }

    void addNode(NodeType t, int number){
        if(t == NodeType.OUTPUT) output_number++;
        nodes.put(number, t);
    }

    public HashMap<Integer, NodeType> getNodes() {
        return nodes;
    }

    public HashMap<Integer, Connection> getConnections() {
        return connections;
    }

    public void addConnection(Connection c) {
        this.connections.put(c.evolutionNumber, c);
    }

    boolean hasConnection(Connection c0){

        Iterator it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Connection c = (Connection) ((Map.Entry)it.next()).getValue();
            if( (c.from == c0.from && c.to == c0.to) || (c.from == c0.to && c.to == c0.from)){
                return true ;
            }
        }

        return false ;
    }

    /*************************
        Evolution monitoring
     *************************/
    public static int getInnovationNumber() {
        return globalInnovationNumber;
    }

    public static void increaseInnovationNumber(){
        globalInnovationNumber++ ;
    }

    public static int nextInnovationNumber() {
        increaseInnovationNumber();
        return getInnovationNumber();
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

        int max = this.getConnections().keySet().stream().reduce(0, Integer::max);
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

        return D + E + 0.3 * W ;

    }

    /*************************
        Neural network
     *************************/

    double sigmoid(double x){
        return 1 / (1 + Math.exp(-x));
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
                if(c.to == n && c.enabled){
                    s += c.weight * thinkAboutAux(c.from, values);
                }
            }

            values[n] = sigmoid(s) ;
        }

        return values[n] ;

    }

    public double[] thinkAbout(double[] input){ // Activates neural network

        double[] values = new double[Fellow.getInnovationNumber() + 1]; // We guarantee the parameters are always given in the same order to the network
        Arrays.fill(values, 0, Fellow.getInnovationNumber(), Double.NaN);

        int c = 0 ;
        for (Integer key: asSortedList(nodes.keySet()))
        {
            if(nodes.get(key) == NodeType.SENSOR){
                values[key] = input[c] ;
                c++ ;
            }
        }

        double[] output = new double[output_number] ;

        c = 0 ;
        for (Integer key: asSortedList(nodes.keySet()))
        {
            if(nodes.get(key) == NodeType.OUTPUT){
                output[c] = thinkAboutAux(key, values);
                c++ ;
            }
        }

        return output ;
    }

    /*************************
        MISC
     *************************/

    @Override
    protected Fellow clone()  {
        Fellow f = new Fellow();

        f.nodes = (HashMap<Integer, NodeType>) this.nodes.clone();
        f.connections = (HashMap<Integer, Connection>) this.connections.clone();
        f.output_number = this.output_number ;
        f.fitness = this.fitness ;

        return f;
    }

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<T>(c);
        Collections.sort(list);
        return list;
    }

}
