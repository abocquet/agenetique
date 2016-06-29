package eu.labrush.agenetic.numfun;

public interface NumFunInterface {

    Object getArguments(int[] dna);
    int getY(int[] dna);

    /**
     * @return the number of bits you need to code the definition set
     */
    int getDNASIZE();

}
