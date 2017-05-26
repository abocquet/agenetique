package eu.labrush.NEAT;

public class Config {

    public static double P_CROSSOVER                  = 0.8;

    public static double P_NODE_ADD_MUTATION          = 0.2 ;
    public static double P_NODE_DEL_MUTATION          = 0.2 ;
    public static double P_NODE_BIAS_MUTATION         = 0.5 ;

    public static double P_CONNECTION_ADD_MUTATION    = 0.2 ;
    public static double P_CONNECTION_DEL_MUTATION    = 0.2 ;
    public static double P_CONNECTION_WEIGHT_MUTATION = 0.5 ;

    public static double SAME_SPECIES_THRESHOLD       = 3.9 ;

    public static double MAX_CONNECTION_WEIGHT = 3.0 ;
    public static double MIN_CONNECTION_WEIGHT = -MAX_CONNECTION_WEIGHT;
    public static double STDEV_CONNECTION_WEIGHT = 1.0 ;

    public static double MAX_NODE_BIAS = 3.0 ;
    public static double MIN_NODE_BIAS = - MAX_NODE_BIAS ;
    public static double STDEV_NODE_BIAS = 1.0 ;

    public static double MAX_NODE_RESPONSE = 30 ;
    public static double MIN_NODE_RESPONSE = -MAX_NODE_RESPONSE ;

    public static double P_NODE_DISABLE = 0.7 ; //Probabilty that a connection is disabled on the child if it is on one and only one of the two parents

    public static double DISJOINT_COEFF     = 1.0 ;
    public static double EXCESS_COEFF       = 1.0 ;
    public static double DIFF_WEIGHT_COEFF  = 0.5  ;

    public static int ELITISM = 2 ;

    public static int MINORITY = 30 ; // age until species are helped
    public static double MINORITY_HELP_MULTIPLIER = 5 ;

    public static int STAGNATION_AGE = 20 ;


    /*

    bias_mutate_power       = 0.5
    bias_mutate_rate        = 0.7
    bias_replace_rate       = 0.1

    # genome compatibility options
    compatibility_weight_coefficient   = 0.5

    # connection enable options
    enabled_default         = True
    enabled_mutate_rate     = 0.01

    feed_forward            = True

    # node response options
    response_init_mean      = 1.0
    response_init_stdev     = 0.0
    response_max_value      = 30.0
    response_min_value      = -30.0
    response_mutate_power   = 0.0
    response_mutate_rate    = 0.0
    response_replace_rate   = 0.0

    # connection weight options
    weight_init_mean        = 0.0
    weight_init_stdev       = 1.0
    weight_max_value        = 30
    weight_min_value        = -30
    weight_mutate_power     = 0.5
    weight_mutate_rate      = 0.8
    weight_replace_rate     = 0.1

    [DefaultSpeciesSet]
    compatibility_threshold = 3.0

    [DefaultStagnation]
    species_fitness_func = max
    max_stagnation       = 20
    species_elitism      = 2

    [DefaultReproduction]
    elitism            = 2
    survival_threshold = 0.2

    */

}
