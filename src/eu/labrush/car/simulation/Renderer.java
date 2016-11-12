package eu.labrush.car.simulation;

import javax.swing.*;

public class Renderer extends JFrame {


    public static int FRAMERATE = 60;

    private WorldPanel pan ;
    private World world ;

    private Thread t = null;

    public Renderer(World world) {
        this.pan = new WorldPanel(world);
        this.world = world ;

        this.setTitle("Car Race !");
        this.setSize(720, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(this.pan);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void start() {
        if (t == null) {
            t = new Thread(() -> {
                while (true) {
                    world.step(1000 / FRAMERATE);
                    pan.repaint();

                    try {
                        Thread.sleep(1000 / FRAMERATE);
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