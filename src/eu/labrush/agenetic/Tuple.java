package eu.labrush.agenetic;

public class Tuple<T, S> {
    public T fst ;
    public S snd ;

    public Tuple(T fst, S snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "fst=" + fst +
                ", snd=" + snd +
                '}';
    }
}
