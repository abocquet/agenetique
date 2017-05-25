package eu.labrush.agenetic;

public class Triple<S,R,T>  {
    public S fst ;
    public R snd ;
    public T thd ;

    public Triple(S fst, R snd, T thd) {
        this.fst = fst;
        this.snd = snd;
        this.thd = thd;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "fst=" + fst +
                ", snd=" + snd +
                ", thd=" + thd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) return false;
        Triple to = (Triple) o;
        return
                this.fst.equals(to.fst) &&
                this.snd.equals(to.snd) &&
                        this.thd.equals(to.thd);
    }

}
