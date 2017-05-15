package eu.labrush.g2048.GameEngine;

public interface Game2048Interface {

    void left();
    void right();
    void up();
    void down();

    int[] getTiles();
    int getScore();
    boolean hasLost();
    void repaint();

}
