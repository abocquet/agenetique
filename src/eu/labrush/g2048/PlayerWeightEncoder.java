package eu.labrush.g2048;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.neural.AbstractWeightEncoder;

/**
 * 16 cases
 * en théorie jusqu'à 16 valeurs possibles + case vide mais
 * on se limite de 2 à 2048 = 2^11 + case vide
 * 3072 entrée au réseau
 */
public class PlayerWeightEncoder extends AbstractWeightEncoder {

    int bit_per_tile = 4 ; //valeur maximale de 2^(max_tiles + 1) par cases: vide: 0 -> 0, 2 -> 1, 4 -> 2, 8 -> 3... 2048 -> 11 donc on a besoin de compter jusqu'à 16 soit 4 bits

    public PlayerWeightEncoder() {
        hidden_layers = 2 ;
        node_per_layer = 100 ;
        bits_per_weight = 2 ;
        outputs = 2 ;
        detectors =  getInputs();
    }

    @Override
    public int requiredDNASize() {
        return node_per_layer * (detectors + node_per_layer * hidden_layers * outputs) * bits_per_weight;
    }

    @Override
    public double readWeight(int pos, AbstractFellow f) {
        return f.getDNA(pos) + f.getDNA(pos + 1) - 1 ;
    }

    @Override
    public double readBias(int pos, AbstractFellow f) {
        return f.getDNA(pos) + f.getDNA(pos + 1) - 1 ;
    }


    public int getInputs(){
        return 16 * bit_per_tile + 2 ; // 2 bits pour connaitre le coup précedent
    }

}
