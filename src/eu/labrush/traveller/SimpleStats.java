package eu.labrush.traveller;

/**
 * An elementary statistic functions set
 */
public class SimpleStats {

    static double avg(double[] lst){
        double s = 0 ;
        for (int i = 0; i < lst.length; i++) {
            s += lst[i];
        }

        s /= lst.length ;
        return s ;
    }

    static double ecartType(double[] lst){
        double avg = avg(lst) ;
        double sigma = 0 ;

        for (int i = 0; i < lst.length; i++) {
            sigma += Math.pow(lst[i] - avg, 2);
        }

        sigma /= lst.length ;

        return sigma ;
    }

}
