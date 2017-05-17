package eu.labrush.NEAT;

public class Mutation {

    static void connectionMutation(Fellow f){

        Connection c ;
        int n = f.getNodes().size() - 1;

        do { c = new Connection(random(n), random(n), Fellow.getEvolutionNumber()); }
        while(!(c.from == c.to) && !f.hasConnection(c));

        Fellow.increaseEvolutionNumber();
        f.addConnection(c);
    }

    static void nodeMutation(Fellow f){

        int n = random(f.getConnections().size() - 1); //The index of the connection on which we are going to add the node
        while(!f.getConnections().get(n).enabled){
            n = random(f.getConnections().size() - 1);
        }

        Connection c = f.getConnections().get(n);
        int from = c.from ;
        int to = c.to ;

        c.enabled = false ;

        n = Fellow.nextEvolutionnaryNumber(); // The number of the node we are inserting
        f.addNode(NodeType.HIDDEN, n);
        f.addConnection(new Connection(from,  n, Fellow.nextEvolutionnaryNumber()));
        f.addConnection(new Connection(n, to, Fellow.nextEvolutionnaryNumber()));
    }

    static int random(int max){ // return random number between 0 and max
        return ((int) (Math.random() * (double) (max + 1))) % max ;
    }

}
