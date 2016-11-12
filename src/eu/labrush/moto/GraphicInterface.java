package eu.labrush.moto;

import eu.labrush.moto.genetic.Moto;
import eu.labrush.moto.genetic.MotoFactory;
import eu.labrush.moto.genetic.Nature;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

class GraphicInterface extends JFrame {

    private CardLayout cl = new CardLayout();
    private JPanel optionPanel = new JPanel();
    private JPanel progressPanel = new JPanel();

    private JTree generationsTree = new JTree();
    private JButton newGenButton = new JButton("Nouvelle génération");
    private JSlider genSlider = new JSlider(1, 100);
    private JCheckBox keepGroundCheckbox = new JCheckBox("Conserver le  terrain");

    private JProgressBar progressBar = new JProgressBar();
    private JLabel progressLabel = new JLabel("Initialisation");

    private int generationsLeft = 0 ; // Le nombre d'évolutions à calculer

    private DefaultMutableTreeNode racine ;

    private Nature nature ;
    private ArrayList<int[][]> genes = new ArrayList<>();
    private ArrayList<GroundDesigner> grounds = new ArrayList<>();

    public GraphicInterface(){
        this.setTitle("Genetic Motos");
        this.setSize(250, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(50, 100);

        Container content = this.getContentPane() ;
        content.setLayout(cl);

        /* On parametre la nature */

            Moto.setPeakNumber(8);
            nature = new Nature(20, 0.5, 0.05, new MotoFactory(), new GroundDesigner());
        nature.getGroundDesigner().setOffset(new Vector2(-5, -5));

        /* On parametre la fenetre qui présente les options et les générations **/

            optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.PAGE_AXIS));
            optionPanel.add(buildTree());
            optionPanel.add(keepGroundCheckbox);
            optionPanel.add(genSlider);
            optionPanel.add(newGenButton);

        genSlider.addChangeListener(e -> {
            newGenButton.setText("Avancer de " + ((JSlider)e.getSource()).getValue()  + " générations");
        });
        genSlider.setValue(1);

        keepGroundCheckbox.setSelected(true);

            content.add(optionPanel, "option");

        /* Puis la fenetre de progression dans l'évolution d'une génération **/

            progressPanel.setLayout(new GridBagLayout());
            JPanel subPanel = new JPanel();
            subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
            subPanel.add(progressBar);
            subPanel.add(progressLabel);

            progressPanel.add(subPanel);
            content.add(progressPanel, "progression");

            newGenButton.addActionListener(e -> {
                generationsLeft = genSlider.getValue();
                cl.show(content, "progression");

                new Thread(() -> {
                    if(!keepGroundCheckbox.isSelected()){
                        nature.getGroundDesigner().generateGround();
                    }

                    nature.evolve();
                    saveGenes();
                }).start();
            });

            nature.setObserver(str -> {

                if(str.equals("done")){
                    progressBar.setValue(0);
                    progressLabel.setText("Initialisation");

                    saveGenes();
                    generationsLeft-- ;

                    if(generationsLeft > 0){
                        new Thread(() -> {
                            if(!keepGroundCheckbox.isSelected()){
                                nature.getGroundDesigner().generateGround();
                            }

                            nature.evolve();
                        }).start();
                    } else {
                        cl.show(content, "option");
                    }
                } else {
                    progressBar.setValue(Integer.valueOf(str));
                    progressLabel.setText("<html>" + generationsLeft + " générations restantes <br/>Actuelle: " + str + "%</html>");
                }
            });

        this.setVisible(true);
    }

    private JScrollPane buildTree(){
        this.racine = new DefaultMutableTreeNode("Nature");

        generationsTree = new JTree(racine);
        generationsTree.setEnabled(true);
        generationsTree.setRootVisible(false);

        ArrayList<int[][]> genes = this.genes ;

        generationsTree.addTreeSelectionListener(event -> {
            if(generationsTree.getLastSelectedPathComponent() != null){
                try {
                    TreePath path = generationsTree.getSelectionPath();

                    int generation = new Scanner(
                                    path.getPathComponent(1).toString()
                            ).useDelimiter("\\D+").nextInt();

                    int fellow = new Scanner(
                            path.getPathComponent(2).toString()
                    ).useDelimiter("\\D+").nextInt();

                    Moto moto = new Moto(genes.get(generation - 1)[fellow] );
                    moto.setGroundDesigner(grounds.get(generation - 1));
                    World world = moto.getSim();

                    Renderer2D simulationWindow = new Renderer2D();
                    simulationWindow.setWorld(world);
                    simulationWindow.setSize(800, 600);
                    simulationWindow.focusOn(world.getBody(0));
                    simulationWindow.start();
                    simulationWindow.setVisible(true);

                } catch (Exception e){
                    System.err.println(e.toString());
                }
            }
        });

        return new JScrollPane(generationsTree) ;
    }

    private void saveGenes(){
        int currentPop[][] = new int[nature.getPOPSIZE()][Moto.AskedDNASIZE] ;
        DefaultMutableTreeNode rep = new DefaultMutableTreeNode("Génération #" + (genes.size()));

        for(int i = 0, c = nature.getPOPSIZE() ; i < c ; i++){
            currentPop[i] = nature.getPopulation()[i].getDna() ;
            DefaultMutableTreeNode rep2 = new DefaultMutableTreeNode("Fellow #" + i + "(fitness: " + nature.getPopulation()[i].getFitness() + ")");
            //DefaultMutableTreeNode rep2 = new DefaultMutableTreeNode("Fellow #" + i );
            rep.add(rep2);
        }

        genes.add(currentPop);
        grounds.add(nature.getGroundDesigner());
        racine.add(rep);
        ((DefaultTreeModel)generationsTree.getModel()).reload();

    }
}
