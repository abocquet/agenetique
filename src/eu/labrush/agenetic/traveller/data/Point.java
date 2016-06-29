package eu.labrush.agenetic.traveller.data;

public class Point {
    public int x ;
    public int y ;

    public Point(int x, int y) {
        this.x = x ;
        this.y = y ;
    }

    public static int distance(Point p1, Point p2){
        int dx = p1.x - p2.x ;
        int dy = p1.y - p2.y ;
        return (int) Math.sqrt((double) (dx*dx + dy*dy)) ;
    }
}
