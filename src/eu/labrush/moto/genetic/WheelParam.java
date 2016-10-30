package eu.labrush.moto.genetic;

class WheelParam {
    public double speed, size ;
    public int torque ;

    public WheelParam(int[] dna, int start) {
        readFromDna(dna, start);
    }

    public void readFromDna(int[] dna, int start){
        this.size = 0.25 * (double)(1 + readIntFromDNA(dna, start, 2));
        this.speed = Math.PI * (double)(1 + readIntFromDNA(dna, start + 2, 2));
        this.torque = 800 * (1 + readIntFromDNA(dna, start + 4, 2));
    }

    /**
     * @param dna
     * @param start
     * @param length
     * @return binary input as int
     */
    private int readIntFromDNA(int[] dna, int start, int length) {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = start ; i < start + length ; i++){
            if(dna[i] == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }
}
