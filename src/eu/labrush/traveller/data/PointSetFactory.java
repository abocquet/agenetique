package eu.labrush.traveller.data;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PointSetFactory {

    ArrayList<String> problems = new ArrayList<>();

    public PointSetFactory() {

        File folder = new File("problems/");

        for (File file : folder.listFiles()) {
            if (file.isFile() && !file.isHidden()) {
                problems.add(file.getName().split("\\.")[0]);
            }
        }

    }

    public PointSet getSet(String name){
        if(!problems.contains(name)){
            System.err.println("No problem named " + name);
            return null ;
        }

        File file = new File("problems/" + name + ".xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = null;
        Document document = null ;

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document =  documentBuilder.parse(file);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] xs = document.getElementsByTagName("x").item(0).getTextContent().split(";");
        int x[] = new int[xs.length];
        for (int i = 0; i < xs.length; i++) {
            x[i] = (int) Double.parseDouble(xs[i]);
        }

        String[] ys = document.getElementsByTagName("x").item(0).getTextContent().split(";");
        int y[] = new int[ys.length];
        for (int i = 0; i < ys.length; i++) {
            y[i] = (int)Double.parseDouble(ys[i]) ;
        }

        String desc = document.getElementsByTagName("comment").item(0).getTextContent();
        int minDist = Integer.parseInt(document.getElementsByTagName("best").item(0).getTextContent());

        return new PointSet(name, desc, minDist, x, y);
    }

    public ArrayList<String> getProblems() {
        return problems;
    }
}
