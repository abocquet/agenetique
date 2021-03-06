package eu.labrush.agenetic;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class Logger {

    private PrintWriter stateWriter  ;
    private PrintWriter solutionWriter ;
    protected AbstractNature nature ;

    public Logger(String folder, String filename, AbstractNature nature){
        new Logger(folder, filename, nature, "generation;maximum fitness;minimum fitness;average fitness;");
    }

    protected Logger(String folder, String filename, AbstractNature nature, String firstLine) {
        try {
            this.nature = nature ;

            this.stateWriter = new PrintWriter(folder + filename, "UTF-8");
            this.solutionWriter = new PrintWriter(folder + "solutions-" + filename, "UTF-8");

            this.stateWriter.println(firstLine);
            this.stateWriter.flush();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(nature + " " + stateWriter + " " + solutionWriter);
    }

    public void log(){ log(false); }

    public void log(boolean logSolutions){
        if(stateWriter != null && solutionWriter != null && nature != null){
            stateWriter.print(nature.getGenerationNumber());
            stateWriter.print(";");
            stateWriter.println(getCSVStats());
            stateWriter.flush();

            if(logSolutions) {
                solutionWriter.println(nature.getBest().showDNA());
                solutionWriter.flush();
            }
        }
    }

    protected String getCSVStats(){

        AbstractFellow[]  population = nature.getPopulation() ;
        long min = population[0].getFitness() ;
        long max ;

        BigDecimal sum = new BigDecimal(min) ;
        max = min ;

        for (AbstractFellow aPopulation : population) {
            long f = aPopulation.getFitness();
            sum = sum.add(new BigDecimal(f));

            if (min > f) min = f;
            if (max < f) max = f;
        }

        return max + ";" + min + ";" + sum.divide(BigDecimal.valueOf(population.length), 10) + ";" ;

    }
}
