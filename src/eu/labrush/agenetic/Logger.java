package eu.labrush.agenetic;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Logger {

    private PrintWriter writer = null ;
    protected AbstractNature nature = null;

    private String firstLine ;

    public Logger(String filename, AbstractNature nature){
        new Logger(filename, nature, "generation;maximum fitness;minimum fitness;average fitness;");
    }

    protected Logger(String filename, AbstractNature nature, String firstLine) {
        try {
            this.firstLine = firstLine ;
            this.nature = nature ;
            this.writer= new PrintWriter(filename, "UTF-8");

            this.writer.println(firstLine);
            this.writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Logger() {
    }

    public void log(){
        if(writer != null && nature != null){
            writer.print(nature.getGenerationNumber());
            writer.print(";");
            writer.println(getCSVStats());

            writer.flush();
        }
    }

    protected String getCSVStats(){

        AbstractFellow[]  population = nature.getPopulation() ;
        BigInteger min = population[0].getFitness() ;
        BigInteger max ;

        BigDecimal sum = new BigDecimal(min) ;
        max = min ;

        for(int i = 0, c = population.length ; i < c ; i++){
            BigInteger f = population[i].getFitness();
            sum = sum.add(new BigDecimal(f)) ;

            if(min.compareTo(f) > 0) min = f ;
            if(max.compareTo(f) < 0) max = f ;
        }

        return max + ";" + min + ";" + sum.divide(BigDecimal.valueOf(population.length)) + ";" ;

    }

    public String getFirstLine() {
        return firstLine;
    }
}
