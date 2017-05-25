package eu.labrush.car.simulation;

import javax.swing.*;

public class Renderer extends JFrame {

    private int sleeptime = 1000 / 60;

    private World world ;

    private WorldPanel pan ;
    private Thread t = null;

    public Renderer(World world) {
        this.pan = new WorldPanel(world);
        this.world = world ;

        this.setTitle("Car Race !");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JMenuBar menu = new JMenuBar();
        JMenu vitesse = new JMenu("Vitesse");

        for (int i = 1; i <= 5; i++) {
            JMenuItem v = new JMenuItem("x" + i);
            int finalI = i;
            v.addActionListener(e -> sleeptime = 1000 / (60 * finalI));
            vitesse.add(v);
        }

        JMenuItem max = new JMenuItem("Vitesse maximum");
        max.addActionListener(e -> sleeptime = 0);
        vitesse.add(max);


        menu.add(vitesse);
        this.setJMenuBar(menu);
        this.setContentPane(this.pan);
        this.setVisible(true);
        this.setResizable(false);


    }

    public void start() {
        if (t == null) {
            t = new Thread(() -> {
                while (true) {
                    world.step(1000 / 60);
                    pan.repaint();

                    try {
                        Thread.sleep(sleeptime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            t.start();
        }
    }

    public void stop() {
        if (t != null) {
            t.interrupt();
            t = null;
        }
    }

}