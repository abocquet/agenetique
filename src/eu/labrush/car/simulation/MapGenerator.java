package eu.labrush.car.simulation;

import eu.labrush.agenetic.Tuple;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;

public class MapGenerator {

    private double minX, minY, maxX, maxY ;

    private ArrayList<Rectangle2D> road = new ArrayList<>() ;
    private Point2D startPoint = null ;

    int maille = 60 ;

    private ArrayList<Tuple<Point2D.Double, ArrayList<Point2D>>> maps = new ArrayList<>() ;

    public MapGenerator(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        int width = (int) ((maxX - minX) / maille), height = (int) (maxY - minY) / maille ;
        double a = Math.min(width -1, height-1) ;
        a -= .1 ;

        ArrayList<Point2D> points;

        points = new ArrayList<>();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(a, 0));
        points.add(new Point2D.Double(a, a));
        points.add(new Point2D.Double(a/2, a/2));
        points.add(new Point2D.Double(0, a/2));

        maps.add(new Tuple<>(
                new Point2D.Double(minX + maille * 5, minY + maille / 2),
                points
        ));


        points = new ArrayList<>();
        points.add(new Point2D.Double(0, a));
        points.add(new Point2D.Double(a, a));
        points.add(new Point2D.Double(a, a/2));
        points.add(new Point2D.Double(2*a/3, a/2));
        points.add(new Point2D.Double(0, 0));

        maps.add(new Tuple<>(
                new Point2D.Double(minX + 5 * maille, minY + 9.5 * maille),
                points
        ));

    }

    private ArrayList<Point2D> getRandomGrid(int nbPoints, int width, int height){
        ArrayList<Point2D> points = new ArrayList<>();

        double x0 = 0, y0 = 0 ;

        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(3, 0));
        points.add(new Point2D.Double(3, 2));

        for (int i = 3; i < nbPoints ; i++) {
            double x =  Math.floor(Math.random() * width) ;
            double y =  Math.floor(Math.random() * height) ;

            x0 += x ;
            y0 += y ;

            points.add(new Point2D.Double(x, y));
        }

        x0 /= nbPoints ;
        y0 /= nbPoints ;

        Point2D center = new Point2D.Double(x0, y0);

        points.sort(Comparator.comparingDouble(e -> arg(e, center)));

        return points ;
    }

    private double arg(Point2D p1, Point2D p0) {
        double x = p1.getX(), y = p1.getY();
        double x0 = p0.getX(), y0 = p0.getY();


        if(x == x0){ return 0 ; }

        double arg = Math.atan((y - y0)/(x - x0));

        if (x - x0 < 0) {
            arg += Math.PI ;
        }

        return arg ;

    }

    public ArrayList<Line2D> getRandomizedGrid(int nbPoints, int maille) {

        int width = (int) ((maxX - minX) / maille), height = (int) (maxY - minY) / maille ;

        ArrayList<Point2D> points = getRandomGrid(nbPoints, width, height);
        startPoint = new Point2D.Double(minX + maille / 2, minY + maille / 2);

        ArrayList<Line2D> guides = new ArrayList<>();
        ArrayList<Line2D> lines = new ArrayList<>();

       for (int i = 0; i < nbPoints ; i++) {
            double x1 = points.get(i).getX(), y1 = points.get(i).getY() ;
            double x2 = points.get((i+1)%nbPoints).getX(), y2 = points.get((i+1)%nbPoints).getY() ;

            x1 = x1 * maille + minX ;
            x2 = x2 * maille + minX ;

            y1 = y1 * maille + minY ;
            y2 = y2 * maille + minY ;

            guides.add(new Line2D.Double(x1, y1, x2, y2));
            //lines.add(new Line2D.Double(x1, y1, x2, y2));
        }

        road.clear();

        lines.addAll(loadLines(guides, maille));

        return lines ;

    }

    public ArrayList<Line2D> getSquareGrid(){

        ArrayList<Line2D> guides = new ArrayList<>();
        ArrayList<Line2D> lines = new ArrayList<>();

        int mapIndex = (int) (Math.random() * maps.size());
        ArrayList<Point2D> points = maps.get(mapIndex).snd ;
        this.startPoint = maps.get(mapIndex).fst ;

        for (int i = 0; i < points.size() ; i++) {
            double x1 = points.get(i).getX(), y1 = points.get(i).getY() ;
            double x2 = points.get((i+1)% points.size()).getX(), y2 = points.get((i+1)%points.size()).getY() ;

            x1 = x1 * maille + minX ;
            x2 = x2 * maille + minX ;

            y1 = y1 * maille + minY ;
            y2 = y2 * maille + minY ;

            guides.add(new Line2D.Double(x1, y1, x2, y2));
            //lines.add(new Line2D.Double(x1, y1, x2, y2));
        }

        road.clear();

        lines.addAll(loadLines(guides, maille));

        return lines ;
    }

    private ArrayList<Line2D> loadLines(ArrayList<Line2D> guides, int maille){
        ArrayList<Line2D> lines = new ArrayList<>();
        int width = (int) (maxX - minX) / maille, height = (int) (maxY - minY) / maille ;
        boolean[][] grid = new boolean[width][height] ;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Rectangle2D rect = new Rectangle2D.Double(minX + x * maille, minY + y * maille, maille, maille);

                if (guides.stream().filter(rect::intersectsLine).count() > 0) {
                    grid[x][y] = true;
                    road.add(rect);
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((x == 0 || !grid[x - 1][y]) && grid[x][y])
                    lines.add(new Line2D.Double(minX + x * maille, minY + y * maille, minX + x * maille, minY + (y + 1) * maille));
                if ((x == width - 1 || !grid[x + 1][y]) && grid[x][y])
                    lines.add(new Line2D.Double(minX + (x + 1) * maille, minY + y * maille, minX + (x + 1) * maille, minY + (y + 1) * maille));
                if ((y == 0 || !grid[x][y - 1]) && grid[x][y])
                    lines.add(new Line2D.Double(minX + x * maille, minY + y * maille, minX + (x + 1) * maille, minY + y * maille));
                if ((y == height - 1 || !grid[x][y + 1]) && grid[x][y])
                    lines.add(new Line2D.Double(minX + x * maille, minY + (y + 1) * maille, minX + (x + 1) * maille, minY + (y + 1) * maille));
            }
        }

        return lines ;
    }

    public ArrayList<Rectangle2D> getRoad() {
        return road;
    }

    public Point2D getStart(){
        return this.startPoint ;
    }

    public Line2D getFinishLine(){
        double x = startPoint.getX() - maille ;
        double y = startPoint.getY() ;

        return new Line2D.Double(x, y + maille / 2, x, y - maille / 2);
    }

}
