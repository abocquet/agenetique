package eu.labrush.NEAT.utils;

public class Indexer {

    int c = 0 ;

    public Indexer(int i) {
        c = i ;
    }

    public int next(){
        c++ ;
        return c ;
    }

    public int current(){
        return c ;
    }
}
