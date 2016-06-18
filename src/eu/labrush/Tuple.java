package eu.labrush;

/**
 * Created by adrienbocquet on 24/04/2016.
 */
public class Tuple<T, S> {
    public T fst ;
    public S snd ;

    public Tuple(T fst, S snd) {
        this.fst = fst;
        this.snd = snd;
    }
}
