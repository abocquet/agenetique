package eu.labrush.moto;

import eu.labrush.moto.genetic.Moto;
import eu.labrush.moto.genetic.MotoFactory;
import eu.labrush.moto.genetic.Nature;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

import java.util.ArrayList;

public class GraphicInterface extends JFrame {

    private CardLayout cl = new CardLayout();
    private JPanel presentationPanel = new JPanel();
    private JPanel progressPanel = new JPanel();

    private JTree generationsTree = new JTree();
    private JButton newGenButton = new JButton("Nouvelle génération");
    private JCheckBox keepGroundCheckbox = new JCheckBox("Conserver le  terrain");

    private JProgressBar progressBar = new JProgressBar();
    private JLabel progressLabel = new JLabel("0%");

    DefaultMutableTreeNode racine ;

    Nature nature ;
    ArrayList<int[][]> genes = new ArrayList<>();

    public GraphicInterface(){
        this.setTitle("Genetic Motos");
        this.setSize(900, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Container content = this.getContentPane() ;
        content.setLayout(cl);

        /** On parametre la nature */

            Moto.setGroundDesigner(new GroundDesigner().setNbBlocks(5000).setDist(10_000));
            Moto.setPeakNumber(8);

            nature = new Nature(20, 0.5, 0.05, new MotoFactory());

        /** On parametre la fenetre qui présente les options et les générations **/
            presentationPanel.setLayout(new BorderLayout());

            JPanel optionPanel = new JPanel();
            presentationPanel.add(optionPanel, BorderLayout.WEST);

            optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
            optionPanel.add(buildTree());
            optionPanel.add(keepGroundCheckbox);
            optionPanel.add(newGenButton);

            presentationPanel.add(new Label("Moto !"), BorderLayout.CENTER);

            content.add(presentationPanel, "presentation");

        /** Puis la fenetre de progression dans l'évolution d'une génération **/

            nature.setObserver(str -> {
                progressBar.setValue(Integer.valueOf(str));
                progressLabel.setText(str + "%");

                if(str.equals("100")){
                    progressBar.setValue(0);
                    progressLabel.setText("Initialisation");
                    saveGenes();

                    cl.show(content, "presentation");
                }
            });

            progressPanel.setLayout(new GridBagLayout());
            progressPanel.add(progressBar);
            progressPanel.add(progressLabel);

            content.add(progressPanel, "progression");

            newGenButton.addActionListener(e -> {
                cl.show(content, "progression");

                new Thread(() -> {
                    nature.evolve();
                    saveGenes();
                }).start();
            });

        this.setVisible(true);
    }

    private JScrollPane buildTree(){
        this.racine = new DefaultMutableTreeNode("Nature");

        generationsTree = new JTree(racine);
        generationsTree.setEnabled(true);
        generationsTree.setRootVisible(false);

        generationsTree.addTreeSelectionListener(event -> {
            if(generationsTree.getLastSelectedPathComponent() != null){
                System.out.println(generationsTree.getLastSelectedPathComponent());
            }
        });

        return new JScrollPane(generationsTree) ;
    }

    private void saveGenes(){
        int currentPop[][] = new int[nature.getPOPSIZE()][Moto.AskedDNASIZE] ;
        DefaultMutableTreeNode rep = new DefaultMutableTreeNode("Génération n°" + (genes.size() + 1));

        for(int i = 0, c = nature.getPOPSIZE() ; i < c ; i++){
            currentPop[i] = nature.getPopulation()[i].getDna() ;
            //DefaultMutableTreeNode rep2 = new DefaultMutableTreeNode("Fellow #" + i + "(fitness: " + nature.getPopulation()[i].getFitness() + ")");
            DefaultMutableTreeNode rep2 = new DefaultMutableTreeNode("Fellow #" + i );
            rep.add(rep2);
        }

        genes.add(currentPop);
        racine.add(rep);
        ((DefaultTreeModel)generationsTree.getModel()).reload();

    }
}
