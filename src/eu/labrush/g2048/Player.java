package eu.labrush.g2048;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.g2048.GameEngine.Game2048Interface;
import eu.labrush.neural.NeuralNetwork;

import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Player extends AbstractFellow {

    private PlayerWeightEncoder we ;
    private double score = 0 ;

    public Player(int[] dna, int DNASIZE, PlayerWeightEncoder we) {
        super(dna, DNASIZE);
        this.we = we ;
    }

    public Player(int DNASIZE, int DNACARD, PlayerWeightEncoder we) {
        super(DNASIZE, DNACARD);
        this.we = we;
    }

    @Override
    protected long calcFitness() {
         return (long) this.score ;
    }

    public double[][][] getWeights() {
        return this.we.getWeights(this);
    }

    public double getScore() {
        return score;
    }

    public void playWith(Game2048Interface game){ playWith(game, false); }

    public void playWith(Game2048Interface game, boolean temporize) {

        int inputs = we.getInputs() ;

        NeuralNetwork brain = new NeuralNetwork(inputs);
        double[][][] weights = we.getWeights(this);

        for(int i = 0 ; i < weights.length ; i++){
            brain.addLayer(weights[i]);
        }

        int[] tiles = game.getTiles() ;
        int lastmove = 0 ;
        int lastscore = 0 ;
        int scorecount = 0 ; // compte le nombre de coups qui n'ont pas fait evoluer le score

        while(!game.hasLost()){
            double[] data = new double[inputs] ;

            for (int i = 0; i < 16 ; i++) {
                double[] bits = intToBinary(log2(tiles[i]), we.bit_per_tile);

                for (int j = 0; j < we.bit_per_tile ; j++) {
                    data[we.bit_per_tile * i + j] = bits[j];
                }
            }

            // on transmet le dernier coup joue
            data[data.length - 2] = lastmove % 2 == 0 ? 0 : 1 ;
            data[data.length - 1] = (lastmove / 2) == 0 ? 0 : 1 ;


            int[] output = Arrays.stream(
                    brain.compute(data)
            ).mapToInt(e -> e < 0.5 ? 0 :1).toArray() ;

            int id = output[0] + 2 * output[1]  ;

            if (lastscore == game.getScore()) { scorecount++ ; } else { scorecount = 0; lastscore = game.getScore(); }
            if(scorecount > 3) break; // si le score stagne, on joue un coup impossible donc on s'arrete

            switch (id) {
                case 0:
                    game.up();
                    break;
                case 1:
                    game.left();
                    break;
                case 2:
                    game.right();
                    break;
                default:
                    game.down();
                    break;
            }

            if(temporize){
                try {
                    game.repaint();
                    System.out.println("Coup: " + id);
                    sleep(100) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.score = game.getScore() ;

    }

    private int log2(int n) {
        int c = 0 ;
        int acc = 1 ;
        while(n <= acc){
            acc*= 2 ;
            c++ ; // ;)
        }
        return c ;
    }

    static private double[] intToBinary(int n, int array_size){
        double[] bits = new double[array_size];
        for (int i = array_size - 1; i >= 0; i--) {
            bits[i] = (n & (1 << i)) != 0 ? 0 : 1;
        }
        return bits ;
    }
}
