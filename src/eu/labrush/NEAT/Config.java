package eu.labrush.NEAT;

public class Config {

    static double P_NODE_ADD_MUTATION = 0.2 ;
    static double P_NODE_DEL_MUTATION = 0.2 ;
    static double P_CONNECTION_ADD_MUTATION = 0.2 ;
    static double P_CONNECTION_DEL_MUTATION = 0.2 ;

    static double SAME_SPECIES_THRESHOLD = 3.0 ;
    static double SURVIVAL_SPECIES_THRESHOLD = 0.2 ;

    static double MAX_CONNECTION_VALUE = 30.0 ;
    static double MIN_CONNECTION_VALUE = MAX_CONNECTION_VALUE ;

    static double P_NODE_DISABLE = 0.7 ; //Probabilty that a connection is disbaled on the child if it is on one and only one of the two parents

    static double DISJOINT_COEFF = 1.0 ;
    static double EXCESS_COEFF   = 1.0 ;
    static double DIFF_COEFF     = .5  ;

    static double INTERSPECIES_RATE = 0.05 ;

    public static int MINORITY = 5 ; // age until species are helped
    public static double MINORITY_HELP_MULTIPLIER = 5 ;

    public static double STAGNATION_MULTIPLIER = 0.1;
    public static int STAGNATION_AGE = 10 ;
}
