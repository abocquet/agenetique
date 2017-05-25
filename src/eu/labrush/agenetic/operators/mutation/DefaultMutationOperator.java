package eu.labrush.agenetic.operators.mutation;

import eu.labrush.agenetic.AbstractFellow;
import eu.labrush.agenetic.AbstractFellowFactory;
import eu.labrush.agenetic.operators.MutationInterface;

public class DefaultMutationOperator implements MutationInterface {

    public void mutate(AbstractFellow[] population, double pmutation, AbstractFellowFactory factory) {

        int DNACARD = factory.getDNACard();

        for (AbstractFellow f : population) {
            for (int i = 0; i < f.getDNASIZE(); i++) {
                /*
                "flip": on choisit de muter
                "mutation" on a une mutation
                "indentique" le gene n'a pas changé après tirage

                p(mutation) = p(flip inter non(identique))
                or flip et indentique sont indépendants
                d'ou p(mutation) = p(flip) * p(non(identique))
                ie p(flip) = p(mutation) / p(non(non(identique)) = p(mutation) / (1 - 1 / DNACARD))

                finalement on passe de l'autre coté pour éviter de manipuler des flottants)
                 */
                if (Math.random() * (DNACARD - 1) <= pmutation * DNACARD)
                    f.setDNA(i, ((int) (Math.random() * 10000) % DNACARD));
            }
        }
    }
}
