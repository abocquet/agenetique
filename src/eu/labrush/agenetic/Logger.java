package eu.labrush.agenetic;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class Logger {

    private PrintWriter stateWriter = null ;
    private PrintWriter solutionWriter = null ;
    protected FellowManagerInterface manager = null;

    public Logger(String folder, String filename, FellowManagerInterface manager){
        new Logger(folder, filename, manager, "generation;maximum fitness;minimum fitness;average fitness;");
    }

    protected Logger(String folder, String filename, FellowManagerInterface manager, String firstLine) {
        try {
            this.manager = manager ;

            this.stateWriter = new PrintWriter(folder + filename, "UTF-8");
            this.solutionWriter = new PrintWriter(folder + "solutions-" + filename, "UTF-8");

            this.stateWriter.println(firstLine);
            this.stateWriter.flush();

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void log(){ log(false); }

    public void log(boolean logSolutions){
        if(stateWriter != null && solutionWriter != null && manager != null){
            stateWriter.print(manager.getGenerationNumber());
            stateWriter.print(";");
            stateWriter.println(getCSVStats());
            stateWriter.flush();

            if(logSolutions && manager instanceof AbstractNature) {
                solutionWriter.println(((AbstractNature)manager).getBest().showDNA());
                solutionWriter.flush();
            }
        }
    }

    protected String getCSVStats(){

        FellowInterface[]  population = manager.getPopulation() ;
        long min = population[0].getFitness() ;
        long max ;

        BigDecimal sum = new BigDecimal(min) ;
        max = min ;

        for (FellowInterface aPopulation : population) {
            long f = aPopulation.getFitness();
            sum = sum.add(new BigDecimal(f));

            if (min > f) min = f;
            if (max < f) max = f;
        }

        return max + ";" + min + ";" + sum.divide(BigDecimal.valueOf(population.length), 10) + ";" ;

    }
}
