package eu.labrush.agenetic.traveller.data;

public class Berlin52 implements PointSet {


    @Override
    public Point[] getPoints() {

        int[] x = {565,25,345,945,845,880,25,525,580,650,1605,1220,1465,1530,845,725,145,415,510,560,300,520,480,835,975,1215,1320,1250,660,410,420,575,1150,700,685,685,770,795,720,760,475,95,875,700,555,830,1170,830,605,595,1340};
        int[] y = {575,185,750,685,655,660,230,1000,1175,1130,620,580,200,5,680,370,665,635,875,365,465,585,415,625,580,245,315,400,180,250,555,665,1160,580,595,610,610,645,635,650,960,260,920,500,815,485,65,610,625,360,725,245};

        Point[] list = new Point[x.length] ;

        for(int i = 0, c = x.length ; i < c ; i++){
            list[i] = new Point(x[i], y[i]);
        }

        return list;
    }

    @Override
    public String introduceYourself() {
        return "Hi, I'm Berlin52, I am the list of 52 locations in Berlin and the best know solution to my set is 7542" ;
    }


}
