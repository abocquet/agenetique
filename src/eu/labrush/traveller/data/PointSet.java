package eu.labrush.traveller.data;

public class PointSet {

    private String name, desc ;
    private long minDist ;

    private Point[] points ;
    private double[][] distances ;

    public PointSet(String name, String desc, long minDist, int[] x, int[] y) {
        this.name = name;
        this.desc = desc;
        this.minDist = minDist;

        points = new Point[x.length] ;

        for(int i = 0, c = x.length ; i < c ; i++){
            points[i] = new Point(x[i], y[i]);
        }

        distances = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            distances[i] = new double[points.length];

            for (int j = 0; j < distances[i].length; j++) {
                distances[i][j] = i == j ? 0 : -1 ;
            }
        }
    }

    public Point[] getPoints() {
        return points ;
    }

    public String introduceYourself() {
        return "Hello, I'm " + getName() + ", " + getDesc() + " my best known score is " + getMinDist() ;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public long getMinDist() {
        return minDist;
    }

    public double distBetween(int i1, int i2){

        return Point.distance(points[i1], points[i2]) ;

        /*if(distances[i1][i2] < 0){
            double distance = Point.distance(points[i1], points[i2]);
            distances[i1][i2] = distance ;
            distances[i2][i1] = distance ;
        }

        return distances[i1][i2] ;*/

    }

}
