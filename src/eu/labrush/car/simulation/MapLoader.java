package eu.labrush.car.simulation;

import eu.labrush.agenetic.Tuple;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapLoader {

    private double minX, minY, maxX, maxY ;

    private ArrayList<Rectangle2D> road = new ArrayList<>() ;
    private Point2D startPoint = null ;

    int maille = 60 ;

    private ArrayList<Tuple<Point2D.Double, ArrayList<Point2D>>> maps = new ArrayList<>() ;

    public MapLoader(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public ArrayList<Line2D> loadRandomMap(){

        File mapFolder=  new File("./maps");
        File[] maps = mapFolder.listFiles();

        int n ;
        do {
            n = (int) (2 * maps.length * Math.random()) % maps.length;
        } while(!maps[n].getName().contains(".map"));

        return loadMap(maps[n].getAbsolutePath());

    }

    public ArrayList<Line2D> loadMap(String filepath) {

        ArrayList<Line2D> lines = new ArrayList<>();
        double width = maxX - minX, height = maxY - minY ;

        Pattern point_pattern = Pattern.compile("(\\w) = \\((\\d+), (\\d+)\\)");
        Pattern path_pattern = Pattern.compile("path:(\\w+)");
        Pattern source_pattern = Pattern.compile("source:\\((\\d+), (\\d+)\\)");

        String line;

        double cx = 0, cy = 0 ; //Le nombre de lignes et de colonnes en lequel on divise l'espace

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            while ((line = br.readLine()) != null) {

                Matcher point_matcher = point_pattern.matcher(line);

                if (point_matcher.matches()) {
                    cx = Math.max(cx, Double.parseDouble(point_matcher.group(2)));
                    cy = Math.max(cy, Double.parseDouble(point_matcher.group(3)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            HashMap<String, Point2D> points = new HashMap<>();

            while ((line = br.readLine()) != null) {

                Matcher point_matcher = point_pattern.matcher(line);
                Matcher path_matcher = path_pattern.matcher(line);
                Matcher source_matcher =source_pattern.matcher(line);

                if(point_matcher.matches()){
                    points.put(
                            point_matcher.group(1),
                            new Point2D.Double(
                                    Double.parseDouble(point_matcher.group(2)) / cx * width + minX,
                                    Double.parseDouble(point_matcher.group(3)) / cy * height + minY
                            )
                    );
                } else if (path_matcher.matches()) {

                    String path = path_matcher.group(1);

                    for (int i = 0, c = path.length(); i < c; i++) {
                        lines.add(new Line2D.Double(
                                points.get(String.valueOf(path.charAt(i))),
                                points.get(String.valueOf(path.charAt((i+1)%c)))
                        ));
                    }
                    

                } else if (source_matcher.matches()) {
                    startPoint = (new Point2D.Double(
                            Double.parseDouble(source_matcher.group(1)) + minX,
                            Double.parseDouble(source_matcher.group(2)) + minY
                    ));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines ;
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
