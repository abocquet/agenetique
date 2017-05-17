package eu.labrush.NEAT;

public class Connection implements Cloneable {

    private static double MAX_CONNECTION_VALUE =  5.0 ;
    private static double MIN_CONNECTION_VALUE = -5.0 ;

    public int from = 0 ;
    public int to = 0 ;

    public int evolutionNumber = 0 ;

    public double weight = 1 ;
    public boolean enabled = true ;

    public Connection(int from, int to, int evolutionNumber) {
        this.from = from;
        this.to = to;

        this.evolutionNumber = evolutionNumber ;

        this.weight = Math.random() * (MAX_CONNECTION_VALUE - MIN_CONNECTION_VALUE) + MIN_CONNECTION_VALUE ;
    }

    @Override
    protected Connection clone() {
        Connection c = new Connection(from, to, evolutionNumber);
        c.weight = weight ;
        c.enabled = enabled ;
        return c;
    }

    public int getEvolutionNumber() {
        return evolutionNumber;
    }
}
