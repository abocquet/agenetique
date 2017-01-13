package eu.labrush.moto.genetic;

import eu.labrush.agenetic.AbstractFellow;

class WheelParam {
    public double speed, size ;
    public int torque ;

    public WheelParam(AbstractFellow f, int start) {
        readFromDna(f, start);
    }

    public void readFromDna(AbstractFellow f, int start){
        this.size = 0.25 * (double)(1 + readIntFromDNA(f, start, 2));
        this.speed = 0.8 * Math.PI * (double)(1 + readIntFromDNA(f, start + 2, 2));
        this.torque = 600 * (1 + readIntFromDNA(f, start + 4, 2));
    }

    /**
     * @return binary input as int
     */
    private int readIntFromDNA(AbstractFellow f, int start, int length) {
        int x = 0 ;
        int tmp = 1 ;
        for(int i = start ; i < start + length ; i++){
            if(f.getDNA(i) == 1) {
                x += tmp ;
            }
            tmp *= 2 ;
        }

        return x;
    }
}
